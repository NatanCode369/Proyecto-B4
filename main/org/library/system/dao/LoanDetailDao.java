package org.library.system.dao;

import org.library.system.config.ConectionDB;
import org.library.system.model.LoanDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanDetailDao {

    public int create(LoanDetails detail) throws SQLException {
        String sql = "{CALL sp_loan_detail_create(?, ?, ?)}";

        try (Connection connection = ConectionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, detail.getLoan_id());
            statement.setInt(2, detail.getBook_id());
            statement.setInt(3, detail.getQuantity());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("loan_detail_id");
                }
            }
        }
        throw new SQLException("Loan detail could not be created.");
    }

    public Optional<LoanDetails> findById(int loanDetailId) throws SQLException {
        String sql = "{CALL sp_loan_detail_read(?)}";

        try (Connection connection = ConectionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, loanDetailId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapDetail(rs));
                }
            }
        }
        return Optional.empty();
    }

    public boolean update(LoanDetails detail) throws SQLException {
        String sql = "{CALL sp_loan_detail_update(?, ?, ?, ?)}";

        try (Connection connection = ConectionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, detail.getLoan_detail_id());
            statement.setInt(2, detail.getBook_id());
            statement.setInt(3, detail.getQuantity());
            statement.setInt(4, detail.getReturned_quantity());

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("affected_rows") > 0;
                }
            }
        }
        return false;
    }

    public boolean delete(int loanDetailId) throws SQLException {
        String sql = "{CALL sp_loan_detail_delete(?)}";

        try (Connection connection = ConectionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, loanDetailId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("affected_rows") > 0;
                }
            }
        }
        return false;
    }

    public List<LoanDetails> search(String search) throws SQLException {
        String sql = "{CALL sp_loan_detail_search(?)}";
        List<LoanDetails> details = new ArrayList<>();

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

    private LoanDetails mapDetail(ResultSet rs) throws SQLException {
        LoanDetails detail = new LoanDetails();
        detail.setLoan_detail_id(rs.getInt("loan_detail_id"));
        detail.setLoan_id(rs.getInt("loan_id"));
        detail.setBook_id(rs.getInt("book_id"));
        detail.setQuantity(rs.getInt("quantity"));
        detail.setReturned_quantity(rs.getInt("returned_quantity"));
        return detail;
    }
}