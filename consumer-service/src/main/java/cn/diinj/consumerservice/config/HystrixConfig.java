package cn.diinj.consumerservice.config;

import cn.diinj.api.client.ProductServiceClient;
import cn.diinj.api.client.UserServiceClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Hystrix Configuration
 * Configures Hystrix for circuit breaking
 */
@Configuration
@EnableHystrix
public class HystrixConfig {

@Bean
public UserServiceClient.UserServiceFallback userServiceFallback() {
    return new UserServiceClient.UserServiceFallback();
}

@Bean
public ProductServiceClient.ProductServiceFallback productServiceFallback() {
    return new ProductServiceClient.ProductServiceFallback();
}
}