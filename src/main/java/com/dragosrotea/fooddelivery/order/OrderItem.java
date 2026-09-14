package com.dragosrotea.fooddelivery.order;

import com.dragosrotea.fooddelivery.restaurant.MenuItem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private FoodOrder order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @Column(name = "item_name", nullable = false, length = 120)
    private String itemName;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "line_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal lineTotal;

    protected OrderItem() {
    }

    public OrderItem(FoodOrder order, MenuItem menuItem, int quantity) {
        this.order = order;
        this.menuItem = menuItem;
        this.itemName = menuItem.getName();
        this.unitPrice = menuItem.getPrice();
        this.quantity = quantity;
        this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getId() { return id; }
    public FoodOrder getOrder() { return order; }
    public MenuItem getMenuItem() { return menuItem; }
    public String getItemName() { return itemName; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
    public BigDecimal getLineTotal() { return lineTotal; }
}
