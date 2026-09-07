DROP DATABASE IF EXISTS library_borrowing_system_in4am;
CREATE DATABASE library_borrowing_system_in4am
 CHARACTER SET utf8mb4
COLLATE utf8mb4_0900_ai_ci;
USE library_borrowing_system_in4am;

-- ============================================================
-- 1. TABLES
-- ============================================================

CREATE TABLE role (
    role_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (role_id),
    UNIQUE KEY uk_role_name (name)
) ENGINE=InnoDB;

CREATE TABLE app_user (
    user_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    role_id INT UNSIGNED NOT NULL,
    institutional_code VARCHAR(30) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (user_id),
    UNIQUE KEY uk_user_institutional_code (institutional_code),
    KEY idx_user_role (role_id),
    CONSTRAINT fk_user_role
        FOREIGN KEY (role_id) REFERENCES role(role_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE audit_log (
    audit_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id INT UNSIGNED NOT NULL,
    action VARCHAR(255) NOT NULL,
    audit_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (audit_id),
    KEY idx_audit_user (user_id),
    KEY idx_audit_date (audit_date),
    CONSTRAINT fk_audit_user
        FOREIGN KEY (user_id) REFERENCES app_user(user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE category (
    category_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    PRIMARY KEY (category_id),
    UNIQUE KEY uk_category_name (name)
) ENGINE=InnoDB;

CREATE TABLE author (
    author_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    nationality VARCHAR(100) NOT NULL,
    PRIMARY KEY (author_id),
    KEY idx_author_name (name)
) ENGINE=InnoDB;

CREATE TABLE book (
    book_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    isbn VARCHAR(20) NOT NULL,
    title VARCHAR(255) NOT NULL,
    category_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (book_id),
    UNIQUE KEY uk_book_isbn (isbn),
    KEY idx_book_title (title),
    KEY idx_book_category (category_id),
    CONSTRAINT fk_book_category
        FOREIGN KEY (category_id) REFERENCES category(category_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE book_author (
    book_id INT UNSIGNED NOT NULL,
    author_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (book_id, author_id),
    KEY idx_book_author_author (author_id),
    CONSTRAINT fk_book_author_book
        FOREIGN KEY (book_id) REFERENCES book(book_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_book_author_author
        FOREIGN KEY (author_id) REFERENCES author(author_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE copy (
    copy_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    book_id INT UNSIGNED NOT NULL,
    barcode VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE',
    PRIMARY KEY (copy_id),
    UNIQUE KEY uk_copy_barcode (barcode),
    KEY idx_copy_book (book_id),
    KEY idx_copy_status (status),
    CONSTRAINT fk_copy_book
        FOREIGN KEY (book_id) REFERENCES book(book_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE loan_request (
    request_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    student_id INT UNSIGNED NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    request_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (request_id),
    KEY idx_request_student (student_id),
    KEY idx_request_status (status),
    KEY idx_request_date (request_date),
    CONSTRAINT fk_request_student
        FOREIGN KEY (student_id) REFERENCES app_user(user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE loan_request_detail (
    request_id INT UNSIGNED NOT NULL,
    copy_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (request_id, copy_id),
    KEY idx_request_detail_copy (copy_id),
    CONSTRAINT fk_request_detail_request
        FOREIGN KEY (request_id) REFERENCES loan_request(request_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_request_detail_copy
        FOREIGN KEY (copy_id) REFERENCES copy(copy_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE loan (
    loan_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    request_id INT UNSIGNED NOT NULL,
    student_id INT UNSIGNED NOT NULL,
    librarian_id INT UNSIGNED NOT NULL,
    due_date DATE NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    PRIMARY KEY (loan_id),
    UNIQUE KEY uk_loan_request (request_id),
    KEY idx_loan_student (student_id),
    KEY idx_loan_librarian (librarian_id),
    KEY idx_loan_status (status),
    KEY idx_loan_due_date (due_date),
    CONSTRAINT fk_loan_request
        FOREIGN KEY (request_id) REFERENCES loan_request(request_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_loan_student
        FOREIGN KEY (student_id) REFERENCES app_user(user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_loan_librarian
        FOREIGN KEY (librarian_id) REFERENCES app_user(user_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE loan_detail (
    loan_id INT UNSIGNED NOT NULL,
    copy_id INT UNSIGNED NOT NULL,
    PRIMARY KEY (loan_id, copy_id),
    KEY idx_loan_detail_copy (copy_id),
    CONSTRAINT fk_loan_detail_loan
        FOREIGN KEY (loan_id) REFERENCES loan(loan_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_loan_detail_copy
        FOREIGN KEY (copy_id) REFERENCES copy(copy_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE receipt (
    receipt_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    loan_id INT UNSIGNED NOT NULL,
    issue_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (receipt_id),
    UNIQUE KEY uk_receipt_loan (loan_id),
    KEY idx_receipt_issue_date (issue_date),
    CONSTRAINT fk_receipt_loan
        FOREIGN KEY (loan_id) REFERENCES loan(loan_id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ============================================================
-- 2. CRUD + SEARCH PROCEDURES
-- Convention:
--   p_create = CREATE
--   p_read   = READ by PK
--   p_update = UPDATE
--   p_delete = DELETE
--   p_search = SEARCH by text/criteria
-- ============================================================

DELIMITER $$

-- ========================= ROLE =============================

CREATE PROCEDURE sp_role_create(
    IN p_name VARCHAR(50)
)
BEGIN
    INSERT INTO role(name) VALUES (p_name);
    SELECT LAST_INSERT_ID() AS role_id;
END$$

CREATE PROCEDURE sp_role_read(IN p_role_id INT UNSIGNED)
BEGIN
    SELECT * FROM role WHERE role_id = p_role_id;
END$$

CREATE PROCEDURE sp_role_update(
    IN p_role_id INT UNSIGNED,
    IN p_name VARCHAR(50)
)
BEGIN
    UPDATE role SET name = p_name WHERE role_id = p_role_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_role_delete(IN p_role_id INT UNSIGNED)
BEGIN
    DELETE FROM role WHERE role_id = p_role_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_role_search(IN p_term VARCHAR(100))
BEGIN
    SELECT *
    FROM role
    WHERE name LIKE CONCAT('%', p_term, '%')
    ORDER BY name;
END$$

-- ======================= USUARIO ============================

CREATE PROCEDURE sp_user_create(
    IN p_role_id INT UNSIGNED,
    IN p_institutional_code VARCHAR(30),
    IN p_password_hash VARCHAR(255),
    IN p_active BOOLEAN
)
BEGIN
    INSERT INTO app_user(role_id,institutional_code,password_hash,active)
    VALUES(p_role_id,p_institutional_code,p_password_hash,COALESCE(p_active,TRUE));
    SELECT LAST_INSERT_ID() AS user_id;
END$$

CREATE PROCEDURE sp_user_read(IN p_user_id INT UNSIGNED)
BEGIN
    SELECT u.*, r.name AS role
    FROM app_user u
    INNER JOIN role r ON r.role_id = u.role_id
    WHERE u.user_id = p_user_id;
END$$

CREATE PROCEDURE sp_user_update(
    IN p_user_id INT UNSIGNED,
    IN p_role_id INT UNSIGNED,
    IN p_institutional_code VARCHAR(30),
    IN p_password_hash VARCHAR(255),
    IN p_active BOOLEAN
)
BEGIN
    UPDATE app_user
    SET role_id=p_role_id,
        institutional_code=p_institutional_code,
        password_hash=p_password_hash,
        active=p_active
    WHERE user_id=p_user_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_user_delete(IN p_user_id INT UNSIGNED)
BEGIN
    DELETE FROM app_user WHERE user_id=p_user_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_user_search(IN p_term VARCHAR(100))
BEGIN
    SELECT u.user_id,u.institutional_code,u.active,
           r.role_id,r.name AS role
    FROM app_user u
    INNER JOIN role r ON r.role_id=u.role_id
    WHERE u.institutional_code LIKE CONCAT('%',p_term,'%')
       OR r.name LIKE CONCAT('%',p_term,'%')
    ORDER BY u.institutional_code;
END$$

-- ====================== AUDITORIA ============================

CREATE PROCEDURE sp_audit_log_create(
    IN p_user_id INT UNSIGNED,
    IN p_action VARCHAR(255),
    IN p_date DATETIME
)
BEGIN
    INSERT INTO audit_log(user_id,action,date)
    VALUES(p_user_id,p_action,COALESCE(p_date,CURRENT_TIMESTAMP));
    SELECT LAST_INSERT_ID() AS audit_id;
END$$

CREATE PROCEDURE sp_audit_log_read(IN p_audit_id BIGINT UNSIGNED)
BEGIN
    SELECT a.*,u.institutional_code
    FROM audit_log a
    INNER JOIN app_user u ON u.user_id=a.user_id
    WHERE a.audit_id=p_audit_id;
END$$

CREATE PROCEDURE sp_audit_log_update(
    IN p_audit_id BIGINT UNSIGNED,
    IN p_user_id INT UNSIGNED,
    IN p_action VARCHAR(255),
    IN p_date DATETIME
)
BEGIN
    UPDATE audit_log
    SET user_id=p_user_id,action=p_action,date=p_date
    WHERE audit_id=p_audit_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_audit_log_delete(IN p_audit_id BIGINT UNSIGNED)
BEGIN
    DELETE FROM audit_log WHERE audit_id=p_audit_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_audit_log_search(IN p_term VARCHAR(150))
BEGIN
    SELECT a.*,u.institutional_code
    FROM audit_log a
    INNER JOIN app_user u ON u.user_id=a.user_id
    WHERE a.action LIKE CONCAT('%',p_term,'%')
       OR u.institutional_code LIKE CONCAT('%',p_term,'%')
    ORDER BY a.audit_date DESC;
END$$

-- ====================== CATEGORIA ============================

CREATE PROCEDURE sp_category_create(
    IN p_name VARCHAR(100),
    IN p_description VARCHAR(500)
)
BEGIN
    INSERT INTO category(name,description)
    VALUES(p_name,p_description);
    SELECT LAST_INSERT_ID() AS category_id;
END$$

CREATE PROCEDURE sp_category_read(IN p_category_id INT UNSIGNED)
BEGIN
    SELECT * FROM category WHERE category_id=p_category_id;
END$$

CREATE PROCEDURE sp_category_update(
    IN p_category_id INT UNSIGNED,
    IN p_name VARCHAR(100),
    IN p_description VARCHAR(500)
)
BEGIN
    UPDATE category SET name=p_name,description=p_description
    WHERE category_id=p_category_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_category_delete(IN p_category_id INT UNSIGNED)
BEGIN
    DELETE FROM category WHERE category_id=p_category_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_category_search(IN p_term VARCHAR(150))
BEGIN
    SELECT * FROM category
    WHERE name LIKE CONCAT('%',p_term,'%')
       OR description LIKE CONCAT('%',p_term,'%')
    ORDER BY name;
END$$

-- ========================= AUTOR =============================

CREATE PROCEDURE sp_author_create(
    IN p_name VARCHAR(150),
    IN p_nationality VARCHAR(100)
)
BEGIN
    INSERT INTO author(name,nationality)
    VALUES(p_name,p_nationality);
    SELECT LAST_INSERT_ID() AS author_id;
END$$

CREATE PROCEDURE sp_author_read(IN p_author_id INT UNSIGNED)
BEGIN
    SELECT * FROM author WHERE author_id=p_author_id;
END$$

CREATE PROCEDURE sp_author_update(
    IN p_author_id INT UNSIGNED,
    IN p_name VARCHAR(150),
    IN p_nationality VARCHAR(100)
)
BEGIN
    UPDATE author SET name=p_name,nationality=p_nationality
    WHERE author_id=p_author_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_author_delete(IN p_author_id INT UNSIGNED)
BEGIN
    DELETE FROM author WHERE author_id=p_author_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_author_search(IN p_term VARCHAR(150))
BEGIN
    SELECT * FROM author
    WHERE name LIKE CONCAT('%',p_term,'%')
       OR nationality LIKE CONCAT('%',p_term,'%')
    ORDER BY name;
END$$

-- ========================== LIBRO ============================

CREATE PROCEDURE sp_book_create(
    IN p_isbn VARCHAR(20),
    IN p_title VARCHAR(255),
    IN p_category_id INT UNSIGNED
)
BEGIN
    INSERT INTO book(isbn,title,category_id)
    VALUES(p_isbn,p_title,p_category_id);
    SELECT LAST_INSERT_ID() AS book_id;
END$$

CREATE PROCEDURE sp_book_read(IN p_book_id INT UNSIGNED)
BEGIN
    SELECT l.*,c.name AS category
    FROM book l
    INNER JOIN category c ON c.category_id=l.category_id
    WHERE l.book_id=p_book_id;
END$$

CREATE PROCEDURE sp_book_update(
    IN p_book_id INT UNSIGNED,
    IN p_isbn VARCHAR(20),
    IN p_title VARCHAR(255),
    IN p_category_id INT UNSIGNED
)
BEGIN
    UPDATE book
    SET isbn=p_isbn,title=p_title,category_id=p_category_id
    WHERE book_id=p_book_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_book_delete(IN p_book_id INT UNSIGNED)
BEGIN
    DELETE FROM book WHERE book_id=p_book_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_book_search(IN p_term VARCHAR(255))
BEGIN
    SELECT l.book_id,l.isbn,l.title,c.name AS category
    FROM book l
    INNER JOIN category c ON c.category_id=l.category_id
    WHERE l.isbn LIKE CONCAT('%',p_term,'%')
       OR l.title LIKE CONCAT('%',p_term,'%')
       OR c.name LIKE CONCAT('%',p_term,'%')
    ORDER BY l.title;
END$$

-- ======================= LIBRO_AUTOR =========================

CREATE PROCEDURE sp_book_author_create(
    IN p_book_id INT UNSIGNED,
    IN p_author_id INT UNSIGNED
)
BEGIN
    INSERT INTO book_author(book_id,author_id)
    VALUES(p_book_id,p_author_id);
END$$

CREATE PROCEDURE sp_book_author_read(
    IN p_book_id INT UNSIGNED,
    IN p_author_id INT UNSIGNED
)
BEGIN
    SELECT la.*,l.title,a.name AS author
    FROM book_author la
    INNER JOIN book l ON l.book_id=la.book_id
    INNER JOIN author a ON a.author_id=la.author_id
    WHERE la.book_id=p_book_id AND la.author_id=p_author_id;
END$$

CREATE PROCEDURE sp_book_author_update(
    IN p_book_id INT UNSIGNED,
    IN p_author_id INT UNSIGNED,
    IN p_new_book_id INT UNSIGNED,
    IN p_new_author_id INT UNSIGNED
)
BEGIN
    UPDATE book_author
    SET book_id=p_new_book_id,author_id=p_new_author_id
    WHERE book_id=p_book_id AND author_id=p_author_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_book_author_delete(
    IN p_book_id INT UNSIGNED,
    IN p_author_id INT UNSIGNED
)
BEGIN
    DELETE FROM book_author
    WHERE book_id=p_book_id AND author_id=p_author_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_book_author_search(IN p_term VARCHAR(255))
BEGIN
    SELECT la.book_id, l.title, la.author_id, a.name AS author
    FROM book_author la
    INNER JOIN book l ON l.book_id=la.book_id
    INNER JOIN author a ON a.author_id=la.author_id
    WHERE l.title LIKE CONCAT('%',p_term,'%')
       OR a.name LIKE CONCAT('%',p_term,'%')
    ORDER BY l.title,a.name;
END$$

-- ========================= EJEMPLAR ==========================

CREATE PROCEDURE sp_copy_create(
    IN p_book_id INT UNSIGNED,
    IN p_barcode VARCHAR(50),
    IN p_status VARCHAR(30)
)
BEGIN
    INSERT INTO copy(book_id,barcode,status)
    VALUES(p_book_id,p_barcode,COALESCE(p_status,'AVAILABLE'));
    SELECT LAST_INSERT_ID() AS copy_id;
END$$

CREATE PROCEDURE sp_copy_read(IN p_copy_id INT UNSIGNED)
BEGIN
    SELECT e.*,l.isbn,l.title
    FROM copy e
    INNER JOIN book l ON l.book_id=e.book_id
    WHERE e.copy_id=p_copy_id;
END$$

CREATE PROCEDURE sp_copy_update(
    IN p_copy_id INT UNSIGNED,
    IN p_book_id INT UNSIGNED,
    IN p_barcode VARCHAR(50),
    IN p_status VARCHAR(30)
)
BEGIN
    UPDATE copy
    SET book_id=p_book_id,barcode=p_barcode,status=p_status
    WHERE copy_id=p_copy_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_copy_delete(IN p_copy_id INT UNSIGNED)
BEGIN
    DELETE FROM copy WHERE copy_id=p_copy_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_copy_search(IN p_term VARCHAR(150))
BEGIN
    SELECT e.*,l.title
    FROM copy e
    INNER JOIN book l ON l.book_id=e.book_id
    WHERE e.barcode LIKE CONCAT('%',p_term,'%')
       OR e.status LIKE CONCAT('%',p_term,'%')
       OR l.title LIKE CONCAT('%',p_term,'%')
    ORDER BY l.title,e.barcode;
END$$

-- ==================== SOLICITUD_PRESTAMO ====================

CREATE PROCEDURE sp_loan_request_create(
    IN p_student_id INT UNSIGNED,
    IN p_status VARCHAR(30),
    IN p_request_date DATETIME
)
BEGIN
    INSERT INTO loan_request(student_id,status,request_date)
    VALUES(p_student_id,COALESCE(p_status,'PENDING'),
           COALESCE(p_request_date,CURRENT_TIMESTAMP));
    SELECT LAST_INSERT_ID() AS request_id;
END$$

CREATE PROCEDURE sp_loan_request_read(IN p_request_id INT UNSIGNED)
BEGIN
    SELECT s.*,u.institutional_code
    FROM loan_request s
    INNER JOIN app_user u ON u.user_id=s.student_id
    WHERE s.request_id=p_request_id;
END$$

CREATE PROCEDURE sp_loan_request_update(
    IN p_request_id INT UNSIGNED,
    IN p_student_id INT UNSIGNED,
    IN p_status VARCHAR(30),
    IN p_request_date DATETIME
)
BEGIN
    UPDATE loan_request
    SET student_id=p_student_id,
        status=p_status,
        request_date=p_request_date
    WHERE request_id=p_request_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_loan_request_delete(IN p_request_id INT UNSIGNED)
BEGIN
    DELETE FROM loan_request WHERE request_id=p_request_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_loan_request_search(IN p_term VARCHAR(100))
BEGIN
    SELECT s.*,u.institutional_code
    FROM loan_request s
    INNER JOIN app_user u ON u.user_id=s.student_id
    WHERE s.status LIKE CONCAT('%',p_term,'%')
       OR u.institutional_code LIKE CONCAT('%',p_term,'%')
    ORDER BY s.request_date DESC;
END$$

-- ==================== SOLICITUD_DETALLE =====================

CREATE PROCEDURE sp_loan_request_detail_create(
    IN p_request_id INT UNSIGNED,
    IN p_copy_id INT UNSIGNED
)
BEGIN
    INSERT INTO loan_request_detail(request_id,copy_id)
    VALUES(p_request_id,p_copy_id);
END$$

CREATE PROCEDURE sp_loan_request_detail_read(
    IN p_request_id INT UNSIGNED,
    IN p_copy_id INT UNSIGNED
)
BEGIN
    SELECT sd.*,e.barcode,l.title
    FROM loan_request_detail sd
    INNER JOIN copy e ON e.copy_id=sd.copy_id
    INNER JOIN book l ON l.book_id=e.book_id
    WHERE sd.request_id=p_request_id
      AND sd.copy_id=p_copy_id;
END$$

CREATE PROCEDURE sp_loan_request_detail_update(
    IN p_request_id INT UNSIGNED,
    IN p_copy_id INT UNSIGNED,
    IN p_new_request_id INT UNSIGNED,
    IN p_new_copy_id INT UNSIGNED
)
BEGIN
    UPDATE loan_request_detail
    SET request_id=p_new_request_id,
        copy_id=p_new_copy_id
    WHERE request_id=p_request_id
      AND copy_id=p_copy_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_loan_request_detail_delete(
    IN p_request_id INT UNSIGNED,
    IN p_copy_id INT UNSIGNED
)
BEGIN
    DELETE FROM loan_request_detail
    WHERE request_id=p_request_id
      AND copy_id=p_copy_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_loan_request_detail_search(IN p_term VARCHAR(150))
BEGIN
    SELECT sd.*,e.barcode,l.title
    FROM loan_request_detail sd
    INNER JOIN copy e ON e.copy_id=sd.copy_id
    INNER JOIN book l ON l.book_id=e.book_id
    WHERE e.barcode LIKE CONCAT('%',p_term,'%')
       OR l.title LIKE CONCAT('%',p_term,'%')
    ORDER BY l.title;
END$$

-- ========================== PRESTAMO =========================

CREATE PROCEDURE sp_loan_create(
    IN p_request_id INT UNSIGNED,
    IN p_student_id INT UNSIGNED,
    IN p_librarian_id INT UNSIGNED,
    IN p_due_date DATE,
    IN p_status VARCHAR(30)
)
BEGIN
    INSERT INTO loan(request_id,student_id,librarian_id,
                         due_date,status)
    VALUES(p_request_id,p_student_id,p_librarian_id,
           p_due_date,COALESCE(p_status,'ACTIVE'));
    SELECT LAST_INSERT_ID() AS loan_id;
END$$

CREATE PROCEDURE sp_loan_read(IN p_loan_id INT UNSIGNED)
BEGIN
    SELECT p.*,
           ue.institutional_code AS estudiante,
           ub.institutional_code AS bibliotecario
    FROM loan p
    INNER JOIN app_user ue ON ue.user_id=p.student_id
    INNER JOIN app_user ub ON ub.user_id=p.librarian_id
    WHERE p.loan_id=p_loan_id;
END$$

CREATE PROCEDURE sp_loan_update(
    IN p_loan_id INT UNSIGNED,
    IN p_request_id INT UNSIGNED,
    IN p_student_id INT UNSIGNED,
    IN p_librarian_id INT UNSIGNED,
    IN p_due_date DATE,
    IN p_status VARCHAR(30)
)
BEGIN
    UPDATE loan
    SET request_id=p_request_id,
        student_id=p_student_id,
        librarian_id=p_librarian_id,
        due_date=p_due_date,
        status=p_status
    WHERE loan_id=p_loan_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_loan_delete(IN p_loan_id INT UNSIGNED)
BEGIN
    DELETE FROM loan WHERE loan_id=p_loan_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_loan_search(IN p_term VARCHAR(100))
BEGIN
    SELECT p.*,
           ue.institutional_code AS estudiante,
           ub.institutional_code AS bibliotecario
    FROM loan p
    INNER JOIN app_user ue ON ue.user_id=p.student_id
    INNER JOIN app_user ub ON ub.user_id=p.librarian_id
    WHERE p.status LIKE CONCAT('%',p_term,'%')
       OR ue.institutional_code LIKE CONCAT('%',p_term,'%')
       OR ub.institutional_code LIKE CONCAT('%',p_term,'%')
    ORDER BY p.due_date;
END$$

-- ======================= PRESTAMO_DETALLE ====================

CREATE PROCEDURE sp_loan_detail_create(
    IN p_loan_id INT UNSIGNED,
    IN p_copy_id INT UNSIGNED
)
BEGIN
    INSERT INTO loan_detail(loan_id,copy_id)
    VALUES(p_loan_id,p_copy_id);
END$$

CREATE PROCEDURE sp_loan_detail_read(
    IN p_loan_id INT UNSIGNED,
    IN p_copy_id INT UNSIGNED
)
BEGIN
    SELECT pd.*,e.barcode,l.title
    FROM loan_detail pd
    INNER JOIN copy e ON e.copy_id=pd.copy_id
    INNER JOIN book l ON l.book_id=e.book_id
    WHERE pd.loan_id=p_loan_id
      AND pd.copy_id=p_copy_id;
END$$

CREATE PROCEDURE sp_loan_detail_update(
    IN p_loan_id INT UNSIGNED,
    IN p_copy_id INT UNSIGNED,
    IN p_new_loan_id INT UNSIGNED,
    IN p_new_copy_id INT UNSIGNED
)
BEGIN
    UPDATE loan_detail
    SET loan_id=p_new_loan_id,
        copy_id=p_new_copy_id
    WHERE loan_id=p_loan_id
      AND copy_id=p_copy_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_loan_detail_delete(
    IN p_loan_id INT UNSIGNED,
    IN p_copy_id INT UNSIGNED
)
BEGIN
    DELETE FROM loan_detail
    WHERE loan_id=p_loan_id
      AND copy_id=p_copy_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_loan_detail_search(IN p_term VARCHAR(150))
BEGIN
    SELECT pd.*,e.barcode,l.title
    FROM loan_detail pd
    INNER JOIN copy e ON e.copy_id=pd.copy_id
    INNER JOIN book l ON l.book_id=e.book_id
    WHERE e.barcode LIKE CONCAT('%',p_term,'%')
       OR l.title LIKE CONCAT('%',p_term,'%')
    ORDER BY l.title;
END$$

-- ======================== COMPROBANTE ========================

CREATE PROCEDURE sp_receipt_create(
    IN p_loan_id INT UNSIGNED,
    IN p_issue_date DATETIME
)
BEGIN
    INSERT INTO receipt(loan_id,issue_date)
    VALUES(p_loan_id,COALESCE(p_issue_date,CURRENT_TIMESTAMP));
    SELECT LAST_INSERT_ID() AS receipt_id;
END$$

CREATE PROCEDURE sp_receipt_read(IN p_receipt_id INT UNSIGNED)
BEGIN
    SELECT c.*,p.status,p.student_id,p.librarian_id
    FROM receipt c
    INNER JOIN loan p ON p.loan_id=c.loan_id
    WHERE c.receipt_id=p_receipt_id;
END$$

CREATE PROCEDURE sp_receipt_update(
    IN p_receipt_id INT UNSIGNED,
    IN p_loan_id INT UNSIGNED,
    IN p_issue_date DATETIME
)
BEGIN
    UPDATE receipt
    SET loan_id=p_loan_id,issue_date=p_issue_date
    WHERE receipt_id=p_receipt_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_receipt_delete(IN p_receipt_id INT UNSIGNED)
BEGIN
    DELETE FROM receipt WHERE receipt_id=p_receipt_id;
    SELECT ROW_COUNT() AS affected_rows;
END$$

CREATE PROCEDURE sp_receipt_search(IN p_term VARCHAR(100))
BEGIN
    SELECT c.*,p.status,p.student_id,p.librarian_id
    FROM receipt c
    INNER JOIN loan p ON p.loan_id=c.loan_id
    WHERE p.status LIKE CONCAT('%',p_term,'%')
    ORDER BY c.issue_date DESC;
END$$

DELIMITER ;