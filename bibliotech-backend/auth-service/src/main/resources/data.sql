INSERT INTO users (name, email, password, role, created_at)
SELECT 'System Admin', 'admin@bibliotech.com', '$2b$10$vibMsyaFuj/etl9yuV5a2eyqFc5SpFg/6q8xxeRRIzKkfYz7/aaTG', 'ADMIN', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@bibliotech.com');

INSERT INTO users (name, email, password, role, created_at)
SELECT 'Head Librarian', 'librarian@bibliotech.com', '$2b$10$EbadI5Lgm.MfgCS5kV9Me.l3KCn3/oOYZZjkeuf8XI/6NLCXmME9a', 'LIBRARIAN', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'librarian@bibliotech.com');

INSERT INTO users (name, email, password, role, created_at)
SELECT 'Alex Student', 'student@bibliotech.com', '$2b$10$2FLu5z6Oyd3KHm3PBSlKg.qYGZHP9umBA1v1v8m1oBhkEZp2N8eGO', 'STUDENT', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'student@bibliotech.com');

INSERT INTO users (name, email, password, role, created_at)
SELECT 'Dr. Faculty', 'faculty@bibliotech.com', '$2b$10$NoYfvGDZx.ibvambrV3H7.Rty70CMMG7gisQQ7uR4i59EoIanGrMS', 'FACULTY', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'faculty@bibliotech.com');
