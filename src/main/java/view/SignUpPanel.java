package view;

import services.AuthService;
import javax.swing.*;
import java.awt.*;

// Panel used by users who want to register for the first time
public class SignUpPanel extends JPanel {
    private AuthService authService = new AuthService();
    private MainFrame parent;

    public SignUpPanel(MainFrame parent) {
        this.parent = parent;
        setLayout(new GridLayout(6, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JTextField nameField = new JTextField();
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();

        // Role choosing
        String[] roles = {"Customer", "Driver", "Admin"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);

        JButton signUpBtn = new JButton("Create Account");
        JButton backBtn = new JButton("Back to Login");

        add(new JLabel("Full Name:")); add(nameField);
        add(new JLabel("Username:")); add(userField);
        add(new JLabel("Password:")); add(passField);
        add(new JLabel("I am a:")); add(roleCombo);
        add(backBtn);
        add(signUpBtn);

        signUpBtn.addActionListener(e -> {
            boolean success = authService.signUp(
                    nameField.getText(),
                    userField.getText(),
                    new String(passField.getPassword()),
                    (String)roleCombo.getSelectedItem()
            );

            if (success) {
                JOptionPane.showMessageDialog(this, "Account created! You can now log in.");
                parent.showLogin();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Try a different username.");
            }
        });

        backBtn.addActionListener(e -> parent.showLogin());
    }
}