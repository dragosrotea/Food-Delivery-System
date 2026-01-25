package model;

import exceptions.InvalidRoleException;

public abstract class User {
    private int id;
    private final String name;
    private final String username;
    private final String password;
    private final String role;

    public User(int id, String name, String username, String password, String role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String name, String username, String password, String role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getName() {return name;}
    public String getUsername() {return username;}
    public String getRole() {return role;}

    public abstract String getDashboardTitle();

    public static boolean isValidRole(String role) throws InvalidRoleException {
        if (role == null) {
            throw new InvalidRoleException("Role cannot be null");
        }

        boolean isValid = role.equals("Customer") || role.equals("Driver") || role.equals("Admin");

        if (!isValid) {
            throw new InvalidRoleException("Access Denied: '" + role + "' is not a recognized role");
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s [ID: %d, Name: %s, Role: %s]",
                getClass().getSimpleName(), id, name, role);
    }
}


