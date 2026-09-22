package org.library.system.dao;

import config.ConnectionDB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoanReportDao {

    public List<LoanInvoiceRow> findLoanInvoice(int loanId) throws SQLException {

        String sql = "{CALL sp_loan_invoice(?)}";

        List<LoanInvoiceRow> rows = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, loanId);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    LoanInvoiceRow row = new LoanInvoiceRow();

                    row.setLoanId(rs.getInt("loan_id"));
                    row.setLoanDate(rs.getTimestamp("loan_date"));
                    row.setDueDate(rs.getDate("due_date"));
                    row.setStatus(rs.getString("status"));

                    row.setUserCode(rs.getString("user_code"));
                    row.setFirstName(rs.getString("first_name"));
                    row.setLastName(rs.getString("last_name"));
                    row.setEmail(rs.getString("email"));

                    row.setIsbn(rs.getString("isbn"));
                    row.setTitle(rs.getString("title"));
                    row.setAuthor(rs.getString("author"));

                    row.setQuantity(rs.getInt("quantity"));
                    row.setReturnedQuantity(
                            rs.getInt("returned_quantity")
                    );

                    rows.add(row);
                }
            }
        }

        return rows;
    }
}