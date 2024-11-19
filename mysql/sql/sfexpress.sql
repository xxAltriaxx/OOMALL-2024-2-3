-- MySQL dump 10.13  Distrib 5.7.39, for Win64 (x86_64)
--
-- Host: mysql    Database: sfexpress
-- ------------------------------------------------------
-- Server version	8.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `sfexpress_cargo_detail`
--

DROP TABLE IF EXISTS `sfexpress_cargo_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sfexpress_cargo_detail`
(
    `id`         bigint NOT NULL AUTO_INCREMENT,
    `express_id` bigint       DEFAULT NULL,
    `name`       varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sfexpress_cargo_detail`
--

LOCK
TABLES `sfexpress_cargo_detail` WRITE;
/*!40000 ALTER TABLE `sfexpress_cargo_detail` DISABLE KEYS */;
INSERT INTO `sfexpress_cargo_detail`
VALUES (3, 2, '商品1'),
       (4, 2, '商品2'),
       (5, 3, '商品1'),
       (6, 3, '商品2'),
       (7, 4, '商品1'),
       (8, 4, '商品2'),
       (9, 5, '商品1'),
       (10, 5, '商品2'),
       (11, 6, '商品1'),
       (12, 6, '商品2'),
       (13, 7, '商品1'),
       (14, 7, '商品2'),
       (15, 8, '商品1'),
       (16, 8, '商品2'),
       (17, 9, '商品1'),
       (18, 9, '商品2'),
       (25, 14, '商品1'),
       (26, 14, '商品2');
/*!40000 ALTER TABLE `sfexpress_cargo_detail` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `sfexpress_express`
--

DROP TABLE IF EXISTS `sfexpress_express`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sfexpress_express`
(
    `id`                     bigint NOT NULL AUTO_INCREMENT,
    `cargo_desc`             varchar(255) DEFAULT NULL,
    `country`                varchar(255) DEFAULT NULL,
    `create_time`            timestamp NULL DEFAULT NULL,
    `dest_code`              varchar(255) DEFAULT NULL,
    `diliver_address`        varchar(255) DEFAULT NULL,
    `diliver_city`           int          DEFAULT NULL,
    `diliver_contact`        varchar(255) DEFAULT NULL,
    `diliver_mobile`         varchar(20)  DEFAULT NULL,
    `express_type_id`        int          DEFAULT NULL,
    `monthly_card`           varchar(255) DEFAULT NULL,
    `order_id`               varchar(255) DEFAULT NULL,
    `origin_code`            varchar(255) DEFAULT NULL,
    `pay_method`             int          DEFAULT NULL,
    `pickup_appoint_endtime` timestamp NULL DEFAULT NULL,
    `send_start_tm`          timestamp NULL DEFAULT NULL,
    `sender_address`         varchar(255) DEFAULT NULL,
    `sender_city`            int          DEFAULT NULL,
    `sender_contact`         varchar(255) DEFAULT NULL,
    `sender_mobile`          varchar(20)  DEFAULT NULL,
    `status`                 int          DEFAULT NULL,
    `total_height` double DEFAULT NULL,
    `total_length` double DEFAULT NULL,
    `total_volume` double DEFAULT NULL,
    `total_weight` double DEFAULT NULL,
    `total_width` double DEFAULT NULL,
    `waybill_no`             varchar(255) DEFAULT NULL,
    `waybill_type`           int          DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sfexpress_express`
--

LOCK
TABLES `sfexpress_express` WRITE;
/*!40000 ALTER TABLE `sfexpress_express` DISABLE KEYS */;
INSERT INTO `sfexpress_express`
VALUES (2, '电子产品', NULL, '2023-12-11 15:45:48', '020', '收件人地址', 20, '收件人', '13987654321', 123,
        'SF123456789', '20231211001', '769', 1, NULL, NULL, '发件人地址', 769, '发件人', '13812345678', 0, NULL, NULL,
        NULL, NULL, NULL, 'SF1183796785387671552', 1),
       (3, '电子产品', NULL, '2023-12-11 15:53:57', NULL, '收件人地址', NULL, '收件人', '13987654321', 123,
        'SF123456789', '1183798840080732160', NULL, 1, NULL, NULL, '发件人地址', NULL, '发件人', '13812345678', 2, NULL,
        NULL, NULL, NULL, NULL, 'SF1183798840500162560', 1),
       (4, '电子产品', NULL, '2023-12-11 21:55:52', NULL, '收件人地址', NULL, '收件人', '13987654321', 123,
        'SF123456789', '1183889915764019200', NULL, 1, NULL, NULL, '发件人地址', NULL, '发件人', '13812345678', 0, NULL,
        NULL, NULL, NULL, NULL, 'SF1183889915793379328', 1),
       (5, '电子产品', NULL, '2023-12-11 21:57:52', NULL, '收件人地址', 20, '收件人', '13987654321', 123, 'SF123456789',
        '1183890419449597952', NULL, 1, NULL, NULL, '发件人地址', 769, '发件人', '13812345678', 0, NULL, NULL, NULL,
        NULL, NULL, 'SF1183890419483152384', 1),
       (6, '电子产品', NULL, '2023-12-11 22:10:18', NULL, '收件人地址', 20, '收件人', '13987654321', 123, 'SF123456789',
        '1183893547897065472', NULL, 1, NULL, NULL, '发件人地址', 769, '发件人', '13812345678', 0, NULL, NULL, NULL,
        NULL, NULL, 'SF1183893547934814208', 1),
       (7, '电子产品', NULL, '2023-12-11 22:11:29', NULL, '收件人地址', 20, '收件人', '13987654321', 123, 'SF123456789',
        '1183893848351838208', NULL, 1, NULL, NULL, '发件人地址', 769, '发件人', '13812345678', 1, NULL, NULL, NULL,
        NULL, NULL, 'SF1183893849006149632', 1),
       (8, '电子产品', NULL, '2023-12-11 22:17:37', NULL, '收件人地址', 20, '收件人', '13987654321', 123, 'SF123456789',
        '1183895391419502592', NULL, 1, NULL, NULL, '发件人地址', 769, '发件人', '13812345678', 2, NULL, NULL, NULL,
        NULL, NULL, 'SF1183895392224808960', 1),
       (10, '电子产品', NULL, '2023-12-11 22:23:05', NULL, '收件人地址', 20, '收件人', '13987654321', 123,
        'SF123456789', '1183896766073278464', NULL, 1, '2023-12-12 17:08:42', '2023-12-12 17:08:42', '发件人地址', 769,
        '发件人', '13812345678', 0, 10, 10, 10, 10, 10, 'SF1183896766740172800', 1),
       (14, '电子产品', NULL, '2023-12-12 23:19:51', NULL, '收件人地址', 20, '收件人', '13987654321', 123,
        'SF123456789', '123456789012345678', NULL, 1, NULL, NULL, '发件人地址', 769, '发件人', '13987654321', 1, 29.98,
        29.98, 26946.035992000005, 2.09, 29.98, 'SF1184273442661732352', 1);
/*!40000 ALTER TABLE `sfexpress_express` ENABLE KEYS */;
UNLOCK
TABLES;

--
-- Table structure for table `sfexpress_route`
--

DROP TABLE IF EXISTS `sfexpress_route`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sfexpress_route`
(
    `id`             bigint NOT NULL AUTO_INCREMENT,
    `accept_address` varchar(255) DEFAULT NULL,
    `accept_time`    datetime(6) DEFAULT NULL,
    `express_id`     bigint       DEFAULT NULL,
    `op_code`        varchar(255) DEFAULT NULL,
    `remark`         varchar(255) DEFAULT NULL,
    `mail_no`        varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sfexpress_route`
--

LOCK
TABLES `sfexpress_route` WRITE;
/*!40000 ALTER TABLE `sfexpress_route` DISABLE KEYS */;
INSERT INTO `sfexpress_route`
VALUES (1, '南京', '2023-12-11 00:00:42.000000', NULL, '33', '运输中', 'SF1183889915793379328'),
       (2, '上海', '2023-12-11 13:54:38.000000', NULL, '21', '异常件', 'SF1183890419483152384'),
       (3, '武汉', '2023-12-11 11:41:22.000000', NULL, '71', '已揽收', 'SF1183893547934814208'),
       (4, '广州', '2023-12-11 16:47:26.000000', NULL, '81', '已签收', 'SF1183893849006149632'),
       (5, '西安', '2023-12-11 05:29:53.000000', NULL, '20', '运输中', 'SF1183895392224808960'),
       (6, '西安', '2023-12-11 01:05:18.000000', NULL, '94', '已签收', 'SF1183896766740172800'),
       (7, '西安', '2023-12-11 00:33:35.000000', NULL, '44', '已揽收', 'SF1183896766740172800'),
       (8, '上海', '2023-12-11 19:56:45.000000', NULL, '92', '异常件', 'SF1183896766740172800'),
       (9, '上海', '2023-12-11 09:57:25.000000', NULL, '96', '已揽收', 'SF1183896766740172800'),
       (10, '上海', '2023-12-11 22:36:06.000000', NULL, '0', '派件中', 'SF1183896766740172800'),
       (11, '重庆', '2023-12-11 07:27:31.000000', NULL, '44', '异常件', 'SF1183896766740172800'),
       (13, '成都', '2023-12-11 17:20:28.000000', NULL, '4', '运输中', 'SF1183896766740172800'),
       (14, '北京', '2023-12-11 23:20:07.000000', NULL, '20', '派件中', 'SF1183896766740172800'),
       (15, '上海', '2023-12-11 13:00:23.000000', NULL, '18', '已揽收', 'SF1183896766740172800'),
       (16, '上海', '2023-12-11 15:22:56.000000', NULL, '7', '已签收', 'SF1183896766740172800'),
       (17, '上海', '2023-12-11 08:55:04.000000', NULL, '41', '已揽收', 'SF1183896766740172800'),
       (18, '杭州', '2023-12-11 14:00:13.000000', NULL, '78', '派件中', 'SF1183896766740172800'),
       (19, '广州', '2023-12-11 14:56:32.000000', NULL, '61', '运输中', 'SF1184273442661732352'),
       (20, '武汉', '2023-12-11 18:58:08.000000', NULL, '9', '已揽收', 'SF1184273442661732352'),
       (21, '杭州', '2023-12-11 12:27:31.000000', NULL, '89', '已揽收', 'SF1184273442661732352');
/*!40000 ALTER TABLE `sfexpress_route` ENABLE KEYS */;
UNLOCK
TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-22 23:42:21
