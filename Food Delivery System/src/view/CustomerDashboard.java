package view;

import model.Restaurant;
import model.Order;
import model.MenuItem;
import services.RestaurantService;
import services.OrderService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDashboard extends JPanel {
    private final RestaurantService restaurantService = new RestaurantService();
    private final OrderService orderService = new OrderService();

    private final DefaultListModel<Restaurant> restModel = new DefaultListModel<>();
    private final JList<Restaurant> restList = new JList<>(restModel);

    private final DefaultListModel<MenuItem> menuModel = new DefaultListModel<>();
    private final JList<MenuItem> menuList = new JList<>(menuModel);

    private final List<MenuItem> cart = new ArrayList<>();
    private final JTextArea cartArea = new JTextArea(10, 15);
    private final JLabel totalLabel = new JLabel("Total: 0.00 RON");

    private final MainFrame parent;

    public CustomerDashboard(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Left Panel: Restaurant Selection
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.add(new JLabel("1. Select a Restaurant"), BorderLayout.NORTH);

        loadRestaurants();
        restList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leftPanel.add(new JScrollPane(restList), BorderLayout.CENTER);

        // Center Panel: Menu Selection
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.add(new JLabel("2. Pick your Food"), BorderLayout.NORTH);

        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        centerPanel.add(new JScrollPane(menuList), BorderLayout.CENTER);

        JButton addBtn = new JButton("Add to Cart");
        centerPanel.add(addBtn, BorderLayout.SOUTH);

        // Right Panel: Checkout
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.add(new JLabel("3. Your Order Summary"), BorderLayout.NORTH);

        cartArea.setEditable(false);
        cartArea.setFont(new Font("Arial", Font.BOLD, 12));
        rightPanel.add(new JScrollPane(cartArea), BorderLayout.CENTER);

        JPanel checkoutPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JButton checkoutBtn = new JButton("Place Order");
        checkoutBtn.setBackground(new Color(46, 204, 113));
        checkoutBtn.setForeground(Color.WHITE);
        checkoutBtn.setFont(new Font("Arial", Font.BOLD, 14));

        checkoutPanel.add(totalLabel);
        checkoutPanel.add(checkoutBtn);
        rightPanel.add(checkoutPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        // Logic
        // Fetch menu when a restaurant is clicked
        restList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Restaurant selected = restList.getSelectedValue();
                if (selected != null) {
                    refreshMenu(selected.getId());
                }
            }
        });

        // Add item to cart
        addBtn.addActionListener(e -> {
            MenuItem selectedItem = menuList.getSelectedValue();
            if (selectedItem != null) {
                addItemToCart(selectedItem);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an item from the menu.");
            }
        });

        // Checkout
        checkoutBtn.addActionListener(e -> handleCheckout());
    }

    private void loadRestaurants() {
        restModel.clear();
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        for (Restaurant r : restaurants) {
            restModel.addElement(r);
        }
    }

    private void refreshMenu(int restaurantId) {
        menuModel.clear();
        // We fetch items from the service only when needed
        List<MenuItem> items = restaurantService.getMenuForRestaurant(restaurantId);
        for (MenuItem item : items) {
            menuModel.addElement(item);
        }
    }

    private void addItemToCart(MenuItem item) {
        // Get the currently selected restaurant from the list
        Restaurant selectedRes = restList.getSelectedValue();

        // If the cart isn't empty, check if we are switching restaurants
        if (!cart.isEmpty()) {
            int cartRestaurantId = cart.get(0).getRestaurantId();

            if (item.getRestaurantId() != cartRestaurantId) {
                JOptionPane.showMessageDialog(this,
                        "You can only order from one restaurant at a time!\n" +
                                "Clear your cart if you want to switch restaurants.",
                        "Order Conflict", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // If it's the same restaurant, add the item
        cart.add(item);
        cartArea.append(String.format("%-15s %.2f RON\n", item.getName(), item.getPrice()));
        updateTotal();
    }

    private void updateTotal() {
        double total = 0;
        for (MenuItem m : cart) {
            total += m.getPrice();
        }
        totalLabel.setText(String.format("Total: %.2f RON", total));
    }

    //Going from the shopping cart to the order
    private void handleCheckout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!");
            return;
        }

        Restaurant selectedRes = restList.getSelectedValue();
        if (selectedRes == null) {
            JOptionPane.showMessageDialog(this, "Selection error. Please select a restaurant again.");
            return;
        }

        int currentUserId = parent.getCurrentUser().getId();
        Order newOrder = new Order(currentUserId, selectedRes.getId());

        for (MenuItem m : cart) {
            newOrder.addItem(m);
        }

        if (orderService.placeNewOrder(newOrder)) {
            JOptionPane.showMessageDialog(this, "Order placed successfully!\nOrder ID: #" + newOrder.getId());
            clearDashboard();
        } else {
            JOptionPane.showMessageDialog(this, "Error: Could not process order. Please try again.");
        }
    }

    private void clearDashboard() {
        cart.clear();
        cartArea.setText("");
        menuModel.clear();
        restList.clearSelection();
        updateTotal();
    }
}