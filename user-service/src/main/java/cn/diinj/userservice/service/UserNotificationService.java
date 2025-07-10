package cn.diinj.userservice.service;

public interface UserNotificationService {
    void notifyPriceDrop(Long productId, String productName, String oldPrice, String newPrice);
    void notifyPriceIncrease(Long productId, String productName, String oldPrice, String newPrice);
} 