package cn.diinj.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = "cn.diinj.api.client")
public class UserServiceApplication {

public static void main(String[] args) {
  SpringApplication.run(UserServiceApplication.class, args);
}
}
