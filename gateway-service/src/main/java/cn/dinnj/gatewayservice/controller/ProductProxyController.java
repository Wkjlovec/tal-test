package cn.dinnj.gatewayservice.controller;

import cn.dinnj.gatewayservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Product Proxy Controller
 * Demonstrates method-level circuit breaking by proxying requests to the product service
 * This controller uses the ProductService which has Hystrix circuit breakers configured
 */
@RestController
@RequestMapping("/proxy/products")
public class ProductProxyController {

    private final ProductService productService;

    @Autowired
    public ProductProxyController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Get all products with circuit breaker
     * This endpoint demonstrates method-level circuit breaking
     * 
     * @return Product list or fallback response
     */
    @GetMapping
    public Mono<Object> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Get product by ID with circuit breaker
     * This endpoint demonstrates method-level circuit breaking
     * 
     * @param id Product ID
     * @return Product or fallback response
     */
    @GetMapping("/{id}")
    public Mono<Object> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }
}