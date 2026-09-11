package test;

import exceptions.InvalidRoleException;
import model.User;

public class SecurityTest {
    public static void main(String[] args) {
        testRoleValidation();
    }

    public static void testRoleValidation() {
        System.out.println("[Security Role Test]");

        // Invalid role
        String roleToTest1 = "Manager";
        try {
            User.isValidRole(roleToTest1);
            System.out.println("Role: " + roleToTest1 + " [FAILED]");
        } catch (InvalidRoleException e) {
            System.out.println("Caught Expected Exception: " + e.getMessage());
            System.out.println("[PASSED]");
        }

        System.out.println();

        // Valid role
        String roleToTest2 = "Customer";
        try {
            User.isValidRole(roleToTest2);
            System.out.println("Role: " + roleToTest2 + " [PASSED]");
        } catch (InvalidRoleException e) {
            System.out.println("Caught Unexpected Exception: " + e.getMessage());
            System.out.println("[FAILED]");
        }
    }
}