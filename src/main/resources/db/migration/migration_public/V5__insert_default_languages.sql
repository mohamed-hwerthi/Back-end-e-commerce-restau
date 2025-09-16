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
