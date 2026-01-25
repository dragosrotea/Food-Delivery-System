package view;

import model.Order;
import services.OrderService;
import database.OrderDAO;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DriverDashboard extends JPanel {
    private OrderService orderService = new OrderService();
    private OrderDAO orderDAO = new OrderDAO();

    // Two models: one for available orders + one for the driver's current task
    private DefaultListModel<Order> availableListModel = new DefaultListModel<>();
    private DefaultListModel<Order> myActiveListModel = new DefaultListModel<>();

    private JList<Order> availableList = new JList<>(availableListModel);
    private JList<Order> activeList = new JList<>(myActiveListModel);

    private MainFrame parent;

    public DriverDashboard(MainFrame parent) {
        this.parent = parent;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel centralPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        // Top Part: Available Orders
        JPanel availablePanel = new JPanel(new BorderLayout());
        availablePanel.add(new JLabel("Available Orders (Pending)"), BorderLayout.NORTH);
        availablePanel.add(new JScrollPane(availableList), BorderLayout.CENTER);

        // Bottom Part: Active Deliveries
        JPanel activePanel = new JPanel(new BorderLayout());
        activePanel.add(new JLabel("My Active Deliveries (In Progress)"), BorderLayout.NORTH);
        activePanel.add(new JScrollPane(activeList), BorderLayout.CENTER);

        centralPanel.add(availablePanel);
        centralPanel.add(activePanel);
        add(centralPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton acceptBtn = new JButton("Accept Selected");
        JButton completeBtn = new JButton("Mark as Delivered");
        buttonPanel.add(acceptBtn);
        buttonPanel.add(completeBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        // Logic
        acceptBtn.addActionListener(e -> {
            Order selected = availableList.getSelectedValue();
            if (selected != null) {
                if (orderService.acceptDelivery(selected.getId(), parent.getCurrentUser().getId())) {
                    JOptionPane.showMessageDialog(this, "Order accepted! It's now in your active list.");
                    refreshOrders();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an order from the 'Available' list.");
            }
        });

        completeBtn.addActionListener(e -> {
            Order selected = activeList.getSelectedValue();
            if (selected != null) {
                if (orderService.completeDelivery(selected.getId())) {
                    JOptionPane.showMessageDialog(this, "Delivery complete! Great job.");
                    refreshOrders();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an order from your 'Active' list.");
            }
        });

        refreshOrders();
    }

    private void refreshOrders() {
        availableListModel.clear();
        myActiveListModel.clear();

        List<Order> pending = orderDAO.getPendingOrders();
        for (Order o : pending) availableListModel.addElement(o);

        List<Order> active = orderDAO.getOrdersByDriver(parent.getCurrentUser().getId(), "ARRIVING");
        for (Order o : active) myActiveListModel.addElement(o);
    }
}