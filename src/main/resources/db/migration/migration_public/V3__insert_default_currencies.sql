-- V3_insert_default_currencies.sql

INSERT INTO currencies (id, code, name, symbol, scale)
VALUES
    (gen_random_uuid(), 'USD', 'United States Dollar', '$', 2),
    (gen_random_uuid(), 'EUR', 'Euro', '€', 2),
    (gen_random_uuid(), 'TND', 'Tunisian Dinar', 'DT', 2)
ON CONFLICT (code) DO NOTHING;
