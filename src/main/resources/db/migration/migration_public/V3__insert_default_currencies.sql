-- V3_insert_default_currencies.sql

INSERT INTO currencies (id, code, name, symbol, scale)
VALUES
    (gen_random_uuid(), 'USD', '{"en": "United States Dollar", "fr": "Dollar américain", "ar": "الدولار الأمريكي"}', '$', 2),
    (gen_random_uuid(), 'EUR', '{"en": "Euro", "fr": "Euro", "ar": "يورو"}', '€', 2),
    (gen_random_uuid(), 'TND', '{"en": "Tunisian Dinar", "fr": "Dinar tunisien", "ar": "الدينار التونسي"}', 'DT', 2)
ON CONFLICT (code) DO NOTHING;
