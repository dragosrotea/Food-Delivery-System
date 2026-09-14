package com.dragosrotea.fooddelivery.order;

import com.dragosrotea.fooddelivery.restaurant.Restaurant;
import com.dragosrotea.fooddelivery.user.UserAccount;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "orders")
public class FoodOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private UserAccount customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private OrderStatus status;

    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "delivery_street", nullable = false, length = 160)
    private String deliveryStreet;

    @Column(name = "delivery_city", nullable = false, length = 100)
    private String deliveryCity;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    protected FoodOrder() {
    }

    public FoodOrder(UserAccount customer, Restaurant restaurant, String deliveryStreet, String deliveryCity) {
        this.customer = customer;
        this.restaurant = restaurant;
        this.deliveryStreet = deliveryStreet;
        this.deliveryCity = deliveryCity;
        this.status = OrderStatus.PLACED;
        this.totalPrice = BigDecimal.ZERO;
        this.createdAt = OffsetDateTime.now();
    }

    public void addItem(OrderItem item) {
        items.add(item);
        totalPrice = totalPrice.add(item.getLineTotal());
    }

    public void changeStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getId() { return id; }
    public UserAccount getCustomer() { return customer; }
    public Restaurant getRestaurant() { return restaurant; }
    public OrderStatus getStatus() { return status; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public String getDeliveryStreet() { return deliveryStreet; }
    public String getDeliveryCity() { return deliveryCity; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }
}
