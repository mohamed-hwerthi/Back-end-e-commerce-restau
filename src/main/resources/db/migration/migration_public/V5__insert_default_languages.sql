
-- ========================================
-- Insert Default Languages
-- ========================================
-- Arabic for Tunisia
INSERT INTO languages (code, country_id)
SELECT 'ar', id FROM countries WHERE code = 'TN';

-- French for France
INSERT INTO languages (code, country_id)
SELECT 'fr', id FROM countries WHERE code = 'FR';

-- English for United States
INSERT INTO languages (code, country_id)
SELECT 'en', id FROM countries WHERE code = 'US';