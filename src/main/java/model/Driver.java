package model;

public class Driver extends User {
    public Driver(int id, String name, String username, String password) {
        super(id, name, username, password, "Driver");
    }

    @Override
    public String getDashboardTitle() {
        return "Driver Dashboard - Online: " + getName();
    }
}
