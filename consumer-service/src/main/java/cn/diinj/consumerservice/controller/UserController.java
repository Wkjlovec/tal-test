package cn.diinj.consumerservice.controller;

import cn.diinj.consumerservice.client.UserServiceClient;
import cn.diinj.consumerservice.model.LoginRequest;
import cn.diinj.consumerservice.model.Result;
import cn.diinj.consumerservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User Controller
 * Provides endpoints that call the user-service through the gateway
 * Demonstrates the use of Feign client with circuit breaking
 */
@RestController
@RequestMapping("/consumer/users")
public class UserController {

    private final UserServiceClient userServiceClient;

    @Autowired
    public UserController(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    /**
     * Login endpoint
     * Calls the user-service login endpoint through the gateway
     * 
     * @param loginRequest login request
     * @return Result with user info if login successful
     */
    @PostMapping("/login")
    public Result<User> login(@RequestBody LoginRequest loginRequest) {
        // Validate request
        if (loginRequest == null || loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return Result.error(400, "Username and password are required");
        }
        
        // Call user-service through gateway
        return userServiceClient.login(loginRequest);
    }
}