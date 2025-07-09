package cn.diinj.productservice.controller;

import cn.diinj.productservice.model.Product;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Product Controller
 * Provides REST endpoints for product operations
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    // Simulate a product database with a Map
    private static final Map<Long, Product> productMap = new HashMap<>();

    // Initialize with some sample products
    static {
        productMap.put(1L, new Product(1L, "iPhone 15 Pro", "Apple's latest smartphone", 
                        new BigDecimal("7999.00"), 100, "Apple", "IP15PRO001"));
        productMap.put(2L, new Product(2L, "MacBook Pro 14", "Apple's professional laptop", 
                        new BigDecimal("14999.00"), 50, "Apple", "MBP14001"));
        productMap.put(3L, new Product(3L, "Xiaomi 13 Pro", "Xiaomi's flagship phone", 
                        new BigDecimal("4999.00"), 200, "Xiaomi", "MI13PRO001"));
    }

    /**
     * Get all products
     * @return List of all products
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return new ArrayList<>(productMap.values());
    }

    /**
     * Get product by ID
     * @param id Product ID
     * @return Product if found, or null
     */
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productMap.get(id);
    }

    /**
     * Create a new product
     * @param product Product to create
     * @return Created product
     */
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        // Generate a new ID (in a real app, this would be done by the database)
        Long newId = productMap.size() + 1L;
        product.setId(newId);
        productMap.put(newId, product);
        return product;
    }

    /**
     * Update an existing product
     * @param id Product ID
     * @param product Updated product data
     * @return Updated product
     */
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        productMap.put(id, product);
        return product;
    }

    /**
     * Delete a product
     * @param id Product ID
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(@PathVariable Long id) {
        productMap.remove(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product deleted successfully");
        return response;
    }

    /**
     * Slow endpoint to demonstrate circuit breaking
     * This endpoint will delay for 3 seconds before responding
     * @return List of products
     */
    @GetMapping("/slow")
    public List<Product> getProductsWithDelay() throws InterruptedException {
        // Simulate a slow response
        TimeUnit.SECONDS.sleep(3);
        return new ArrayList<>(productMap.values());
    }

    /**
     * Endpoint that randomly fails to demonstrate circuit breaking
     * @return List of products or error
     */
    @GetMapping("/unstable")
    public List<Product> getUnstableProducts() {
        // Randomly fail 50% of the time
        if (Math.random() < 0.5) {
            throw new RuntimeException("Service temporarily unavailable");
        }
        return new ArrayList<>(productMap.values());
    }
}