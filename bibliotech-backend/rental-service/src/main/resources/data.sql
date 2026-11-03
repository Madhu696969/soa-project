INSERT INTO rentals (user_id, book_copy_id, book_title, borrowed_at, due_date, returned_at, status)
SELECT 3, 2, 'Clean Code', CURDATE() - INTERVAL 20 DAY, CURDATE() - INTERVAL 6 DAY, NULL, 'OVERDUE'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM rentals WHERE book_copy_id = 2 AND status IN ('ACTIVE','OVERDUE'));

INSERT INTO rentals (user_id, book_copy_id, book_title, borrowed_at, due_date, returned_at, status)
SELECT 4, 5, 'Introduction to Algorithms', CURDATE() - INTERVAL 30 DAY, CURDATE() - INTERVAL 16 DAY, CURDATE() - INTERVAL 13 DAY, 'RETURNED'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM rentals WHERE book_copy_id = 5 AND status = 'RETURNED');
