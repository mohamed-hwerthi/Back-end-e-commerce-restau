-- Insert default Timbre
INSERT INTO timbres (id, amount)
VALUES (gen_random_uuid(), 0)
ON CONFLICT (id) DO NOTHING;
