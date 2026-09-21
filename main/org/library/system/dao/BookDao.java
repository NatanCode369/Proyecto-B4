package org.library.system.dao;

import org.library.system.config.ConectionDB;
import org.library.system.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDao {

    public int create(Book book) throws SQLException {
        String sql = "{CALL sp_book_create(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConectionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setString(1, book.getIsbn());
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setString(4, book.getPublisher());
            statement.setInt(5, book.getPublication_year());
            statement.setInt(6, book.getTotal_stock());
            statement.setInt(7, book.getAvailable_stock());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("book_id");
                }
            }
        }
        throw new SQLException("Book could not be created.");
    }

    public Optional<Book> findById(int bookId) throws SQLException {
        String sql = "{CALL sp_book_read(?)}";

        try (Connection connection = ConectionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, bookId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapBook(rs));
                }
            }
        }
        return Optional.empty();
    }

    public boolean update(Book book) throws SQLException {
        String sql = "{CALL sp_book_update(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = ConectionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, book.getBook_id());
            statement.setString(2, book.getIsbn());
            statement.setString(3, book.getTitle());
            statement.setString(4, book.getAuthor());
            statement.setString(5, book.getPublisher());
            statement.setInt(6, book.getPublication_year());
            statement.setInt(7, book.getTotal_stock());
            statement.setInt(8, book.getAvailable_stock());
            statement.setBoolean(9, book.getActive());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("affected_rows") > 0;
                }
            }
        }
        return false;
    }

    public boolean delete(int bookId) throws SQLException {
        String sql = "{CALL sp_book_delete(?)}";

        try (Connection connection = ConectionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, bookId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("affected_rows") > 0;
                }
            }
        }
        return false;
    }

    public List<Book> search(String search) throws SQLException {
        String sql = "{CALL sp_book_search(?)}";
        List<Book> books = new ArrayList<>();

        try (Connection connection = ConectionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setString(1, search);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    books.add(mapBook(rs));
                }
            }
        }
        return books;
    }

    private Book mapBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setBook_id(rs.getInt("book_id"));
        book.setIsbn(rs.getString("isbn"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPublisher(rs.getString("publisher"));
        book.setPublication_year(rs.getInt("publication_year"));
        book.setTotal_stock(rs.getInt("total_stock"));
        book.setAvailable_stock(rs.getInt("available_stock"));
        book.setActive(rs.getBoolean("active"));
        return book;
    }
}