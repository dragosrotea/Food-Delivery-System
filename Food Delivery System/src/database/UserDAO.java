package database;

import model.Admin;
import model.Customer;
import model.Driver;
import model.User;

import java.sql.*;

public class UserDAO {

    public User login(String username, String password) {
        String sql = "SELECT UserID, FullName, Username, Password, UserRole FROM Users WHERE Username = ? AND Password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("UserID");
                String name = rs.getString("FullName");
                String role = rs.getString("UserRole");

                if ("Customer".equalsIgnoreCase(role)) {
                    return new Customer(id, name, username, password);
                }
                else if ("Driver".equalsIgnoreCase(role)) {
                    return new Driver(id, name, username, password);
                }
                else if ("Admin".equalsIgnoreCase(role)) {
                    return new Admin(id, name, username, password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean registerUser(String name, String username, String password, String role) {
        String sql = "INSERT INTO Users (FullName, Username, Password, UserRole) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setString(4, role);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}