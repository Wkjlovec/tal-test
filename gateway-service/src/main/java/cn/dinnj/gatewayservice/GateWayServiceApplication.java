package cn.dinnj.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class GateWayServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GateWayServiceApplication.class, args);
    }
}
