package cn.diinj.userservice.controller;

import cn.diinj.userservice.entity.User;
import cn.diinj.userservice.model.LoginRequest;
import cn.diinj.userservice.model.Result;
import cn.diinj.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User controller
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

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
}