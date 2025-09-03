INSERT INTO activity_sectors (code, name)
VALUES
    ('ELEC', 'Électronique'),
    ('FASH', 'Mode et accessoires'),
    ('CLOT', 'Vêtements'),
    ('HOME', 'Maison et cuisine'),
    ('BEAU', 'Beauté et santé'),
    ('SPRT', 'Sport et plein air'),
    ('TOYS', 'Jouets et jeux'),
    ('AUTO', 'Auto et moto'),
    ('FOOD', 'Alimentation et boissons'),
    ('BOOK', 'Livres et papeterie'),
    ('PET',  'Animaux de compagnie'),
    ('GARD', 'Jardin et bricolage'),
    ('MUSI', 'Musique et instruments'),
    ('COMP', 'Informatique et bureautique'),
    ('JEWL', 'Bijoux et montres'),
    ('ARTS', 'Art et artisanat')
ON CONFLICT (code) DO NOTHING;