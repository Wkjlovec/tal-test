package cn.dinnj.gatewayservice.test;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HystrixTestService {

private static final Logger log = LoggerFactory.getLogger(HystrixTestService.class);

@HystrixCommand(
        commandKey = "testCommand",
        fallbackMethod = "fallbackMethod"
)
//使用注解只会构建一次HystrixCommand对象,不会重新读取动态改变的配置
public String testHystrixTimeout() {
    try {
        
        Object property = ConfigurationManager.getConfigInstance().getProperty("hystrix.command" +
                ".default.execution.isolation.thread.timeoutInMilliseconds");
        log.info("timeout property:{}", property);
        Thread.sleep(6000);
        return "成功执行";
    } catch (InterruptedException e) {
        return "被中断";
    }
}

public String fallbackMethod() {
    return "执行了fallback方法 - 说明Hystrix配置生效，当前时间：" + System.currentTimeMillis();
}
}
