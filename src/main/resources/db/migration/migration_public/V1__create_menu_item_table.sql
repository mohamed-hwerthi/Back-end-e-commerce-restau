CREATE TABLE IF NOT EXISTS menu_item (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description TEXT NULL,
    price NUMERIC(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS store (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(255) NULL,
    email VARCHAR(255) NULL,
    address VARCHAR(255) NULL,
    facebook_url VARCHAR(255) NULL,
    instagram_url VARCHAR(255) NULL,
    linked_in_url VARCHAR(255) NULL,
    website_url VARCHAR(255) NULL,
    about TEXT NULL,
    background_color VARCHAR(10) NULL,
    template_name VARCHAR(255) NULL,
    accent_color VARCHAR(10) NULL,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS users (
    id  UUID   PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) DEFAULT 'Default Name',
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255),
    image_url TEXT DEFAULT 'https://www.pngarts.com/files/11/Avatar-Transparent-Images.png',
    phone_number VARCHAR(50) DEFAULT '000-000-0000',
    created_on TIMESTAMP,
    DTYPE VARCHAR(31)
);




