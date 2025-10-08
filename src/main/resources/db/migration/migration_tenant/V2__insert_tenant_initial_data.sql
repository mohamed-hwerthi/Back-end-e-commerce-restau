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

