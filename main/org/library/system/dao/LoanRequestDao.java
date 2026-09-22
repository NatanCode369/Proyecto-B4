package org.library.system.dao;

import org.library.system.config.ConectionDB;
import org.library.system.enums.RequestStatus;
import org.library.system.model.LoanApplication;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoanRequestDao {

    public int create(LoanApplication request) throws SQLException {
        String sql = "{CALL sp_loan_request_create(?, ?)}";

        try (Connection connection = ConectionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, request.getStudent_id());

            if (request.getObservation() != null) {
                statement.setString(2, request.getObservation());
            } else {
                statement.setNull(2, Types.VARCHAR);
            }

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("request_id");
                }
            }
        }
        throw new SQLException("Loan request could not be created.");
    }

    public Optional<LoanApplication> findById(int requestId) throws SQLException {
        String sql = "{CALL sp_loan_request_read(?)}";

        try (Connection connection = ConectionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, requestId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapLoanRequest(rs));
                }
            }
        }
        return Optional.empty();
    }

    public boolean update(LoanApplication request) throws SQLException {
        String sql = "{CALL sp_loan_request_update(?, ?, ?, ?, ?)}";

        try (Connection connection = ConectionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, request.getRequest_id());
            statement.setString(2, request.getStatus().name());

            if (request.getObservation() != null) {
                statement.setString(3, request.getObservation());
            } else {
                statement.setNull(3, Types.VARCHAR);
            }

            if (request.getLibrarian_id() != null) {
                statement.setInt(4, request.getLibrarian_id());
            } else {
                statement.setNull(4, Types.INTEGER);
            }

            if (request.getResponse_date() != null) {
                statement.setDate(5, Date.valueOf(request.getResponse_date()));
            } else {
                statement.setNull(5, Types.TIMESTAMP);
            }

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("affected_rows") > 0;
                }
            }
        }
        return false;
    }

    public boolean delete(int requestId) throws SQLException {
        String sql = "{CALL sp_loan_request_delete(?)}";

        try (Connection connection = ConectionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, requestId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("affected_rows") > 0;
                }
            }
        }
        return false;
    }

    public List<LoanApplication> search(String search) throws SQLException {
        String sql = "{CALL sp_loan_request_search(?)}";
        List<LoanApplication> requests = new ArrayList<>();

        try (Connection connection = ConectionDB.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setString(1, search);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapLoanRequest(rs));
                }
            }
        }
        return requests;
    }

    private LoanApplication mapLoanRequest(ResultSet rs) throws SQLException {
        LoanApplication request = new LoanApplication();
        request.setRequest_id(rs.getInt("request_id"));
        request.setStudent_id(rs.getInt("student_id"));

        Date requestDate = rs.getDate("request_date");
        if (requestDate != null) {
            request.setRequest_date(requestDate.toLocalDate());
        }

        request.setStatus(RequestStatus.valueOf(rs.getString("status")));
        request.setObservation(rs.getString("observation"));

        int librarianId = rs.getInt("librarian_id");
        if (rs.wasNull()) {
            request.setLibrarian_id(null);
        } else {
            request.setLibrarian_id(librarianId);
        }

        Date responseDate = rs.getDate("response_date");
        if (responseDate != null) {
            request.setResponse_date(responseDate.toLocalDate());
        } else {
            request.setResponse_date(null);
        }

        return request;
    }
}