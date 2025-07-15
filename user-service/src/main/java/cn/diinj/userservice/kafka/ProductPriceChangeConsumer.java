package cn.diinj.userservice.kafka;

import cn.diinj.api.event.ProductPriceChangeEvent;
import cn.diinj.api.event.ProductPriceChangeEvent.PriceChangeType;
import cn.diinj.userservice.service.UserNotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ProductPriceChangeConsumer {

private static final Logger log = LoggerFactory.getLogger(ProductPriceChangeConsumer.class);
@Autowired
private UserNotificationService userNotificationService;

@Autowired
private ObjectMapper objectMapper;

@KafkaListener(topics = "product-price-change", groupId = "user-notification-group")
public void onPriceChange(String message) {
    log.debug("receive message");
    try {
        ProductPriceChangeEvent event = objectMapper.readValue(message,
                ProductPriceChangeEvent.class);
        // 根据价格变更类型发送不同的通知
        if (PriceChangeType.DECREASE.equals(event.getChangeType())) {
            userNotificationService.notifyPriceDrop(
                    event.getProductId(),
                    event.getProductName(),
                    event.getOldPrice().toString(),
                    event.getNewPrice().toString()
            );
        }
        else if (PriceChangeType.INCREASE.equals(event.getChangeType())) {
            userNotificationService.notifyPriceIncrease(
                    event.getProductId(),
                    event.getProductName(),
                    event.getOldPrice().toString(),
                    event.getNewPrice().toString()
            );
        }
    } catch (Exception e) {
        // 记录日志
        e.printStackTrace();
    }
}
} 