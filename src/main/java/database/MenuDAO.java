package database;

import model.MenuItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {

    public List<MenuItem> getItemsByRestaurant(int restaurantId) {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT ItemID, RestaurantID, ItemName, Price, Category FROM MenuItems WHERE RestaurantID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, restaurantId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                MenuItem item = new MenuItem(
                        rs.getInt("ItemID"),
                        rs.getInt("RestaurantID"),
                        rs.getString("ItemName"),
                        rs.getDouble("Price"),
                        rs.getString("Category")
                );
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("MenuDAO Error: " + e.getMessage());
        }
        return items;
    }
}