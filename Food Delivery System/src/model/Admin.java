package model;

public class Admin extends User {

    public Admin(int id, String name, String username, String password) {
        super(id, name, username, password, "Admin");
    }

    @Override
    public String getDashboardTitle() {
        return "Administrator Panel - " + getName();
    }
}
