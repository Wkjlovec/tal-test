package cn.diinj.consumerservice.controller;

import cn.diinj.api.client.ProductServiceClient;
import cn.diinj.api.model.Product;
import cn.diinj.api.model.Result;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    //调用方使用HystrixCommand进行细粒度的熔断控制
class GetProductCommand extends HystrixCommand<Product> {
    private final Long productId;
    
    public GetProductCommand(Long productId) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductService")) // 3
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(20) // 3
                )
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(100) // 3
                )
        );
        this.productId = productId;
    }
    
    @Override
    protected Product run() {
        return productServiceClient.getProductById(productId);
    }
    
    @Override
    protected Product getFallback() {
        //todo
        return null;
    }
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
        GetProductCommand hystrixCommand = new GetProductCommand(id);
        Product product = hystrixCommand.execute();
        if (product != null) {
            return Result.success(product);
        }
        else {
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