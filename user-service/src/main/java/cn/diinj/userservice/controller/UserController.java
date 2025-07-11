package cn.diinj.userservice.controller;

import cn.diinj.api.client.ProductServiceClient;
import cn.diinj.api.model.Product;
import cn.diinj.userservice.command.GetUserFavoriteProductsCommand;
import cn.diinj.userservice.entity.User;
import cn.diinj.userservice.model.LoginRequest;
import cn.diinj.userservice.model.Result;
import cn.diinj.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * User controller
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductServiceClient productServiceClient;

    /**
     * Login endpoint
     * @param loginRequest login request
     * @return Result with user info if login successful
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody LoginRequest loginRequest) {
        // Validate request
        if (loginRequest == null || loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return Result.error(400, "Username and password are required");
        }

        // Attempt login
        User user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        
        // Return result
        if (user != null) {
            // Don't return password in response
            user.setPassword(null);
            return Result.success(user);
        } else {
            return Result.error(401, "Invalid username or password");
        }
    }

/**
 * Get user's favorite products
 *
 * @param userId User ID
 * @return List of user's favorite products
 */
@GetMapping("/{userId}/favorites")
public Result<List<Product>> getUserFavoriteProducts(@PathVariable Long userId) {
    try {
        // 使用HystrixCommand进行编程式熔断保护
        GetUserFavoriteProductsCommand command =
                new GetUserFavoriteProductsCommand(productServiceClient, userId);
        List<Product> favoriteProducts = command.execute();
        
        return Result.success(favoriteProducts);
    } catch (Exception e) {
        return Result.error(500, "Failed to get user favorite products: " + e.getMessage());
    }
}


/**
 * Add product to user's favorites
 *
 * @param userId    User ID
 * @param productId Product ID
 * @return Success message
 */
@PostMapping("/{userId}/favorites/{productId}")
public Result<Map<String, String>> addToUserFavorites(@PathVariable Long userId,
        @PathVariable Long productId) {
    try {
        Map<String, String> result = productServiceClient.addToUserFavorites(userId, productId);
        return Result.success(result);
    } catch (Exception e) {
        return Result.error(500, "Failed to add product to favorites: " + e.getMessage());
    }
}

/**
 * Remove product from user's favorites
 *
 * @param userId    User ID
 * @param productId Product ID
 * @return Success message
 */
@DeleteMapping("/{userId}/favorites/{productId}")
public Result<Map<String, String>> removeFromUserFavorites(@PathVariable Long userId,
        @PathVariable Long productId) {
    try {
        Map<String, String> result = productServiceClient.removeFromUserFavorites(userId,
                productId);
        return Result.success(result);
    } catch (Exception e) {
        return Result.error(500, "Failed to remove product from favorites: " + e.getMessage());
    }
}
}