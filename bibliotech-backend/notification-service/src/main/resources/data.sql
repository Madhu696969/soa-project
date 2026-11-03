INSERT INTO notifications (user_id, message, type, is_read, created_at)
SELECT 3, 'Your book ''Clean Code'' is overdue by 6 days. Please return it as soon as possible.', 'OVERDUE', false, NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = 3 AND type = 'OVERDUE');

INSERT INTO notifications (user_id, message, type, is_read, created_at)
SELECT 4, 'You returned ''Introduction to Algorithms'' late. Fine charged: Rs.30', 'RETURN', true, NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM notifications WHERE user_id = 4 AND type = 'RETURN');
