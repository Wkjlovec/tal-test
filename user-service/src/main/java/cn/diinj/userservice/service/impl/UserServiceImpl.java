package cn.diinj.userservice.service.impl;

import cn.diinj.userservice.entity.User;
import cn.diinj.userservice.mapper.UserMapper;
import cn.diinj.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * User service implementation
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        // Find user by username
        User user = userMapper.findByUsername(username);
        
        // Check if user exists and password matches
        if (user != null && password.equals(user.getPassword())) {
            // Update last login time
            user.setLastLoginTime(new Date());
            userMapper.updateLastLoginTime(user.getId(), user.getLastLoginTime());
            return user;
        }
        
        return null;
    }
}