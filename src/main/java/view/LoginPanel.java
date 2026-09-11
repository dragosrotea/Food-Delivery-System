package view;

import javax.swing.*;
import java.awt.*;

// The login panel used by users to access the app or to continue to the register new user panel
public class LoginPanel extends JPanel {
    private JTextField userField;
    private JPasswordField passField;
    private MainFrame parent;

    public LoginPanel(MainFrame parent) {
        this.parent = parent;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("Welcome Back :)", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        // Username
        gbc.gridwidth = 1; gbc.gridy = 1;
        add(new JLabel("Username:"), gbc);
        userField = new JTextField(15);
        gbc.gridx = 1;
        add(userField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);
        passField = new JPasswordField(15);
        gbc.gridx = 1;
        add(passField, gbc);

        // Login Button
        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(70, 130, 180));
        loginBtn.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(loginBtn, gbc);

        // Sign Up Link
        JButton signUpBtn = new JButton("Don't have an account? Sign Up here");
        signUpBtn.setBorderPainted(false);
        signUpBtn.setContentAreaFilled(false);
        signUpBtn.setForeground(Color.BLUE);
        signUpBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 4;
        add(signUpBtn, gbc);

        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            parent.handleLogin(username, password);
        });

        signUpBtn.addActionListener(e -> {
            parent.showSignUp();
        });
    }
}