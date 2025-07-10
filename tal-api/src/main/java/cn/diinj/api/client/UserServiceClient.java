package cn.diinj.api.client;

import cn.diinj.api.model.LoginRequest;
import cn.diinj.api.model.Result;
import cn.diinj.api.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * User Service Client
 * Feign client for calling user-service through the gateway
 * The fallback class provides fallback responses when the service is unavailable
 */

/**
 * 微服务内部之间调用不需要走网关
 */
@FeignClient(name = "user-service", path = "/users", fallback = UserServiceClient.UserServiceFallback.class)
public interface UserServiceClient {

    /**
     * Login endpoint
     * @param loginRequest login request
     * @return Result with user info if login successful
     */
    @PostMapping("/login")
    Result<User> login(@RequestBody LoginRequest loginRequest);
    
    /**
     * Fallback implementation for UserServiceClient
     * Provides fallback responses when the service is unavailable
     */
    class UserServiceFallback implements UserServiceClient {
        
        @Override
        public Result<User> login(LoginRequest loginRequest) {
            return Result.error(503, "User service is currently unavailable. Please try again later.");
        }
    }
} 