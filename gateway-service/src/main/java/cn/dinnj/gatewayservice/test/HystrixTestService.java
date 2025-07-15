package cn.dinnj.gatewayservice.test;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class HystrixTestService {

private static final Logger log = LoggerFactory.getLogger(HystrixTestService.class);


//@HystrixCommand(
//        commandKey = "testCommand",
//        fallbackMethod = "fallbackMethod"
//)
//使用注解只会构建一次HystrixCommand对象,不会重新读取动态改变的配置
//todo: 这里的动态配置不会生效，超时熔断的机制是通过
// HystrixTimer 创建一个 Runnable 任务，并通过 scheduleAtFixedRate 方法调度，延迟和周期都是配置的超时时间,
// debug的时候发现scheduleAtFixedRate中的参数没有随apollo配置一起改变
public String testHystrixTimeout() throws InterruptedException {
    //try {
    //    
    //    String property = (String) ConfigurationManager.getConfigInstance().getProperty
    //    ("hystrix.command" +
    //            ".default.execution.isolation.thread.timeoutInMilliseconds");
    //    log.info("timeout property:{}", property);
    //    Thread.sleep(6000);
    //    return "成功执行";
    //} catch (InterruptedException e) {
    //    return "被中断";
    //}
    String commandKey = "testCommand";
    // 每次从配置中心动态获取
    int timeout =
            Integer.parseInt(ConfigurationManager.getConfigInstance().getString("hystrix" +
                    ".command.default.execution.isolation.thread.timeoutInMilliseconds", "1000"));
    log.info("timeout: {}mills", timeout);
    Supplier<HystrixCommand<String>> commandSupplier = () -> new HystrixCommand<String>(
            HystrixCommand.Setter
                    .withGroupKey(HystrixCommandGroupKey.Factory.asKey("TestGroup"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                            .withExecutionTimeoutInMilliseconds(timeout))) {
        @Override
        protected String run() throws Exception {
            log.info("properties timeout: {}mills",
                    getProperties().executionTimeoutInMilliseconds().get());
            //todo:properties里的timeout没有随apollo配置一起改变
            MockRemoteService mockRemoteService = new MockRemoteService(timeout);
            mockRemoteService.execute();
            return "成功执行" + timeout + "millis";
        }
        
        @Override
        protected String getFallback() {
            return "降级返回" + timeout + "millis";
        }
    };
    
    return commandSupplier.get().execute();
}


public String fallbackMethod() {
    return "执行了fallback方法 - 说明Hystrix配置生效，当前时间：" + System.currentTimeMillis();
}
}
