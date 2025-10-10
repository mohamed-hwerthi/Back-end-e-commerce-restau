-- Insert default Timbre
INSERT INTO timbres (id, amount)
VALUES (gen_random_uuid(), 0)
ON CONFLICT (id) DO NOTHING;

-- Insert default order statuses with multilingual names
INSERT INTO order_statuses (code, name)
VALUES
    ('PENDING', '{"en": "Pending", "fr": "En attente", "ar": "قيد الانتظار"}'),
    ('PROCESSING', '{"en": "Processing", "fr": "En cours", "ar": "قيد المعالجة"}'),
    ('COMPLETED', '{"en": "Completed", "fr": "Terminé", "ar": "مكتمل"}'),
    ('CANCELLED', '{"en": "Cancelled", "fr": "Annulé", "ar": "ملغي"}');



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