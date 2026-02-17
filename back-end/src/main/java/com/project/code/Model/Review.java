package com.project.code.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotNull;

@Document(collection = "reviews")
public class Review {

    // 1. MongoDB Primary Key
    @Id
    private String id;

    // 2. Customer ID - Cannot be null
    @NotNull(message = "Customer cannot be null")
    private Long customerId;

    // 3. Product ID - Cannot be null
    @NotNull(message = "Product cannot be null")
    private Long productId;

    // 4. Store ID - Cannot be null
    @NotNull(message = "Store cannot be null")
    private Long storeId;

    // 5. Rating - Cannot be null
    @NotNull(message = "Rating cannot be null")
    private Integer rating;

    // 6. Optional Comment
    private String comment;

    // 7. No-Argument Constructor (Required)
    public Review() {
    }

    // 8. Parameterized Constructor
    public Review(Long customerId, Long productId, Long storeId, Integer rating, String comment) {
        this.customerId = customerId;
        this.productId = productId;
        this.storeId = storeId;
        this.rating = rating;
        this.comment = comment;
    }

    // ======================
    // Getters and Setters
    // ======================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}