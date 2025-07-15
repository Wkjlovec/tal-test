package cn.diinj.productservice.controller;

import cn.diinj.productservice.model.Product;
import cn.diinj.productservice.service.ProductSearchService;
import cn.diinj.productservice.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {
private static final Logger log = LoggerFactory.getLogger(ProductController.class);
@Autowired
private ProductService productService;
@Autowired
private ProductSearchService productSearchService;

@GetMapping
public List<Product> getAllProducts() {
    return productService.getAllProducts();
}

@GetMapping("/hystrix-timeout")
public String testHystrixTimeout(int timeout) throws InterruptedException {
    Thread.sleep(timeout);
    return "成功执行";
}
@GetMapping("/aggregate")
public Map<String, Map<String, Object>> aggregate() {
    return productSearchService.aggregateByBrand();
}

@GetMapping("/{id}")
public Product getProductById(@PathVariable Long id) {
    return productService.getProductById(id);
}

/**
 * es 模糊查询商品
 */
@GetMapping("/search")
public List<Product> searchProductsByName(@RequestParam String name) {
    log.debug("name :{}", name);
    List<Product> result = productSearchService.searchByName(name);
    log.debug(result.toString());
    return result;
}

@GetMapping("/matches")
public List<Product> matchesByName(@RequestParam String name) {
    log.debug("name :{}", name);
    List<Product> result = productSearchService.searchMathesName(name);
    log.debug(result.toString());
    return result;
}

@GetMapping("/search/like")
public List<Product> searchByNameLike(@RequestParam String name) {
    return productSearchService.searchByNameLike(name);
}

@GetMapping("/search/brand-price")
public List<Product> searchByBrandAndPriceBetween(@RequestParam String brand,
        @RequestParam double min, @RequestParam double max) {
    return productSearchService.searchByBrandAndPriceBetween(brand, min, max);
}

@GetMapping("/search/prefix")
public List<Product> searchByNamePrefixOrderByPriceDesc(@RequestParam String prefix) {
    return productSearchService.searchByNamePrefixOrderByPriceDesc(prefix);
}

@PostMapping
public Product createProduct(@RequestBody Product product) {
    productService.addProduct(product);
    return product;
}

@PutMapping("/{id}")
public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
    product.setId(id);
    productService.updateProduct(product);
    return product;
}

@DeleteMapping("/{id}")
public Map<String, String> deleteProduct(@PathVariable Long id) {
    productService.deleteProduct(id);
    Map<String, String> response = new HashMap<>();
    response.put("message", "Product deleted successfully");
    return response;
}

/**
 * Get user's favorite products
 *
 * @param userId User ID
 * @return List of user's favorite products
 */
@GetMapping("/user/{userId}/favorites")
public List<Product> getUserFavoriteProducts(@PathVariable Long userId) {
    log.debug("Getting favorite products for user: {}", userId);
    // This would typically query a user_favorites table or similar
    // For demo purposes, we'll return some sample products
    List<Product> favoriteProducts = productService.getUserFavoriteProducts(userId);
    log.debug("Found {} favorite products for user {}", favoriteProducts.size(), userId);
    return favoriteProducts;
}

/**
 * Add product to user's favorites
 *
 * @param userId    User ID
 * @param productId Product ID
 * @return Success message
 */
@PostMapping("/user/{userId}/favorites/{productId}")
public Map<String, String> addToUserFavorites(@PathVariable Long userId,
        @PathVariable Long productId) {
    log.debug("Adding product {} to favorites for user: {}", productId, userId);
    productService.addToUserFavorites(userId, productId);
    Map<String, String> response = new HashMap<>();
    response.put("message", "Product added to favorites successfully");
    return response;
}

/**
 * Remove product from user's favorites
 *
 * @param userId    User ID
 * @param productId Product ID
 * @return Success message
 */
@DeleteMapping("/user/{userId}/favorites/{productId}")
public Map<String, String> removeFromUserFavorites(@PathVariable Long userId,
        @PathVariable Long productId) {
    log.debug("Removing product {} from favorites for user: {}", productId, userId);
    productService.removeFromUserFavorites(userId, productId);
    Map<String, String> response = new HashMap<>();
    response.put("message", "Product removed from favorites successfully");
    return response;
}
}