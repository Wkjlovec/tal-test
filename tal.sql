-- 电商系统数据库表结构设计
-- 数据库创建
CREATE DATABASE IF NOT EXISTS ecommerce_db
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ecommerce_db;

-- 1. 用户表 (user)
CREATE TABLE `user`
(
    `id`              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `username`        VARCHAR(50)  NOT NULL UNIQUE COMMENT '用户名',
    `password`        VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    `email`           VARCHAR(100) UNIQUE COMMENT '邮箱',
    `phone`           VARCHAR(20) UNIQUE COMMENT '手机号',
    `real_name`       VARCHAR(50) COMMENT '真实姓名',
    `avatar`          VARCHAR(255) COMMENT '头像URL',
    `gender`          TINYINT COMMENT '性别：0-未知，1-男，2-女',
    `birthday`        DATE COMMENT '生日',
    `status`          TINYINT   DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `last_login_time` TIMESTAMP COMMENT '最后登录时间',
    `created_at`      TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `updated_at`      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX `idx_username` (`username`),
    INDEX `idx_email` (`email`),
    INDEX `idx_phone` (`phone`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户表';

-- 2. 商品表 (product)
CREATE TABLE `product`
(
    `id`             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
    `name`           VARCHAR(255)   NOT NULL COMMENT '商品名称',
    `description`    TEXT COMMENT '商品描述',
    `price`          DECIMAL(10, 2) NOT NULL COMMENT '商品价格',
    `original_price` DECIMAL(10, 2) COMMENT '原价',
    `stock_quantity` INT            NOT NULL DEFAULT 0 COMMENT '库存数量',
    `sold_quantity`  INT                     DEFAULT 0 COMMENT '已售数量',
    `category_id`    BIGINT COMMENT '分类ID',
    `brand`          VARCHAR(100) COMMENT '品牌',
    `sku`            VARCHAR(100) UNIQUE COMMENT '商品SKU',
    `main_image`     VARCHAR(255) COMMENT '主图URL',
    `images`         JSON COMMENT '商品图片列表',
    `specifications` JSON COMMENT '商品规格',
    `weight`         DECIMAL(8, 2) COMMENT '重量(kg)',
    `status`         TINYINT                 DEFAULT 1 COMMENT '状态：0-下架，1-上架，2-售罄',
    `sort_order`     INT                     DEFAULT 0 COMMENT '排序值',
    `created_at`     TIMESTAMP               DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`     TIMESTAMP               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX `idx_name` (`name`),
    INDEX `idx_price` (`price`),
    INDEX `idx_category_id` (`category_id`),
    INDEX `idx_brand` (`brand`),
    INDEX `idx_sku` (`sku`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_at` (`created_at`),
    INDEX `idx_sort_order` (`sort_order`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='商品表';

-- 3. 订单表 (order)
CREATE TABLE `order`
(
    `id`               BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    `order_no`         VARCHAR(32)    NOT NULL UNIQUE COMMENT '订单号',
    `user_id`          BIGINT         NOT NULL COMMENT '用户ID',
    `status`           TINYINT        DEFAULT 1 COMMENT '订单状态：1-待支付，2-已支付，3-已发货，4-已完成，5-已取消',
    `total_amount`     DECIMAL(10, 2) NOT NULL COMMENT '订单总金额',
    `discount_amount`  DECIMAL(10, 2) DEFAULT 0.00 COMMENT '优惠金额',
    `shipping_fee`     DECIMAL(10, 2) DEFAULT 0.00 COMMENT '运费',
    `actual_amount`    DECIMAL(10, 2) NOT NULL COMMENT '实际支付金额',
    `payment_method`   VARCHAR(50) COMMENT '支付方式：alipay/wechat/bank',
    `shipping_address` JSON COMMENT '收货地址信息',
    `remark`           TEXT COMMENT '订单备注',
    `created_at`       TIMESTAMP      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `payment_time`     TIMESTAMP      NULL COMMENT '支付时间',
    `shipped_time`     TIMESTAMP      NULL COMMENT '发货时间',
    `completed_time`   TIMESTAMP      NULL COMMENT '完成时间',
    `updated_at`       TIMESTAMP      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    INDEX `idx_order_no` (`order_no`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_at` (`created_at`),
    INDEX `idx_payment_time` (`payment_time`),
    INDEX `idx_user_status` (`user_id`, `status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='订单表';

-- 4. 订单明细表 (order_item)
CREATE TABLE `order_item`
(
    `id`             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细ID',
    `order_id`       BIGINT         NOT NULL COMMENT '订单ID',
    `product_id`     BIGINT         NOT NULL COMMENT '商品ID',
    `product_name`   VARCHAR(255)   NOT NULL COMMENT '商品名称（冗余）',
    `product_image`  VARCHAR(255) COMMENT '商品图片（冗余）',
    `product_sku`    VARCHAR(100) COMMENT '商品SKU（冗余）',
    `quantity`       INT            NOT NULL COMMENT '商品数量',
    `unit_price`     DECIMAL(10, 2) NOT NULL COMMENT '商品单价',
    `total_price`    DECIMAL(10, 2) NOT NULL COMMENT '小计金额',
    `specifications` JSON COMMENT '商品规格（冗余）',
    `created_at`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE RESTRICT,
    INDEX `idx_order_id` (`order_id`),
    INDEX `idx_product_id` (`product_id`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='订单明细表';

-- 5. 支付记录表 (payment)
CREATE TABLE `payment`
(
    `id`                   BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '支付ID',
    `payment_no`           VARCHAR(32)    NOT NULL UNIQUE COMMENT '支付单号',
    `order_id`             BIGINT         NOT NULL COMMENT '订单ID',
    `order_no`             VARCHAR(32)    NOT NULL COMMENT '订单号（冗余）',
    `user_id`              BIGINT         NOT NULL COMMENT '用户ID（冗余）',
    `payment_method`       VARCHAR(50)    NOT NULL COMMENT '支付方式：alipay/wechat/bank',
    `payment_channel`      VARCHAR(50) COMMENT '支付渠道：app/web/h5',
    `payment_status`       TINYINT     DEFAULT 1 COMMENT '支付状态：1-待支付，2-支付成功，3-支付失败，4-已退款',
    `payment_amount`       DECIMAL(10, 2) NOT NULL COMMENT '支付金额',
    `currency`             VARCHAR(10) DEFAULT 'CNY' COMMENT '币种',
    `third_party_trade_no` VARCHAR(100) COMMENT '第三方交易流水号',
    `callback_data`        JSON COMMENT '支付回调数据',
    `failure_reason`       VARCHAR(255) COMMENT '失败原因',
    `payment_time`         TIMESTAMP      NULL COMMENT '支付时间',
    `created_at`           TIMESTAMP   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`           TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    INDEX `idx_payment_no` (`payment_no`),
    INDEX `idx_order_id` (`order_id`),
    INDEX `idx_order_no` (`order_no`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_payment_method` (`payment_method`),
    INDEX `idx_payment_status` (`payment_status`),
    INDEX `idx_payment_time` (`payment_time`),
    INDEX `idx_created_at` (`created_at`),
    INDEX `idx_third_party_trade_no` (`third_party_trade_no`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='支付记录表';

-- 6. 商品分类表 (category) - 扩展表
CREATE TABLE `category`
(
    `id`         BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    `name`       VARCHAR(100) NOT NULL COMMENT '分类名称',
    `parent_id`  BIGINT    DEFAULT 0 COMMENT '父分类ID，0表示顶级分类',
    `level`      TINYINT   DEFAULT 1 COMMENT '分类层级',
    `sort_order` INT       DEFAULT 0 COMMENT '排序值',
    `icon`       VARCHAR(255) COMMENT '分类图标',
    `status`     TINYINT   DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX `idx_parent_id` (`parent_id`),
    INDEX `idx_level` (`level`),
    INDEX `idx_status` (`status`),
    INDEX `idx_sort_order` (`sort_order`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='商品分类表';

-- 为 product 表添加外键约束
ALTER TABLE `product`
    ADD CONSTRAINT `fk_product_category`
        FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE SET NULL;

-- 插入示例数据

-- 插入用户数据
INSERT INTO `user` (`username`, `password`, `email`, `phone`, `real_name`, `gender`, `status`)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEIgy',
        'admin@example.com', '13800138000', '管理员', 1, 1),
       ('user001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEIgy',
        'user001@example.com', '13800138001', '张三', 1, 1),
       ('user002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEIgy',
        'user002@example.com', '13800138002', '李四', 2, 1);

-- 插入商品分类数据
INSERT INTO `category` (`name`, `parent_id`, `level`, `sort_order`, `status`)
VALUES ('电子产品', 0, 1, 1, 1),
       ('服装鞋帽', 0, 1, 2, 1),
       ('图书音像', 0, 1, 3, 1),
       ('手机数码', 1, 2, 1, 1),
       ('电脑办公', 1, 2, 2, 1),
       ('男装', 2, 2, 1, 1),
       ('女装', 2, 2, 2, 1);

-- 插入商品数据
INSERT INTO `product` (`name`, `description`, `price`, `original_price`, `stock_quantity`,
                       `category_id`, `brand`, `sku`, `status`)
VALUES ('iPhone 15 Pro', '苹果最新款智能手机', 7999.00, 8999.00, 100, 4, 'Apple', 'IP15PRO001', 1),
       ('MacBook Pro 14英寸', '苹果笔记本电脑', 14999.00, 15999.00, 50, 5, 'Apple', 'MBP14001', 1),
       ('小米13 Pro', '小米旗舰手机', 4999.00, 5999.00, 200, 4, 'Xiaomi', 'MI13PRO001', 1),
       ('戴尔XPS 13', '戴尔轻薄笔记本', 8999.00, 9999.00, 30, 5, 'Dell', 'XPS13001', 1),
       ('Nike Air Max', '耐克运动鞋', 899.00, 999.00, 150, 6, 'Nike', 'NAM001', 1);

-- 插入订单数据
INSERT INTO `order` (`order_no`, `user_id`, `status`, `total_amount`, `actual_amount`,
                     `payment_method`)
VALUES ('ORD202501080001', 2, 2, 7999.00, 7999.00, 'alipay'),
       ('ORD202501080002', 3, 1, 4999.00, 4999.00, 'wechat'),
       ('ORD202501080003', 2, 3, 899.00, 899.00, 'alipay');

-- 插入订单明细数据
INSERT INTO `order_item` (`order_id`, `product_id`, `product_name`, `product_sku`, `quantity`,
                          `unit_price`, `total_price`)
VALUES (1, 1, 'iPhone 15 Pro', 'IP15PRO001', 1, 7999.00, 7999.00),
       (2, 3, '小米13 Pro', 'MI13PRO001', 1, 4999.00, 4999.00),
       (3, 5, 'Nike Air Max', 'NAM001', 1, 899.00, 899.00);

-- 插入支付记录数据
INSERT INTO `payment` (`payment_no`, `order_id`, `order_no`, `user_id`, `payment_method`,
                       `payment_status`, `payment_amount`, `payment_time`)
VALUES ('PAY202501080001', 1, 'ORD202501080001', 2, 'alipay', 2, 7999.00, NOW()),
       ('PAY202501080002', 2, 'ORD202501080002', 3, 'wechat', 1, 4999.00, NULL),
       ('PAY202501080003', 3, 'ORD202501080003', 2, 'alipay', 2, 899.00, NOW());
