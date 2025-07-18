package cn.diinj.api.client;

import cn.diinj.api.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Product Service Client
 * Feign client for calling product-service through the gateway
 * The fallback class provides fallback responses when the service is unavailable
 */
@FeignClient(name = "product-service", path = "/products", fallback =
        ProductServiceClient.ProductServiceFallback.class)
//api模块 feign进行简单fallback,方便复用
public interface ProductServiceClient {

/**
 * Get all products
 *
 * @return List of all products
 */
@GetMapping
List<Product> getAllProducts();

@GetMapping("/hystrix-timeout")
String testHystrixTimeout(int timeout) throws InterruptedException;


/**
 * Get product by ID
 *
 * @param id Product ID
 * @return Product if found, or null
 */
@GetMapping("/{id}")
Product getProductById(@PathVariable("id") Long id);

/**
 * Create a new product
 *
 * @param product Product to create
 * @return Created product
 */
@PostMapping
Product createProduct(@RequestBody Product product);

/**
 * Update an existing product
 *
 * @param id      Product ID
 * @param product Updated product data
 * @return Updated product
 */
@PutMapping("/{id}")
Product updateProduct(@PathVariable("id") Long id, @RequestBody Product product);

/**
 * Delete a product
 *
 * @param id Product ID
 * @return Success message
 */
@DeleteMapping("/{id}")
Map<String, String> deleteProduct(@PathVariable("id") Long id);

/**
 * Get products with delay (to demonstrate circuit breaking)
 *
 * @return List of products
 */
@GetMapping("/slow")
List<Product> getProductsWithDelay();

/**
 * Get unstable products (to demonstrate circuit breaking)
 *
 * @return List of products
 */
@GetMapping("/unstable")
List<Product> getUnstableProducts();

/**
 * Get user's favorite products
 *
 * @param userId User ID
 * @return List of user's favorite products
 */
@GetMapping("/user/{userId}/favorites")
List<Product> getUserFavoriteProducts(@PathVariable("userId") Long userId);

/**
 * Add product to user's favorites
 *
 * @param userId    User ID
 * @param productId Product ID
 * @return Success message
 */
@PostMapping("/user/{userId}/favorites/{productId}")
Map<String, String> addToUserFavorites(@PathVariable("userId") Long userId, @PathVariable(
        "productId") Long productId);

/**
 * Remove product from user's favorites
 *
 * @param userId    User ID
 * @param productId Product ID
 * @return Success message
 */
@DeleteMapping("/user/{userId}/favorites/{productId}")
Map<String, String> removeFromUserFavorites(@PathVariable("userId") Long userId, @PathVariable(
        "productId") Long productId);

/**
 * Fallback implementation for ProductServiceClient
 * Provides fallback responses when the service is unavailable
 */
class ProductServiceFallback implements ProductServiceClient {
    
    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>();
    }
    
    @Override
    public String testHystrixTimeout(int timeout) throws InterruptedException {
        return "";
    }
    
    @Override
    public Product getProductById(Long id) {
        return null;
    }
    
    @Override
    public Product createProduct(Product product) {
        return null;
    }
    
    @Override
    public Product updateProduct(Long id, Product product) {
        return null;
    }
    
    @Override
    public Map<String, String> deleteProduct(Long id) {
        Map<String, String> message = new HashMap<>();
        message.put("message", "deleteProduct:Product service is currently unavailable. ");
        return message;
    }
    
    @Override
    public List<Product> getProductsWithDelay() {
        return new ArrayList<>();
    }
    
    @Override
    public List<Product> getUnstableProducts() {
        return new ArrayList<>();
    }
    
    @Override
    public List<Product> getUserFavoriteProducts(Long userId) {
        return new ArrayList<>();
    }
    
    @Override
    public Map<String, String> addToUserFavorites(Long userId, Long productId) {
        Map<String, String> message = new HashMap<>();
        message.put("message", "addToUserFavorites: Product service is currently unavailable.");
        return message;
    }
    
    @Override
    public Map<String, String> removeFromUserFavorites(Long userId, Long productId) {
        Map<String, String> message = new HashMap<>();
        message.put("message", "removeFromUserFavorites: Product service is currently " +
                "unavailable.");
        return message;
    }
}
} 