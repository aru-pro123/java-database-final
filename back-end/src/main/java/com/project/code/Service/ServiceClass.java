package com.project.code.Service;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceClass {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    // Constructor Injection (Recommended)
    public ServiceClass(InventoryRepository inventoryRepository,
                        ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    // 1. Validate Inventory (Prevent duplicate product-store pair)
    public boolean validateInventory(Inventory inventory) {

        Inventory existingInventory =
                inventoryRepository.findByProduct_IdAndStore_Id(
                        inventory.getProduct().getId(),
                        inventory.getStore().getId()
                );

        // If inventory already exists → return false
        return existingInventory == null;
    }

    // 2. Validate Product (Prevent duplicate product name)
    public boolean validateProduct(Product product) {

        Product existingProduct =
                productRepository.findByName(product.getName());

        // If product with same name exists → return false
        return existingProduct == null;
    }

    // 3. Validate Product by ID
    public boolean validateProductId(long id) {

        Optional<Product> product = productRepository.findById(id);

        // If product does not exist → return false
        return product.isPresent();
    }

    // 4. Get Inventory by Product-Store combination
    public Inventory getInventoryId(Inventory inventory) {

        return inventoryRepository.f(
                inventory.getProduct().getId(),
                inventory.getStore().getId()
        );
    }
}