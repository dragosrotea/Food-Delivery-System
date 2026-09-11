package view;

import services.RestaurantService;
import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JPanel {
    private RestaurantService restaurantService = new RestaurantService();
    private MainFrame parent;

    public AdminDashboard(MainFrame parent) {
        this.parent = parent;

        // Layout
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header
        JLabel headerLabel = new JLabel("Admin Panel: Register New Restaurant", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(headerLabel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 15));

        JTextField nameField = new JTextField();
        JTextField streetField = new JTextField();
        JTextField cityField = new JTextField();
        JButton submitButton = new JButton("Save Restaurant");

        formPanel.add(new JLabel("Restaurant Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Street:"));
        formPanel.add(streetField);
        formPanel.add(new JLabel("City:"));
        formPanel.add(cityField);

        formPanel.add(new JLabel(""));
        formPanel.add(submitButton);

        add(formPanel, BorderLayout.CENTER);

        // Logic
        submitButton.addActionListener(e -> {
            String name = nameField.getText();
            String street = streetField.getText();
            String city = cityField.getText();

            boolean success = restaurantService.addRestaurant(name, street, city);

            if (success) {
                JOptionPane.showMessageDialog(this, "Successfully added: " + name);
                // Clear fields for the next entry
                nameField.setText("");
                streetField.setText("");
                cityField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Error: Name might be duplicate or empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Delete Section
        JPanel deletePanel = new JPanel(new FlowLayout());
        deletePanel.setBorder(BorderFactory.createTitledBorder("Remove Restaurant"));

        JTextField idField = new JTextField(5);
        JButton deleteBtn = new JButton("Delete by ID");
        deleteBtn.setBackground(new Color(231, 76, 60));
        deleteBtn.setForeground(Color.WHITE);

        deletePanel.add(new JLabel("Enter Restaurant ID:"));
        deletePanel.add(idField);
        deletePanel.add(deleteBtn);

        add(deletePanel, BorderLayout.SOUTH);

        // Delete Logic
        deleteBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure? This will delete all its menu items too!");

                if (confirm == JOptionPane.YES_OPTION) {
                    if (restaurantService.deleteRestaurant(id)) {
                        JOptionPane.showMessageDialog(this, "Restaurant #" + id + " deleted.");
                        idField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(this, "ID not found.");
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID.");
            }
        });
    }
}