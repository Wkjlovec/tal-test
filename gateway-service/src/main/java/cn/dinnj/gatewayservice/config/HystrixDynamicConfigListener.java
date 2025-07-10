package cn.dinnj.gatewayservice.config;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.netflix.config.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HystrixDynamicConfigListener {

private static final Logger logger = LoggerFactory.getLogger(HystrixDynamicConfigListener.class);

@ApolloConfigChangeListener(value = {"application", "hystrix-config"})//value->namespace
public void onConfigChange(ConfigChangeEvent changeEvent) {
    changeEvent.changedKeys().stream()
            .filter(key -> key.startsWith("hystrix."))
            .forEach(key -> {
                String newValue = changeEvent.getChange(key).getNewValue();
                ConfigurationManager.getConfigInstance().setProperty(key, newValue);
                logger.info("动态更新Hystrix配置: {} = {}", key, newValue);
            });
}
}
