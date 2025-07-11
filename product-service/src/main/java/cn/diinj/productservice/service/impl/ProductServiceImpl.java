package cn.diinj.productservice.service.impl;

import cn.diinj.api.event.ProductPriceChangeEvent;
import cn.diinj.api.event.ProductPriceChangeEvent.PriceChangeType;
import cn.diinj.productservice.kafka.PricesChangeEventProducer;
import cn.diinj.productservice.mapper.ProductMapper;
import cn.diinj.productservice.model.Product;
import cn.diinj.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProductServiceImpl implements ProductService {
private static final String PRODUCT_CACHE_PREFIX = "product:";

@Autowired
private ProductMapper productMapper;
@Autowired
private PricesChangeEventProducer pricesChangeEventProducer;
@Autowired
private RedisTemplate<String, Object> redisTemplate;

@Override
public Product getProductById(Long id) {
    String key = PRODUCT_CACHE_PREFIX + id;
    Product product = (Product) redisTemplate.opsForValue().get(key);
    if (product == null) {
        product = productMapper.selectProductById(id);
        if (product != null) {
            redisTemplate.opsForValue().set(key, product, 10, TimeUnit.MINUTES);
        }
    }
    return product;
}

@Override
public List<Product> getAllProducts() {
    return productMapper.selectAllProducts();
}

@Override
public void addProduct(Product product) {
    productMapper.insertProduct(product);
    // 新增后可缓存
    String key = PRODUCT_CACHE_PREFIX + product.getId();
    redisTemplate.opsForValue().set(key, product, 10, TimeUnit.MINUTES);
}

@Override
public void updateProduct(Product product) {
    // 获取更新前的商品信息
    Product oldProduct = productMapper.selectProductById(product.getId());
    
    productMapper.updateProduct(product);
    String key = PRODUCT_CACHE_PREFIX + product.getId();
    redisTemplate.delete(key);
    
    // 向下游通知价格发生变化
    if (oldProduct != null && !oldProduct.getPrice().equals(product.getPrice())) {
        BigDecimal oldPrice = oldProduct.getPrice();
        BigDecimal newPrice = product.getPrice();
        BigDecimal changeAmount = newPrice.subtract(oldPrice);
        PriceChangeType changeType = changeAmount.compareTo(BigDecimal.ZERO) > 0 ?
                PriceChangeType.INCREASE : PriceChangeType.DECREASE;
        
        ProductPriceChangeEvent event = new ProductPriceChangeEvent(
                product.getId(),
                product.getName(),
                oldPrice,
                newPrice,
                changeType,
                changeAmount.abs(),
                "价格调整"
        );
        pricesChangeEventProducer.sendPriceChangeEvent(event);
    }
}

@Override
public void deleteProduct(Long id) {
    productMapper.deleteProduct(id);
    String key = PRODUCT_CACHE_PREFIX + id;
    redisTemplate.delete(key);
}

@Override
public List<Product> getUserFavoriteProducts(Long userId) {
    // 从Redis缓存中获取用户收藏的商品ID列表
    String userFavoritesKey = "user:favorites:" + userId;
    List<Long> favoriteProductIds = (List<Long>) redisTemplate.opsForValue().get(userFavoritesKey);
    
    if (favoriteProductIds == null || favoriteProductIds.isEmpty()) {
        // 如果缓存中没有，从数据库查询（这里简化处理，实际应该查询user_favorites表）
        // 为了演示，返回一些示例商品
        return productMapper.selectAllProducts().subList(0, Math.min(3,
                productMapper.selectAllProducts().size()));
    }
    
    // 根据商品ID列表获取商品详情
    return productMapper.selectProductsByIds(favoriteProductIds);
}

@Override
public void addToUserFavorites(Long userId, Long productId) {
    // 验证商品是否存在
    Product product = getProductById(productId);
    if (product == null) {
        throw new RuntimeException("Product not found with id: " + productId);
    }
    
    // 将商品添加到用户收藏列表
    String userFavoritesKey = "user:favorites:" + userId;
    List<Long> favoriteProductIds = (List<Long>) redisTemplate.opsForValue().get(userFavoritesKey);
    
    if (favoriteProductIds == null) {
        favoriteProductIds = new java.util.ArrayList<>();
    }
    
    if (!favoriteProductIds.contains(productId)) {
        favoriteProductIds.add(productId);
        redisTemplate.opsForValue().set(userFavoritesKey, favoriteProductIds, 30, TimeUnit.DAYS);
    }
    
    // 实际项目中应该同时更新数据库
    // productMapper.insertUserFavorite(userId, productId);
}

@Override
public void removeFromUserFavorites(Long userId, Long productId) {
    // 从用户收藏列表中移除商品
    String userFavoritesKey = "user:favorites:" + userId;
    List<Long> favoriteProductIds = (List<Long>) redisTemplate.opsForValue().get(userFavoritesKey);
    
    if (favoriteProductIds != null) {
        favoriteProductIds.remove(productId);
        if (favoriteProductIds.isEmpty()) {
            redisTemplate.delete(userFavoritesKey);
        }
        else {
            redisTemplate.opsForValue().set(userFavoritesKey, favoriteProductIds, 30,
                    TimeUnit.DAYS);
        }
    }
    
    // 实际项目中应该同时更新数据库
    // productMapper.deleteUserFavorite(userId, productId);
}
} 