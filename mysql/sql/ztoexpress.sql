/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80200 (8.2.0)
 Source Host           : localhost:3306
 Source Schema         : ztoexpress

 Target Server Type    : MySQL
 Target Server Version : 80200 (8.2.0)
 File Encoding         : 65001

 Date: 20/12/2023 22:57:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for zto_express
-- ----------------------------
DROP TABLE IF EXISTS `zto_express`;
CREATE TABLE `zto_express`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `zto_order_id` int NOT NULL,
  `bill_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `scan_date` date NOT NULL,
  `scan_site_id` int NOT NULL,
  `parcel_weight` int NOT NULL,
  `parcel_packing_fee` int NOT NULL,
  `parcel_size` int NOT NULL,
  `parcel_quantity` int NOT NULL,
  `parcel_other_fee` int NOT NULL,
  `express_status` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of zto_express
-- ----------------------------
INSERT INTO `zto_express` VALUES (1, 1, 'ZT1743827865978880006895', '2023-01-01', 101, 3, 10, 2, 1, 5, '1');
INSERT INTO `zto_express` VALUES (2, 2, 'ZT1743827865978880006896', '2023-01-02', 102, 2, 8, 1, 2, 3, '1');
INSERT INTO `zto_express` VALUES (3, 3, 'ZT1743827865978880006897', '2023-01-03', 103, 3, 12, 3, 1, 7, '1');
INSERT INTO `zto_express` VALUES (4, 4, 'ZT1743827865978880006898', '2023-01-04', 104, 2, 6, 1, 3, 2, '1');
INSERT INTO `zto_express` VALUES (5, 34, 'ZT1743827865978880006899', '2023-01-05', 105, 2, 9, 2, 1, 4, '1');
INSERT INTO `zto_express` VALUES (6, 6, 'ZT1743827865978880006900', '2023-01-06', 106, 3, 11, 2, 2, 6, '2');
INSERT INTO `zto_express` VALUES (7, 7, 'ZT1743827865978880006901', '2023-01-07', 107, 4, 15, 3, 1, 8, '0');
INSERT INTO `zto_express` VALUES (8, 8, 'ZT1743827865978880006902', '2023-01-08', 108, 1, 5, 1, 2, 2, '2');
INSERT INTO `zto_express` VALUES (9, 9, 'ZT1743827865978880006903', '2023-01-09', 109, 2, 10, 2, 1, 5, '1');
INSERT INTO `zto_express` VALUES (10, 10, 'ZT1743827865978880006904', '2023-01-10', 110, 3, 12, 2, 3, 7, '0');

-- ----------------------------
-- Table structure for zto_order
-- ----------------------------
DROP TABLE IF EXISTS `zto_order`;
CREATE TABLE `zto_order`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `partner_type` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `order_type` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `partner_order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `order_code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `sender_id` int NOT NULL,
  `receiver_id` int NOT NULL,
  `parcel_order_sum` decimal(10, 0) NULL DEFAULT NULL,
  `order_remark` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `order_status` int NULL DEFAULT NULL,
  `freight` decimal(10, 0) NULL DEFAULT NULL,
  `premium` decimal(10, 0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 86 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of zto_order
-- ----------------------------
INSERT INTO `zto_order` VALUES (30, '0', '1', 'PartnerCode1', 'ZT1743827865978880006895', 101, 201, 3, 'Remark1', 0, 25, 3);
INSERT INTO `zto_order` VALUES (31, '1', '2', 'PartnerCode2', 'ZT1743827865978880006896', 102, 202, 2, 'Remark2', 1, 18, 2);
INSERT INTO `zto_order` VALUES (32, '0', '3', 'PartnerCode3', 'ZT1743827865978880006897', 103, 203, 4, 'Remark3', 0, 32, 3);
INSERT INTO `zto_order` VALUES (33, '1', '4', 'PartnerCode4', 'ZT1743827865978880006898', 104, 204, 1, 'Remark4', 0, 15, 2);
INSERT INTO `zto_order` VALUES (34, '0', '1', 'PartnerCode5', 'ZT1743827865978880006899', 105, 205, 2, 'Remark5', 1, 20, 2);
INSERT INTO `zto_order` VALUES (35, '1', '2', 'PartnerCode6', 'ZT1743827865978880006900', 106, 206, 3, 'Remark6', 0, 28, 3);
INSERT INTO `zto_order` VALUES (36, '0', '3', 'PartnerCode7', 'ZT1743827865978880006901', 107, 207, 4, 'Remark7', 1, 35, 4);
INSERT INTO `zto_order` VALUES (37, '1', '4', 'PartnerCode8', 'ZT1743827865978880006902', 108, 208, 2, 'Remark8', 0, 12, 1);
INSERT INTO `zto_order` VALUES (38, '0', '1', 'PartnerCode9', 'ZT1743827865978880006903', 109, 209, 3, 'Remark9', 1, 23, 2);
INSERT INTO `zto_order` VALUES (39, '1', '2', 'PartnerCode10', 'ZT1743827865978880006904', 110, 210, 3, 'Remark10', 0, 26, 3);

-- ----------------------------
-- Table structure for zto_personinfo
-- ----------------------------
DROP TABLE IF EXISTS `zto_personinfo`;
CREATE TABLE `zto_personinfo`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `phone` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Province` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `City` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `District` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `Address` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 194 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of zto_personinfo
-- ----------------------------
INSERT INTO `zto_personinfo` VALUES (53, '张三', '123-456-7890', '广东省', '广州市', '天河区', 'XX街道XX号');
INSERT INTO `zto_personinfo` VALUES (54, '李四', '987-654-3210', '江苏省', '南京市', '玄武区', 'XX路XX号');
INSERT INTO `zto_personinfo` VALUES (56, '赵六', '111-222-3333', '上海市', '上海市', '黄浦区', 'XX弄XX号');
INSERT INTO `zto_personinfo` VALUES (57, '钱七', '444-555-6666', '北京市', '北京市', '朝阳区', 'XX路XX号');
INSERT INTO `zto_personinfo` VALUES (58, '孙八', '777-888-9999', '四川省', '成都市', '武侯区', 'XX街道XX号');
INSERT INTO `zto_personinfo` VALUES (59, '周九', '333-999-1111', '河南省', '郑州市', '中原区', 'XX路XX号');
INSERT INTO `zto_personinfo` VALUES (60, '吴十', '666-111-2222', '湖北省', '武汉市', '江岸区', 'XX巷XX号');
INSERT INTO `zto_personinfo` VALUES (61, '郑十一', '888-222-3333', '山东省', '青岛市', '市南区', 'XX路XX号');
INSERT INTO `zto_personinfo` VALUES (62, '王十二', '222-777-4444', '陕西省', '西安市', '雁塔区', 'XX路XX号');
INSERT INTO `zto_personinfo` VALUES (101, '王五', '555-555-5555', '浙江省', '杭州市', '西湖区', 'XX巷XX号');

-- ----------------------------
-- Table structure for zto_site
-- ----------------------------
DROP TABLE IF EXISTS `zto_site`;
CREATE TABLE `zto_site`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `code` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `prov` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `city` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `is_center` int NOT NULL,
  `phone` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 104 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of zto_site
-- ----------------------------
INSERT INTO `zto_site` VALUES (1, 'site1', 'S001', '广东省', '广州市', 1, '123-456-7890');
INSERT INTO `zto_site` VALUES (2, 'site2', 'S002', '江苏省', '南京市', 0, '987-654-3210');
INSERT INTO `zto_site` VALUES (3, 'site3', 'S003', '浙江省', '杭州市', 1, '555-555-5555');
INSERT INTO `zto_site` VALUES (5, 'site5', 'S005', '北京市', '北京市', 1, '444-555-6666');
INSERT INTO `zto_site` VALUES (6, 'site6', 'S006', '四川省', '成都市', 0, '777-888-9999');
INSERT INTO `zto_site` VALUES (103, 'site4', 'S004', '上海市', '上海市', 1, '111-222-3333');

SET FOREIGN_KEY_CHECKS = 1;
