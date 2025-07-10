package cn.diinj.api.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductPriceChangeEvent {
    private Long productId;
    private String productName;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private PriceChangeType changeType;
    private BigDecimal changeAmount;
    private String changeReason;
    private LocalDateTime changeTime;

    public enum PriceChangeType {
        INCREASE, DECREASE
    }

    public ProductPriceChangeEvent() {}

    public ProductPriceChangeEvent(Long productId, String productName, BigDecimal oldPrice, 
                                 BigDecimal newPrice, PriceChangeType changeType, BigDecimal changeAmount, 
                                 String changeReason) {
        this.productId = productId;
        this.productName = productName;
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
        this.changeType = changeType;
        this.changeAmount = changeAmount;
        this.changeReason = changeReason;
        this.changeTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getOldPrice() { return oldPrice; }
    public void setOldPrice(BigDecimal oldPrice) { this.oldPrice = oldPrice; }

    public BigDecimal getNewPrice() { return newPrice; }
    public void setNewPrice(BigDecimal newPrice) { this.newPrice = newPrice; }

    public PriceChangeType getChangeType() { return changeType; }
    public void setChangeType(PriceChangeType changeType) { this.changeType = changeType; }

    public BigDecimal getChangeAmount() { return changeAmount; }
    public void setChangeAmount(BigDecimal changeAmount) { this.changeAmount = changeAmount; }

    public String getChangeReason() { return changeReason; }
    public void setChangeReason(String changeReason) { this.changeReason = changeReason; }

    public LocalDateTime getChangeTime() { return changeTime; }
    public void setChangeTime(LocalDateTime changeTime) { this.changeTime = changeTime; }
} 