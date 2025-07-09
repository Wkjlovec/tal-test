package cn.dinnj.gatewayservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient Configuration
 * Configures a load-balanced WebClient.Builder bean
 */
@Configuration
public class WebClientConfig {

    /**
     * Creates a load-balanced WebClient.Builder bean
     * The @LoadBalanced annotation enables client-side load balancing
     * 
     * @return WebClient.Builder
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}