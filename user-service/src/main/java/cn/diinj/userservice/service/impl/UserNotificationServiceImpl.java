package cn.diinj.userservice.service.impl;

import cn.diinj.userservice.service.UserNotificationService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class UserNotificationServiceImpl implements UserNotificationService {
    
    @Override
    public void notifyPriceDrop(Long productId, String productName, String oldPrice, String newPrice) {
        // 1. 查询关注该商品的用户列表
        List<Long> interestedUsers = getUsersInterestedInProduct(productId);
        
        // 2. 发送降价通知
        for (Long userId : interestedUsers) {
            sendPriceDropNotification(userId, productId, productName, oldPrice, newPrice);
        }
    }
    
    @Override
    public void notifyPriceIncrease(Long productId, String productName, String oldPrice, String newPrice) {
        // 1. 查询关注该商品的用户列表
        List<Long> interestedUsers = getUsersInterestedInProduct(productId);
        
        // 2. 发送涨价通知（可选，通常用户更关心降价）
        for (Long userId : interestedUsers) {
            sendPriceIncreaseNotification(userId, productId, productName, oldPrice, newPrice);
        }
    }
    
    private List<Long> getUsersInterestedInProduct(Long productId) {
        //todo
        return Collections.emptyList();
    }
    
    private void sendPriceDropNotification(Long userId, Long productId, String productName, String oldPrice, String newPrice) {
        //todo
    }
    
    private void sendPriceIncreaseNotification(Long userId, Long productId, String productName, String oldPrice, String newPrice) {
        //todo
    }
} 