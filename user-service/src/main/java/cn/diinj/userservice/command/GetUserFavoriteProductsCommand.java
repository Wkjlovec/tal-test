package cn.diinj.userservice.command;

import cn.diinj.api.client.ProductServiceClient;
import cn.diinj.api.model.Product;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * HystrixCommand for getting user favorite products
 * Provides circuit breaker protection for product service calls
 */
public class GetUserFavoriteProductsCommand extends HystrixCommand<List<Product>> {

private static final Logger log = LoggerFactory.getLogger(GetUserFavoriteProductsCommand.class);

private final ProductServiceClient productServiceClient;
private final Long userId;

public GetUserFavoriteProductsCommand(ProductServiceClient productServiceClient, Long userId) {
    super(Setter
            .withGroupKey(HystrixCommandGroupKey.Factory.asKey("UserService"))
            .andCommandKey(HystrixCommandKey.Factory.asKey("GetUserFavoriteProducts"))
            .andCommandPropertiesDefaults(
                    HystrixCommandProperties.Setter()
                            // 熔断器开启的阈值，默认20次
                            .withCircuitBreakerRequestVolumeThreshold(10)
                            // 熔断器开启的错误比例，默认50%
                            .withCircuitBreakerErrorThresholdPercentage(50)
                            // 熔断器开启后多久尝试恢复，默认5秒
                            .withCircuitBreakerSleepWindowInMilliseconds(5000)
                            // 超时时间，默认1秒
                            .withExecutionTimeoutInMilliseconds(3000)
                            // 是否启用超时
                            .withExecutionTimeoutEnabled(true)
                            // 是否启用熔断器
                            .withCircuitBreakerEnabled(true)
                            // 是否启用降级
                            .withFallbackEnabled(true)
            )
    );
    
    this.productServiceClient = productServiceClient;
    this.userId = userId;
}

@Override
protected List<Product> run() throws Exception {
    log.info("Executing GetUserFavoriteProductsCommand for userId: {}", userId);
    
    // 调用product-service获取用户收藏商品
    List<Product> favoriteProducts = productServiceClient.getUserFavoriteProducts(userId);
    
    log.info("Successfully retrieved {} favorite products for userId: {}",
            favoriteProducts.size(), userId);
    
    return favoriteProducts;
}

@Override
protected List<Product> getFallback() {
    log.warn("Fallback triggered for GetUserFavoriteProductsCommand, userId: {}", userId);
    
    // 返回空列表作为降级响应
    List<Product> fallbackProducts = new ArrayList<>();
    
    log.info("Returning fallback response with {} products for userId: {}",
            0, userId);
    
    return fallbackProducts;
}
} 