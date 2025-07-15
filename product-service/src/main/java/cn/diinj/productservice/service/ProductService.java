package cn.diinj.productservice.service;

import cn.diinj.productservice.model.Product;

import java.util.List;

public interface ProductService {
Product getProductById(Long id);

List<Product> getAllProducts();

void addProduct(Product product);

void updateProduct(Product product);

void deleteProduct(Long id);

/**
 * Get user's favorite products
 *
 * @param userId User ID
 * @return List of user's favorite products
 */
List<Product> getUserFavoriteProducts(Long userId);

/**
 * Add product to user's favorites
 *
 * @param userId    User ID
 * @param productId Product ID
 */
void addToUserFavorites(Long userId, Long productId);

/**
 * Remove product from user's favorites
 *
 * @param userId    User ID
 * @param productId Product ID
 */
void removeFromUserFavorites(Long userId, Long productId);
} 