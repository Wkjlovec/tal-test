package cn.diinj.productservice.kafka;

import cn.diinj.api.event.ProductPriceChangeEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PricesChangeEventProducer {

private static final String TOPIC = "product-price-change";
private static final Logger log = LoggerFactory.getLogger(PricesChangeEventProducer.class);

@Autowired
private KafkaTemplate<String, String> kafkaTemplate;

@Autowired
private ObjectMapper objectMapper;

public void sendPriceChangeEvent(ProductPriceChangeEvent event) {
    try {
        String message = objectMapper.writeValueAsString(event);
        kafkaTemplate.send(TOPIC, event.getProductId().toString(), message);
    } catch (JsonProcessingException e) {
        log.error("推送价格变更失败{}", event.getProductId());
    }
}
    
}
