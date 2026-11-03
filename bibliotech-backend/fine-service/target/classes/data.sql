INSERT INTO fines (rental_id, user_id, late_days, amount, status, created_at)
SELECT 2, 4, 3, 30.00, 'PAID', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM fines WHERE rental_id = 2);
