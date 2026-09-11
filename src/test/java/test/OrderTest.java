package test;

import model.Order;
import model.MenuItem;

public class OrderTest {

    public static void main(String[] args) {
        testTotalPriceCalculation();
    }

    public static void testTotalPriceCalculation() {
        System.out.println("[Testing Total Price Logic]");

        Order testOrder = new Order(101, 1);
        testOrder.addItem(new MenuItem(1, 1, "Pizza Diavola", 39.50, "Pizza"));
        testOrder.addItem(new MenuItem(2, 1, "Tiramisu", 24.00, "Dessert"));
        testOrder.addItem(new MenuItem(3, 1, "Cola", 8.25, "Drink"));

        double actualTotal = testOrder.getTotalPrice();
        double expectedTotal = 71.75;

        System.out.println("Expected: " + expectedTotal + " RON");
        System.out.println("Actual: " + actualTotal + " RON");

        if (Math.abs(actualTotal - expectedTotal) < 0.001) {
            System.out.println("[PASSED] - The total price logic is right.");
        } else {
            System.out.println("[FAILED] - Something in the price logic is wrong.");
        }
    }
}