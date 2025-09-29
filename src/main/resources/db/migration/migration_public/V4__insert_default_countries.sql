-- ========================================
-- Insert Default Countries
-- ========================================
INSERT INTO countries (code, name, flag_url) VALUES
('TN', '{"en":"Tunisia","fr":"Tunisie"}', 'data:image/png;base64,iVBORw0...'),
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
