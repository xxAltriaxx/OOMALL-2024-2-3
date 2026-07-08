-- prodorder 模块基线表结构（新环境初始化）
USE prodorder;

DROP TABLE IF EXISTS `order_item`;
DROP TABLE IF EXISTS `order_order`;
DROP TABLE IF EXISTS `order_idempotent`;

CREATE TABLE `order_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `creator_id` bigint DEFAULT NULL,
  `creator_name` varchar(128) DEFAULT NULL,
  `modifier_id` bigint DEFAULT NULL,
  `modifier_name` varchar(128) DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  `shop_id` bigint DEFAULT NULL,
  `order_sn` varchar(128) DEFAULT NULL,
  `pid` bigint DEFAULT NULL,
  `consignee` varchar(128) DEFAULT NULL,
  `region_id` bigint DEFAULT NULL,
  `address` varchar(512) DEFAULT NULL,
  `mobile` varchar(64) DEFAULT NULL,
  `message` varchar(512) DEFAULT NULL,
  `activity_id` bigint DEFAULT NULL,
  `package_id` bigint DEFAULT NULL,
  `idempotent_key` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_idempotent_shop` (`idempotent_key`, `shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单';

CREATE TABLE `order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `creator_id` bigint DEFAULT NULL,
  `creator_name` varchar(128) DEFAULT NULL,
  `modifier_id` bigint DEFAULT NULL,
  `modifier_name` varchar(128) DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime DEFAULT NULL,
  `order_id` bigint DEFAULT NULL,
  `onsale_id` bigint DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `price` bigint DEFAULT NULL,
  `discount_price` bigint DEFAULT NULL,
  `point` bigint DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `coupon_activity_id` bigint DEFAULT NULL,
  `coupon_id` bigint DEFAULT NULL,
  `commented` tinyint DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_order_item_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单明细';

CREATE TABLE `order_idempotent` (
  `idempotent_key` varchar(64) NOT NULL,
  `status` tinyint NOT NULL COMMENT '0=进行中，1=已成功，2=已失败',
  `expected_shop_count` int NOT NULL,
  `gmt_create` datetime NOT NULL,
  `gmt_modified` datetime NOT NULL,
  PRIMARY KEY (`idempotent_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单幂等记录';
