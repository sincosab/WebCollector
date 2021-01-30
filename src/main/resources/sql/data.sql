-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: mall
-- ------------------------------------------------------
-- Server version	8.0.21

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
-- Table structure for table `crawl_data`
--

DROP TABLE IF EXISTS `crawl_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `crawl_data` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `site_id` bigint NOT NULL,
  `title` varchar(200) DEFAULT NULL,
  `content` text,
  `publish_time` datetime DEFAULT NULL,
  `status` int DEFAULT '0',
  `url` varchar(250) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crawl_data`
--

LOCK TABLES `crawl_data` WRITE;
/*!40000 ALTER TABLE `crawl_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `crawl_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crawl_site`
--

DROP TABLE IF EXISTS `crawl_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `crawl_site` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `domain_name` varchar(45) DEFAULT NULL,
  `base_url` varchar(100) DEFAULT NULL,
  `site` varchar(100) DEFAULT NULL,
  `init_url` varchar(200) DEFAULT NULL,
  `page_url` varchar(200) DEFAULT NULL,
  `page_start` int DEFAULT NULL,
  `page_end` int DEFAULT NULL,
  `regex` varchar(200) DEFAULT NULL,
  `match_url` varchar(250) DEFAULT NULL,
  `title` varchar(200) DEFAULT NULL,
  `content` varchar(200) DEFAULT NULL,
  `meta_flag` varchar(30) DEFAULT NULL,
  `keyword` varchar(100) DEFAULT NULL,
  `publish_time` varchar(30) DEFAULT NULL,
  `domain` varchar(50) DEFAULT NULL,
  `status` int DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crawl_site`
--

LOCK TABLES `crawl_site` WRITE;
/*!40000 ALTER TABLE `crawl_site` DISABLE KEYS */;
INSERT INTO `crawl_site` VALUES (1,'北京市公共资源交易服务平台_工程建设','https://ggzyfw.beijing.gov.cn/jyxxggjtbyqs/','meta[http-equiv=SiteName]','index.html','index_',2,3,'.*','.*/.*','meta[http-equiv=ArticleTitle]','div.div-content',NULL,'meta[http-equiv=ColumnKeywords]','meta[http-equiv=PubDate]','meta[http-equiv=SiteDomain]',1,'2021-01-06 16:01:57','2021-01-29 18:06:56'),(2,'北京市公共资源交易服务平台_政府采购','https://ggzyfw.beijing.gov.cn/jyxxcggg/','meta[http-equiv=SiteName]','index.html','index_',2,3,'.*','.*/.*','meta[http-equiv=ArticleTitle]','div.div-content',NULL,'meta[http-equiv=ColumnKeywords]','meta[http-equiv=PubDate]','meta[http-equiv=SiteDomain]',1,'2021-01-06 16:01:57','2021-01-29 18:06:56'),(3,'北京市公共资源交易服务平台_土地使用权','https://ggzyfw.beijing.gov.cn/jyxxzpggg/','meta[http-equiv=SiteName]','index.html','index_',2,3,'.*','.*/.*','meta[http-equiv=ArticleTitle]','div.div-content',NULL,'meta[http-equiv=ColumnKeywords]','meta[http-equiv=PubDate]','meta[http-equiv=SiteDomain]',1,'2021-01-06 16:01:57','2021-01-29 18:06:56'),(4,'北京市公共资源交易服务平台_国有产权','https://ggzyfw.beijing.gov.cn/jyxxswzcgpplxx/','meta[http-equiv=SiteName]','index.html','index_',2,3,'.*','.*/.*','meta[http-equiv=ArticleTitle]','div.div-content',NULL,'meta[http-equiv=ColumnKeywords]','meta[http-equiv=PubDate]','meta[http-equiv=SiteDomain]',1,'2021-01-06 16:01:57','2021-01-29 18:06:56'),(5,'北京市公共资源交易服务平台_软件和信息服务','https://ggzyfw.beijing.gov.cn/jyxxrjxxzbgg/','meta[http-equiv=SiteName]','index.html','index_',2,3,'.*','.*/.*','meta[http-equiv=ArticleTitle]','div.div-content',NULL,'meta[http-equiv=ColumnKeywords]','meta[http-equiv=PubDate]','meta[http-equiv=SiteDomain]',1,'2021-01-06 16:01:57','2021-01-29 18:06:56'),(6,'北京市公共资源交易服务平台_其他','https://ggzyfw.beijing.gov.cn/jyxxqtjygg/','meta[http-equiv=SiteName]','index.html','index_',2,3,'.*','.*/.*','meta[http-equiv=ArticleTitle]','div.div-content',NULL,'meta[http-equiv=ColumnKeywords]','meta[http-equiv=PubDate]','meta[http-equiv=SiteDomain]',1,'2021-01-06 16:01:57','2021-01-29 18:06:56'),(7,'中国政府采购网_地方公告','http://www.ccgp.gov.cn/cggg/dfgg/','meta[name=SiteName]','index.htm','index_',2,3,'.*','.*/.*','meta[name=ArticleTitle]','div.vF_detail_main',NULL,'meta[name=ColumnKeywords]','meta[name=PubDate]','meta[name=SiteDomain]',1,'2021-01-06 16:01:57','2021-01-29 18:06:56'),(8,'中国政府采购网_中央批量采购招标公告','http://www.ccgp.gov.cn/zydwplcg/zy/zyzb/','meta[name=SiteName]','index.htm','index_',1,2,'http://www.ccgp.gov.cn/cggg/zygg/zbgg.*','matchDate:http://www.ccgp.gov.cn/cggg/zygg/zbgg.*/.*','div.vF_detail_header h2.tc','div.vF_detail_main',NULL,'meta[name=ColumnKeywords]','meta[name=PubDate]','meta[name=SiteDomain]',0,'2021-01-06 16:01:57','2021-01-29 18:06:56'),(9,'中国政府采购网_中直批量采购招标公告','http://www.ccgp.gov.cn/zydwplcg/zz/zzzb/','meta[name=SiteName]','index.htm','index_',1,1,'http://www.ccgp.gov.cn/cggg/zygg/gkzb.*','matchDate:http://www.ccgp.gov.cn/cggg/zygg/gkzb/.*','div h2.tc','div.vF_detail_main,div.vT_detail_main',NULL,'meta[name=ColumnKeywords]','#pubTime','meta[name=SiteDomain]',1,'2021-01-06 16:01:57','2021-01-29 18:06:56'),(10,'中国政府采购网_中央公告','http://www.ccgp.gov.cn/cggg/zygg/','meta[name=SiteName]','index.htm','index_',2,3,'.*','.*/.*','meta[name=ArticleTitle]','div.vF_detail_main',NULL,'meta[name=ColumnKeywords]','','meta[name=SiteDomain]',1,'2021-01-06 16:01:57','2021-01-29 18:06:56'),(11,'中国政府采购网_中直批量采购招标公告','http://www.ccgp.gov.cn/zydwplcg/zz/zzzb/','meta[name=SiteName]','index.htm','index_',2,3,'http://www.ccgp.gov.cn/cggg/zygg/gkzb.*','http://www.ccgp.gov.cn/cggg/zygg/gkzb/.*','div h2.tc','div.vF_detail_main,div.vT_detail_main',NULL,'meta[name=ColumnKeywords]','#pubTime','meta[name=SiteDomain]',1,'2021-01-06 16:01:57','2021-01-29 18:06:56');
/*!40000 ALTER TABLE `crawl_site` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'mall'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-01-30 17:19:19
