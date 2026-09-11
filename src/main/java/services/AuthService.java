package services;

import database.UserDAO;
import exceptions.InvalidLoginException;
import model.User;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public User authenticate(String username, String password) throws InvalidLoginException {
        if (username == null || username.isEmpty()) {
            throw new InvalidLoginException("Username cannot be empty");
        }

        User user = userDAO.login(username, password);

        if (user == null) {
            throw new InvalidLoginException("Invalid username or password");
        }

        return user;
    }

    public boolean signUp(String name, String username, String password, String role) {
        if (name.trim().isEmpty() || username.trim().isEmpty() || password.isEmpty()) {
            return false;
        }

        return userDAO.registerUser(name, username, password, role);
    }
}