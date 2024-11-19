/*
 Navicat MySQL Data Transfer

 Source Server         : navicat
 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Host           : localhost:3306
 Source Schema         : alipay

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 25/12/2023 21:54:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for alipay_div_receiver
-- ----------------------------
DROP TABLE IF EXISTS `alipay_div_receiver`;
CREATE TABLE `alipay_div_receiver`  (
                                        `id` bigint NOT NULL AUTO_INCREMENT,
                                        `out_request_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'outRequestNo',
                                        `trans_out` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'transOut',
                                        `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'type',
                                        `account` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'account',
                                        `bind_login_name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'bindLoginName',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'alipay_div_receiver' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of alipay_div_receiver
-- ----------------------------
INSERT INTO `alipay_div_receiver` VALUES (1, '123456', 'testShop123456', 'userId', 'oomall123456', NULL);

-- ----------------------------
-- Table structure for alipay_payment
-- ----------------------------
DROP TABLE IF EXISTS `alipay_payment`;
CREATE TABLE `alipay_payment`  (
                                   `id` bigint NOT NULL AUTO_INCREMENT,
                                   `appid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'appid',
                                   `receiver_account` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'receiverAccount',
                                   `buyer_logon_id` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'buyerLogonId',
                                   `trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'tradeNo',
                                   `out_trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'outTradeNo',
                                   `trade_status` int NULL DEFAULT NULL COMMENT 'tradeStatus',
                                   `total_amount` int NULL DEFAULT NULL COMMENT 'tradeAmount',
                                   `success_time` datetime(0) NULL DEFAULT NULL COMMENT 'successTime',
                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'alipay_payment' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of alipay_payment
-- ----------------------------
INSERT INTO `alipay_payment` VALUES (1, 'oomall123456', 'testShop123456', NULL, '1187051287120949248', '123456', 1, 10000, '2023-12-20 15:18:01');
INSERT INTO `alipay_payment` VALUES (2, 'oomall123456', 'testShop123456', NULL, '1187052387907645440', '123457', 2, 9901, NULL);
INSERT INTO `alipay_payment` VALUES (3, 'oomall123456', 'testShop123456', NULL, '1187052387907645441', '2', 2, 53, NULL);
INSERT INTO `alipay_payment` VALUES (4, 'oomall123456', 'testShop123456', NULL, '1187052387907645452', '7', 2, 25944, NULL);
-- ----------------------------
-- Table structure for alipay_refund
-- ----------------------------
DROP TABLE IF EXISTS `alipay_refund`;
CREATE TABLE `alipay_refund`  (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `out_request_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'outRequestNo',
                                  `out_trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'outTradeNo',
                                  `trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'tradeNo',
                                  `total_amount` int NULL DEFAULT NULL,
                                  `refund_amount` int NULL DEFAULT NULL,
                                  `gmt_refund_pay` datetime(0) NULL DEFAULT NULL COMMENT 'gmt_refund_pay',
                                  `refund_status` int NULL DEFAULT NULL COMMENT 'refundStatus',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'alipay_refund' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of alipay_refund
-- ----------------------------
INSERT INTO `alipay_refund` VALUES (1, 'out_request_no123', '123456', '1187051287120949248', 10000, 9900, '2023-12-20 15:18:46', 0);

-- ----------------------------
-- Table structure for alipay_refund_royalty_detail
-- ----------------------------
DROP TABLE IF EXISTS `alipay_refund_royalty_detail`;
CREATE TABLE `alipay_refund_royalty_detail`  (
                                                 `id` bigint NOT NULL AUTO_INCREMENT,
                                                 `out_request_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'outRequestNo',
                                                 `out_trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'outTradeNo',
                                                 `trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'tradeNo',
                                                 `trans_in` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'transIn',
                                                 `trans_out` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'transOut',
                                                 `refund_amount` int NULL DEFAULT NULL COMMENT 'refund_amount',
                                                 `royalty_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'royalty_type',
                                                 `result_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'result_code',
                                                 `gmt_refund_pay` datetime(0) NULL DEFAULT NULL COMMENT 'gmt_refund_pay',
                                                 PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'alipay_refund_royalty_detail' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of alipay_refund_royalty_detail
-- ----------------------------
INSERT INTO `alipay_refund_royalty_detail` VALUES (1, 'out_request_no123', '123456', '1187051287120949248', 'testShop123456', 'oomall123', 100, 'transfer', 'SUCCESS', '2023-12-20 15:18:51');

-- ----------------------------
-- Table structure for alipay_royalty_detail
-- ----------------------------
DROP TABLE IF EXISTS `alipay_royalty_detail`;
CREATE TABLE `alipay_royalty_detail`  (
                                          `id` bigint NOT NULL AUTO_INCREMENT,
                                          `trans_in` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'transIn',
                                          `trans_out` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'transOut',
                                          `amount` int NULL DEFAULT NULL COMMENT 'amount',
                                          `settle_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'settleNo',
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'alipay_royalty_detail' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of alipay_royalty_detail
-- ----------------------------

-- ----------------------------
-- Table structure for alipay_royalty_entity
-- ----------------------------
DROP TABLE IF EXISTS `alipay_royalty_entity`;
CREATE TABLE `alipay_royalty_entity`  (
                                          `id` bigint NOT NULL AUTO_INCREMENT,
                                          `out_request_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'outRequestNo',
                                          `trans_out` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'transOut',
                                          `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'type',
                                          `account` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'account',
                                          `login_name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'loginName',
                                          `bind_login_name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'bindLoginName',
                                          `memo` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'memo',
                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'alipay_div_receiver' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of alipay_royalty_entity
-- ----------------------------

-- ----------------------------
-- Table structure for alipay_settlement
-- ----------------------------
DROP TABLE IF EXISTS `alipay_settlement`;
CREATE TABLE `alipay_settlement`  (
                                      `id` bigint NOT NULL AUTO_INCREMENT,
                                      `settle_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'settleNo',
                                      `out_request_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'outRequestNo',
                                      `trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'tradeNo',
                                      `out_trade_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'outTradeNo',
                                      `amount_total` int NULL DEFAULT NULL COMMENT 'amount_total',
                                      `success_time` datetime(0) NULL DEFAULT NULL COMMENT 'successTime',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'alipay_settlement' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of alipay_settlement
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
