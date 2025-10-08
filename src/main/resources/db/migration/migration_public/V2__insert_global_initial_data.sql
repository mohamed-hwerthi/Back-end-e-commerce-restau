INSERT INTO activity_sectors (code, name)
VALUES
    ('ELEC', '{"fr": "Électronique", "en": "Electronics", "ar": "إلكترونيات"}'),
    ('FASH', '{"fr": "Mode et accessoires", "en": "Fashion & Accessories", "ar": "الموضة والإكسسوارات"}'),
    ('CLOT', '{"fr": "Vêtements", "en": "Clothing", "ar": "ملابس"}'),
    ('HOME', '{"fr": "Maison et cuisine", "en": "Home & Kitchen", "ar": "المنزل والمطبخ"}'),
    ('BEAU', '{"fr": "Beauté et santé", "en": "Beauty & Health", "ar": "الجمال والصحة"}'),
    ('SPRT', '{"fr": "Sport et plein air", "en": "Sports & Outdoors", "ar": "الرياضة والهواء الطلق"}'),
    ('TOYS', '{"fr": "Jouets et jeux", "en": "Toys & Games", "ar": "الألعاب والدمى"}'),
    ('AUTO', '{"fr": "Auto et moto", "en": "Automotive", "ar": "السيارات والدراجات النارية"}'),
    ('FOOD', '{"fr": "Alimentation et boissons", "en": "Food & Beverages", "ar": "الطعام والمشروبات"}'),
    ('BOOK', '{"fr": "Livres et papeterie", "en": "Books & Stationery", "ar": "الكتب والقرطاسية"}'),
    ('PET',  '{"fr": "Animaux de compagnie", "en": "Pet Supplies", "ar": "مستلزمات الحيوانات"}'),
    ('GARD', '{"fr": "Jardin et bricolage", "en": "Garden & DIY", "ar": "الحديقة والأشغال اليدوية"}'),
    ('MUSI', '{"fr": "Musique et instruments", "en": "Music & Instruments", "ar": "الموسيقى والآلات"}'),
    ('COMP', '{"fr": "Informatique et bureautique", "en": "Computers & Office", "ar": "الحواسيب والمكاتب"}'),
    ('JEWL', '{"fr": "Bijoux et montres", "en": "Jewelry & Watches", "ar": "المجوهرات والساعات"}'),
    ('ARTS', '{"fr": "Art et artisanat", "en": "Arts & Crafts", "ar": "الفنون والحرف اليدوية"}')
ON CONFLICT (code) DO NOTHING;

-- ========================================
-- Insert Default Currencies
-- ========================================
INSERT INTO currencies (id, code, name, symbol, scale)
VALUES
    (gen_random_uuid(), 'USD', '{"en": "United States Dollar", "fr": "Dollar américain", "ar": "الدولار الأمريكي"}', '$', 2),
    (gen_random_uuid(), 'EUR', '{"en": "Euro", "fr": "Euro", "ar": "يورو"}', '€', 2),
    (gen_random_uuid(), 'TND', '{"en": "Tunisian Dinar", "fr": "Dinar tunisien", "ar": "الدينار التونسي"}', 'DT', 2)
ON CONFLICT (code) DO NOTHING;



-- ========================================
-- Insert Default Countries
-- ========================================
INSERT INTO countries (code, name, flag_url) VALUES
('TN', '{"en":"Tunisia","fr":"Tunisie"}', 'ddata:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAT4AAACfCAMAAABX0UX9AAAAclBMVEUAAAAAljn////tLjgAjBgAmDn0LzoAmjnwKzjtJzL0JzjtLDbvTFSrISj5wMKrWTjsHCnsCR30h4zWPThxFhvWKjMzCgz96utxdTkzijkrCAorjDmuVzjvSFDsFiXxJjgmBwn/zdDybHL/x84AlCjbODh8hviEAAADU0lEQVR4nO2bC3baMBAAnaohjtOWpCn9pk2/979iMZSCwZJXWgk9vDMnkOZ5MLZXzesGFCxW32sv4ZJxbvGu9houGLf2R8DJuN7f6nPtZVwqbkNLwGls9RFwIm7nj4BTcHsIOJ4DfQQcjzv0t3pfezmXhhtCwHEc6SPgONyxPwKO4VgfAUcxoo+A5YzoI2A5Y/oIWIxHHwHL8OgjYBk+fQQsIqCPgKcJ6CPgaUL6CHiSCX0EHGZCHwGHmdJHwEEE+gjYj0AfAfuR6CNgL0J9BDyOUB8BjyPVR8CjROgj4FMi9BHwKTH6CPiESH0EPCRSH5NYQ2L1OQI+JEEfAe9J0EfAe1L0OQLekaiPgLck6iPgLan6HAH3KPQRsEofAev0cRpJqc98wEp91gPW6jMesF6f6YAz6HOGD/Tn0OfuFj9emCSPPnf38Lb2TqqQSZ9z918/1N5LBbLpc7ff3tTezPnJp89kwBn1WQw4qz57AefVZy7gzPrWAb+yFHB2fe723lDA+fWZCriAPksBF9FnJ+Ay+swEXEiflYCL6bMRcDl9JgIuqM9CwEX1zT/gsvpmH3BhfXMPuLi+eQdcXt+sAz6DvjkHfBZ98w34PPpmG/CZ9Dn38LP2VkvA1aeC3z4V3HlV8L9PBU8dKnjmVcEbFxW871PB22YVfOtQwZc2FXznVcGUgQpmXFQwYaWC+T4VTJeqYLZZBZP1KjjXoYJTRSo406aCE5UqOM+rgtPkKvLoa7tftTdShyz6uj9ffr80SQ593dPN9ZVN9Pra5483tXdRDbW+dbh27an1GQ63R6fPdLg9Kn22w+3R6Oueri2H25Ouz3y4Pcn6CLcnVR/hbkjTR7j/SNJHuDtS9BHuf+L1Ee4B0fq6R8LdE6uPcAfE6Wtbwh0Qpa97/IS9ATH6uiXhHiHX14eLvSPE+gh3DKk+wh1Fpo9wPYj0Ea4PiT7C9TKtj3ADTOoj3BBT+gg3SFhf6wg3SFAf4U4R0tctr7j0wvj1cccV4NVHuBJ8+rjjihjXR7hCRvURrpQxfYQr5lQf4UZwoo9wYzjWR7hRDPURbiQDfQxgxNIQroa9PianEmgIV0NDuBoawtWw0cesciq9PkYek2kIV0NDuBqaJU9pCv4CgSsXH5q9CiAAAAAASUVORK5CYII='),
('FR', '{"en":"France","fr":"France"}', 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRL52JovZOgF3-dTg7n2yjxwJt3NMCOMpI4yQ&s'),
('US', '{"en":"United States","fr":"États-Unis"}','data:image/png;base64,iVBORw0...'),
('GB', '{"en":"United Kingdom","fr":"Royaume-Uni"}', 'https://flagcdn.com/gb.svg'),
('DE', '{"en":"Germany","fr":"Allemagne"}', 'https://flagcdn.com/de.svg'),
('ES', '{"en":"Spain","fr":"Espagne"}', 'https://flagcdn.com/es.svg'),
('IT', '{"en":"Italy","fr":"Italie"}', 'https://flagcdn.com/it.svg'),
('CA', '{"en":"Canada","fr":"Canada"}', 'https://flagcdn.com/ca.svg'),
('MA', '{"en":"Morocco","fr":"Maroc"}', 'https://flagcdn.com/ma.svg'),
('DZ', '{"en":"Algeria","fr":"Algérie"}', 'https://flagcdn.com/dz.svg'),
('JP', '{"en":"Japan","fr":"Japon"}', 'https://flagcdn.com/jp.svg'),
('CN', '{"en":"China","fr":"Chine"}', 'https://flagcdn.com/cn.svg'),
('IN', '{"en":"India","fr":"Inde"}', 'https://flagcdn.com/in.svg'),
('BR', '{"en":"Brazil","fr":"Brésil"}', 'https://flagcdn.com/br.svg'),
('RU', '{"en":"Russia","fr":"Russie"}', 'https://flagcdn.com/ru.svg')
ON CONFLICT (code) DO NOTHING;

-- ========================================
-- Insert Default Languages
-- ========================================

-- Arabic for Tunisia
INSERT INTO languages (code, country_id, name)
SELECT
    'ar',
    id,
    '{"ar":"العربية","fr":"Arabe","en":"Arabic"}'
FROM countries WHERE code = 'TN';

-- French for France
INSERT INTO languages (code, country_id, name)
SELECT
    'fr',
    id,
    '{"ar":"الفرنسية","fr":"Français","en":"French"}'
FROM countries WHERE code = 'FR';

-- English for United States
INSERT INTO languages (code, country_id, name)
SELECT
    'en',
    id,
    '{"ar":"الإنجليزية","fr":"Anglais","en":"English"}'
FROM countries WHERE code = 'US';

