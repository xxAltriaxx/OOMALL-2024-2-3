-- 订单幂等记录表：跟踪 MQ 事务进行中 / 已成功 / 已失败
CREATE TABLE IF NOT EXISTS order_idempotent (
    idempotent_key VARCHAR(64) NOT NULL PRIMARY KEY,
    status TINYINT NOT NULL COMMENT '0=进行中，1=已成功，2=已失败',
    expected_shop_count INT NOT NULL,
    gmt_create DATETIME NOT NULL,
    gmt_modified DATETIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 同一幂等键下每个店铺仅允许一条订单，防止部分重试重复落库
ALTER TABLE order_order
    ADD UNIQUE INDEX uk_order_idempotent_shop (idempotent_key, shop_id);
