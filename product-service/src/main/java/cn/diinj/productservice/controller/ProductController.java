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
}