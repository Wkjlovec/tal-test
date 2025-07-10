package cn.diinj.productservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

/**
 * Product model
 * Represents a product in the system
 */
@Document(indexName = "product")
public class Product {

/**
 * type = FieldType.Text：
 * 表示这是一个可被全文搜索（Full Text Search）分析的字段。
 * Elasticsearch 会将其作为文本进行分析（分词）。
 * analyzer = "ik_smart"：
 * 用于 创建索引时 的分词器（index analyzer）。
 * ik_smart 是 IK 分词器的一种，倾向于 粗粒度 分词（适合提升搜索性能，牺牲部分精度）。
 * searchAnalyzer = "ik_smart"：
 * 用于 搜索时 的分词器。
 * 和 analyzer 一样配置表示索引和搜索使用相同的分词规则
 */

@Id
    private Long id;
@Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String name;
@Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String description;
@Field(type = FieldType.Double)
    private BigDecimal price;
@Field(type = FieldType.Integer)
    private Integer stockQuantity;
/**
 * FiledType.KeyWord
 * 用于精确匹配、聚合和排序
 * 原样存储为一个整体（keyword doc values）
 */
@Field(type = FieldType.Keyword)
    private String brand;
@Field(type = FieldType.Keyword)
    private String sku;
@Field(type = FieldType.Integer)
private Integer soldQuantity;
@Field(type = FieldType.Long)
private Long categoryId;
    
    // Default constructor
    public Product() {
    }
    
    // Constructor with all fields
    public Product(Long id, String name, String description, BigDecimal price, 
                  Integer stockQuantity, String brand, String sku) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.brand = brand;
        this.sku = sku;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Integer getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getSku() {
        return sku;
    }
    
    public void setSku(String sku) {
        this.sku = sku;
    }

public Integer getSoldQuantity() {
    return soldQuantity;
}

public void setSoldQuantity(Integer soldQuantity) {
    this.soldQuantity = soldQuantity;
}

public Long getCategoryId() {
    return categoryId;
}

public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
}
    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", brand='" + brand + '\'' +
                ", sku='" + sku + '\'' +
                ", soldQuantity=" + soldQuantity +
                ", categoryId=" + categoryId +
                '}';
    }
}