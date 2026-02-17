package com.project.code.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Entity
public class Store {

    // 1. Primary Key - Auto Increment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // 2. Name - Cannot be null or blank
    @NotNull(message = "Store name cannot be null")
    @NotBlank(message = "Store name cannot be blank")
    private String name;

    // 3. Address - Cannot be null or blank
    @NotNull(message = "Address cannot be null")
    @NotBlank(message = "Address cannot be blank")
    private String address;

    // 4. One-to-Many relationship with Inventory
    @OneToMany(mappedBy = "store")
    @JsonManagedReference("inventory-store")
    private List<Inventory> inventories;

    // 5. Default Constructor (Required by JPA)
    public Store() {
    }

    // 6. Constructor with name and address
    public Store(String name, String address) {
        this.name = name;
        this.address = address;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }
}