package com.dragosrotea.fooddelivery.order;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<FoodOrder, Long> {

    @EntityGraph(attributePaths = {
            "customer", "restaurant", "driver", "items", "items.menuItem"
    })
    List<FoodOrder> findByCustomerIdOrderByCreatedAtDesc(Long customerId);

    @EntityGraph(attributePaths = {
            "customer", "restaurant", "driver", "items", "items.menuItem"
    })
    List<FoodOrder> findByDriverIdOrderByCreatedAtDesc(Long driverId);

    @EntityGraph(attributePaths = {
            "customer", "restaurant", "driver", "items", "items.menuItem"
    })
    List<FoodOrder> findByStatusAndDriverIsNullOrderByCreatedAtAsc(OrderStatus status);

    @Override
    @EntityGraph(attributePaths = {
            "customer", "restaurant", "driver", "items", "items.menuItem"
    })
    Optional<FoodOrder> findById(Long id);

    @Override
    @EntityGraph(attributePaths = {
            "customer", "restaurant", "driver", "items", "items.menuItem"
    })
    List<FoodOrder> findAll();
}
