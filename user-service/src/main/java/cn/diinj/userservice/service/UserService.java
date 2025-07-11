package cn.diinj.userservice.service;

import cn.diinj.userservice.entity.User;

/**
 * User service interface
 */
public interface UserService {
    
    /**
     * Login
     * @param username username
     * @param password password
     * @return User if login successful, null otherwise
     */
    User login(String username, String password);
    
    /**
     * Get user by ID
     * @param userId User ID
     * @return User if found, null otherwise
     */
    User getUserById(Long userId);
}