package database;

import model.Order;
import model.MenuItem;
import java.sql.*;
import java.util.*;

public class OrderDAO {

    public void placeOrder(Order order) {
        String orderSql = "INSERT INTO Orders (CustomerID, RestaurantID, Status) VALUES (?, ?, ?)";
        String itemSql = "INSERT INTO OrderDetails (OrderID, ItemID, Quantity) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, order.getCustomerID());
                pstmt.setInt(2, order.getRestaurantID());
                pstmt.setString(3, "Pending");

                pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newOrderId = generatedKeys.getInt(1);
                        order.setId(newOrderId);

                        try (PreparedStatement itemPstmt = conn.prepareStatement(itemSql)) {
                            for (MenuItem item : order.getItems()) {
                                itemPstmt.setInt(1, newOrderId);
                                itemPstmt.setInt(2, item.getId());
                                itemPstmt.setInt(3, 1);
                                itemPstmt.addBatch();
                            }
                            itemPstmt.executeBatch();
                        }
                    }
                }
                conn.commit();
                System.out.println("Order #" + order.getId() + " is now in the database!");

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("OrderDAO Error: " + e.getMessage());
        }
    }

    public boolean acceptOrder(int orderId, int driverId) {
        String sql = "UPDATE Orders SET DriverID = ?, Status = 'Arriving' " +
                "WHERE OrderID = ? AND Status = 'Pending'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, driverId);
            pstmt.setInt(2, orderId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean completeOrder(int orderId) {
        String sql = "UPDATE Orders SET Status = 'Delivered' WHERE OrderID = ? AND Status = 'Arriving'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> getPendingOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE Status = 'Pending'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Order(
                        rs.getInt("OrderID"),
                        rs.getInt("CustomerID"),
                        rs.getInt("RestaurantID"),
                        null,
                        rs.getString("Status")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Order> getOrdersByDriver(int driverId, String status) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE DriverID = ? AND Status = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, driverId);
            pstmt.setString(2, status);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("OrderID");
                    int custId = rs.getInt("CustomerID");
                    int restId = rs.getInt("RestaurantID");

                    Integer dId = (Integer) rs.getObject("DriverID");
                    String statusStr = rs.getString("Status");

                    Order o = new Order(id, custId, restId, dId, statusStr);

                    list.add(o);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}