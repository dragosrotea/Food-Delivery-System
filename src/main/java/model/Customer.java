package model;

public class Customer extends User {

    public Customer(int id, String name, String username, String password) {
        super(id, name, username, password, "Customer");
    }

    @Override
    public String getDashboardTitle() {
        return "Customer Portal - Welcome, " + getName();
    }
}