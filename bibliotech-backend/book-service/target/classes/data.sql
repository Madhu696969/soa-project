INSERT INTO branches (name, location) SELECT 'Main Campus Library', 'Block A, Central Campus' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM branches WHERE name = 'Main Campus Library');
INSERT INTO branches (name, location) SELECT 'Engineering Block Library', 'Block C, Engineering Wing' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM branches WHERE name = 'Engineering Block Library');
INSERT INTO branches (name, location) SELECT 'City Extension Branch', 'Downtown Annex, 2nd Floor' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM branches WHERE name = 'City Extension Branch');

INSERT INTO books (isbn, title, author, category, publisher, description, created_at)
SELECT '9780132350884', 'Clean Code', 'Robert C. Martin', 'Software Engineering', 'Prentice Hall', 'A handbook of agile software craftsmanship.', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780132350884');

INSERT INTO books (isbn, title, author, category, publisher, description, created_at)
SELECT '9780201633610', 'Design Patterns', 'Erich Gamma', 'Software Engineering', 'Addison-Wesley', 'Elements of reusable object-oriented software.', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780201633610');

INSERT INTO books (isbn, title, author, category, publisher, description, created_at)
SELECT '9780262033848', 'Introduction to Algorithms', 'Thomas H. Cormen', 'Computer Science', 'MIT Press', 'Comprehensive introduction to algorithms.', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780262033848');

INSERT INTO books (isbn, title, author, category, publisher, description, created_at)
SELECT '9780134685991', 'Effective Java', 'Joshua Bloch', 'Programming', 'Addison-Wesley', 'Best practices for the Java platform.', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780134685991');

INSERT INTO books (isbn, title, author, category, publisher, description, created_at)
SELECT '9781491950357', 'Building Microservices', 'Sam Newman', 'Software Architecture', 'O''Reilly Media', 'Designing fine-grained systems.', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9781491950357');

INSERT INTO books (isbn, title, author, category, publisher, description, created_at)
SELECT '9780596007126', 'Head First Design Patterns', 'Eric Freeman', 'Software Engineering', 'O''Reilly Media', 'A brain-friendly guide to design patterns.', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780596007126');

INSERT INTO books (isbn, title, author, category, publisher, description, created_at)
SELECT '9780135957059', 'The Pragmatic Programmer', 'David Thomas', 'Software Engineering', 'Addison-Wesley', 'Your journey to mastery.', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780135957059');

INSERT INTO books (isbn, title, author, category, publisher, description, created_at)
SELECT '9780132126953', 'Refactoring', 'Martin Fowler', 'Software Engineering', 'Addison-Wesley', 'Improving the design of existing code.', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780132126953');

INSERT INTO books (isbn, title, author, category, publisher, description, created_at)
SELECT '9781449373320', 'Designing Data-Intensive Applications', 'Martin Kleppmann', 'Databases', 'O''Reilly Media', 'The big ideas behind reliable, scalable, maintainable systems.', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9781449373320');

INSERT INTO books (isbn, title, author, category, publisher, description, created_at)
SELECT '9780596517748', 'JavaScript: The Good Parts', 'Douglas Crockford', 'Programming', 'O''Reilly Media', 'Unearthing the excellent features of JavaScript.', NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM books WHERE isbn = '9780596517748');

INSERT INTO book_copies (book_id, branch_id, copy_code, status)
SELECT b.id, br.id, 'CC-CLEAN-01', 'AVAILABLE' FROM books b, branches br WHERE b.isbn = '9780132350884' AND br.name = 'Main Campus Library'
AND NOT EXISTS (SELECT 1 FROM book_copies WHERE copy_code = 'CC-CLEAN-01');

INSERT INTO book_copies (book_id, branch_id, copy_code, status)
SELECT b.id, br.id, 'CC-CLEAN-02', 'BORROWED' FROM books b, branches br WHERE b.isbn = '9780132350884' AND br.name = 'Engineering Block Library'
AND NOT EXISTS (SELECT 1 FROM book_copies WHERE copy_code = 'CC-CLEAN-02');

INSERT INTO book_copies (book_id, branch_id, copy_code, status)
SELECT b.id, br.id, 'CC-DESIGN-01', 'AVAILABLE' FROM books b, branches br WHERE b.isbn = '9780201633610' AND br.name = 'Main Campus Library'
AND NOT EXISTS (SELECT 1 FROM book_copies WHERE copy_code = 'CC-DESIGN-01');

INSERT INTO book_copies (book_id, branch_id, copy_code, status)
SELECT b.id, br.id, 'CC-ALGO-01', 'AVAILABLE' FROM books b, branches br WHERE b.isbn = '9780262033848' AND br.name = 'Engineering Block Library'
AND NOT EXISTS (SELECT 1 FROM book_copies WHERE copy_code = 'CC-ALGO-01');

INSERT INTO book_copies (book_id, branch_id, copy_code, status)
SELECT b.id, br.id, 'CC-ALGO-02', 'BORROWED' FROM books b, branches br WHERE b.isbn = '9780262033848' AND br.name = 'Main Campus Library'
AND NOT EXISTS (SELECT 1 FROM book_copies WHERE copy_code = 'CC-ALGO-02');

INSERT INTO book_copies (book_id, branch_id, copy_code, status)
SELECT b.id, br.id, 'CC-EFFJAVA-01', 'AVAILABLE' FROM books b, branches br WHERE b.isbn = '9780134685991' AND br.name = 'Main Campus Library'
AND NOT EXISTS (SELECT 1 FROM book_copies WHERE copy_code = 'CC-EFFJAVA-01');

INSERT INTO book_copies (book_id, branch_id, copy_code, status)
SELECT b.id, br.id, 'CC-MICRO-01', 'AVAILABLE' FROM books b, branches br WHERE b.isbn = '9781491950357' AND br.name = 'City Extension Branch'
AND NOT EXISTS (SELECT 1 FROM book_copies WHERE copy_code = 'CC-MICRO-01');

INSERT INTO book_copies (book_id, branch_id, copy_code, status)
SELECT b.id, br.id, 'CC-HFDP-01', 'AVAILABLE' FROM books b, branches br WHERE b.isbn = '9780596007126' AND br.name = 'Engineering Block Library'
AND NOT EXISTS (SELECT 1 FROM book_copies WHERE copy_code = 'CC-HFDP-01');

INSERT INTO book_copies (book_id, branch_id, copy_code, status)
SELECT b.id, br.id, 'CC-PRAG-01', 'AVAILABLE' FROM books b, branches br WHERE b.isbn = '9780135957059' AND br.name = 'Main Campus Library'
AND NOT EXISTS (SELECT 1 FROM book_copies WHERE copy_code = 'CC-PRAG-01');

INSERT INTO book_copies (book_id, branch_id, copy_code, status)
SELECT b.id, br.id, 'CC-REFACTOR-01', 'AVAILABLE' FROM books b, branches br WHERE b.isbn = '9780132126953' AND br.name = 'City Extension Branch'
AND NOT EXISTS (SELECT 1 FROM book_copies WHERE copy_code = 'CC-REFACTOR-01');

INSERT INTO book_copies (book_id, branch_id, copy_code, status)
SELECT b.id, br.id, 'CC-DDIA-01', 'AVAILABLE' FROM books b, branches br WHERE b.isbn = '9781449373320' AND br.name = 'Engineering Block Library'
AND NOT EXISTS (SELECT 1 FROM book_copies WHERE copy_code = 'CC-DDIA-01');

INSERT INTO book_copies (book_id, branch_id, copy_code, status)
SELECT b.id, br.id, 'CC-JSGP-01', 'AVAILABLE' FROM books b, branches br WHERE b.isbn = '9780596517748' AND br.name = 'Main Campus Library'
AND NOT EXISTS (SELECT 1 FROM book_copies WHERE copy_code = 'CC-JSGP-01');
