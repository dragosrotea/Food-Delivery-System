// Customer: elena_d, pass1234
// Driver: alex_m, driver_pass1
// Admin: andrei_admin, adminPass!55

package view;

import model.User;
import model.Customer;
import services.AuthService;
import javax.swing.*;
import java.awt.*;

// Main window for the app
public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private AuthService authService = new AuthService();
    private User currentUser;

    public MainFrame() {
        setTitle("Food Delivery System - Project");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new LoginPanel(this), "LOGIN");
        mainPanel.add(new SignUpPanel(this), "SIGNUP");

        add(mainPanel);
        cardLayout.show(mainPanel, "LOGIN");
        setVisible(true);
    }

    // Authenticate and send to correct dashboard
    public void handleLogin(String username, String password) {
        try {
            this.currentUser = authService.authenticate(username, password);

            if (currentUser instanceof Customer) {
                showCustomerDashboard();
            } else if (currentUser.getRole().equalsIgnoreCase("Driver")) {
                showDriverDashboard();
            } else if (currentUser.getRole().equalsIgnoreCase("Admin")) {
                showAdminDashboard();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Login Failed: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Navigation methods
    public void showSignUp() {
        cardLayout.show(mainPanel, "SIGNUP");
        setTitle("Create New Account");
    }

    public void showLogin() {
        cardLayout.show(mainPanel, "LOGIN");
        setTitle("Login - Food Delivery System");
        setSize(450, 400);
    }

    private void showCustomerDashboard() {
        CustomerDashboard dashboard = new CustomerDashboard(this);
        mainPanel.add(dashboard, "CUSTOMER_DASHBOARD");
        cardLayout.show(mainPanel, "CUSTOMER_DASHBOARD");

        setTitle(currentUser.getDashboardTitle());
        setSize(850, 550);
        setLocationRelativeTo(null);
    }

    private void showDriverDashboard() {
        DriverDashboard dashboard = new DriverDashboard(this);
        mainPanel.add(dashboard, "DRIVER_DASHBOARD");
        cardLayout.show(mainPanel, "DRIVER_DASHBOARD");

        setTitle(currentUser.getDashboardTitle());
        setSize(600, 500);
        setLocationRelativeTo(null);
    }

    private void showAdminDashboard() {
        AdminDashboard dashboard = new AdminDashboard(this);
        mainPanel.add(dashboard, "ADMIN_DASHBOARD");
        cardLayout.show(mainPanel, "ADMIN_DASHBOARD");

        setTitle(currentUser.getDashboardTitle());
        setSize(500, 450);
        setLocationRelativeTo(null);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}