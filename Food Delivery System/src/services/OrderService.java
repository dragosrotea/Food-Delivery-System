package services;

import database.OrderDAO;
import model.Order;

public class OrderService {
    private final OrderDAO orderDAO = new OrderDAO();

    public boolean placeNewOrder(Order order) {
        if (order.getItems().isEmpty()) {
            System.out.println("OrderService: Cannot place an empty order.");
            return false;
        }

        try {
            orderDAO.placeOrder(order);
            return true;
        } catch (Exception e) {
            System.err.println("OrderService: Failed to place order - " + e.getMessage());
            return false;
        }
    }

    public boolean acceptDelivery(int orderId, int driverId) {
        return orderDAO.acceptOrder(orderId, driverId);
    }

    public boolean completeDelivery(int orderId) {
        return orderDAO.completeOrder(orderId);
    }
}