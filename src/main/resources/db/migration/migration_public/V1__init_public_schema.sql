-- ========================================
-- Create Activity Sectors Table
-- ========================================
CREATE TABLE IF NOT EXISTS activity_sectors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL
);

-- ========================================
-- Create Countries Table
-- ========================================
CREATE TABLE IF NOT EXISTS countries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(2) NOT NULL UNIQUE,
    name JSON NOT NULL,
    flag_url VARCHAR(10000)
);

-- ========================================
-- Create Languages Table
-- ========================================
CREATE TABLE IF NOT EXISTS languages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(5) NOT NULL,
    name JSON NOT NULL,
    country_id UUID NOT NULL,
    CONSTRAINT fk_language_country FOREIGN KEY (country_id) REFERENCES countries(id)
);

-- ========================================
-- Create Currencies Table
-- ========================================
CREATE TABLE IF NOT EXISTS currencies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    code VARCHAR(255) UNIQUE,
    name VARCHAR(255),
    symbol VARCHAR(10),
    scale INT
);

-- ========================================
-- Create Medias Table
-- ========================================
CREATE TABLE IF NOT EXISTS medias (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    path VARCHAR(1024) NOT NULL,
    url VARCHAR(1024) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    type VARCHAR(50) NOT NULL
);

-- ========================================
-- Create Users Table
-- ========================================
CREATE TABLE IF NOT EXISTS "users" (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    image_url VARCHAR(1024) DEFAULT 'https://www.pngarts.com/files/11/Avatar-Transparent-Images.png',
    phone_number VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    dtype VARCHAR(31) NOT NULL
);

-- ========================================
-- Create Tokens Table
-- ========================================
CREATE TABLE IF NOT EXISTS tokens (
    id SERIAL PRIMARY KEY,
    access_token TEXT,
    refresh_token TEXT,
    access_token_expiry_date TIMESTAMP NOT NULL,
    refresh_token_expiry_date TIMESTAMP NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT fk_tokens_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ========================================
-- Create Stores Table
-- ========================================
CREATE TABLE IF NOT EXISTS stores (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description JSON  ,
    slug VARCHAR(255) NOT NULL,
    address VARCHAR(512),
    facebook_url VARCHAR(512),
    instagram_url VARCHAR(512),
    linked_in_url VARCHAR(512),
    website_url VARCHAR(512),
    about VARCHAR(2048),
    background_color VARCHAR(10),
    template_name VARCHAR(255),
    accent_color VARCHAR(10),
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    activity_sector_id UUID,
    logo_media_id UUID,
    owner_id UUID,
    currency_id UUID,
    country_id UUID,
    default_language_id UUID,

    CONSTRAINT fk_store_activity_sector FOREIGN KEY (activity_sector_id) REFERENCES activity_sectors(id),
    CONSTRAINT fk_store_logo_media FOREIGN KEY (logo_media_id) REFERENCES medias(id),
    CONSTRAINT fk_store_owner FOREIGN KEY (owner_id) REFERENCES "users"(id),
    CONSTRAINT fk_currency_id FOREIGN KEY (currency_id) REFERENCES currencies(id),
    CONSTRAINT fk_store_country FOREIGN KEY (country_id) REFERENCES countries(id) ,
    CONSTRAINT fk_store_default_language FOREIGN KEY (default_language_id) REFERENCES languages(id)

);
