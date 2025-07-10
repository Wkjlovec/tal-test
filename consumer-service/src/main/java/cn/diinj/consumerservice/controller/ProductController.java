package cn.diinj.consumerservice.controller;

import cn.diinj.api.client.ProductServiceClient;
import cn.diinj.api.model.Product;
import cn.diinj.api.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Product Controller
 * Provides endpoints that call the product-service through the gateway
 * Demonstrates the use of Feign client with circuit breaking
 */
@RestController
@RequestMapping("/consumer/products")
public class ProductController {

    private final ProductServiceClient productServiceClient;

    @Autowired
    public ProductController(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    /**
     * Get all products
     * Calls the product-service getAllProducts endpoint through the gateway
     * 
     * @return List of all products
     */
    @GetMapping
    public Result<List<Product>> getAllProducts() {
        List<Product> products = productServiceClient.getAllProducts();
        return Result.success(products);
    }

    /**
     * Get product by ID
     * Calls the product-service getProductById endpoint through the gateway
     * 
     * @param id Product ID
     * @return Product if found, or error
     */
    @GetMapping("/{id}")
    public Result<Product> getProductById(@PathVariable Long id) {
        Product product = productServiceClient.getProductById(id);
        if (product != null) {
            return Result.success(product);
        } else {
            return Result.error(404, "Product not found");
        }
    }

    /**
     * Create a new product
     * Calls the product-service createProduct endpoint through the gateway
     * 
     * @param product Product to create
     * @return Created product
     */
    @PostMapping
    public Result<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productServiceClient.createProduct(product);
        return Result.success(createdProduct);
    }

    /**
     * Update an existing product
     * Calls the product-service updateProduct endpoint through the gateway
     * 
     * @param id Product ID
     * @param product Updated product data
     * @return Updated product
     */
    @PutMapping("/{id}")
    public Result<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productServiceClient.updateProduct(id, product);
        return Result.success(updatedProduct);
    }

    /**
     * Delete a product
     * Calls the product-service deleteProduct endpoint through the gateway
     * 
     * @param id Product ID
     * @return Success message
     */
    @DeleteMapping("/{id}")
    public Result<Map<String, String>> deleteProduct(@PathVariable Long id) {
        Map<String, String> result = productServiceClient.deleteProduct(id);
        return Result.success(result);
    }
}