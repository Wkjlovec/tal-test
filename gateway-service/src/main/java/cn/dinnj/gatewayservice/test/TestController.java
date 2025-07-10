package cn.dinnj.gatewayservice.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

@Autowired
private HystrixTestService hystrixTestService;

@GetMapping("/test/hystrix")
public String testHystrix() {
    return hystrixTestService.testHystrixTimeout();
}
}
