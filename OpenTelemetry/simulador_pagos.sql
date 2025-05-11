-- MySQL dump 10.13  Distrib 8.4.0, for Win64 (x86_64)
--
-- Host: localhost    Database: simulador_pagos
-- ------------------------------------------------------
-- Server version	8.4.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `balances`
--

DROP TABLE IF EXISTS `balances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `balances` (
  `id` int NOT NULL AUTO_INCREMENT,
  `balance` double DEFAULT NULL,
  `modified` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `balances`
--

LOCK TABLES `balances` WRITE;
/*!40000 ALTER TABLE `balances` DISABLE KEYS */;
INSERT INTO `balances` VALUES (5,1000,'2024-12-12 10:09:15.856000'),(6,500,'2024-12-12 10:21:50.723000'),(7,200,'2024-12-12 10:22:02.465000'),(8,1136,'2024-12-26 09:13:58.869000'),(9,236,'2024-12-26 09:11:08.878000'),(10,358,'2024-12-26 09:13:58.853000'),(11,500,'2024-12-18 09:58:08.000000');
/*!40000 ALTER TABLE `balances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` int NOT NULL AUTO_INCREMENT,
  `amount` double DEFAULT NULL,
  `date` datetime(6) DEFAULT NULL,
  `receiver` int DEFAULT NULL,
  `sender` int DEFAULT NULL,
  `state` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKo4j1bmdj2nta3b2k445nsxgng` (`receiver`),
  KEY `FK2pgq7g0reh535pr5xtr4r9l0t` (`sender`),
  KEY `FK3m3cspd72xas8luekvdea9stw` (`state`),
  CONSTRAINT `FK2pgq7g0reh535pr5xtr4r9l0t` FOREIGN KEY (`sender`) REFERENCES `users` (`id`),
  CONSTRAINT `FK3m3cspd72xas8luekvdea9stw` FOREIGN KEY (`state`) REFERENCES `states` (`state`),
  CONSTRAINT `FKo4j1bmdj2nta3b2k445nsxgng` FOREIGN KEY (`receiver`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (17,50,'2024-12-18 10:57:49.975000',1,2,3),(18,100,'2024-12-18 11:15:43.199000',3,2,3),(19,50,'2024-12-18 11:31:15.935000',3,2,3),(20,10,'2024-12-18 11:37:53.737000',1,2,3),(21,20,'2024-12-19 10:24:49.317000',3,2,3),(22,5,'2024-12-19 10:31:57.590000',1,2,3),(23,33,'2024-12-19 11:13:23.607000',1,2,3),(24,4,'2024-12-19 11:32:48.046000',1,2,3),(25,8,'2024-12-19 11:40:53.109000',3,2,3),(26,30,'2024-12-19 12:08:50.322000',3,2,3),(27,4,'2024-12-19 12:10:52.223000',1,2,3),(28,30,'2024-12-26 09:10:23.253000',3,3,3),(29,50,'2024-12-26 09:11:01.381000',2,3,3),(30,30,'2024-12-26 09:13:51.276000',1,3,3);
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `states`
--

DROP TABLE IF EXISTS `states`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `states` (
  `state` int NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `states`
--

LOCK TABLES `states` WRITE;
/*!40000 ALTER TABLE `states` DISABLE KEYS */;
INSERT INTO `states` VALUES (1,'Iniciado'),(2,'Procesando'),(3,'Aceptado'),(4,'Rechazado');
/*!40000 ALTER TABLE `states` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `dni` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `cc` int DEFAULT NULL,
  `createdAt` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKqag5h43sqkc4owsaty4ehhws4` (`cc`),
  CONSTRAINT `FKiapdavnkipwbonfngjsmtln8p` FOREIGN KEY (`cc`) REFERENCES `balances` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,NULL,'12345678A','alice@example.com','Alice','$2a$10$EKCiXR/k4fQQMVIojw27k.mguso5H8aQideNAmac7I5r.rJzfxzAC','Doe',8,'2024-12-12 11:21:01.190000'),(2,NULL,'12345678B','paco@example.com','Paco','$2a$10$38af5Bra2V/B./abkCf66uQ7TpFDBMQdLyqRaKFP5eOXHyeqjWJkm','Fernandez',9,'2024-12-18 09:49:56.812000'),(3,NULL,'12345678C','armandojaleo@example.com','Armando','$2a$10$I6JgN/HSS2v4dw0EoqQRMOI1XVrIHKRB.2c7NmYmrCvl9HZ7T2lgy','Jaleo',10,'2024-12-18 09:50:09.256000'),(4,NULL,'12345678J','pedrito@example.com','Pedrito','picapiedra','Fernandes',11,'2024-12-18 09:59:47.000000');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-27 14:55:57
