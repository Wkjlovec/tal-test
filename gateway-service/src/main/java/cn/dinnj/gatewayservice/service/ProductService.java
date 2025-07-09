package cn.dinnj.gatewayservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Product Service
 * Demonstrates method-level circuit breaking with fallback functions
 */
//todo
@Service
public class ProductService {

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public ProductService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * Get all products with circuit breaker
     * This method demonstrates method-level circuit breaking with a fallback method
     * 
     * @return Product list or fallback response
     */
    @HystrixCommand(fallbackMethod = "getAllProductsFallback",
            commandProperties = {
                    // 熔断器在整个统计时间内是否开启的阀值
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
                    // 至少有3个请求才进行熔断错误比率计算
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    // 熔断器默认工作时间,默认:5秒
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),
                    // 错误率 超过50%触发熔断
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    // 统计滚动的时间窗口
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000")
            })
    public Mono<Object> getAllProducts() {
        return webClientBuilder.build()
                .get()
                .uri("lb://product-service/api/products")
                .retrieve()
                .bodyToMono(Object.class)
                .onErrorResume(error -> {
                    return getAllProductsFallback();
                });
    }

    /**
     * Fallback method for getAllProducts
     * This method is called when the circuit is open or when an error occurs
     * 
     * @return Fallback response
     */
    public Mono<Object> getAllProductsFallback() {
        Map<String, Object> fallbackResponse = new HashMap<>();
        fallbackResponse.put("status", "error");
        fallbackResponse.put("message", "Unable to retrieve products. Using cached or default data.");
        fallbackResponse.put("isFallback", true);
        return Mono.just(fallbackResponse);
    }

    /**
     * Get product by ID with circuit breaker
     * This method demonstrates method-level circuit breaking with a fallback method
     * 
     * @param id Product ID
     * @return Product or fallback response
     */
    @HystrixCommand(fallbackMethod = "getProductByIdFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50")
            })
    public Mono<Object> getProductById(Long id) {
        return webClientBuilder.build()
                .get()
                .uri("lb://product-service/api/products/" + id)
                .retrieve()
                .bodyToMono(Object.class)
                .onErrorResume(error -> {
                    return getProductByIdFallback(id);
                });
    }

    /**
     * Fallback method for getProductById
     * This method is called when the circuit is open or when an error occurs
     * 
     * @param id Product ID
     * @return Fallback response
     */
    public Mono<Object> getProductByIdFallback(Long id) {
        Map<String, Object> fallbackResponse = new HashMap<>();
        fallbackResponse.put("status", "error");
        fallbackResponse.put("message", "Unable to retrieve product with ID: " + id + ". Using cached or default data.");
        fallbackResponse.put("productId", id);
        fallbackResponse.put("isFallback", true);
        return Mono.just(fallbackResponse);
    }
}