-- prodorder 增量迁移 001：幂等键与幂等记录表
-- 适用于已有 order_order / order_item 表、尚未接入幂等能力的环境
USE prodorder;

CREATE TABLE IF NOT EXISTS `order_idempotent` (
  `idempotent_key` varchar(64) NOT NULL,
  `status` tinyint NOT NULL COMMENT '0=进行中，1=已成功，2=已失败',
  `expected_shop_count` int NOT NULL,
  `gmt_create` datetime NOT NULL,
  `gmt_modified` datetime NOT NULL,
  PRIMARY KEY (`idempotent_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单幂等记录';

SET @column_exists := (
  SELECT COUNT(*)
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'order_order'
    AND COLUMN_NAME = 'idempotent_key'
);
SET @add_column_sql := IF(
  @column_exists = 0,
  'ALTER TABLE `order_order` ADD COLUMN `idempotent_key` varchar(64) DEFAULT NULL AFTER `package_id`',
  'SELECT ''skip add column idempotent_key'''
);
PREPARE add_column_stmt FROM @add_column_sql;
EXECUTE add_column_stmt;
DEALLOCATE PREPARE add_column_stmt;

SET @index_exists := (
  SELECT COUNT(*)
  FROM information_schema.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'order_order'
    AND INDEX_NAME = 'uk_order_idempotent_shop'
);
SET @add_index_sql := IF(
  @index_exists = 0,
  'ALTER TABLE `order_order` ADD UNIQUE INDEX `uk_order_idempotent_shop` (`idempotent_key`, `shop_id`)',
  'SELECT ''skip add index uk_order_idempotent_shop'''
);
PREPARE add_index_stmt FROM @add_index_sql;
EXECUTE add_index_stmt;
DEALLOCATE PREPARE add_index_stmt;
