package com.dragosrotea.fooddelivery.driver;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DriverResponsePrivacyTest {

    @Test
    void availableDeliveriesDoNotExposeCustomerOrExactAddress() {
        Set<String> fields = recordFields(AvailableDeliveryResponse.class);

        assertFalse(fields.contains("customerId"));
        assertFalse(fields.contains("customerEmail"));
        assertFalse(fields.contains("deliveryStreet"));
        assertTrue(fields.contains("deliveryCity"));
    }

    @Test
    void assignedDeliveriesIncludeAddressButNotCustomerIdentity() {
        Set<String> fields = recordFields(DriverDeliveryResponse.class);

        assertTrue(fields.contains("deliveryStreet"));
        assertTrue(fields.contains("deliveryCity"));
        assertFalse(fields.contains("customerId"));
        assertFalse(fields.contains("customerEmail"));
    }

    private Set<String> recordFields(Class<?> recordType) {
        return Arrays.stream(recordType.getRecordComponents())
                .map(component -> component.getName())
                .collect(Collectors.toSet());
    }
}
