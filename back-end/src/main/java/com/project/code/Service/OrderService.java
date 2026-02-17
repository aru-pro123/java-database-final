package com.project.code.Service;

import com.project.code.Model.*;
import com.project.code.Repo.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;


    @Transactional
    public void saveOrder(PlaceOrderRequestDTO placeOrderRequest) {

        // ==============================
        // 1️⃣ Retrieve or Create Customer
        // ==============================

        Customer customer = customerRepository
                .findByEmail(placeOrderRequest.getCustomer().getEmail())
                .orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setName(placeOrderRequest.getCustomer().getName());
                    newCustomer.setEmail(placeOrderRequest.getCustomer().getEmail());
                    return customerRepository.save(newCustomer);
                });

        // ==============================
        // 2️⃣ Retrieve Store
        // ==============================

        Store store = storeRepository
                .findById(placeOrderRequest.getStoreId())
                .orElseThrow(() ->
                        new RuntimeException("Store not found with id: " + placeOrderRequest.getStoreId())
                );

        // ==============================
        // 3️⃣ Create OrderDetails
        // ==============================

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCustomer(customer);
        orderDetails.setStore(store);
        orderDetails.setTotalPrice(placeOrderRequest.getTotalPrice());
        orderDetails.setOrderDate(LocalDateTime.now());

        orderDetails = orderDetailsRepository.save(orderDetails);

        // ==============================
        // 4️⃣ Process Each Product
        // ==============================

        for (PurchaseProductDTO purchaseProduct : placeOrderRequest.getPurchaseProduct()) {

            // 🔹 Get Product using DTO id
            Product product = productRepository
                    .findById(purchaseProduct.getId())
                    .orElseThrow(() ->
                            new RuntimeException("Product not found with id: " + purchaseProduct.getId())
                    );

            // 🔹 Get Inventory for product + store
            Inventory inventory = inventoryRepository
                    .findByProductIdAndStoreId(product.getId(), store.getId())
                    .orElseThrow(() ->
                            new RuntimeException("Inventory not found for product in this store")
                    );

            // 🔹 Check stock availability
            if (inventory.getStockLevel() < purchaseProduct.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for product: " + product.getName()
                );
            }

            // 🔹 Decrease stock
            inventory.setStockLevel(
                    inventory.getStockLevel() - purchaseProduct.getQuantity()
            );

            inventoryRepository.save(inventory);

            // ==============================
            // 5️⃣ Create OrderItem
            // ==============================

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderDetails(orderDetails);
            orderItem.setProduct(product);
            orderItem.setQuantity(purchaseProduct.getQuantity());

            // You can use product price OR DTO price
            orderItem.setPrice(product.getPrice());

            orderItemRepository.save(orderItem);
        }
    }
}