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