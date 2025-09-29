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
