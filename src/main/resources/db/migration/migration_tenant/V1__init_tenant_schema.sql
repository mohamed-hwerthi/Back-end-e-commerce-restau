-- Migration for tenant schema initialization

-- Taxes
CREATE TABLE taxes (
    id SERIAL PRIMARY KEY,
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
CREATE TABLE timbre(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount DOUBLE PRECISION NOT NULL
);

-- Order status enum
CREATE TYPE order_status AS ENUM ('PENDING', 'COMPLETED', 'CANCELLED');

-- Orders
CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    total_cost DOUBLE PRECISION NOT NULL,
    status order_status NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    paid BOOLEAN NOT NULL DEFAULT FALSE,
    user_id UUID NOT NULL,
    CONSTRAINT fk_orders_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Menu Items
CREATE TABLE menu_items (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title jsonb NOT NULL,
    description jsonb ,
    price DOUBLE PRECISION NOT NULL DEFAULT 1.0,
    code_bar VARCHAR(255) UNIQUE,
    purchase_price NUMERIC CHECK (purchase_price > 0) ,
    quantity INT CHECK (quantity >= 0) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    tax_id BIGINT,
    CONSTRAINT fk_menu_items_tax FOREIGN KEY(tax_id) REFERENCES taxes(id)
);

-- Reviews
CREATE TABLE reviews (
    id SERIAL PRIMARY KEY,
    comment VARCHAR(1000) NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    user_id UUID NOT NULL,
    menu_item_id UUID NOT NULL,
    CONSTRAINT fk_reviews_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_reviews_menu_item FOREIGN KEY(menu_item_id) REFERENCES menu_items(id) ON DELETE CASCADE
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
    description jsonb ,
);

-- Many-to-many tables
CREATE TABLE menu_menu_items (
    menu_id BIGINT NOT NULL,
    menu_item_id UUID NOT NULL,
    PRIMARY KEY (menu_id, menu_item_id),
    CONSTRAINT fk_menu_menu_items_menu FOREIGN KEY(menu_id) REFERENCES menus(id) ON DELETE CASCADE,
    CONSTRAINT fk_menu_menu_items_menu_item FOREIGN KEY(menu_item_id) REFERENCES menu_items(id) ON DELETE CASCADE
);

CREATE TABLE categories_medias (
    category_id UUID NOT NULL,
    media_id UUID NOT NULL,
    PRIMARY KEY (category_id, media_id),
    CONSTRAINT fk_category_medias_category FOREIGN KEY(category_id) REFERENCES categories(id) ON DELETE CASCADE,
    CONSTRAINT fk_category_medias_media FOREIGN KEY(media_id) REFERENCES medias(id) ON DELETE CASCADE
);

CREATE TABLE menu_item_categories (
    menu_item_id UUID NOT NULL,
    category_id UUID NOT NULL,
    PRIMARY KEY (menu_item_id, category_id),
    CONSTRAINT fk_menu_item_categories_menu_item FOREIGN KEY(menu_item_id) REFERENCES menu_items(id) ON DELETE CASCADE,
    CONSTRAINT fk_menu_item_categories_category FOREIGN KEY(category_id) REFERENCES categories(id) ON DELETE CASCADE
);

CREATE TABLE menu_item_promotions (
    menu_item_id UUID NOT NULL,
    promotion_id BIGINT NOT NULL,
    PRIMARY KEY (menu_item_id, promotion_id)
);

CREATE TABLE category_promotions (
    category_id UUID NOT NULL,
    promotion_id BIGINT NOT NULL,
    PRIMARY KEY (category_id, promotion_id)
);

CREATE TABLE menu_item_medias (
    menu_item_id UUID NOT NULL,
    media_id UUID NOT NULL,
    PRIMARY KEY (menu_item_id, media_id),
    CONSTRAINT fk_menu_item_medias_menu_item FOREIGN KEY(menu_item_id) REFERENCES menu_items(id) ON DELETE CASCADE,
    CONSTRAINT fk_menu_item_medias_media FOREIGN KEY(media_id) REFERENCES medias(id) ON DELETE CASCADE
);

-- Order menu items mapping
CREATE TABLE order_menu_items (
    order_id UUID NOT NULL,
    menu_item_id UUID NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (order_id, menu_item_id),
    CONSTRAINT fk_order_menu_items_order FOREIGN KEY(order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_menu_items_menu_item FOREIGN KEY(menu_item_id) REFERENCES menu_items(id) ON DELETE CASCADE
);
-- Promotion base table
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

-- Join table between menu_items and promotions (already exists but validate)
CREATE TABLE IF NOT EXISTS menu_item_promotions (
    menu_item_id UUID NOT NULL,
    promotion_id UUID NOT NULL,
    PRIMARY KEY (menu_item_id, promotion_id),
    CONSTRAINT fk_menu_item_promotions_menu_item FOREIGN KEY(menu_item_id) REFERENCES menu_items(id) ON DELETE CASCADE,
    CONSTRAINT fk_menu_item_promotions_promotion FOREIGN KEY(promotion_id) REFERENCES promotions(id) ON DELETE CASCADE
);

-- Join table between categories and promotions (already exists but validate)
CREATE TABLE IF NOT EXISTS category_promotions (
    category_id UUID NOT NULL,
    promotion_id BIGINT NOT NULL,
    PRIMARY KEY (category_id, promotion_id),
    CONSTRAINT fk_category_promotions_category FOREIGN KEY(category_id) REFERENCES categories(id) ON DELETE CASCADE,
    CONSTRAINT fk_category_promotions_promotion FOREIGN KEY(promotion_id) REFERENCES promotions(id) ON DELETE CASCADE
);

