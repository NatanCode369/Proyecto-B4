package dao;

import config.ConectionDB;
import model.User;
import model.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao {

    public int create(User user) throws SQLException {

        String sql = "{CALL sp_user_create(?, ?, ?, ?, ?, ?)}";

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setString(1, user.getUser_code());
            statement.setString(2, user.getFirst_name());
            statement.setString(3, user.getLast_name());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPassword_hash());
            statement.setString(6, user.getUser_role().name());

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        }

        throw new SQLException("User could not be created.");
    }

    public Optional<User> login(String userCode) throws SQLException {

        String sql = "{CALL sp_user_login(?)}";

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setString(1, userCode);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapUser(rs, true));
                }
            }
        }

        return Optional.empty();
    }

    public Optional<User> findByCode(String userCode) throws SQLException {

        String sql = "{CALL sp_user_exists_by_code(?)}";

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setString(1, userCode);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapUser(rs, false));
                }
            }
        }

        return Optional.empty();
    }

    public Optional<User> findByEmail(String email) throws SQLException {

        String sql = "{CALL sp_user_exists_by_email(?)}";

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setString(1, email);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapUser(rs, false));
                }
            }
        }

        return Optional.empty();
    }

    public Optional<User> findById(int userId) throws SQLException {

        String sql = "{CALL sp_user_read(?)}";

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, userId);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(mapUser(rs, false));
                }
            }
        }

        return Optional.empty();
    }

    public boolean update(User user) throws SQLException {

        String sql = "{CALL sp_user_update(?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, user.getUser_id());
            statement.setString(2, user.getUser_code());
            statement.setString(3, user.getFirst_name());
            statement.setString(4, user.getLast_name());
            statement.setString(5, user.getEmail());
            statement.setString(6, user.getPassword_hash());
            statement.setString(7, user.getUser_role().name());
            statement.setBoolean(8, user.getActive());

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("affected_rows") > 0;
                }
            }
        }

        return false;
    }

    public boolean delete(int userId) throws SQLException {

        String sql = "{CALL sp_user_delete(?)}";

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, userId);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("affected_rows") > 0;
                }
            }
        }

        return false;
    }

    public List<User> search(String search) throws SQLException {

        String sql = "{CALL sp_user_search(?)}";

        List<User> users = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setString(1, search);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    users.add(mapUser(rs, false));
                }
            }
        }

        return users;
    }

    private User mapUser(ResultSet rs, boolean includePassword) throws SQLException {

        User user = new User();

        user.setUser_id(rs.getInt("user_id"));
        user.setUser_code(rs.getString("user_code"));
        user.setFirst_name(rs.getString("first_name"));
        user.setLast_name(rs.getString("last_name"));
        user.setEmail(rs.getString("email"));

        if (includePassword) {
            user.setPassword_hash(rs.getString("password_hash"));
        }

        user.setUser_role(
                Role.valueOf(rs.getString("user_role"))
        );

        user.setActive(rs.getBoolean("active"));

        return user;
    }
}
