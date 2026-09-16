package dao;

import config.ConectionDB;
import model.Loan;
import model.LoanStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanDao {

    public int create(Loan loan) throws SQLException {

        String sql = "{CALL sp_loan_create(?, ?, ?, ?)}";

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, loan.getRequest_id());
            statement.setInt(2, loan.getStudent_id());
            statement.setInt(3, loan.getLibrarian_id());
            statement.setDate(
                    4,
                    Date.valueOf(loan.getDue_date())
            );

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("loan_id");
                }
            }
        }

        throw new SQLException("Loan could not be created.");
    }

    public Optional<Loan> findById(int loanId) throws SQLException {

        String sql = "{CALL sp_loan_read(?)}";

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, loanId);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapLoan(rs));
                }
            }
        }

        return Optional.empty();
    }

    public boolean update(Loan loan) throws SQLException {

        String sql = "{CALL sp_loan_update(?, ?, ?, ?)}";

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, loan.getLoan_id());
            statement.setInt(2, loan.getLibrarian_id());
            statement.setDate(
                    3,
                    Date.valueOf(loan.getDue_date())
            );
            statement.setString(
                    4,
                    loan.getStatus().name()
            );

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("affected_rows") > 0;
                }
            }
        }

        return false;
    }

    public boolean delete(int loanId) throws SQLException {

        String sql = "{CALL sp_loan_delete(?)}";

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, loanId);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("affected_rows") > 0;
                }
            }
        }

        return false;
    }

    public List<Loan> search(String search) throws SQLException {

        String sql = "{CALL sp_loan_search(?)}";

        List<Loan> loans = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setString(1, search);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    loans.add(mapLoan(rs));
                }
            }
        }

        return loans;
    }

    private Loan mapLoan(ResultSet rs) throws SQLException {

        Loan loan = new Loan();

        loan.setLoan_id(
                rs.getInt("loan_id")
        );

        loan.setRequest_id(
                rs.getInt("request_id")
        );

        loan.setStudent_id(
                rs.getInt("student_id")
        );

        loan.setLibrarian_id(
                rs.getInt("librarian_id")
        );

        Date loanDate = rs.getDate("loan_date");

        if (loanDate != null) {
            loan.setLoan_date(
                    loanDate.toLocalDate()
            );
        }

        Date dueDate = rs.getDate("due_date");

        if (dueDate != null) {
            loan.setDue_date(
                    dueDate.toLocalDate()
            );
        }

        loan.setStatus(
                LoanStatus.valueOf(
                        rs.getString("status")
                )
        );

        return loan;
    }
}
