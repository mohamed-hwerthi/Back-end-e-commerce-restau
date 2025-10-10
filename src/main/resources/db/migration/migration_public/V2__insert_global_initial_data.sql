INSERT INTO activity_sectors (id, code, name)
VALUES
    ('ELEC', 'ELEC', '{"fr": "Électronique", "en": "Electronics", "ar": "إلكترونيات"}'),
    ('FASH', 'FASH', '{"fr": "Mode et accessoires", "en": "Fashion & Accessories", "ar": "الموضة والإكسسوارات"}'),
    ('CLOT', 'CLOT', '{"fr": "Vêtements", "en": "Clothing", "ar": "ملابس"}'),
    ('HOME', 'HOME', '{"fr": "Maison et cuisine", "en": "Home & Kitchen", "ar": "المنزل والمطبخ"}'),
    ('BEAU', 'BEAU', '{"fr": "Beauté et santé", "en": "Beauty & Health", "ar": "الجمال والصحة"}'),
    ('SPRT', 'SPRT', '{"fr": "Sport et plein air", "en": "Sports & Outdoors", "ar": "الرياضة والهواء الطلق"}'),
    ('TOYS', 'TOYS', '{"fr": "Jouets et jeux", "en": "Toys & Games", "ar": "الألعاب والدمى"}'),
    ('AUTO', 'AUTO', '{"fr": "Auto et moto", "en": "Automotive", "ar": "السيارات والدراجات النارية"}'),
    ('FOOD', 'FOOD', '{"fr": "Alimentation et boissons", "en": "Food & Beverages", "ar": "الطعام والمشروبات"}'),
    ('BOOK', 'BOOK', '{"fr": "Livres et papeterie", "en": "Books & Stationery", "ar": "الكتب والقرطاسية"}'),
    ('PET',  'PET',  '{"fr": "Animaux de compagnie", "en": "Pet Supplies", "ar": "مستلزمات الحيوانات"}'),
    ('GARD', 'GARD', '{"fr": "Jardin et bricolage", "en": "Garden & DIY", "ar": "الحديقة والأشغال اليدوية"}'),
    ('MUSI', 'MUSI', '{"fr": "Musique et instruments", "en": "Music & Instruments", "ar": "الموسيقى والآلات"}'),
    ('COMP', 'COMP', '{"fr": "Informatique et bureautique", "en": "Computers & Office", "ar": "الحواسيب والمكاتب"}'),
    ('JEWL', 'JEWL', '{"fr": "Bijoux et montres", "en": "Jewelry & Watches", "ar": "المجوهرات والساعات"}'),
    ('ARTS', 'ARTS', '{"fr": "Art et artisanat", "en": "Arts & Crafts", "ar": "الفنون والحرف اليدوية"}'),
    ('OTHR', 'OTHR', '{"fr": "Autres", "en": "Others", "ar": "أخرى"}')
ON CONFLICT (id) DO NOTHING;



-- ========================================
-- Insert Default Currencies with id = code
-- ========================================

INSERT INTO currencies (id, code, name, symbol, scale)
VALUES
    ('USD', 'USD', '{"en": "United States Dollar", "fr": "Dollar américain", "ar": "الدولار الأمريكي"}', '$', 2),
    ('EUR', 'EUR', '{"en": "Euro", "fr": "Euro", "ar": "يورو"}', '€', 2),
    ('TND', 'TND', '{"en": "Tunisian Dinar", "fr": "Dinar tunisien", "ar": "الدينار التونسي"}', 'DT', 2)
ON CONFLICT (id) DO NOTHING;




-- Insert default countries
INSERT INTO countries (id, code, name, flag_url) VALUES
('TN', 'TN', '{"en":"Tunisia","fr":"Tunisie"}', 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/Flag_of_Tunisia.svg/2560px-Flag_of_Tunisia.svg.png'),
('FR', 'FR', '{"en":"France","fr":"France"}', 'https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/Flag_of_France.svg/1280px-Flag_of_France.svg.png'),
('US', 'US', '{"en":"United States","fr":"États-Unis"}','https://upload.wikimedia.org/wikipedia/en/thumb/a/a4/Flag_of_the_United_States.svg/2560px-Flag_of_the_United_States.svg.png'),
('GB', 'GB', '{"en":"United Kingdom","fr":"Royaume-Uni"}', 'https://flagcdn.com/gb.svg'),
('DE', 'DE', '{"en":"Germany","fr":"Allemagne"}', 'https://flagcdn.com/de.svg'),
('ES', 'ES', '{"en":"Spain","fr":"Espagne"}', 'https://flagcdn.com/es.svg'),
('IT', 'IT', '{"en":"Italy","fr":"Italie"}', 'https://flagcdn.com/it.svg'),
('CA', 'CA', '{"en":"Canada","fr":"Canada"}', 'https://flagcdn.com/ca.svg'),
('MA', 'MA', '{"en":"Morocco","fr":"Maroc"}', 'https://flagcdn.com/ma.svg'),
('DZ', 'DZ', '{"en":"Algeria","fr":"Algérie"}', 'https://flagcdn.com/dz.svg'),
('JP', 'JP', '{"en":"Japan","fr":"Japon"}', 'https://flagcdn.com/jp.svg'),
('CN', 'CN', '{"en":"China","fr":"Chine"}', 'https://flagcdn.com/cn.svg'),
('IN', 'IN', '{"en":"India","fr":"Inde"}', 'https://flagcdn.com/in.svg'),
('BR', 'BR', '{"en":"Brazil","fr":"Brésil"}', 'https://flagcdn.com/br.svg'),
('RU', 'RU', '{"en":"Russia","fr":"Russie"}', 'https://flagcdn.com/ru.svg'),
('PS', 'PS', '{"en":"Palestine","fr":"Palestine","ar":"فلسطين"}', 'https://upload.wikimedia.org/wikipedia/commons/thumb/0/00/Flag_of_Palestine.svg/1920px-Flag_of_Palestine.svg.png')

ON CONFLICT (id) DO NOTHING;


-- Insert only main languages for default countries
INSERT INTO languages (id, code, country_id, name)
VALUES
    ('ar', 'ar', 'PS', '{"ar":"العربية","fr":"Arabe","en":"Arabic"}'),
    ('fr', 'fr', 'FR', '{"ar":"الفرنسية","fr":"Français","en":"French"}'),
    ('en', 'en', 'US', '{"ar":"الإنجليزية","fr":"Anglais","en":"English"}')
ON CONFLICT (code, country_id) DO NOTHING;



