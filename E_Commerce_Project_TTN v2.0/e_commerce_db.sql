-- MySQL dump 10.13  Distrib 5.7.29, for Linux (x86_64)
--
-- Host: localhost    Database: e_commerce
-- ------------------------------------------------------
-- Server version	5.7.29-0ubuntu0.18.04.1

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
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address_line` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `label` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `zip_code` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKda8tuywtf0gb6sedwk7la1pgi` (`user_id`),
  CONSTRAINT `FKda8tuywtf0gb6sedwk7la1pgi` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (1,'badli',NULL,NULL,NULL,NULL,0,13),(2,'ajmer',NULL,NULL,NULL,NULL,0,13),(3,NULL,'katra',NULL,NULL,'jammu',0,15),(4,NULL,'pulwama',NULL,NULL,'kashmir',0,15),(5,NULL,'katra',NULL,NULL,'jammu',0,16),(6,NULL,'pulwama',NULL,NULL,'kashmir',0,16),(7,NULL,'usa',NULL,NULL,'delhi',0,23),(8,NULL,'canada',NULL,NULL,'delhi',0,23),(9,NULL,'usa',NULL,NULL,'delhi',0,24),(10,NULL,'canada',NULL,NULL,'delhi',0,24),(11,NULL,'usa',NULL,NULL,'delhi',0,25),(12,NULL,'canada',NULL,NULL,'delhi',0,25),(13,NULL,'usa',NULL,NULL,'delhi',0,26),(14,NULL,'canada',NULL,NULL,'delhi',0,26),(23,NULL,'usa',NULL,NULL,'delhi',0,31),(24,NULL,'canada',NULL,NULL,'delhi',0,NULL),(25,NULL,'usa',NULL,NULL,'delhi',0,33),(26,NULL,'canada',NULL,NULL,'delhi',0,33),(27,'afadsfasdfasdfas',NULL,'afghanistan','asdfadsf','asfasdf',12312,46),(28,'afadsfasdfasdfas',NULL,'taliban','asdfadsf','asfasdf',12312,47),(29,'afadsfasdfasdfas',NULL,'taliban','asdfadsf','asfasdf',12312,48),(30,'afadsfasdfasdfas',NULL,'taliban','asdfadsf','asfasdf',12312,NULL),(31,'afadsfasdfasdfas',NULL,'taliban','asdfadsf','asfasdf',12312,50),(38,NULL,'pitampura',NULL,NULL,'delhi',0,54),(39,NULL,'pitampura',NULL,NULL,'mumbai',0,54),(40,NULL,'pitampura',NULL,NULL,'delhi',0,55),(41,NULL,'pitampura',NULL,NULL,'mumbai',0,55);
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cart` (
  `user_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `is_wishlisted_item` tinyint(1) DEFAULT NULL,
  `product_variation_id` int(11) DEFAULT NULL,
  KEY `user_id` (`user_id`),
  KEY `product_variation_id` (`product_variation_id`),
  CONSTRAINT `cart_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `cart_ibfk_2` FOREIGN KEY (`product_variation_id`) REFERENCES `product_variation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKsa5hf7rt7noxjdswkcspeiwx1` (`parent_id`),
  CONSTRAINT `FKsa5hf7rt7noxjdswkcspeiwx1` FOREIGN KEY (`parent_id`) REFERENCES `parent_category` (`id`),
  CONSTRAINT `category_ibfk_1` FOREIGN KEY (`parent_id`) REFERENCES `parent_category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'TV',NULL),(3,'TV',3),(4,'TV',4),(5,'TV',5);
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_placed`
--

DROP TABLE IF EXISTS `order_placed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_placed` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `amount_paid` int(11) DEFAULT NULL,
  `date_created` date DEFAULT NULL,
  `payment_method` varchar(20) DEFAULT NULL,
  `customer_address_address_line` varchar(50) DEFAULT NULL,
  `customer_address_city` varchar(50) DEFAULT NULL,
  `customer_address_state` varchar(50) DEFAULT NULL,
  `customer_address_country` varchar(50) DEFAULT NULL,
  `customer_address_zip_code` int(11) DEFAULT NULL,
  `customer_address_label` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `order_placed_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_placed`
--

LOCK TABLES `order_placed` WRITE;
/*!40000 ALTER TABLE `order_placed` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_placed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_product`
--

DROP TABLE IF EXISTS `order_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `product_variation_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `product_variation_id` (`product_variation_id`),
  CONSTRAINT `order_product_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `order_placed` (`id`),
  CONSTRAINT `order_product_ibfk_2` FOREIGN KEY (`product_variation_id`) REFERENCES `product_variation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_product`
--

LOCK TABLES `order_product` WRITE;
/*!40000 ALTER TABLE `order_product` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_status`
--

DROP TABLE IF EXISTS `order_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_status` (
  `order_product_id` int(11) DEFAULT NULL,
  `from_status` varchar(40) DEFAULT NULL,
  `to_status` varchar(40) DEFAULT NULL,
  KEY `order_product_id` (`order_product_id`),
  CONSTRAINT `order_status_ibfk_1` FOREIGN KEY (`order_product_id`) REFERENCES `order_product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_status`
--

LOCK TABLES `order_status` WRITE;
/*!40000 ALTER TABLE `order_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parent_category`
--

DROP TABLE IF EXISTS `parent_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parent_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parent_category`
--

LOCK TABLES `parent_category` WRITE;
/*!40000 ALTER TABLE `parent_category` DISABLE KEYS */;
INSERT INTO `parent_category` VALUES (3,'Electronics'),(4,'Electronics'),(5,'Electronics');
/*!40000 ALTER TABLE `parent_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `seller_user_id` int(11) DEFAULT NULL,
  `name` varchar(60) DEFAULT NULL,
  `description` text,
  `category_id` int(11) DEFAULT NULL,
  `is_cancellable` tinyint(1) DEFAULT NULL,
  `is_returnable` tinyint(1) DEFAULT NULL,
  `brand` varchar(50) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1mtsbur82frn64de7balymq9s` (`category_id`),
  KEY `FKjh9cre5eds0b96q9dulabyjbu` (`seller_user_id`),
  CONSTRAINT `FK1mtsbur82frn64de7balymq9s` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `FKjh9cre5eds0b96q9dulabyjbu` FOREIGN KEY (`seller_user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`seller_user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `product_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (4,NULL,'smart 54inch TV','the is the new oled diplay smart samsung tv with bazzle less display',NULL,1,1,'samsung',0),(6,NULL,'smart 54inch TV','the is the new oled diplay smart samsung tv with bazzle less display',1,1,1,'samsung',0),(7,NULL,'smart 54inch TV','the is the new TV',3,1,1,'TVS',0),(8,NULL,'smart 54inch TV','the is the new TV',4,1,1,'TVS',0),(9,12,'smart 54inch TV','the is the new TV',5,1,1,'TVS',0);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_review`
--

DROP TABLE IF EXISTS `product_review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_review` (
  `user_id` int(11) DEFAULT NULL,
  `review` varchar(100) DEFAULT NULL,
  `rating` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  KEY `user_id` (`user_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `product_review_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `product_review_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_review`
--

LOCK TABLES `product_review` WRITE;
/*!40000 ALTER TABLE `product_review` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_variation`
--

DROP TABLE IF EXISTS `product_variation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_variation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) DEFAULT NULL,
  `quantity_available` int(11) DEFAULT NULL,
  `price` int(11) DEFAULT NULL,
  `metadata` json DEFAULT NULL,
  `image` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `product_variation_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_variation`
--

LOCK TABLES `product_variation` WRITE;
/*!40000 ALTER TABLE `product_variation` DISABLE KEYS */;
INSERT INTO `product_variation` VALUES (1,NULL,2,1000,'{\"brand\": \"bentton\", \"color\": \"black\"}',NULL),(2,NULL,2,1000,'{\"brand\": \"levis\", \"color\": \"blue\"}',NULL);
/*!40000 ALTER TABLE `product_variation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `authority` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ADMIN'),(2,'SELLER'),(3,'CUSTOMER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seller`
--

DROP TABLE IF EXISTS `seller`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seller` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company_contact` varchar(255) DEFAULT NULL,
  `company_name` varchar(255) DEFAULT NULL,
  `gst` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6rgw0e6tb24n93c27njlv0wcl` (`user_id`),
  CONSTRAINT `FK6rgw0e6tb24n93c27njlv0wcl` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seller`
--

LOCK TABLES `seller` WRITE;
/*!40000 ALTER TABLE `seller` DISABLE KEYS */;
INSERT INTO `seller` VALUES (7,'9818124789','ttn','12345',54),(8,'9818124789','ttn','12345',55);
/*!40000 ALTER TABLE `seller` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test`
--

DROP TABLE IF EXISTS `test`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `test` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `test_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test`
--

LOCK TABLES `test` WRITE;
/*!40000 ALTER TABLE `test` DISABLE KEYS */;
/*!40000 ALTER TABLE `test` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) DEFAULT NULL,
  `middle_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT NULL,
  `contact` varchar(10) DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'deepak',NULL,'kalra','deepak@Gmail.com',0,1,NULL,NULL),(2,'deepak',NULL,'kalra','deepak@Gmail.com',0,1,'9818124789',NULL),(3,'deepak',NULL,'kalra','deepak@Gmail.com',0,1,'9818124789',NULL),(4,'amar',NULL,'kumar','amar@Gmail.com',0,1,'9818124789',NULL),(5,'faizal',NULL,'ali','amar@Gmail.com',0,1,'9818124789',NULL),(6,'gunjan',NULL,'kalra','deepak@Gmail.com',0,1,'9818124789',NULL),(7,'sushma',NULL,'kalra','deepak@Gmail.com',0,1,'9818124789',NULL),(8,'sanjay',NULL,'kalra','deepak@Gmail.com',0,1,'9818124789',NULL),(9,'sanjay',NULL,'kalra','deepak@Gmail.com',0,1,'9818124789',NULL),(10,'pankaj',NULL,'kalra','deepak@Gmail.com',0,1,'9818124789',NULL),(11,'piyush',NULL,'kalra','deepak@Gmail.com',0,1,'9818124789',NULL),(12,'changu',NULL,'kalra','deepak@Gmail.com',0,1,'9818124789',NULL),(13,'viju',NULL,'kalra','deepak@Gmail.com',0,1,'9818124789',NULL),(15,'deepak',NULL,'kalra','deepak@gmail.com',0,0,NULL,NULL),(16,'akbar',NULL,'kalra','deepaki@ok.com',0,0,NULL,NULL),(17,'jalaudin',NULL,'kalra','deepaki@omail.com',0,0,NULL,NULL),(18,'deepu',NULL,'kalra','deepak@gm.com',0,0,NULL,'$2a$10$6MF2YiWPneZNC83WjosLaONp4PWkG8YpZ52mZPz4iBWfvAQzl8kaO'),(19,'punjabi',NULL,'kalra','deepaki@omail.com',0,0,NULL,'$2a$10$VrYKJY3fsJ34OIf.Ok7YM./6J4du2nR.Jp1c3C5e9fh02YyGyX/3S'),(20,'punjabi',NULL,'kalra','deepakimail@gm.com',0,0,NULL,'$2a$10$1CoCdvvJp6yUA1Ge2yDqmuxFRkVEQthRf8t6SujoFWHh6PLwzdED.'),(21,NULL,'punjabi','kalra','deepakimail@gm.com',0,0,NULL,'$2a$10$6Mw4RfPFFEQqnyxTrXYg0O25Y9YptPxQMSGHaxqLoNDQ.wToph2SW'),(22,'punjabi',NULL,'kalra','deepakimail@gm.com',0,0,'9818124789','$2a$10$Y4FBhtQ.FCkrVSktM14mQubXUEFfNuRF.WygkM8A/ELqoYZR.c0cO'),(23,'deepak',NULL,'kalra','deepakkalra4@gmail.com',0,1,NULL,'$2a$10$wP2WkIs932Ny009Hffx5SOXMuiYJUp7bSYhCpdlZeoGjn9brB6Qxi'),(24,'pinki',NULL,'kalra','deepakkalra411@gmail.com',0,0,NULL,'$2a$10$YQdl6Yyu65hOI7ugGhSKt.fg0wJSx0OOOXR/D9d5jNH1qhPL8dANO'),(25,'deepak',NULL,'kalra','dee@gmail.com',0,0,NULL,'$2a$10$Uf/SwwGUr7OQ5T9zgQdnau/iYq215l4XDLw31NeOYA99.i9kKGRw2'),(26,'deepak',NULL,'kalra','dee@gmail.com',0,0,NULL,'$2a$10$bhuzfZbirbbl9euH.U0eue.ek5DR0EAi3yskHerpOlrJs9G4qLItq'),(31,'deepak',NULL,'kalra','deepakkalra4111@gmail.com',0,1,NULL,'12345678'),(32,'latika',NULL,'kalra','latika@gmail.com',0,0,NULL,'$2a$10$RDhHI89M1V1dPvHRhg7ahe8IHtUaMX34HgSeERRd0uzlu0RSzIxqe'),(33,'deepak',NULL,'kalra','latika4@gmail.com',0,0,NULL,'$2a$10$kbyVa.KwTshpuiUKQ4fFceX8//C2PaAGMf1YCAZERmNwYvNx0H.tu'),(34,'bihari',NULL,'babua','deepak44@gmail.com',0,0,NULL,'$2a$10$UlnIWv8FPsZKN1rPMws.uOFPGkgJPT5Bgh0pp/crwcjINnNbjjpuG'),(35,'bihari',NULL,'babua','deepak444@gmail.com',0,0,NULL,'$2a$10$mkwekjhXbT5tklE1jWsTPOJQXN0VshkVvqrWceQseMWm0Nfv5bS.K'),(36,'bihari',NULL,'babua','deepa44@gmail.com',0,0,NULL,'$2a$10$p3MIUgwFRXO0rfQCzWBTdOfmb4B3QxNFPL7DxNuIDcdw1on9DS4iu'),(37,'bihari',NULL,'babua','deepa4444@gmail.com',0,0,NULL,'$2a$10$lQjWxvB2nR4aDA7Y26fNQO2tpPsy/fc.pnVq2Pgxqq6PRHG9w3sLu'),(38,'bihari',NULL,'babua','deepa44444@gmail.com',0,0,NULL,'$2a$10$3lIgPT3PCxmki/A4JgyWqOak4lrjSKktixBFc0Z61a6MCj49wqVfm'),(39,'bihari',NULL,'babua','deepa444444@gmail.com',0,0,NULL,'$2a$10$3DNNWZ2Sc98/L6UBiQ1.9.s2cFalwUBoVw55SOsZk8rDElElJpgke'),(42,'deepak',NULL,'kalra','deepak.kalra1@tothenew.com',0,0,'9818124789','$2a$10$GGiFI2b2WjGXtRr4GVenfekhRw7BUhgBvFyJjqF84j2DmDcSq2S0G'),(43,'abba jabba dabba',NULL,'adsfasf','deeeeeeeeeeeeeeeeeeeeeeeeeeepak@gm.com',0,0,NULL,NULL),(44,'abbaaaaaa',NULL,'adsfasf','deeeeeeeeeeeeeeeeeeeeeeeeeeepak@gm.com',0,0,NULL,NULL),(45,'abbaaaaaa',NULL,'adsfasf','deeeeeeeeeeeeeeeeeeeeeeeeeeepak@gm.com',0,0,NULL,NULL),(46,'check1',NULL,'adsfasf','deeeeeeeeeeeeeeeeeeeeeeeeeeepak@gm.com',0,0,NULL,NULL),(47,'check2',NULL,'adsfasf','deeeeeeeeeeeeeeeeeeeeeeeeeeepak@gm.com',0,0,NULL,NULL),(48,'check3test',NULL,'adsfasf','deeeeeeeeeeeeeeeeeeeeeeeeeeepak@gm.com',0,0,NULL,NULL),(50,'check6test',NULL,'adsfasf','deeeeeeeeeeeeeeeeeeeeeeeeeeepak@gm.com',0,0,NULL,NULL),(54,'anish',NULL,'sharma','554n25he@fuwamofu.com',0,0,'9818124789','$2a$10$NzsvWU/DWubGBkEfZVmAvuOIj6L/mEK9HKwgfZZLkMVJj/PQwGi8a'),(55,'anish',NULL,'sharma','54n25he@fuwamofu.com',0,0,'9818124789','$2a$10$sR5MD/niUS/x3QDAz1aRLOSfhQGwZ1wdmVEhUP8YxzxKexxMgts26');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_oauth`
--

DROP TABLE IF EXISTS `user_oauth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_oauth` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_oauth`
--

LOCK TABLES `user_oauth` WRITE;
/*!40000 ALTER TABLE `user_oauth` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_oauth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
  KEY `FK859n2jvi8ivhui0rl0esws6o` (`user_id`),
  CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (1,NULL,13),(2,3,15),(3,3,16),(4,3,17),(5,3,18),(6,3,19),(7,3,20),(8,3,21),(9,3,22),(11,3,24),(15,3,32),(16,3,33),(17,3,34),(18,3,35),(19,3,36),(20,3,37),(21,3,38),(22,3,39),(25,3,42),(26,1,23),(27,3,NULL),(28,3,NULL),(30,3,NULL),(32,3,NULL),(34,3,NULL),(36,3,NULL),(43,3,NULL),(45,3,50),(49,2,54),(50,2,55);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-04-10 13:38:10
