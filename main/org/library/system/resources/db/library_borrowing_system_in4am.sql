-- ============================================================
-- DATABASE
-- ============================================================

DROP DATABASE IF EXISTS library_borrowing_system_in4am;

CREATE DATABASE library_borrowing_system_in4am
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;

USE library_borrowing_system_in4am;


-- ============================================================
-- 1. USER
-- ============================================================

CREATE TABLE users (
    user_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_code VARCHAR(30) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,

    user_role ENUM(
        'STUDENT',
        'LIBRARIAN',
        'MANAGER'
    ) NOT NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE
)
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;


-- ============================================================
-- 2. BOOK
-- ============================================================

CREATE TABLE book (
    book_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    isbn VARCHAR(20) NOT NULL,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(200) NOT NULL,
    publisher VARCHAR(150) NOT NULL,
    publication_year YEAR NOT NULL,
    total_stock INT UNSIGNED NOT NULL DEFAULT 0,
    available_stock INT UNSIGNED NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    PRIMARY KEY (book_id),

    UNIQUE KEY uk_book_isbn (isbn),

    KEY idx_book_title (title),
    KEY idx_book_author (author),
    KEY idx_book_active (active),

    CONSTRAINT chk_book_stock
        CHECK (available_stock <= total_stock)
);


-- ============================================================
-- 3. LOAN REQUEST
-- ============================================================

CREATE TABLE loan_request (
    request_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    student_id INT UNSIGNED NOT NULL,
    request_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    status ENUM(
        'PENDING',
        'APPROVED',
        'REJECTED',
        'CANCELLED'
    ) NOT NULL DEFAULT 'PENDING',

    observation VARCHAR(500) NULL,

    librarian_id INT UNSIGNED NULL,
    response_date DATETIME NULL,

    PRIMARY KEY (request_id),

    KEY idx_request_student (student_id),
    KEY idx_request_librarian (librarian_id),
    KEY idx_request_status (status),
    KEY idx_request_date (request_date),

    CONSTRAINT fk_request_student
        FOREIGN KEY (student_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_request_librarian
        FOREIGN KEY (librarian_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);


-- ============================================================
-- 4. LOAN REQUEST DETAIL
-- ============================================================

CREATE TABLE loan_request_detail (
    request_detail_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    request_id INT UNSIGNED NOT NULL,
    book_id INT UNSIGNED NOT NULL,
    quantity INT UNSIGNED NOT NULL,

    PRIMARY KEY (request_detail_id),

    KEY idx_request_detail_request (request_id),
    KEY idx_request_detail_book (book_id),

    CONSTRAINT fk_request_detail_request
        FOREIGN KEY (request_id)
        REFERENCES loan_request(request_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_request_detail_book
        FOREIGN KEY (book_id)
        REFERENCES book(book_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT chk_request_detail_quantity
        CHECK (quantity > 0)
);


-- ============================================================
-- 5. LOAN
-- ============================================================

CREATE TABLE loan (
    loan_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    request_id INT UNSIGNED NOT NULL,
    student_id INT UNSIGNED NOT NULL,
    librarian_id INT UNSIGNED NOT NULL,

    loan_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    due_date DATE NOT NULL,

    status ENUM(
        'ACTIVE',
        'RETURNED',
        'OVERDUE'
    ) NOT NULL DEFAULT 'ACTIVE',

    PRIMARY KEY (loan_id),

    UNIQUE KEY uk_loan_request (request_id),

    KEY idx_loan_student (student_id),
    KEY idx_loan_librarian (librarian_id),
    KEY idx_loan_status (status),
    KEY idx_loan_due_date (due_date),

    CONSTRAINT fk_loan_request
        FOREIGN KEY (request_id)
        REFERENCES loan_request(request_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_loan_student
        FOREIGN KEY (student_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_loan_librarian
        FOREIGN KEY (librarian_id)
        REFERENCES users(user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);


-- ============================================================
-- 6. LOAN DETAIL
-- ============================================================

CREATE TABLE loan_detail (
    loan_detail_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    loan_id INT UNSIGNED NOT NULL,
    book_id INT UNSIGNED NOT NULL,
    quantity INT UNSIGNED NOT NULL,
    returned_quantity INT UNSIGNED NOT NULL DEFAULT 0,

    PRIMARY KEY (loan_detail_id),

    KEY idx_loan_detail_loan (loan_id),
    KEY idx_loan_detail_book (book_id),

    CONSTRAINT fk_loan_detail_loan
        FOREIGN KEY (loan_id)
        REFERENCES loan(loan_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_loan_detail_book
        FOREIGN KEY (book_id)
        REFERENCES book(book_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT chk_loan_detail_quantity
        CHECK (quantity > 0),

    CONSTRAINT chk_loan_detail_returned
        CHECK (returned_quantity <= quantity)
);


-- ============================================================
-- STORED PROCEDURES
-- USER LOGIN
-- ============================================================

DELIMITER $$


-- ============================================================
-- USER LOGIN
-- ============================================================
-- The application should hash the password and send the hash
-- to this procedure.
--
-- Returns the user information when:
--   1. The user code exists
--   2. The password hash matches
--   3. The user is active
-- ============================================================

-- ============================================================
-- USER LOGIN
-- ============================================================

DELIMITER $$

CREATE PROCEDURE sp_user_login(
    IN p_user_code VARCHAR(30)
)
BEGIN
    SELECT
        user_id,
        user_code,
        first_name,
        last_name,
        email,
        password_hash,
        user_role,
        active
    FROM users
    WHERE user_code = p_user_code
      AND active = TRUE;
END $$

DELIMITER ;


-- ============================================================
-- CHECK USER BY CODE
-- ============================================================

DELIMITER $$

CREATE PROCEDURE sp_user_exists_by_code(
    IN p_user_code VARCHAR(30)
)
BEGIN
    SELECT
        user_id,
        user_code,
        first_name,
        last_name,
        email,
        user_role,
        active
    FROM users
    WHERE user_code = p_user_code;
END $$

DELIMITER ;

-- ============================================================
-- CHECK USER BY EMAIL
-- ============================================================

DELIMITER $$

CREATE PROCEDURE sp_user_exists_by_email(
    IN p_email VARCHAR(150)
)
BEGIN
    SELECT
        user_id,
        user_code,
        first_name,
        last_name,
        email,
        user_role,
        active
    FROM users
    WHERE email = p_email;
END $$

DELIMITER ;


-- ============================================================
-- CREATE USER
-- ============================================================

DELIMITER $$

CREATE PROCEDURE sp_user_create(
    IN p_user_code VARCHAR(30),
    IN p_first_name VARCHAR(100),
    IN p_last_name VARCHAR(100),
    IN p_email VARCHAR(150),
    IN p_password_hash VARCHAR(255),
    IN p_user_role VARCHAR(20)
)
BEGIN
    INSERT INTO users (
        user_code,
        first_name,
        last_name,
        email,
        password_hash,
        user_role,
        active
    )
    VALUES (
        p_user_code,
        p_first_name,
        p_last_name,
        p_email,
        p_password_hash,
        p_user_role,
        TRUE
    );

    SELECT LAST_INSERT_ID() AS user_id;
END $$

DELIMITER ;


-- ============================================================
-- READ USER
-- ============================================================

DELIMITER $$

CREATE PROCEDURE sp_user_read(
    IN p_user_id INT
)
BEGIN
    SELECT
        user_id,
        user_code,
        first_name,
        last_name,
        email,
        user_role,
        active
    FROM users
    WHERE user_id = p_user_id;
END $$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE sp_user_update(
    IN p_user_id INT,
    IN p_user_code VARCHAR(30),
    IN p_first_name VARCHAR(100),
    IN p_last_name VARCHAR(100),
    IN p_email VARCHAR(150),
    IN p_password_hash VARCHAR(255),
    IN p_user_role VARCHAR(20),
    IN p_active BOOLEAN
)
BEGIN
    UPDATE users
    SET
        user_code = p_user_code,
        first_name = p_first_name,
        last_name = p_last_name,
        email = p_email,
        password_hash = p_password_hash,
        user_role = p_user_role,
        active = p_active
    WHERE user_id = p_user_id;

    SELECT ROW_COUNT() AS affected_rows;
END $$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE sp_user_delete(
    IN p_user_id INT
)
BEGIN
    DELETE FROM users
    WHERE user_id = p_user_id;

    SELECT ROW_COUNT() AS affected_rows;
END $$

DELIMITER ;
DELIMITER $$

CREATE PROCEDURE sp_user_search(
    IN p_search VARCHAR(150)
)
BEGIN
    SELECT
        user_id,
        user_code,
        first_name,
        last_name,
        email,
        user_role,
        active
    FROM users
    WHERE user_code LIKE CONCAT('%', p_search, '%')
       OR first_name LIKE CONCAT('%', p_search, '%')
       OR last_name LIKE CONCAT('%', p_search, '%')
       OR email LIKE CONCAT('%', p_search, '%')
    ORDER BY first_name, last_name;
END $$

DELIMITER ;

-- ============================================================
-- BOOK CRUD
-- ============================================================

-- ------------------------------------------------------------
-- CREATE BOOK
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_book_create(
    IN p_isbn VARCHAR(20),
    IN p_title VARCHAR(255),
    IN p_author VARCHAR(200),
    IN p_publisher VARCHAR(150),
    IN p_publication_year YEAR,
    IN p_total_stock INT UNSIGNED,
    IN p_available_stock INT UNSIGNED
)
BEGIN
    INSERT INTO book (
        isbn,
        title,
        author,
        publisher,
        publication_year,
        total_stock,
        available_stock,
        active
    )
    VALUES (
        p_isbn,
        p_title,
        p_author,
        p_publisher,
        p_publication_year,
        p_total_stock,
        p_available_stock,
        TRUE
    );

    SELECT LAST_INSERT_ID() AS book_id;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- READ BOOK
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_book_read(
    IN p_book_id INT UNSIGNED
)
BEGIN
    SELECT
        book_id,
        isbn,
        title,
        author,
        publisher,
        publication_year,
        total_stock,
        available_stock,
        active
    FROM book
    WHERE book_id = p_book_id;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- UPDATE BOOK
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_book_update(
    IN p_book_id INT UNSIGNED,
    IN p_isbn VARCHAR(20),
    IN p_title VARCHAR(255),
    IN p_author VARCHAR(200),
    IN p_publisher VARCHAR(150),
    IN p_publication_year YEAR,
    IN p_total_stock INT UNSIGNED,
    IN p_available_stock INT UNSIGNED,
    IN p_active BOOLEAN
)
BEGIN
    UPDATE book
    SET
        isbn = p_isbn,
        title = p_title,
        author = p_author,
        publisher = p_publisher,
        publication_year = p_publication_year,
        total_stock = p_total_stock,
        available_stock = p_available_stock,
        active = p_active
    WHERE book_id = p_book_id;

    SELECT ROW_COUNT() AS affected_rows;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- DELETE BOOK
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_book_delete(
    IN p_book_id INT UNSIGNED
)
BEGIN
    DELETE FROM book
    WHERE book_id = p_book_id;

    SELECT ROW_COUNT() AS affected_rows;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- SEARCH BOOK
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_book_search(
    IN p_search VARCHAR(255)
)
BEGIN
    SELECT
        book_id,
        isbn,
        title,
        author,
        publisher,
        publication_year,
        total_stock,
        available_stock,
        active
    FROM book
    WHERE isbn LIKE CONCAT('%', p_search, '%')
       OR title LIKE CONCAT('%', p_search, '%')
       OR author LIKE CONCAT('%', p_search, '%')
       OR publisher LIKE CONCAT('%', p_search, '%')
    ORDER BY title;
END $$

DELIMITER ;


-- ============================================================
-- LOAN REQUEST CRUD
-- ============================================================

-- ------------------------------------------------------------
-- CREATE LOAN REQUEST
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_request_create(
    IN p_student_id INT UNSIGNED,
    IN p_observation VARCHAR(500)
)
BEGIN
    INSERT INTO loan_request (
        student_id,
        observation
    )
    VALUES (
        p_student_id,
        p_observation
    );

    SELECT LAST_INSERT_ID() AS request_id;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- READ LOAN REQUEST
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_request_read(
    IN p_request_id INT UNSIGNED
)
BEGIN
    SELECT
        lr.request_id,
        lr.student_id,
        u.first_name,
        u.last_name,
        u.user_code,
        lr.request_date,
        lr.status,
        lr.observation,
        lr.librarian_id,
        lr.response_date
    FROM loan_request lr
    INNER JOIN users u
        ON lr.student_id = u.user_id
    WHERE lr.request_id = p_request_id;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- UPDATE LOAN REQUEST
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_request_update(
    IN p_request_id INT UNSIGNED,
    IN p_status VARCHAR(20),
    IN p_observation VARCHAR(500),
    IN p_librarian_id INT UNSIGNED,
    IN p_response_date DATETIME
)
BEGIN
    UPDATE loan_request
    SET
        status = p_status,
        observation = p_observation,
        librarian_id = p_librarian_id,
        response_date = p_response_date
    WHERE request_id = p_request_id;

    SELECT ROW_COUNT() AS affected_rows;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- DELETE LOAN REQUEST
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_request_delete(
    IN p_request_id INT UNSIGNED
)
BEGIN
    DELETE FROM loan_request
    WHERE request_id = p_request_id;

    SELECT ROW_COUNT() AS affected_rows;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- SEARCH LOAN REQUEST
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_request_search(
    IN p_search VARCHAR(255)
)
BEGIN
    SELECT
        lr.request_id,
        lr.student_id,
        u.user_code,
        u.first_name,
        u.last_name,
        lr.request_date,
        lr.status,
        lr.observation,
        lr.librarian_id,
        lr.response_date
    FROM loan_request lr
    INNER JOIN users u
        ON lr.student_id = u.user_id
    WHERE CAST(lr.request_id AS CHAR) LIKE CONCAT('%', p_search, '%')
       OR u.user_code LIKE CONCAT('%', p_search, '%')
       OR u.first_name LIKE CONCAT('%', p_search, '%')
       OR u.last_name LIKE CONCAT('%', p_search, '%')
       OR lr.status LIKE CONCAT('%', p_search, '%')
    ORDER BY lr.request_date DESC;
END $$

DELIMITER ;


-- ============================================================
-- LOAN REQUEST DETAIL CRUD
-- ============================================================

-- ------------------------------------------------------------
-- CREATE LOAN REQUEST DETAIL
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_request_detail_create(
    IN p_request_id INT UNSIGNED,
    IN p_book_id INT UNSIGNED,
    IN p_quantity INT UNSIGNED
)
BEGIN
    INSERT INTO loan_request_detail (
        request_id,
        book_id,
        quantity
    )
    VALUES (
        p_request_id,
        p_book_id,
        p_quantity
    );

    SELECT LAST_INSERT_ID() AS request_detail_id;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- READ LOAN REQUEST DETAIL
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_request_detail_read(
    IN p_request_detail_id INT UNSIGNED
)
BEGIN
    SELECT
        lrd.request_detail_id,
        lrd.request_id,
        lrd.book_id,
        b.isbn,
        b.title,
        lrd.quantity
    FROM loan_request_detail lrd
    INNER JOIN book b
        ON lrd.book_id = b.book_id
    WHERE lrd.request_detail_id = p_request_detail_id;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- UPDATE LOAN REQUEST DETAIL
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_request_detail_update(
    IN p_request_detail_id INT UNSIGNED,
    IN p_book_id INT UNSIGNED,
    IN p_quantity INT UNSIGNED
)
BEGIN
    UPDATE loan_request_detail
    SET
        book_id = p_book_id,
        quantity = p_quantity
    WHERE request_detail_id = p_request_detail_id;

    SELECT ROW_COUNT() AS affected_rows;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- DELETE LOAN REQUEST DETAIL
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_request_detail_delete(
    IN p_request_detail_id INT UNSIGNED
)
BEGIN
    DELETE FROM loan_request_detail
    WHERE request_detail_id = p_request_detail_id;

    SELECT ROW_COUNT() AS affected_rows;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- SEARCH LOAN REQUEST DETAIL
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_request_detail_search(
    IN p_search VARCHAR(255)
)
BEGIN
    SELECT
        lrd.request_detail_id,
        lrd.request_id,
        lrd.book_id,
        b.isbn,
        b.title,
        lrd.quantity
    FROM loan_request_detail lrd
    INNER JOIN book b
        ON lrd.book_id = b.book_id
    WHERE CAST(lrd.request_id AS CHAR) LIKE CONCAT('%', p_search, '%')
       OR b.isbn LIKE CONCAT('%', p_search, '%')
       OR b.title LIKE CONCAT('%', p_search, '%')
    ORDER BY lrd.request_id DESC;
END $$

DELIMITER ;


-- ============================================================
-- LOAN CRUD
-- ============================================================

-- ------------------------------------------------------------
-- CREATE LOAN
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_create(
    IN p_request_id INT UNSIGNED,
    IN p_student_id INT UNSIGNED,
    IN p_librarian_id INT UNSIGNED,
    IN p_due_date DATE
)
BEGIN
    INSERT INTO loan (
        request_id,
        student_id,
        librarian_id,
        due_date
    )
    VALUES (
        p_request_id,
        p_student_id,
        p_librarian_id,
        p_due_date
    );

    SELECT LAST_INSERT_ID() AS loan_id;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- READ LOAN
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_read(
    IN p_loan_id INT UNSIGNED
)
BEGIN
    SELECT
        l.loan_id,
        l.request_id,
        l.student_id,
        student.user_code AS student_code,
        student.first_name AS student_first_name,
        student.last_name AS student_last_name,
        l.librarian_id,
        librarian.user_code AS librarian_code,
        librarian.first_name AS librarian_first_name,
        librarian.last_name AS librarian_last_name,
        l.loan_date,
        l.due_date,
        l.status
    FROM loan l
    INNER JOIN users student
        ON l.student_id = student.user_id
    INNER JOIN users librarian
        ON l.librarian_id = librarian.user_id
    WHERE l.loan_id = p_loan_id;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- UPDATE LOAN
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_update(
    IN p_loan_id INT UNSIGNED,
    IN p_librarian_id INT UNSIGNED,
    IN p_due_date DATE,
    IN p_status VARCHAR(20)
)
BEGIN
    UPDATE loan
    SET
        librarian_id = p_librarian_id,
        due_date = p_due_date,
        status = p_status
    WHERE loan_id = p_loan_id;

    SELECT ROW_COUNT() AS affected_rows;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- DELETE LOAN
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_delete(
    IN p_loan_id INT UNSIGNED
)
BEGIN
    DELETE FROM loan
    WHERE loan_id = p_loan_id;

    SELECT ROW_COUNT() AS affected_rows;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- SEARCH LOAN
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_search(
    IN p_search VARCHAR(255)
)
BEGIN
    SELECT
        l.loan_id,
        l.request_id,
        l.student_id,
        student.user_code AS student_code,
        student.first_name AS student_first_name,
        student.last_name AS student_last_name,
        l.librarian_id,
        librarian.user_code AS librarian_code,
        librarian.first_name AS librarian_first_name,
        librarian.last_name AS librarian_last_name,
        l.loan_date,
        l.due_date,
        l.status
    FROM loan l
    INNER JOIN users student
        ON l.student_id = student.user_id
    INNER JOIN users librarian
        ON l.librarian_id = librarian.user_id
    WHERE CAST(l.loan_id AS CHAR) LIKE CONCAT('%', p_search, '%')
       OR student.user_code LIKE CONCAT('%', p_search, '%')
       OR student.first_name LIKE CONCAT('%', p_search, '%')
       OR student.last_name LIKE CONCAT('%', p_search, '%')
       OR l.status LIKE CONCAT('%', p_search, '%')
    ORDER BY l.loan_date DESC;
END $$

DELIMITER ;


-- ============================================================
-- LOAN DETAIL CRUD
-- ============================================================

-- ------------------------------------------------------------
-- CREATE LOAN DETAIL
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_detail_create(
    IN p_loan_id INT UNSIGNED,
    IN p_book_id INT UNSIGNED,
    IN p_quantity INT UNSIGNED
)
BEGIN
    INSERT INTO loan_detail (
        loan_id,
        book_id,
        quantity,
        returned_quantity
    )
    VALUES (
        p_loan_id,
        p_book_id,
        p_quantity,
        0
    );

    SELECT LAST_INSERT_ID() AS loan_detail_id;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- READ LOAN DETAIL
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_detail_read(
    IN p_loan_detail_id INT UNSIGNED
)
BEGIN
    SELECT
        ld.loan_detail_id,
        ld.loan_id,
        ld.book_id,
        b.isbn,
        b.title,
        ld.quantity,
        ld.returned_quantity
    FROM loan_detail ld
    INNER JOIN book b
        ON ld.book_id = b.book_id
    WHERE ld.loan_detail_id = p_loan_detail_id;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- UPDATE LOAN DETAIL
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_detail_update(
    IN p_loan_detail_id INT UNSIGNED,
    IN p_book_id INT UNSIGNED,
    IN p_quantity INT UNSIGNED,
    IN p_returned_quantity INT UNSIGNED
)
BEGIN
    UPDATE loan_detail
    SET
        book_id = p_book_id,
        quantity = p_quantity,
        returned_quantity = p_returned_quantity
    WHERE loan_detail_id = p_loan_detail_id;

    SELECT ROW_COUNT() AS affected_rows;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- DELETE LOAN DETAIL
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_detail_delete(
    IN p_loan_detail_id INT UNSIGNED
)
BEGIN
    DELETE FROM loan_detail
    WHERE loan_detail_id = p_loan_detail_id;

    SELECT ROW_COUNT() AS affected_rows;
END $$

DELIMITER ;


-- ------------------------------------------------------------
-- SEARCH LOAN DETAIL
-- ------------------------------------------------------------

DELIMITER $$

CREATE PROCEDURE sp_loan_detail_search(
    IN p_search VARCHAR(255)
)
BEGIN
    SELECT
        ld.loan_detail_id,
        ld.loan_id,
        ld.book_id,
        b.isbn,
        b.title,
        ld.quantity,
        ld.returned_quantity
    FROM loan_detail ld
    INNER JOIN book b
        ON ld.book_id = b.book_id
    WHERE CAST(ld.loan_id AS CHAR) LIKE CONCAT('%', p_search, '%')
       OR b.isbn LIKE CONCAT('%', p_search, '%')
       OR b.title LIKE CONCAT('%', p_search, '%')
    ORDER BY ld.loan_id DESC;
END $$

DELIMITER ;