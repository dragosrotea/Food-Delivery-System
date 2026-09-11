package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTest {

    @Test
    void calculatesTotalPriceForAllItems() {
        Order order = new Order(101, 1);
        order.addItem(new MenuItem(1, 1, "Pizza Diavola", 39.50, "Pizza"));
        order.addItem(new MenuItem(2, 1, "Tiramisu", 24.00, "Dessert"));
        order.addItem(new MenuItem(3, 1, "Cola", 8.25, "Drink"));

        assertEquals(71.75, order.getTotalPrice(), 0.001);
    }

    @Test
    void emptyOrderHasZeroTotal() {
        Order order = new Order(101, 1);

        assertEquals(0.0, order.getTotalPrice(), 0.001);
    }
}
