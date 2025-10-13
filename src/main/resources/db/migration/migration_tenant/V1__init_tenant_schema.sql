-- Migration for tenant schema initialization

-- Taxes
CREATE TABLE taxes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255),
    rate DOUBLE PRECISION
);

-- Medias
CREATE TABLE medias (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    path VARCHAR(1024) NOT NULL,
    url TEXT NOT NULL,
    type VARCHAR(50) NOT NULL
);

-- Users
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50),
    image_url TEXT DEFAULT 'https://www.pngarts.com/files/11/Avatar-Transparent-Images.png',
    phone_number VARCHAR(20) DEFAULT '000-000-0000',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
    dtype VARCHAR(31)
);

-- ========================================
-- Create Countries Table
-- ========================================
CREATE TABLE IF NOT EXISTS countries (
    id VARCHAR(2)  NOT NULL UNIQUE PRIMARY KEY,
    code VARCHAR(2) NOT NULL UNIQUE,
    name JSON NOT NULL,
    flag_url VARCHAR(10000)
);


-- Tokens
CREATE TABLE tokens (
    id SERIAL PRIMARY KEY,
    access_token TEXT,
    refresh_token TEXT,
    access_token_expiry_date TIMESTAMP NOT NULL,
    refresh_token_expiry_date TIMESTAMP NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT fk_tokens_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Timbres
CREATE TABLE timbres(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount DOUBLE PRECISION NOT NULL
);
-- ========================================
-- Partners Table (Single Table Inheritance)
-- ========================================
CREATE TABLE partners (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    firstName VARCHAR(255),
    lastName VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(50) NOT NULL UNIQUE,
    address TEXT,
    partner_type VARCHAR(50) NOT NULL
);


CREATE TABLE order_statuses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(50),
    name jsonb NOT NULL
);
CREATE TABLE payment_methods (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL UNIQUE,
    code VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL,
    status_id UUID,
    created_at TIMESTAMP WITH TIME ZONE  NOT NULL DEFAULT now(),
    total NUMERIC(19,2) NOT NULL DEFAULT 0,
    sub_total NUMERIC(19,2) NOT NULL DEFAULT 0,
      -- Embedded Address fields
      delivery_street VARCHAR(255) NOT NULL,
      delivery_city VARCHAR(255) NOT NULL,
      delivery_country_id TEXT,
      delivery_postal_code VARCHAR(50),
    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES  partners    (id) ON DELETE CASCADE,
    CONSTRAINT fk_orders_status FOREIGN KEY (status_id) REFERENCES order_statuses(id) ON DELETE SET NULL
);


CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount NUMERIC(19, 2),
    payment_date TIMESTAMP WITH TIME ZONE,
    payment_method_id UUID,
    order_id UUID UNIQUE,
    CONSTRAINT fk_payment_method FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id) ON DELETE SET NULL,
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

-- Products
CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title jsonb NOT NULL,
    description jsonb,
    parent_id UUID,
    is_variant BOOLEAN NOT NULL DEFAULT FALSE,
    is_option BOOLEAN NOT NULL DEFAULT FALSE,
    is_supplement BOOLEAN NOT NULL DEFAULT FALSE,  
    price DOUBLE PRECISION NOT NULL DEFAULT 1.0,
    code_bar VARCHAR(255) UNIQUE,
    sku VARCHAR(255) UNIQUE,
    purchase_price NUMERIC CHECK (purchase_price > 0),
    quantity INT CHECK (quantity >= 0) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    tax_id UUID  ,

    low_stock_threshold INT ,
    CONSTRAINT fk_products_tax FOREIGN KEY(tax_id) REFERENCES taxes(id) ,
    CONSTRAINT fk_products_parent FOREIGN KEY(parent_id) REFERENCES products(id) ON DELETE CASCADE

);

-- Product Attributes
CREATE TABLE product_attributes (
    id UUID PRIMARY KEY,
    name JSON NOT NULL  ,
    product_id UUID NOT NULL,
    CONSTRAINT fk_product_attributes_product FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Product Attribute Values
CREATE TABLE attribute_values (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    value VARCHAR(255) NOT NULL,
    attribute_id UUID NOT NULL,
    CONSTRAINT fk_attribute_values_attribute FOREIGN KEY(attribute_id) REFERENCES product_attributes(id) ON DELETE CASCADE
);


CREATE TABLE product_option_groups (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name jsonb NOT NULL,
    product_id UUID NOT NULL,
    CONSTRAINT fk_product_option_group_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

    CREATE TABLE product_options (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
        override_price NUMERIC CHECK (override_price >= 0),
        product_option_group_id UUID NOT NULL,
        linked_product_id UUID NOT NULL,
        CONSTRAINT fk_product_option_group FOREIGN KEY (product_option_group_id) REFERENCES product_option_groups(id) ON DELETE CASCADE,
        CONSTRAINT fk_product_option_linked_product FOREIGN KEY (linked_product_id) REFERENCES products(id) ON DELETE CASCADE
    );






-- ========================================
-- Create Custom Attributes Table
-- ========================================
CREATE TABLE IF NOT EXISTS custom_attributes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id UUID NOT NULL,
    name jsonb NOT NULL,
    value VARCHAR(255) NOT NULL,
    type VARCHAR(20) NOT NULL,
    CONSTRAINT fk_custom_attribute_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS product_variant_attributes (
    product_id UUID NOT NULL,
    attribute_value_id UUID NOT NULL,
    CONSTRAINT fk_variant_product_join FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_variant_attr_value FOREIGN KEY(attribute_value_id) REFERENCES attribute_values(id) ON DELETE CASCADE,
    PRIMARY KEY(product_id, attribute_value_id)
);

-- ========================================
-- Order Items
-- ========================================
CREATE TABLE order_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INT NOT NULL CHECK (quantity >= 0),
    unit_price NUMERIC(19,2) NOT NULL CHECK (unit_price >= 0),
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);
-- ========================================
-- Create Order Item Options Table
-- ========================================
CREATE TABLE order_item_options (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_item_id UUID NOT NULL,
    product_option_id UUID NOT NULL,

    CONSTRAINT fk_order_item_options_order_item
        FOREIGN KEY (order_item_id)
        REFERENCES order_items(id) ON DELETE CASCADE,

    CONSTRAINT fk_order_item_options_product_option
        FOREIGN KEY (product_option_id)
        REFERENCES product_options(id) ON DELETE CASCADE
);


-- Reviews
CREATE TABLE reviews (
    id SERIAL PRIMARY KEY,
    comment VARCHAR(1000) NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    user_id UUID NOT NULL,
    product_id UUID NOT NULL,
    CONSTRAINT fk_reviews_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_product FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Menus
CREATE TABLE menus (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
);

-- Employees
CREATE TABLE employees (
    id UUID PRIMARY KEY,
    CONSTRAINT fk_employees_user FOREIGN KEY(id) REFERENCES users(id)
);

-- Categories
CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name jsonb NOT NULL,
    description jsonb
);

-- Many-to-many tables
CREATE TABLE menu_products (
    menu_id BIGINT NOT NULL,
    product_id UUID NOT NULL,
    PRIMARY KEY (menu_id, product_id),
    CONSTRAINT fk_menu_products_menu FOREIGN KEY(menu_id) REFERENCES menus(id) ON DELETE CASCADE,
    CONSTRAINT fk_menu_products_product FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE categories_medias (
    category_id UUID NOT NULL,
    media_id UUID NOT NULL,
    PRIMARY KEY (category_id, media_id),
    CONSTRAINT fk_category_medias_category FOREIGN KEY(category_id) REFERENCES categories(id) ON DELETE CASCADE,
    CONSTRAINT fk_category_medias_media FOREIGN KEY(media_id) REFERENCES medias(id) ON DELETE CASCADE
);

CREATE TABLE product_categories (
    product_id UUID NOT NULL,
    category_id UUID NOT NULL,
    PRIMARY KEY (product_id, category_id),
    CONSTRAINT fk_product_categories_product FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_product_categories_category FOREIGN KEY(category_id) REFERENCES categories(id) ON DELETE CASCADE
);

CREATE TABLE product_promotions (
    product_id UUID NOT NULL,
    promotion_id UUID NOT NULL,
    PRIMARY KEY (product_id, promotion_id)
);

CREATE TABLE category_promotions (
    category_id UUID NOT NULL,
    promotion_id UUID NOT NULL,
    PRIMARY KEY (category_id, promotion_id)
);

CREATE TABLE product_medias (
    product_id UUID NOT NULL,
    media_id UUID NOT NULL,
    PRIMARY KEY (product_id, media_id),
    CONSTRAINT fk_product_medias_product FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_product_medias_media FOREIGN KEY(media_id) REFERENCES medias(id) ON DELETE CASCADE
);

-- Order products mapping
CREATE TABLE order_products (
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_order_products_order FOREIGN KEY(order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_products_product FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- Promotions base table
CREATE TABLE promotions (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    start_date DATE,
    end_date DATE,
    active BOOLEAN,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    promotion_type VARCHAR(31) NOT NULL,
    promotion_target VARCHAR(50)
);

-- Percentage discount promotion (inherits from promotions)
CREATE TABLE percentage_discount_promotion (
    id UUID PRIMARY KEY,
    discount_percentage INT,
    discount_type VARCHAR(50),
    promotional_price DOUBLE PRECISION,
    CONSTRAINT fk_percentage_promotion FOREIGN KEY(id) REFERENCES promotions(id) ON DELETE CASCADE
);

-- Join table between products and promotions
CREATE TABLE IF NOT EXISTS product_promotions (
    product_id UUID NOT NULL,
    promotion_id UUID NOT NULL,
    PRIMARY KEY (product_id, promotion_id),
    CONSTRAINT fk_product_promotions_product FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_product_promotions_promotion FOREIGN KEY(promotion_id) REFERENCES promotions(id) ON DELETE CASCADE
);

-- Join table between categories and promotions
CREATE TABLE IF NOT EXISTS category_promotions (
    category_id UUID NOT NULL,
    promotion_id UUID NOT NULL,
    PRIMARY KEY (category_id, promotion_id),
    CONSTRAINT fk_category_promotions_category FOREIGN KEY(category_id) REFERENCES categories(id) ON DELETE CASCADE,
    CONSTRAINT fk_category_promotions_promotion FOREIGN KEY(promotion_id) REFERENCES promotions(id) ON DELETE CASCADE
);



