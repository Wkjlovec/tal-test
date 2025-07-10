package cn.dinnj.gatewayservice;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * Gateway Service Application
 * Main entry point for the gateway service
 * Enables Eureka client for service discovery
 * Enables Hystrix for circuit breaking
 */
@SpringBootApplication
@EnableEurekaClient
@EnableHystrix
@EnableCircuitBreaker
@EnableApolloConfig
public class GateWayServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GateWayServiceApplication.class, args);
    }
}
