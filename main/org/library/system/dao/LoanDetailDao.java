package dao;

import config.ConectionDB;
import model.LoanDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanDetailDao {

    public int create(LoanDetail detail) throws SQLException {

        String sql = "{CALL sp_loan_detail_create(?, ?, ?)}";

        try (Connection connection = DatabaseConnection.getConnection();
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

    public Optional<LoanDetail> findById(int loanDetailId)
            throws SQLException {

        String sql = "{CALL sp_loan_detail_read(?)}";

        try (Connection connection = DatabaseConnection.getConnection();
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

    public boolean update(LoanDetail detail) throws SQLException {

        String sql = "{CALL sp_loan_detail_update(?, ?, ?, ?)}";

        try (Connection connection = DatabaseConnection.getConnection();
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

        try (Connection connection = DatabaseConnection.getConnection();
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

    public List<LoanDetail> search(String search) throws SQLException {

        String sql = "{CALL sp_loan_detail_search(?)}";

        List<LoanDetail> details = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
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

    private LoanDetail mapDetail(ResultSet rs) throws SQLException {

        LoanDetail detail = new LoanDetail();

        detail.setLoan_detail_id(
                rs.getInt("loan_detail_id")
        );

        detail.setLoan_id(
                rs.getInt("loan_id")
        );

        detail.setBook_id(
                rs.getInt("book_id")
        );

        detail.setQuantity(
                rs.getInt("quantity")
        );

        detail.setReturned_quantity(
                rs.getInt("returned_quantity")
        );

        return detail;
    }
}
