package com.project.code.Controller;

import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ServiceClass serviceClass;

    @Autowired
    private InventoryRepository inventoryRepository;

    @PostMapping
    public Map<String, String> addProduct(@RequestBody Product product) {
        Map<String, String> response = new HashMap<>();

        try {
            boolean isValid = serviceClass.validateProduct(product);

            if (!isValid) {
                response.put("message", "Product already exists");
                return response;
            }

            productRepository.save(product);
            response.put("message", "Product saved successfully");
        } catch (DataIntegrityViolationException ex) {
            response.put("message", "Data integrity violation");
        } catch (Exception ex) {
            response.put("message", "Error occurred while saving product");
        }

        return response;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getProductById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            response.put("products", product.get());
        } else {
            response.put("products", null);
        }

        return response;
    }

    @PutMapping
    public Map<String, String> updateProduct(@RequestBody Product product) {
        Map<String, String> response = new HashMap<>();

        try {
            productRepository.save(product);
            response.put("message", "Product updated successfully");
        } catch (Exception ex) {
            response.put("message", "Error occurred while updating product");
        }

        return response;
    }

    @GetMapping("/category/{name}/{category}")
    public Map<String, Object> filterByCategoryProduct(
            @PathVariable String name,
            @PathVariable String category) {

        Map<String, Object> response = new HashMap<>();
        List<Product> products;

        if ("null".equalsIgnoreCase(name)) {
            products = productRepository.findByCategory(category);
        } else if ("null".equalsIgnoreCase(category)) {
            products = productRepository.findProductBySubName(name);
        } else {
            products = productRepository.findProductBySubNameAndCategory(name, category);
        }

        response.put("products", products);
        return response;
    }

    @GetMapping
    public Map<String, Object> listProduct() {
        Map<String, Object> response = new HashMap<>();

        List<Product> products = productRepository.findAll();
        response.put("products", products);

        return response;
    }

    @GetMapping("/filter/{category}/{storeid}")
    public Map<String, Object> getProductByCategoryAndStoreId(
            @PathVariable String category,
            @PathVariable Long storeid) {

        Map<String, Object> response = new HashMap<>();

        List<Product> products = productRepository.findProductByCategory(category, storeid);
        response.put("product", products);

        return response;
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();

        boolean exists = serviceClass.validateProductId(id);

        if (!exists) {
            response.put("message", "Product not present in database");
            return response;
        }

        inventoryRepository.deleteByProductId(id);
        productRepository.deleteById(id);

        response.put("message", "Product deleted successfully");
        return response;
    }

    @GetMapping("/searchProduct/{name}")
    public Map<String, Object> searchProduct(@PathVariable String name) {
        Map<String, Object> response = new HashMap<>();

        List<Product> products = productRepository.findProductBySubName(name);
        response.put("products", products);

        return response;
    }
}