package com.project.code.Controller;

import com.project.code.Model.*;
import com.project.code.Repo.*;
import com.project.code.Service.ServiceClass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ServiceClass serviceClass;

    //  UPDATE INVENTORY
    @PutMapping
    public Map<String, String> updateInventory(@RequestBody CombinedRequest request) {

        Map<String, String> response = new HashMap<>();

        try {
            Product product = request.getProduct();
            Inventory inventory = request.getInventory();

            // Validate product ID
            if (!serviceClass.validateProductId(product.getId())) {
                response.put("message", "Product not found");
                return response;
            }

            // Check inventory
            Inventory existingInventory =
                    inventoryRepository.findByProductIdAndStoreId(
                            product.getId(),
                            inventory.getStore().getId()
                    );

            if (existingInventory != null) {
                existingInventory.setStockLevel(inventory.getStockLevel());
                inventoryRepository.save(existingInventory);
                response.put("message", "Successfully updated product");
            } else {
                response.put("message", "No data available");
            }

        } catch (DataIntegrityViolationException e) {
            response.put("message", "Database error");
        }

        return response;
    }

    // SAVE INVENTORY
    @PostMapping
    public Map<String, String> saveInventory(@RequestBody Inventory inventory) {

        Map<String, String> response = new HashMap<>();

        try {
            boolean isValid = serviceClass.validateInventory(inventory);

            if (!isValid) {
                response.put("message", "Data already present");
            } else {
                inventoryRepository.save(inventory);
                response.put("message", "Data saved successfully");
            }

        } catch (Exception e) {
            response.put("message", "Error saving data");
        }

        return response;
    }

    //  GET PRODUCTS BY STORE
    @GetMapping("/{storeId}")
    public Map<String, Object> getAllProducts(@PathVariable Long storeId) {

        Map<String, Object> response = new HashMap<>();

        List<Product> products = productRepository.findProductsByStoreId(storeId);
        response.put("products", products);

        return response;
    }

    // FILTER PRODUCTS
    @GetMapping("filter/{category}/{name}/{storeId}")
    public Map<String, Object> getProductName(
            @PathVariable String category,
            @PathVariable String name,
            @PathVariable Long storeId) {

        Map<String, Object> response = new HashMap<>();
        List<Product> products;

        if ("null".equals(category)) {
            products = productRepository.findByNameLike(storeId, name);
        } else if ("null".equals(name)) {
            products = productRepository.findByCategoryAndStoreId(storeId, category);
        } else {
            products = productRepository.findByNameAndCategory(storeId, name, category);
        }

        response.put("product", products);
        return response;
    }

    //  SEARCH PRODUCT
    @GetMapping("search/{name}/{storeId}")
    public Map<String, Object> searchProduct(
            @PathVariable String name,
            @PathVariable Long storeId) {

        Map<String, Object> response = new HashMap<>();

        List<Product> products =
                productRepository.findByNameLike(storeId, name);

        response.put("product", products);
        return response;
    }

    // DELETE PRODUCT
    @DeleteMapping("/{id}")
    public Map<String, String> removeProduct(@PathVariable Long id) {

        Map<String, String> response = new HashMap<>();

        if (!serviceClass.validateProductId(id)) {
            response.put("message", "Product not present in database");
            return response;
        }

        productRepository.deleteById(id);
        inventoryRepository.deleteByProductId(id);

        response.put("message", "Product deleted successfully");
        return response;
    }

    // VALIDATE QUANTITY
    @GetMapping("validate/{quantity}/{storeId}/{productId}")
    public boolean validateQuantity(
            @PathVariable int quantity,
            @PathVariable Long storeId,
            @PathVariable Long productId) {

        Inventory inventory =
                inventoryRepository.findByProductIdAndStoreId(productId, storeId);

        if (inventory == null) {
            return false;
        }

        return inventory.getStockLevel() >= quantity;
    }
}