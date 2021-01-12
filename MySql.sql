/* --------------- DOCUMENTATI ON --------------- */
-- Followin table have an ER-diagram --

/* --------------- DROP DATABasE --------------- */
-- DROP DATABASE BookDealer2;

/* --------------- CREATE & USE DATABasE --------------- */
CREATE DATABASE BookDealer2;
USE BookDealer2;

/* --------------- TABLE GRADES --------------- */
CREATE TABLE grades(
grade_id INT PRIMARY KEY NOT NULL,
grade CHAR(5) NOT NULL
);
/* --------------- Insertion of table grades --------------- */
INSERT INTO grades(grade_id, grade)VALUE
(1,'*'),
(2,'**'),
(3,'***'),
(4,'****'),
(5,'*****'
);

SELECT * FROM grades;

/* --------------- TABLE AUTHOR --------------- */
CREATE TABLE author(
author_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
author_name CHAR(100) NOT NULL CHECK(author_name != '')
)AUTO_INCREMENT = 1;

/* --------------- Insertion of table author --------------- */
INSERT INTO author(author_name)VALUE
('J K Rowling'),
('J. R. R. Tolkien'),
('F. Scott Fitzgerald'),
('Herman Melville'),
('Stephenie Meyer'),
('Lani Sarem'),
('Victoria Foyt'),
('Alice Sebold'),
('Attila Szabo'),
('Niclas Larson'),
('Gunilla Viklund'),
('Daniel Dufåker'),
('Mikael Marklund'
);

SELECT * FROM author;

/* --------------- TABLE CUSTOMER --------------- */
CREATE TABLE customer(
customer_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
social_security_number CHAR(13) NOT NULL DEFAULT 'YYYYMMDD-XXXX' CHECK(social_security_number != ''),
first_name CHAR(50) NOT NULL CHECK(first_name != ''),
last_name CHAR(50) NOT NULL CHECK(last_name != '')
)AUTO_INCREMENT = 1;

/* --------------- Insertion of table customer --------------- */
INSERT INTO customer(social_security_number, first_name, last_name)VALUE
('19500817-2317', 'Steven', 'Hero'),
('19750210-2530', 'Rikard', 'Binero'),
('19741203-1287', 'Linus', 'Lego'),
('20000101-2517', 'Steven', 'Hero'),
('19981030-1015', 'Erik', 'Svan'),
('19930310-1718', 'Chloe', 'Robertson'),
('19560102-2314', 'Bengt', 'Åke'
);

SELECT * FROM customer;

/* --------------- TABLE BOOK --------------- */
CREATE TABLE book(
isbn INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
book_title CHAR(100) NOT NULL CHECK (book_title != ''),
release_year INT NOT NULL,
grade_id INT NOT NULL,
FOREIGN KEY (grade_id) REFERENCES grades(grade_id)
)AUTO_INCREMENT = 98400;

/* --------------- Insertion of table book --------------- */
INSERT INTO book(book_title, release_year, grade_id)VALUE
('Harry Potter - PS', 2014, 4),
('The Hobbit', 2006, 4),
('The Great Gatsby', 1925,4),
('Moby dick',1851,5),
('Twilight',2005,5),
('Handbook for Mortals',2017,1),
('Revealing Eden', 2011, 2),
('The Almost Moon', 2007, 3 ),
('Matematik Origo 1B', 2011, 5
);

SELECT * FROM book;

/* --------------- TABLE BOOK_AUTHOR --------------- */
CREATE TABLE book_author(
isbn INT NOT NULL,
FOREIGN KEY (isbn) REFERENCES book(isbn),
author_id INT NOT NULL,
FOREIGN KEY (author_id) REFERENCES author(author_id)
);

/* --------------- Insertion of table book_author --------------- */
INSERT INTO book_author(isbn, author_id)VALUE
(98400,1),
(98401,2),
(98402,3),
(98403,4),
(98404,5),
(98405,6),
(98406,7),
(98407,8),
(98408,9),
(98408,10),
(98408,11),
(98408,12),
(98408,13
);

SELECT * FROM book_author;

/* --------------- TABLE BOOKS --------------- */
CREATE TABLE books(
book_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
isbn INT NOT NULL,
FOREIGN KEY (isbn) REFERENCES book(isbn)
)AUTO_INCREMENT = 1;

/* --------------- Insertion of table books --------------- */
INSERT INTO books(isbn)VALUE
(98400),
(98401),
(98402),
(98403),
(98404),
(98405),
(98406),
(98407),
(98408),
(98400),
(98401),
(98402),
(98403),
(98404),
(98405),
(98406),
(98407),
(98408),
(98400),
(98401),
(98402),
(98403),
(98404),
(98405),
(98406),
(98407),
(98408
);

SELECT * FROM books;

/* --------------- TABLE RENTAL --------------- */
CREATE TABLE rental(
rental_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
rent_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
expected_return_date TIMESTAMP GENERATED ALWAYS as (rent_date + INTERVAL 30 DAY),
return_status char(19) NOT NULL DEFAULT 'Pending',
customer_id INT NOT NULL,
book_id int NOT NULL,
FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
FOREIGN KEY (book_id) REFERENCES books(book_id)
)AUTO_INCREMENT = 1;

/* --------------- Insertion of table rental --------------- */
INSERT INTO rental(customer_id, book_id)VALUE
(1,1),
(1,2),
(2,3),
(2,4),
(3,5),
(3,6),
(4,7),
(4,8),
(5,9),
(5,10),
(6,12),
(6,13),
(7,14),
(7,15
);

SELECT * FROM rental;

/* --------------- Show total book information --------------- */
create VIEW bookInfo as
SELECT  books.book_id, books.isbn, book.book_title
FROM book
JOIN books
ON book.isbn = books.isbn
left JOIN rental
ON books.book_id = rental.book_id
WHERE rental.return_status LIKE '2%'
ORDER BY books.book_id;
;
SELECT * FROM bookinfo
;

-- create VIEW bookInfo as
-- SELECT books.book_id, books.isbn, book.book_title
-- FROM book
-- JOIN books ON book.isbn = books.isbn
-- join book_author on book_author.isbn = books.isbn
-- right join rental on rental.book_id = books.book_id
-- where rental.rent_date is null
-- ORDER BY books.book_id;
-- ;
-- SELECT * FROM bookinfo
-- ;

-- CREATE VIEW bookInfo as
-- SELECT  books.book_id, books.isbn, book.book_title, book.release_year, book.grade_id
-- FROM books
-- JOIN book
--      ON book.isbn = books.isbn
-- JOIN rental
--      ON books.book_id = rental.book_id
--   GROUP BY books.book_id
-- ;
-- SELECT * FROM bookinfo;

/* --------------- Show total book information --------------- */
Create VIEW bookInfoNotBorrowed as
SELECT  books.book_id, books.isbn, book.book_title, rental.rent_date
FROM book
JOIN books
ON book.isbn = books.isbn
left JOIN rental ON rental.book_id = books.book_id
 WHERE rental.rent_date is null
ORDER BY books.book_id
;
SELECT * FROM bookInfoNotBorrowed
;

/* --------------- List for "Recent" ISBN --------------- */
CREATE VIEW bookisbn AS
SELECT
  book.isbn
FROM book
ORDER BY isbn DESC
LIMIT 1
;
SELECT * FROM bookisbn;

/* --------------- Connection for book & author --------------- */
CREATE VIEW booksAuthorDetails AS
SELECT
  author.author_id AS 'AuthorId',
  author.author_name AS 'AuthorName',
  book.isbn AS 'Isbn',
  book.book_title AS 'BookTitle',
  book.release_year AS 'ReleaseYear',
  book.grade_id AS 'BookGrade'
FROM author
JOIN book_author ON author.author_id = book_author.author_id
JOIN book ON book_author.isbn = book.isbn
;
SELECT * FROM booksauthorDetails;

/* --------------- Customer list --------------- */
CREATE VIEW customerList AS
SELECT
customer.customer_id AS 'CustomerId',
customer.social_security_number AS 'SocialSecurityNumber' ,
ConCAT(customer.first_Name, ' ', customer.last_name) AS 'CustomerName',
book.book_title AS 'BookTitle',
books.book_id AS 'BookId',
rental.rent_date AS 'RentDate',
rental.expected_return_date AS 'ExpectedReturnDate',
rental.return_status AS 'ReturnStatus'
FROM Customer
JOIN Rental ON customer.customer_id = rental.customer_id
JOIN books ON books.book_id = rental.book_id
JOIN book ON books.isbn = book.isbn
;
SELECT * FROM customerList;

/* --------------- Customer list --------------- */
CREATE VIEW customerListWithUnicInfo AS
SELECT
customer.customer_id AS 'CustomerId',
customer.social_security_number AS 'SocialSecurityNumber' ,
books.book_id AS 'BookId',
book.book_title AS 'BookTitle',
rental.rent_date AS 'RentDate',
rental.return_status AS 'ReturnStatus'
FROM Customer
JOIN Rental ON customer.customer_id = rental.customer_id
JOIN books ON books.book_id = rental.book_id
JOIN book ON books.isbn = book.isbn
WHERE rental.return_status NOT LIKE '2%'
;
SELECT * FROM customerListWithUnicInfo;

/* --------------- Display titles of book ID's --------------- */
CREATE VIEW bookid AS
SELECT
   books.book_id AS BookId,
   book.book_title AS BookTitle
FROM book
JOIN books ON books.isbn = book.isbn
LEFT JOIN rental ON rental.book_id = books.book_id
WHERE rental.rent_date IS NULL OR rental.return_status LIKE '2%'
GROUP BY books.book_id
ORDER BY books.book_id
;
SELECT * FROM bookid;

/* --------------- Display amount of book editions --------------- */
create VIEW bookList AS
SELECT book.isbn, count(books.isbn )AS 'AmountOfEdition', book_title, release_year, grade_id
FROM BOOK
left JOIN books ON book.isbn = books.isbn
GROUP BY book.isbn;
SELECT * FROM bookList;
