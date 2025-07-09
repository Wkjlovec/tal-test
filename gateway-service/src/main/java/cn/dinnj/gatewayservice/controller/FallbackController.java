package cn.dinnj.gatewayservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * Fallback Controller
 * Provides fallback responses when services are unavailable
 * This is an example of service-level circuit breaking
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    /**
     * Fallback for user-service
     * @return Fallback response
     */
    @GetMapping("/user-service")
    public Mono<Map<String, Object>> userServiceFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "User service is currently unavailable. Please try again later.");
        response.put("code", 503);
        response.put("service", "user-service");
        return Mono.just(response);
    }

    /**
     * Fallback for product-service
     * @return Fallback response
     */
    @GetMapping("/product-service")
    public Mono<Map<String, Object>> productServiceFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "Product service is currently unavailable. Please try again later.");
        response.put("code", 503);
        response.put("service", "product-service");
        return Mono.just(response);
    }
}