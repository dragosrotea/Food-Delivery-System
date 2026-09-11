package database;

import model.Restaurant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDAO {

    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> list = new ArrayList<>();
        String sql = "SELECT * FROM Restaurants";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Restaurant(
                        rs.getInt("RestaurantID"),
                        rs.getString("Name"),
                        rs.getString("Street"),
                        rs.getString("City")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM Restaurants WHERE Name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void saveRestaurant(Restaurant restaurant) {
        String sql = "INSERT INTO Restaurants (Name, Street, City) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, restaurant.getName());
            pstmt.setString(2, restaurant.getStreet());
            pstmt.setString(3, restaurant.getCity());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    restaurant.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteRestaurantById(int id) {
        String sql = "DELETE FROM Restaurants WHERE RestaurantID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affected = pstmt.executeUpdate();

            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}