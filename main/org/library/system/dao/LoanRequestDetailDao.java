package org.library.system.dao;

import org.library.system.config.ConectionDB;
import org.library.system.model.RequestDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanRequestDetailDao {

    public int create(RequestDetails detail) throws SQLException {
        String sql = "{CALL sp_loan_request_detail_create(?, ?, ?)}";

        try (Connection connection = ConectionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, detail.getRequest_id());
            statement.setInt(2, detail.getBook_id());
            statement.setInt(3, detail.getQuantity());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("request_detail_id");
                }
            }
        }
        throw new SQLException("Loan request detail could not be created.");
    }

    public Optional<RequestDetails> findById(int requestDetailId) throws SQLException {
        String sql = "{CALL sp_loan_request_detail_read(?)}";

        try (Connection connection = ConectionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, requestDetailId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapDetail(rs));
                }
            }
        }
        return Optional.empty();
    }

    public boolean update(RequestDetails detail) throws SQLException {
        String sql = "{CALL sp_loan_request_detail_update(?, ?, ?)}";

        try (Connection connection = ConectionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, detail.getRequest_detail_id());
            statement.setInt(2, detail.getBook_id());
            statement.setInt(3, detail.getQuantity());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("affected_rows") > 0;
                }
            }
        }
        return false;
    }

    public boolean delete(int requestDetailId) throws SQLException {
        String sql = "{CALL sp_loan_request_detail_delete(?)}";

        try (Connection connection = ConectionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, requestDetailId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("affected_rows") > 0;
                }
            }
        }
        return false;
    }

    public List<RequestDetails> search(String search) throws SQLException {
        String sql = "{CALL sp_loan_request_detail_search(?)}";
        List<RequestDetails> details = new ArrayList<>();

        try (Connection connection = ConectionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setString(1, search);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    details.add(mapDetail(rs));
                }
            }
        }
        return details;
    }

    private RequestDetails mapDetail(ResultSet rs) throws SQLException {
        RequestDetails detail = new RequestDetails();
        detail.setRequest_detail_id(rs.getInt("request_detail_id"));
        detail.setRequest_id(rs.getInt("request_id"));
        detail.setBook_id(rs.getInt("book_id"));
        detail.setQuantity(rs.getInt("quantity"));
        return detail;
    }
}