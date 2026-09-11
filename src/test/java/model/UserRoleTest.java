package model;

import exceptions.InvalidRoleException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRoleTest {

    @Test
    void acceptsKnownRole() {
        assertTrue(User.isValidRole("Customer"));
    }

    @Test
    void rejectsUnknownRole() {
        assertThrows(InvalidRoleException.class, () -> User.isValidRole("Manager"));
    }

    @Test
    void rejectsNullRole() {
        assertThrows(InvalidRoleException.class, () -> User.isValidRole(null));
    }
}
