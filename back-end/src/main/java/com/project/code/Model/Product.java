package com.project.code.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Entity
@Table(
    name = "product",
    uniqueConstraints = @UniqueConstraint(columnNames = "sku")
)
public class Product {

    // 1. Primary Key - Auto Increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // 2. Name - Cannot be null
    @NotNull(message = "Name cannot be null")
    private String name;

    // 3. Category - Cannot be null
    @NotNull(message = "Category cannot be null")
    private String category;

    // 4. Price - Cannot be null
    @NotNull(message = "Price cannot be null")
    private Double price;

    // 5. SKU - Cannot be null and must be unique
    @NotNull(message = "SKU cannot be null")
    private String sku;

    // 6. One-to-Many relationship with Inventory
    @OneToMany(mappedBy = "product")
    @JsonManagedReference("inventory-product")
    private List<Inventory> inventories;

    // ======================
    // Getters and Setters
    // ======================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }
}