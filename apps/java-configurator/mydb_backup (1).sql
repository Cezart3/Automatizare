-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: mydb
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin_users`
--

DROP TABLE IF EXISTS `admin_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_users`
--

LOCK TABLES `admin_users` WRITE;
/*!40000 ALTER TABLE `admin_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `balamale`
--

DROP TABLE IF EXISTS `balamale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `balamale` (
  `CodProdus` varchar(10) NOT NULL,
  `Denumire` varchar(255) DEFAULT NULL,
  `material_id` int NOT NULL,
  `finish_id` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  PRIMARY KEY (`CodProdus`,`material_id`,`finish_id`),
  KEY `material_id` (`material_id`),
  KEY `finish_id` (`finish_id`),
  CONSTRAINT `balamale_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `materials` (`material_id`),
  CONSTRAINT `balamale_ibfk_2` FOREIGN KEY (`finish_id`) REFERENCES `finishes` (`finish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `balamale`
--

LOCK TABLES `balamale` WRITE;
/*!40000 ALTER TABLE `balamale` DISABLE KEYS */;
INSERT INTO `balamale` VALUES ('BA01','Balama pentru toc de aluminiu sau lemn',2,1,19.00,'Pictures/BA01.jpg'),('BA01','Balama pentru toc de aluminiu sau lemn',2,3,21.00,'Pictures/BA01.jpg'),('BA02','Balama pentru toc de aluminiu sau lemn',3,1,18.00,'Pictures/BA02.jpg'),('BH01','Balamale profesionale perete-sticlă',3,1,53.00,'Pictures/BH01.jpg'),('BH02','Balamale profesionale sticlă-sticlă',3,1,61.00,'Pictures/BH02.jpg'),('BP01','Balama perete-sticlă',3,1,26.00,'Pictures/BP01.jpg'),('BP01','Balama perete-sticlă',3,2,28.50,'Pictures/BP01.jpg'),('BP01','Balama perete-sticlă',3,3,30.00,'Pictures/BP01.jpg'),('BP01','Balama perete-sticlă',3,4,30.00,'Pictures/BP01.jpg'),('BP01','Balama perete-sticlă',3,5,36.00,'Pictures/BP01.jpg'),('BP02','Balama sticlă-sticlă',3,1,31.50,'Pictures/BP02.jpg'),('BP02','Balama sticlă-sticlă',3,2,34.00,'Pictures/BP02.jpg'),('BP02','Balama sticlă-sticlă',3,3,37.00,'Pictures/BP02.jpg'),('BP02','Balama sticlă-sticlă',3,4,37.00,'Pictures/BP02.jpg'),('BP02','Balama sticlă-sticlă',3,5,40.00,'Pictures/BP02.jpg'),('BP03','Balama perete-sticlă',3,2,36.00,'Pictures/BP03.jpg'),('SH301','Balama perete-sticlă maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',1,1,13.00,'Pictures/SH301.jpg'),('SH301','Balama perete-sticlă maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',1,2,14.00,'Pictures/SH301.jpg'),('SH301','Balama perete-sticlă maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,1,15.00,'Pictures/SH301.jpg'),('SH301','Balama perete-sticlă maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,2,16.00,'Pictures/SH301.jpg'),('SH301','Balama perete-sticlă maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,3,18.00,'Pictures/SH301.jpg'),('SH301','Balama perete-sticlă maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,4,18.00,'Pictures/SH301.jpg'),('SH301','Balama perete-sticlă maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,5,23.00,'Pictures/SH301.jpg'),('SH301','Balama perete-sticlă maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,6,26.00,'Pictures/SH301.jpg'),('SH302','Balama sticlă-sticlă la 135° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',1,1,25.00,'Pictures/SH302.jpg'),('SH302','Balama sticlă-sticlă la 135° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',1,2,26.50,'Pictures/SH302.jpg'),('SH302','Balama sticlă-sticlă la 135° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,1,28.00,'Pictures/SH302.jpg'),('SH302','Balama sticlă-sticlă la 135° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,2,29.00,'Pictures/SH302.jpg'),('SH302','Balama sticlă-sticlă la 135° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,3,32.00,'Pictures/SH302.jpg'),('SH302','Balama sticlă-sticlă la 135° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,4,33.50,'Pictures/SH302.jpg'),('SH302','Balama sticlă-sticlă la 135° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,5,38.00,'Pictures/SH302.jpg'),('SH303','Balama sticlă-sticlă la 180° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,1,25.00,'Pictures/SH303.jpg'),('SH303','Balama sticlă-sticlă la 180° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,2,25.00,'Pictures/SH303.jpg'),('SH303','Balama sticlă-sticlă la 180° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,3,26.00,'Pictures/SH303.jpg'),('SH303','Balama sticlă-sticlă la 180° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,4,26.00,'Pictures/SH303.jpg'),('SH303','Balama sticlă-sticlă la 180° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,5,35.00,'Pictures/SH303.jpg'),('SH303','Balama sticlă-sticlă la 180° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,6,38.00,'Pictures/SH303.jpg'),('SH304','Balama sticlă-sticlă la 90° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',1,1,24.00,'Pictures/SH304.jpg'),('SH304','Balama sticlă-sticlă la 90° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',1,2,24.50,'Pictures/SH304.jpg'),('SH304','Balama sticlă-sticlă la 90° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,1,27.00,'Pictures/SH304.jpg'),('SH304','Balama sticlă-sticlă la 90° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,2,28.00,'Pictures/SH304.jpg'),('SH304','Balama sticlă-sticlă la 90° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,3,31.00,'Pictures/SH304.jpg'),('SH304','Balama sticlă-sticlă la 90° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,4,31.00,'Pictures/SH304.jpg'),('SH304','Balama sticlă-sticlă la 90° maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,5,37.00,'Pictures/SH304.jpg'),('SH305','Balama perete-sticlă maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',1,1,13.00,'Pictures/SH305.jpg'),('SH305','Balama perete-sticlă maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',1,2,14.00,'Pictures/SH305.jpg'),('SH305','Balama perete-sticlă maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,1,15.00,'Pictures/SH305.jpg'),('SH305','Balama perete-sticlă maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,2,16.00,'Pictures/SH305.jpg'),('SH305','Balama perete-sticlă maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,3,18.00,'Pictures/SH305.jpg'),('SH305','Balama perete-sticlă maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,4,18.00,'Pictures/SH305.jpg'),('SH305','Balama perete-sticlă maxim 40kg cu 2 balamale – sticlă securizată de 8-10mm',3,5,23.00,'Pictures/SH305.jpg'),('SH306','Prindere fixă maxim 40kg – sticlă securizată de 8-10mm',3,1,18.00,'Pictures/SH306.jpg'),('SH306','Prindere fixă maxim 40kg – sticlă securizată de 8-10mm',3,3,22.00,'Pictures/SH306.jpg'),('SH306','Prindere fixă maxim 40kg – sticlă securizată de 8-10mm',3,4,22.00,'Pictures/SH306.jpg'),('SH307','Balama perete-sticlă la 135° maxim 40kg – sticlă securizată de 8-10mm',3,1,22.00,'Pictures/SH307.jpg'),('SH308','Prindere fixă maxim 40kg – sticlă securizată de 8-10mm',3,1,21.50,'Pictures/SH308.jpg');
/*!40000 ALTER TABLE `balamale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `finishes`
--

DROP TABLE IF EXISTS `finishes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `finishes` (
  `finish_id` int NOT NULL AUTO_INCREMENT,
  `finish_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`finish_id`),
  UNIQUE KEY `finish_name` (`finish_name`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `finishes`
--

LOCK TABLES `finishes` WRITE;
/*!40000 ALTER TABLE `finishes` DISABLE KEYS */;
INSERT INTO `finishes` VALUES (4,'Alb'),(5,'Gold'),(2,'Lucios'),(3,'Negru'),(6,'Rose-Gold'),(1,'Satin'),(32,'Sticla_tmp'),(10,'undefined_finish_tmp'),(34,'undefined_tmp');
/*!40000 ALTER TABLE `finishes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garnituri`
--

DROP TABLE IF EXISTS `garnituri`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garnituri` (
  `CodProdus` varchar(10) NOT NULL,
  `Denumire` varchar(255) DEFAULT NULL,
  `material_id` int NOT NULL,
  `finish_id` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  PRIMARY KEY (`CodProdus`,`material_id`,`finish_id`),
  KEY `material_id` (`material_id`),
  KEY `finish_id` (`finish_id`),
  CONSTRAINT `garnituri_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `materials` (`material_id`),
  CONSTRAINT `garnituri_ibfk_2` FOREIGN KEY (`finish_id`) REFERENCES `finishes` (`finish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garnituri`
--

LOCK TABLES `garnituri` WRITE;
/*!40000 ALTER TABLE `garnituri` DISABLE KEYS */;
INSERT INTO `garnituri` VALUES ('K01','Conector perete pentru bara de rigidizare',10,1,6.00,'Pictures/K01.jpg'),('K01','Conector perete pentru bara de rigidizare',10,2,6.00,'Pictures/K01.jpg'),('K02','Conector de trecere pentru bara de rigidizare',10,1,6.50,'Pictures/K02.jpg'),('K02','Conector de trecere pentru bara de rigidizare',10,2,6.50,'Pictures/K02.jpg'),('K03','Conector sticlă pentru bara de rigidizare',10,1,6.50,'Pictures/K03.jpg'),('K03','Conector sticlă pentru bara de rigidizare',10,2,6.50,'Pictures/K03.jpg'),('K10','Conector sticlă pentru bara de rigidizare reglabil',10,1,11.50,'Pictures/K10.jpg'),('K10','Conector sticlă pentru bara de rigidizare reglabil',10,2,11.50,'Pictures/K10.jpg'),('K10','Conector sticlă pentru bara de rigidizare reglabil',10,3,13.00,'Pictures/K10.jpg'),('K10','Conector sticlă pentru bara de rigidizare reglabil',10,4,13.00,'Pictures/K10.jpg'),('K15','Conector perete pentru bara de rigidizare 135°',10,1,6.00,'Pictures/K15.jpg'),('K15','Conector perete pentru bara de rigidizare 135°',10,2,6.50,'Pictures/K15.jpg'),('K15','Conector perete pentru bara de rigidizare 135°',10,3,7.50,'Pictures/K15.jpg'),('K15','Conector perete pentru bara de rigidizare 135°',10,4,7.50,'Pictures/K15.jpg'),('K16','Conector perete pentru bara de rigidizare',10,1,7.50,'Pictures/K16.jpg'),('K16','Conector perete pentru bara de rigidizare',10,2,8.00,'Pictures/K16.jpg'),('K17','Conector de trecere pentru bara de rigidizare',10,1,12.00,'Pictures/K17.jpg'),('K17','Conector de trecere pentru bara de rigidizare',10,2,12.00,'Pictures/K17.jpg'),('K18','Conector de trecere pentru bara de rigidizare 90°',10,1,12.00,'Pictures/K18.jpg'),('K18','Conector de trecere pentru bara de rigidizare 90°',10,2,12.00,'Pictures/K18.jpg'),('K19','Conector de trecere pentru bara de rigidizare 90° cu prindere',10,1,13.00,'Pictures/K19.jpg'),('K19','Conector de trecere pentru bara de rigidizare 90° cu prindere',10,2,13.00,'Pictures/K19.jpg'),('PS108','Garnitură adeziv între sticle de 10 mm (90°)',10,10,16.50,'Pictures/PS108.jpg'),('PS61','Garnitură adeziv între sticle de 10 mm',10,10,7.50,'Pictures/PS61.jpg'),('S01','Garnitură de etanșare',10,10,3.00,'Pictures/S01.jpg'),('S01-BL','Garnitură de etanșare – variantă BL',10,10,4.50,'Pictures/S01-BL.jpg'),('S02','Garnitură de etanșare',10,10,3.00,'Pictures/S02.jpg'),('S03','Garnitură de etanșare',10,10,3.00,'Pictures/S03.jpg'),('S04','Garnitură de etanșare',10,10,3.00,'Pictures/S04.jpg'),('S05','Garnitură de etanșare',10,10,3.00,'Pictures/S05.jpg'),('S10','Garnitură magnetică la 90°',10,10,11.50,'Pictures/S10.jpg'),('S10-BL','Garnitură magnetică la 90° – variantă BL',10,10,15.50,'Pictures/S10-BL.jpg'),('S10-GOLD','Garnitură magnetică la 90° – variantă Gold',10,10,23.00,'Pictures/S10-GOLD.jpg'),('S11','Set rigidizare 45 sau 90 grade (400–600 mm deschidere)',10,1,12.00,'Pictures/S11.jpg'),('S11','Set rigidizare 45 sau 90 grade (400–600 mm deschidere)',10,2,13.00,'Pictures/S11.jpg'),('S11','Set rigidizare 45 sau 90 grade (400–600 mm deschidere)',10,3,15.50,'Pictures/S11.jpg'),('S11','Set rigidizare 45 sau 90 grade (400–600 mm deschidere)',10,4,15.50,'Pictures/S11.jpg'),('S11','Set rigidizare 45 sau 90 grade (400–600 mm deschidere)',10,5,18.00,'Pictures/S11.jpg'),('S11','Garnitură magnetică la 180°',10,10,11.50,'Pictures/S11.jpg'),('S11-GOLD','Garnitură magnetică la 180° – variantă Gold',10,10,23.00,'Pictures/S11-GOLD.jpg'),('S12','Set rigidizare 45 sau 90 grade (135°) 300–500 mm deschidere',10,1,11.50,'Pictures/S12.jpg'),('S12','Garnitură magnetică la 135°',10,10,11.50,'Pictures/S12.jpg'),('S14','Set rigidizare 45 sau 90 grade (135°) 800–1000 mm deschidere',10,1,18.50,'Pictures/S14.jpg');
/*!40000 ALTER TABLE `garnituri` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `incuietori`
--

DROP TABLE IF EXISTS `incuietori`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `incuietori` (
  `CodProdus` varchar(10) NOT NULL,
  `Denumire` varchar(255) DEFAULT NULL,
  `material_id` int NOT NULL,
  `finish_id` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  PRIMARY KEY (`CodProdus`,`material_id`,`finish_id`),
  KEY `material_id` (`material_id`),
  KEY `finish_id` (`finish_id`),
  CONSTRAINT `incuietori_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `materials` (`material_id`),
  CONSTRAINT `incuietori_ibfk_2` FOREIGN KEY (`finish_id`) REFERENCES `finishes` (`finish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `incuietori`
--

LOCK TABLES `incuietori` WRITE;
/*!40000 ALTER TABLE `incuietori` DISABLE KEYS */;
INSERT INTO `incuietori` VALUES ('D90','Amortizor pe ușă (set complet)',3,1,67.00,'Pictures/D90.jpg'),('GC01','Conector perete',3,1,6.00,'Pictures/GC01.jpg'),('GC01-L','Conector perete – variantă Lucios',3,2,6.00,'Pictures/GC01-L.jpg'),('GC05','Conector sticlă-sticlă la 90°',3,1,10.00,'Pictures/GC05.jpg'),('GC06','Conector sticlă-sticlă la 135°',3,1,10.00,'Pictures/GC06.jpg'),('GC07','Conector sticlă-sticlă la 180°',3,1,10.00,'Pictures/GC07.jpg'),('R1000','Închidere glisantă sticlă–sticlă',3,1,47.00,'Pictures/R1000.jpg'),('R1001','Închidere glisantă sticlă–perete',3,1,30.00,'Pictures/R1001.jpg'),('R1002','Închidere perete–sticlă fără decupaj',3,1,12.00,'Pictures/R1002.jpg'),('R1003','Închidere perete–sticlă fără decupaj',3,1,14.00,'Pictures/R1003.jpg'),('R1006-1','Închidere buton',3,2,13.00,'Pictures/R1006-1.jpg'),('R1006-2','Închidere buton',3,2,15.00,'Pictures/R1006-2.jpg'),('R307','Închidere sticlă–sticlă fără decupaj',3,1,15.00,'Pictures/R307.jpg'),('R308','Închidere sticlă–sticlă fără decupaj',3,1,15.00,'Pictures/R308.jpg'),('R309','Închidere dublă sticlă–sticlă fără decupaj',3,1,18.00,'Pictures/R309.jpg'),('R563','Clema stâlp balustradă',3,1,7.50,'Pictures/R563.jpg'),('R701','Balamă parte de jos',3,1,8.00,'Pictures/R701.jpg'),('R702','Balamă parte de sus',3,1,8.00,'Pictures/R702.jpg'),('R703','Închidere',3,1,14.00,'Pictures/R703.jpg'),('R704','Prindere supralumină-bolt',3,1,13.00,'Pictures/R704.jpg'),('R706','Balamă ușă sticlă',3,1,8.50,'Pictures/R706.jpg'),('RV1','Ventuza cu trei brațe',3,1,47.00,'Pictures/RV1.jpg');
/*!40000 ALTER TABLE `incuietori` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `maner_si_contraplacanta_pentru_usi_de_sticla`
--

DROP TABLE IF EXISTS `maner_si_contraplacanta_pentru_usi_de_sticla`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `maner_si_contraplacanta_pentru_usi_de_sticla` (
  `CodProdus` varchar(30) NOT NULL,
  `Denumire` varchar(255) DEFAULT NULL,
  `material_id` int NOT NULL,
  `finish_id` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  PRIMARY KEY (`CodProdus`,`material_id`,`finish_id`),
  KEY `material_id` (`material_id`),
  KEY `finish_id` (`finish_id`),
  CONSTRAINT `maner_si_contraplacanta_pentru_usi_de_sticla_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `materials` (`material_id`),
  CONSTRAINT `maner_si_contraplacanta_pentru_usi_de_sticla_ibfk_2` FOREIGN KEY (`finish_id`) REFERENCES `finishes` (`finish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `maner_si_contraplacanta_pentru_usi_de_sticla`
--

LOCK TABLES `maner_si_contraplacanta_pentru_usi_de_sticla` WRITE;
/*!40000 ALTER TABLE `maner_si_contraplacanta_pentru_usi_de_sticla` DISABLE KEYS */;
INSERT INTO `maner_si_contraplacanta_pentru_usi_de_sticla` VALUES ('204','Mâner pentru ușă de sticlă',3,1,55.00,'Pictures/204.jpg'),('205','Mâner pentru ușă de sticlă – închidere pe toc',3,1,62.00,'Pictures/205.jpg'),('205-2','Contraplăcă cu montaj pe sticlă',3,1,18.00,'Pictures/205-2.jpg'),('CP1','Contraplăcă cu montaj pe perete',3,1,7.50,'Pictures/CP1.jpg'),('CP2','Contraplăcă cu montaj pe perete',3,1,7.50,'Pictures/CP2.jpg'),('LHL080','Mâner pentru ușă de sticlă',3,1,57.00,'Pictures/LHL080.jpg'),('LHL080','Mâner pentru ușă de sticlă',3,3,65.00,'Pictures/LHL080.jpg'),('maner+contraplaca','Set mâner + contraplăcă',3,1,87.00,'Pictures/maner+contraplaca.jpg');
/*!40000 ALTER TABLE `maner_si_contraplacanta_pentru_usi_de_sticla` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manere_buton`
--

DROP TABLE IF EXISTS `manere_buton`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manere_buton` (
  `CodProdus` varchar(10) NOT NULL,
  `Denumire` varchar(255) DEFAULT NULL,
  `material_id` int NOT NULL,
  `finish_id` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  PRIMARY KEY (`CodProdus`,`material_id`,`finish_id`),
  KEY `material_id` (`material_id`),
  KEY `finish_id` (`finish_id`),
  CONSTRAINT `manere_buton_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `materials` (`material_id`),
  CONSTRAINT `manere_buton_ibfk_2` FOREIGN KEY (`finish_id`) REFERENCES `finishes` (`finish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manere_buton`
--

LOCK TABLES `manere_buton` WRITE;
/*!40000 ALTER TABLE `manere_buton` DISABLE KEYS */;
INSERT INTO `manere_buton` VALUES ('BR02','Buton pentru cabina de duș',3,1,9.00,'Pictures/BR02.jpg'),('BR02','Buton pentru cabina de duș',3,2,10.00,'Pictures/BR02.jpg'),('BR20','Buton pentru cabina de duș',3,1,8.00,'Pictures/BR20.jpg'),('BR20','Buton pentru cabina de duș',3,2,8.00,'Pictures/BR20.jpg'),('BR20','Buton pentru cabina de duș',3,3,10.00,'Pictures/BR20.jpg'),('BR20','Buton pentru cabina de duș',3,4,11.50,'Pictures/BR20.jpg'),('BR20','Buton pentru cabina de duș',3,5,14.50,'Pictures/BR20.jpg'),('BR20','Buton pentru cabina de duș',3,6,14.50,'Pictures/BR20.jpg'),('BR21','Buton pentru cabina de duș',3,1,11.50,'Pictures/BR21.jpg'),('BR22','Buton pentru cabina de duș',3,1,8.00,'Pictures/BR22.jpg'),('BR22','Buton pentru cabina de duș',3,2,8.00,'Pictures/BR22.jpg'),('BR22','Buton pentru cabina de duș',3,3,9.00,'Pictures/BR22.jpg'),('BR22','Buton pentru cabina de duș',3,4,9.00,'Pictures/BR22.jpg'),('BR23','Buton pentru cabina de duș',2,2,7.50,'Pictures/BR23.jpg'),('BR24','Buton pentru cabina de duș',2,2,7.50,'Pictures/BR24.jpg'),('BR25','Buton pentru cabina de duș',2,2,7.50,'Pictures/BR25.jpg'),('BR26','Buton pentru cabina de duș',2,2,7.50,'Pictures/BR26.jpg'),('BR27','Buton pentru cabina de duș',2,2,11.00,'Pictures/BR27.jpg'),('MS01','Mâner buton sticlă pătrat',4,32,18.00,'Pictures/MS01.jpg'),('MS02','Mâner buton sticlă pătrat',4,32,18.00,'Pictures/MS02.jpg'),('MS03','Mâner buton sticlă rotund',4,32,18.00,'Pictures/MS03.jpg');
/*!40000 ALTER TABLE `manere_buton` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manere_diverse`
--

DROP TABLE IF EXISTS `manere_diverse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manere_diverse` (
  `CodProdus` varchar(10) NOT NULL,
  `Denumire` varchar(255) DEFAULT NULL,
  `material_id` int NOT NULL,
  `finish_id` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  PRIMARY KEY (`CodProdus`,`material_id`,`finish_id`),
  KEY `material_id` (`material_id`),
  KEY `finish_id` (`finish_id`),
  CONSTRAINT `manere_diverse_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `materials` (`material_id`),
  CONSTRAINT `manere_diverse_ibfk_2` FOREIGN KEY (`finish_id`) REFERENCES `finishes` (`finish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manere_diverse`
--

LOCK TABLES `manere_diverse` WRITE;
/*!40000 ALTER TABLE `manere_diverse` DISABLE KEYS */;
INSERT INTO `manere_diverse` VALUES ('9002','Mâner H 1500 cu închidere în pardoseală – 35×1300×1500×10 mm',3,1,184.00,'Pictures/9002.jpg'),('A28','Mâner pentru uși de sticlă',2,1,24.00,'Pictures/A28.jpg'),('H02','Mâner oval ușă sticlă',3,1,20.00,'Pictures/H02.jpg'),('H03','Mâner port-prosop țeavă rotundă',3,1,14.50,'Pictures/H03.jpg'),('H03','Mâner port-prosop țeavă rotundă',3,3,18.50,'Pictures/H03.jpg'),('H03','Mâner port-prosop țeavă rotundă',3,5,23.00,'Pictures/H03.jpg'),('H13','Mâner pentru cabina de duș și uși de sticlă',3,1,16.00,'Pictures/H13.jpg'),('H13','Mâner pentru cabina de duș și uși de sticlă',3,2,17.00,'Pictures/H13.jpg'),('H13','Mâner pentru cabina de duș și uși de sticlă',3,3,18.50,'Pictures/H13.jpg'),('H13','Mâner pentru cabina de duș și uși de sticlă',3,4,18.50,'Pictures/H13.jpg'),('H13','Mâner pentru cabina de duș și uși de sticlă',3,5,23.00,'Pictures/H13.jpg'),('H15','Mâner port-prosop țeavă patrata-țesit',3,1,37.00,'Pictures/H15.jpg'),('H15','Mâner port-prosop țeavă patrata-țesit',3,2,38.00,'Pictures/H15.jpg'),('H15','Mâner port-prosop țeavă patrata-țesit',3,3,41.50,'Pictures/H15.jpg'),('H15','Mâner port-prosop țeavă patrata-țesit',3,4,43.00,'Pictures/H15.jpg'),('H1500','Mâner H 1500',3,1,36.00,'Pictures/H1500.jpg'),('H17','Mâner port-prosop',3,1,37.00,'Pictures/H17.jpg'),('H17','Mâner port-prosop',3,2,37.00,'Pictures/H17.jpg'),('H22','Mâner pentru cabina de duș și uși de sticlă',3,1,33.50,'Pictures/H22.jpg'),('H23','Mâner ușă sticlă 10×30×250×225×1.0 mm',3,1,38.00,'Pictures/H23.jpg'),('H27','Mâner 10×40×440 mm',3,1,20.00,'Pictures/H27.jpg'),('H500','Mâner H 500',3,1,18.00,'Pictures/H500.jpg'),('H500','Mâner H 500',3,3,24.00,'Pictures/H500.jpg'),('H500','Mâner H 500',3,5,28.00,'Pictures/H500.jpg'),('H800','Mâner H 800',3,1,22.00,'Pictures/H800.jpg'),('MR1200','Mâner dreptunghiular',3,1,68.50,'Pictures/MR1200.jpg'),('MR1500','Mâner dreptunghiular',3,1,77.00,'Pictures/MR1500.jpg'),('MR200','Mâner dreptunghiular',3,1,22.00,'Pictures/MR200.jpg'),('MR200','Mâner dreptunghiular',3,3,26.00,'Pictures/MR200.jpg'),('MR200','Mâner dreptunghiular',3,5,27.00,'Pictures/MR200.jpg'),('MR300','Mâner dreptunghiular',3,1,28.00,'Pictures/MR300.jpg'),('MR300','Mâner dreptunghiular',3,3,30.00,'Pictures/MR300.jpg'),('MR300','Mâner dreptunghiular',3,5,33.50,'Pictures/MR300.jpg'),('MR600','Mâner dreptunghiular',3,1,30.00,'Pictures/MR600.jpg'),('MR800','Mâner dreptunghiular',3,1,36.00,'Pictures/MR800.jpg'),('MS04','Mâner port-prosop din sticlă',4,32,57.00,'Pictures/MS04.jpg'),('MS05','Mâner dreptunghiular din sticlă',4,32,50.00,'Pictures/MS05.jpg');
/*!40000 ALTER TABLE `manere_diverse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manere_pentru_usi_de_sticla_glisante`
--

DROP TABLE IF EXISTS `manere_pentru_usi_de_sticla_glisante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manere_pentru_usi_de_sticla_glisante` (
  `CodProdus` varchar(10) NOT NULL,
  `Denumire` varchar(255) DEFAULT NULL,
  `material_id` int NOT NULL,
  `finish_id` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  PRIMARY KEY (`CodProdus`,`material_id`,`finish_id`),
  KEY `material_id` (`material_id`),
  KEY `finish_id` (`finish_id`),
  CONSTRAINT `manere_pentru_usi_de_sticla_glisante_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `materials` (`material_id`),
  CONSTRAINT `manere_pentru_usi_de_sticla_glisante_ibfk_2` FOREIGN KEY (`finish_id`) REFERENCES `finishes` (`finish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manere_pentru_usi_de_sticla_glisante`
--

LOCK TABLES `manere_pentru_usi_de_sticla_glisante` WRITE;
/*!40000 ALTER TABLE `manere_pentru_usi_de_sticla_glisante` DISABLE KEYS */;
INSERT INTO `manere_pentru_usi_de_sticla_glisante` VALUES ('DK04','Mâner patrat pentru ușă glisantă',3,1,27.00,'Pictures/DK04.jpg'),('DK04','Mâner patrat pentru ușă glisantă',3,2,36.50,'Pictures/DK04.jpg'),('DK04','Mâner patrat pentru ușă glisantă',3,3,32.00,'Pictures/DK04.jpg'),('DK08','Mâner rotund pentru ușă glisantă',3,1,9.00,'Pictures/DK08.jpg'),('DK08','Mâner rotund pentru ușă glisantă',3,2,9.50,'Pictures/DK08.jpg'),('DK08','Mâner rotund pentru ușă glisantă',3,3,11.00,'Pictures/DK08.jpg'),('NCY833','Mâner rotund pentru ușă glisantă',3,2,21.00,'Pictures/NCY833.jpg'),('NCY835','Mâner rotund pentru ușă glisantă',3,2,12.00,'Pictures/NCY835.jpg'),('SKR07','Mâner patrat pentru ușă glisantă',3,1,27.00,'Pictures/SKR07.jpg'),('SKR07','Mâner patrat pentru ușă glisantă',3,2,36.50,'Pictures/SKR07.jpg'),('SKR07','Mâner patrat pentru ușă glisantă',3,3,32.00,'Pictures/SKR07.jpg'),('SKR16','Mâner rotund pentru ușă glisantă',3,1,9.00,'Pictures/SKR16.jpg'),('SKR16','Mâner rotund pentru ușă glisantă',3,2,9.50,'Pictures/SKR16.jpg'),('SKR16','Mâner rotund pentru ușă glisantă',3,3,11.00,'Pictures/SKR16.jpg'),('SKR40','Mâner patrat glisat cu adeziv (fără găurire)',3,1,16.50,'Pictures/SKR40.jpg');
/*!40000 ALTER TABLE `manere_pentru_usi_de_sticla_glisante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `materials`
--

DROP TABLE IF EXISTS `materials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `materials` (
  `material_id` int NOT NULL AUTO_INCREMENT,
  `material_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`material_id`),
  UNIQUE KEY `material_name` (`material_name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `materials`
--

LOCK TABLES `materials` WRITE;
/*!40000 ALTER TABLE `materials` DISABLE KEYS */;
INSERT INTO `materials` VALUES (2,'aluminiu'),(4,'otelInox+sticla'),(3,'OtelInoxidabil#304'),(5,'undefined'),(10,'undefined_material'),(1,'zinc');
/*!40000 ALTER TABLE `materials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions` (
  `permission_id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL,
  `can_view_main_page` tinyint(1) NOT NULL DEFAULT '0',
  `can_access_admin_page` tinyint(1) NOT NULL DEFAULT '0',
  `can_modify_data` tinyint(1) NOT NULL DEFAULT '0',
  `can_create_admins` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`permission_id`),
  KEY `role_id_idx` (`role_id`),
  CONSTRAINT `fk_permission_role` FOREIGN KEY (`role_id`) REFERENCES `user_roles` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `piese_balustrade`
--

DROP TABLE IF EXISTS `piese_balustrade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `piese_balustrade` (
  `CodProdus` varchar(10) NOT NULL,
  `Denumire` varchar(255) DEFAULT NULL,
  `material_id` int NOT NULL,
  `finish_id` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  PRIMARY KEY (`CodProdus`,`material_id`,`finish_id`),
  KEY `material_id` (`material_id`),
  KEY `finish_id` (`finish_id`),
  CONSTRAINT `piese_balustrade_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `materials` (`material_id`),
  CONSTRAINT `piese_balustrade_ibfk_2` FOREIGN KEY (`finish_id`) REFERENCES `finishes` (`finish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `piese_balustrade`
--

LOCK TABLES `piese_balustrade` WRITE;
/*!40000 ALTER TABLE `piese_balustrade` DISABLE KEYS */;
INSERT INTO `piese_balustrade` VALUES ('CBF901020','Baza de fixare 90° sticlă-balustradă',3,1,11.50,'Pictures/CBF901020.jpg'),('CBF901620','Baza de fixare 90° sticlă-balustradă',3,1,12.00,'Pictures/CBF901620.jpg'),('CBR1020','Balama reglabilă sticlă-balustradă',3,1,21.50,'Pictures/CBR1020.jpg'),('CBR1620','Balama reglabilă sticlă-balustradă',3,1,23.00,'Pictures/CBR1620.jpg'),('CBSS180101','Conector egalizare balustradă',3,1,11.50,'Pictures/CBSS1801012.jpg'),('CBSS180162','Conector egalizare balustradă',3,1,12.00,'Pictures/CBSS1801620.jpg'),('CBSS901012','Conector reglabil 90° sticlă-balustr.',3,1,14.00,'Pictures/CBSS901012.jpg'),('CBSS901620','Conector reglabil 90° sticlă-balustr.',3,1,15.00,'Pictures/CBSS901620.jpg'),('RG4438','Conector în puncte pentru balustradă',3,1,13.00,'Pictures/RG4438.jpg'),('RG5020','Conector în puncte pentru balustradă',3,1,8.50,'Pictures/RG5020.jpg'),('RG5030','Conector în puncte pentru balustradă',3,1,10.00,'Pictures/RG5030.jpg'),('RG5040','Conector în puncte pentru balustradă',3,1,11.00,'Pictures/RG5040.jpg'),('RG5050','Conector în puncte pentru balustradă',3,1,13.00,'Pictures/RG5050.jpg'),('RG6030','Conector în puncte pentru balustradă',3,1,12.00,'Pictures/RG6030.jpg'),('RGR50-40-5','Conector în puncte reglabil',3,1,18.50,'Pictures/RGR50-40-50.jpg'),('SP011','Spigot balustradă pentru sticlă 10-12 mm',3,1,42.00,'Pictures/SP011.jpg'),('SP9037','Spigot balustradă pentru sticlă 10-12 mm',3,1,42.00,'Pictures/SP9037.jpg'),('SP9047','Spigot balustradă pentru sticlă 10-12 mm',3,1,42.00,'Pictures/SP9047.jpg'),('SP9049-250','Stâlpișor balustradă 250 mm*32 mm',3,1,23.00,'Pictures/SP9049-250.jpg'),('SP9049-600','Stâlpișor balustradă 600 mm*38 mm',3,1,18.00,'Pictures/SP9049-600.jpg');
/*!40000 ALTER TABLE `piese_balustrade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_categories`
--

DROP TABLE IF EXISTS `product_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_categories` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(100) NOT NULL,
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `category_name_UNIQUE` (`category_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_categories`
--

LOCK TABLES `product_categories` WRITE;
/*!40000 ALTER TABLE `product_categories` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `product_id` int NOT NULL AUTO_INCREMENT,
  `CodProdus` varchar(10) NOT NULL,
  `Denumire` varchar(255) DEFAULT NULL,
  `category_id` int NOT NULL,
  `material_id` int NOT NULL,
  `finish_id` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `CodProdus_UNIQUE` (`CodProdus`,`material_id`,`finish_id`),
  KEY `fk_category_idx` (`category_id`),
  KEY `fk_material_idx` (`material_id`),
  KEY `fk_finish_idx` (`finish_id`),
  CONSTRAINT `fk_category` FOREIGN KEY (`category_id`) REFERENCES `product_categories` (`category_id`),
  CONSTRAINT `fk_finish` FOREIGN KEY (`finish_id`) REFERENCES `finishes` (`finish_id`),
  CONSTRAINT `fk_material` FOREIGN KEY (`material_id`) REFERENCES `materials` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profile`
--

DROP TABLE IF EXISTS `profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profile` (
  `CodProdus` varchar(10) NOT NULL,
  `Denumire` varchar(255) DEFAULT NULL,
  `material_id` int NOT NULL,
  `finish_id` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  PRIMARY KEY (`CodProdus`,`material_id`,`finish_id`),
  KEY `material_id` (`material_id`),
  KEY `finish_id` (`finish_id`),
  CONSTRAINT `profile_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `materials` (`material_id`),
  CONSTRAINT `profile_ibfk_2` FOREIGN KEY (`finish_id`) REFERENCES `finishes` (`finish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profile`
--

LOCK TABLES `profile` WRITE;
/*!40000 ALTER TABLE `profile` DISABLE KEYS */;
INSERT INTO `profile` VALUES ('GG01','Prag pentru cabina de duș: Profil M + profil U',2,1,38.00,'Pictures/GG01.jpg'),('GG02','Prag pentru cabina de duș: Profil M + profil U',2,1,13.00,'Pictures/GG02.jpg'),('GPU','Garnitură brăduț pentru 2 mm',10,10,1.20,'Pictures/GPU.jpg'),('MU20','Mascare de capăt pentru profil U20',3,1,2.50,'Pictures/MU20.jpg'),('MU20','Mascare de capăt pentru profil U20',3,2,3.00,'Pictures/MU20.jpg'),('MU20','Mascare de capăt pentru profil U20',3,3,3.50,'Pictures/MU20.jpg'),('MU20','Mascare de capăt pentru profil U20',3,5,4.20,'Pictures/MU20.jpg'),('MU40','Mascare de capăt pentru profil U40',3,1,3.50,'Pictures/MU40.jpg'),('PS60','Prag cabina de duș – La 3 metri',10,10,12.00,'Pictures/PS60.jpg'),('SF21','Colțar pentru prag PS60',10,10,2.50,'Pictures/SF21.jpg'),('SFL120','Profil de colț – îmbinare',2,1,29.50,'Pictures/SFL120.jpg'),('SL029','Profil zid – prindere garnituri magnetica',2,1,30.00,'Pictures/SL029.jpg'),('U20','Profil U H=20 mm (2,2 m și 3 m)',2,1,4.50,'Pictures/U20.jpg'),('U20','Profil U H=20 mm (2,2 m și 3 m)',2,2,5.20,'Pictures/U20.jpg'),('U20','Profil U H=20 mm (2,2 m și 3 m)',2,3,6.00,'Pictures/U20.jpg'),('U20','Profil U H=20 mm (2,2 m și 3 m)',2,4,6.50,'Pictures/U20.jpg'),('U20','Profil U H=20 mm (2,2 m și 3 m)',2,5,7.50,'Pictures/U20.jpg'),('U40','Profil U H=40 mm',2,1,7.50,'Pictures/U40.jpg');
/*!40000 ALTER TABLE `profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profile_rigidizare_si_conectori`
--

DROP TABLE IF EXISTS `profile_rigidizare_si_conectori`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `profile_rigidizare_si_conectori` (
  `CodProdus` varchar(10) NOT NULL,
  `Denumire` varchar(255) DEFAULT NULL,
  `material_id` int NOT NULL,
  `finish_id` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  PRIMARY KEY (`CodProdus`,`material_id`,`finish_id`),
  KEY `material_id` (`material_id`),
  KEY `finish_id` (`finish_id`),
  CONSTRAINT `profile_rigidizare_si_conectori_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `materials` (`material_id`),
  CONSTRAINT `profile_rigidizare_si_conectori_ibfk_2` FOREIGN KEY (`finish_id`) REFERENCES `finishes` (`finish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profile_rigidizare_si_conectori`
--

LOCK TABLES `profile_rigidizare_si_conectori` WRITE;
/*!40000 ALTER TABLE `profile_rigidizare_si_conectori` DISABLE KEYS */;
INSERT INTO `profile_rigidizare_si_conectori` VALUES ('C34','Conector perete-teava 30x10',3,1,6.00,'Pictures/C34.jpg'),('C34','Conector perete-teava 30x10',3,2,6.00,'Pictures/C34.jpg'),('C34','Conector perete-teava 30x10',3,3,7.50,'Pictures/C34.jpg'),('C34','Conector perete-teava 30x10',3,4,7.50,'Pictures/C34.jpg'),('C34','Conector perete-teava 30x10',3,5,8.00,'Pictures/C34.jpg'),('C34','Conector perete-teava 30x10',3,6,10.00,'Pictures/C34.jpg'),('C35','Conector teava-sticla 30x10',3,1,8.00,'Pictures/C35.jpg'),('C35','Conector teava-sticla 30x10',3,2,8.00,'Pictures/C35.jpg'),('C35','Conector teava-sticla 30x10',3,3,10.00,'Pictures/C35.jpg'),('C35','Conector teava-sticla 30x10',3,4,10.00,'Pictures/C35.jpg'),('C35','Conector teava-sticla 30x10',3,5,12.00,'Pictures/C35.jpg'),('C35','Conector teava-sticla 30x10',3,6,14.50,'Pictures/C35.jpg'),('C36','Conector de trecere-distantare teava-sticla 30x10',3,1,11.00,'Pictures/C36.jpg'),('C36','Conector de trecere-distantare teava-sticla 30x10',3,2,11.00,'Pictures/C36.jpg'),('C36','Conector de trecere-distantare teava-sticla 30x10',3,3,14.00,'Pictures/C36.jpg'),('C36','Conector de trecere-distantare teava-sticla 30x10',3,4,14.00,'Pictures/C36.jpg'),('C36','Conector de trecere-distantare teava-sticla 30x10',3,5,15.50,'Pictures/C36.jpg'),('C36','Conector de trecere-distantare teava-sticla 30x10',3,6,17.00,'Pictures/C36.jpg'),('C38','Conector de trecere teava-sticla 30x10',3,1,10.00,'Pictures/C38.jpg'),('C38','Conector de trecere teava-sticla 30x10',3,2,11.00,'Pictures/C38.jpg'),('C38','Conector de trecere teava-sticla 30x10',3,3,13.00,'Pictures/C38.jpg'),('C38','Conector de trecere teava-sticla 30x10',3,4,13.00,'Pictures/C38.jpg'),('C38','Conector de trecere teava-sticla 30x10',3,5,16.00,'Pictures/C38.jpg'),('C39','Conector teava-teava la 90 de grade 30x10',3,1,12.00,'Pictures/C39.jpg'),('C39','Conector teava-teava la 90 de grade 30x10',3,2,13.00,'Pictures/C39.jpg'),('C39','Conector teava-teava la 90 de grade 30x10',3,3,14.00,'Pictures/C39.jpg'),('C39','Conector teava-teava la 90 de grade 30x10',3,5,16.00,'Pictures/C39.jpg'),('C4','Coltar sticla',3,2,12.00,'Pictures/C4.jpg'),('C40','Conector teava-teava la 90 de grade 30x10',3,1,13.00,'Pictures/C40.jpg'),('C40','Conector teava-teava la 90 de grade 30x10',3,2,14.00,'Pictures/C40.jpg'),('C40','Conector teava-teava la 90 de grade 30x10',3,3,14.50,'Pictures/C40.jpg'),('C40','Conector teava-teava la 90 de grade 30x10',3,5,17.00,'Pictures/C40.jpg'),('C41','Conector teava-teava la 135 de grade 30x10',3,1,13.00,'Pictures/C41.jpg'),('C41','Conector teava-teava la 135 de grade 30x10',3,2,14.00,'Pictures/C41.jpg'),('C41','Conector teava-teava la 135 de grade 30x10',3,3,14.50,'Pictures/C41.jpg'),('C41','Conector teava-teava la 135 de grade 30x10',3,5,17.00,'Pictures/C41.jpg'),('C42','Conector teava-sticla 30x10, capat 135 grade',3,1,12.00,'Pictures/C42.jpg'),('C42','Conector teava-sticla 30x10, capat 135 grade',3,2,13.00,'Pictures/C42.jpg'),('C42','Conector teava-sticla 30x10, capat 135 grade',3,3,14.00,'Pictures/C42.jpg'),('C42','Conector teava-sticla 30x10, capat 135 grade',3,5,16.00,'Pictures/C42.jpg'),('C43-HL34A','Ornament capat teava 30x10',3,1,6.00,'Pictures/C43-HL34A.jpg'),('C43-HL34A','Ornament capat teava 30x10',3,2,6.50,'Pictures/C43-HL34A.jpg'),('C43-HL34A','Ornament capat teava 30x10',3,3,8.50,'Pictures/C43-HL34A.jpg'),('C43-HL34A','Ornament capat teava 30x10',3,5,10.00,'Pictures/C43-HL34A.jpg'),('C43-T','Conector T pentru teava 30x10',3,1,28.00,'Pictures/C43-T.jpg'),('C43-T','Conector T pentru teava 30x10',3,2,29.00,'Pictures/C43-T.jpg'),('C44','Opritor pentru teava 30x10',3,1,15.00,'Pictures/C44.jpg'),('C44','Opritor pentru teava 30x10',3,2,15.00,'Pictures/C44.jpg'),('FG02','Imbinare 90 de grade – glisanta dus',3,1,14.00,'Pictures/FG02.jpg'),('FG02','Imbinare 90 de grade – glisanta dus',3,2,15.00,'Pictures/FG02.jpg'),('FG03','Ornament imbinare 90 de grade – glisanta dus',3,1,14.00,'Pictures/FG03.jpg'),('FG03','Ornament imbinare 90 de grade – glisanta dus',3,2,15.00,'Pictures/FG03.jpg'),('GT02-304','Profil rectangular 30x10 mm otel-inox',3,1,10.00,'Pictures/GT02-304.jpg'),('GT02-304','Profil rectangular 30x10 mm otel-inox',3,2,10.00,'Pictures/GT02-304.jpg'),('GT02-304','Profil rectangular 30x10 mm otel-inox',3,3,14.00,'Pictures/GT02-304.jpg'),('GT02-304','Profil rectangular 30x10 mm otel-inox',3,5,16.00,'Pictures/GT02-304.jpg'),('GT02-304','Profil rectangular 30x10 mm otel-inox',3,6,18.50,'Pictures/GT02-304.jpg'),('S111','Set rigidizare 45 sau 90 grade (400–600 mm deschidere)',3,1,12.00,'Pictures/S11.jpg'),('S111','Set rigidizare 45 sau 90 grade (400–600 mm deschidere)',3,2,13.00,'Pictures/S11.jpg'),('S111','Set rigidizare 45 sau 90 grade (400–600 mm deschidere)',3,3,15.50,'Pictures/S11.jpg'),('S111','Set rigidizare 45 sau 90 grade (400–600 mm deschidere)',3,4,15.50,'Pictures/S11.jpg'),('S111','Set rigidizare 45 sau 90 grade (400–600 mm deschidere)',3,5,18.00,'Pictures/S11.jpg');
/*!40000 ALTER TABLE `profile_rigidizare_si_conectori` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `set_glisanta_pentru_cabina_de_dus`
--

DROP TABLE IF EXISTS `set_glisanta_pentru_cabina_de_dus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `set_glisanta_pentru_cabina_de_dus` (
  `CodProdus` varchar(10) NOT NULL,
  `Denumire` varchar(255) DEFAULT NULL,
  `material_id` int NOT NULL,
  `finish_id` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  PRIMARY KEY (`CodProdus`,`material_id`,`finish_id`),
  KEY `material_id` (`material_id`),
  KEY `finish_id` (`finish_id`),
  CONSTRAINT `set_glisanta_pentru_cabina_de_dus_ibfk_1` FOREIGN KEY (`material_id`) REFERENCES `materials` (`material_id`),
  CONSTRAINT `set_glisanta_pentru_cabina_de_dus_ibfk_2` FOREIGN KEY (`finish_id`) REFERENCES `finishes` (`finish_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `set_glisanta_pentru_cabina_de_dus`
--

LOCK TABLES `set_glisanta_pentru_cabina_de_dus` WRITE;
/*!40000 ALTER TABLE `set_glisanta_pentru_cabina_de_dus` DISABLE KEYS */;
INSERT INTO `set_glisanta_pentru_cabina_de_dus` VALUES ('R201','Sistem de glisare',2,2,128.00,'Pictures/R201.jpg'),('S02 KIT','Set glisantă pentru cabina de duș',2,1,54.00,'Pictures/S02 KIT.jpg'),('S02 KIT','Set glisantă pentru cabina de duș',2,2,55.50,'Pictures/S02 KIT.jpg'),('S02 KIT','Set glisantă pentru cabina de duș',2,3,58.50,'Pictures/S02 KIT.jpg'),('S02 KIT','Set glisantă pentru cabina de duș',2,4,58.50,'Pictures/S02 KIT.jpg'),('S02 KIT','Set glisantă pentru cabina de duș',2,5,74.50,'Pictures/S02 KIT.jpg');
/*!40000 ALTER TABLE `set_glisanta_pentru_cabina_de_dus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sticle`
--

DROP TABLE IF EXISTS `sticle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sticle` (
  `id` int NOT NULL AUTO_INCREMENT,
  `tip_sticla` varchar(100) NOT NULL,
  `grosime_mm` varchar(10) NOT NULL,
  `simpla_debitata` decimal(10,2) DEFAULT NULL,
  `securizata_calita` decimal(10,2) DEFAULT NULL,
  `manopera_slefuire` decimal(10,2) DEFAULT NULL,
  `manopera_gaurire_4_20` decimal(10,2) DEFAULT NULL,
  `manopera_gaurire_21_30` decimal(10,2) DEFAULT NULL,
  `manopera_gaurire_31_60_cnc` decimal(10,2) DEFAULT NULL,
  `adaos_forma_proc` decimal(5,2) DEFAULT NULL,
  `adaos_sablon_proc` decimal(5,2) DEFAULT NULL,
  `manopera_decupe_feron` decimal(10,2) DEFAULT '8.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sticle`
--

LOCK TABLES `sticle` WRITE;
/*!40000 ALTER TABLE `sticle` DISABLE KEYS */;
INSERT INTO `sticle` VALUES (1,'DARK GREY','8',65.00,77.00,2.00,2.50,4.00,10.00,15.00,25.00,8.00),(2,'DARK GREY','6',48.00,57.00,1.70,2.00,3.30,9.00,12.00,20.00,8.00),(3,'STICLA ULTRACLARA','4',22.00,29.00,1.50,1.70,2.80,8.00,8.00,12.00,8.00),(4,'STICLA ULTRACLARA','10',57.00,75.00,1.80,2.00,3.60,9.00,12.00,17.00,8.00),(5,'KREPI','8',44.00,56.00,2.00,2.50,4.00,9.00,12.00,16.00,8.00),(6,'STICLA PARSOL','6',34.00,42.00,1.60,1.70,2.80,7.00,8.00,12.00,8.00),(7,'STICLA PARSOL','10',57.00,74.00,1.80,2.00,3.60,9.00,12.00,17.00,8.00),(8,'STICLA PARSOL','4',23.00,29.00,1.50,1.70,2.80,7.00,8.00,12.00,8.00),(9,'STICLA ULTRACLARA','8',44.00,56.00,1.70,1.80,3.30,9.00,10.00,15.00,8.00),(10,'KREPI','6',36.00,44.00,1.70,2.00,3.30,8.00,10.00,14.00,8.00),(11,'STICLA SATINATA','8',48.00,59.00,1.70,1.80,3.30,8.00,10.00,15.00,8.00),(12,'FLOAT -STICLA CLARA','6',20.00,32.00,1.50,1.60,2.80,7.00,8.00,12.00,8.00),(13,'STICLA SATINATA','4',25.00,33.00,1.50,1.70,2.80,7.00,8.00,12.00,8.00),(14,'FLOAT -STICLA CLARA','12',57.00,79.00,0.00,0.00,0.00,0.00,0.00,0.00,8.00),(15,'STICLA PARSOL','8',44.00,56.00,1.70,1.80,3.30,8.00,10.00,15.00,8.00),(16,'FLOAT -STICLA CLARA','4',15.00,22.00,1.50,1.60,2.80,7.00,8.00,12.00,8.00),(17,'STICLA SATINATA','6',36.00,47.00,1.50,1.70,2.80,7.00,8.00,12.00,8.00),(18,'FLOAT -STICLA CLARA','10',37.00,57.00,1.60,1.80,3.50,9.00,12.00,17.00,8.00),(19,'STICLA ULTRACLARA','6',34.00,42.00,1.60,1.70,2.80,8.00,8.00,12.00,8.00),(20,'FLOAT -STICLA CLARA','8',28.00,44.00,1.60,1.70,3.20,8.00,10.00,15.00,8.00),(21,'STICLA SATINATA','10',59.00,77.00,1.80,2.00,3.60,9.00,12.00,17.00,8.00);
/*!40000 ALTER TABLE `sticle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_name_UNIQUE` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `role_id` int NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  KEY `role_id_idx` (`role_id`),
  CONSTRAINT `fk_user_role` FOREIGN KEY (`role_id`) REFERENCES `user_roles` (`role_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'mydb'
--
/*!50003 DROP PROCEDURE IF EXISTS `CalculateGlassPrice` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `CalculateGlassPrice`(
    IN p_sticla_id INT,
    IN p_height DECIMAL(10,2),
    IN p_width DECIMAL(10,2),
    OUT p_final_price DECIMAL(10,2)
)
BEGIN
    DECLARE base_price DECIMAL(10,2);
    DECLARE min_price DECIMAL(10,2);
    DECLARE area DECIMAL(10,2);

    -- Se obțin parametrii de calcul pentru sticlă
    SELECT price_per_unit_area, min_price
      INTO base_price, min_price 
      FROM sticla
      WHERE sticla_id = p_sticla_id;

    -- Calcularea suprafeței în m²
    SET area = p_height * p_width;

    -- Prețul inițial se calculează prin înmulțirea prețului unitar cu suprafața
    SET p_final_price = base_price * area;

    -- Aplicare preț minim dacă este definit și dacă valoarea calculată este mai mică
    IF min_price IS NOT NULL AND p_final_price < min_price THEN
      SET p_final_price = min_price;
    END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UpdateProductPrice` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `UpdateProductPrice`(
    IN p_CodProdus VARCHAR(10),
    IN p_material_id INT,
    IN p_finish_id INT,
    IN p_new_price DECIMAL(10,2)
)
BEGIN
  UPDATE products
    SET price = p_new_price
    WHERE CodProdus = p_CodProdus
      AND material_id = p_material_id
      AND finish_id = p_finish_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-01  2:33:59
