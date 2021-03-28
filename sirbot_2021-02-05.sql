# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.19)
# Database: sirbot
# Generation Time: 2021-02-05 21:38:04 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table games
# ------------------------------------------------------------

DROP TABLE IF EXISTS `games`;

CREATE TABLE `games` (
  `gameid` int(10) NOT NULL AUTO_INCREMENT,
  `started` timestamp NULL DEFAULT NULL,
  `ended` timestamp NULL DEFAULT NULL,
  `timestart` bigint(120) DEFAULT NULL,
  `timeend` bigint(120) DEFAULT '1000000000',
  `division` int(11) DEFAULT NULL,
  `result` int(11) DEFAULT '3',
  `type` int(10) DEFAULT NULL,
  KEY `gameid` (`gameid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table gamevotes
# ------------------------------------------------------------

DROP TABLE IF EXISTS `gamevotes`;

CREATE TABLE `gamevotes` (
  `disc_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gameid` int(11) DEFAULT NULL,
  `side` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table kicked
# ------------------------------------------------------------

DROP TABLE IF EXISTS `kicked`;

CREATE TABLE `kicked` (
  `disc_id` varchar(50) DEFAULT NULL,
  `hostmask` varchar(150) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table players
# ------------------------------------------------------------

DROP TABLE IF EXISTS `players`;

CREATE TABLE `players` (
  `disc_id` varchar(50) DEFAULT NULL,
  `gameid` int(11) DEFAULT NULL,
  `side` int(11) DEFAULT NULL,
  `points` int(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `game_acc` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '(must be set)',
  `disc_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `vouched` int(11) DEFAULT '0',
  `vouchedby` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `vouchdate` timestamp NULL DEFAULT NULL,
  `rank` int(11) DEFAULT '1',
  `comment` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `division` int(11) DEFAULT '2',
  `wins` int(10) DEFAULT '0',
  `loss` int(10) DEFAULT '0',
  `draw` int(10) DEFAULT '0',
  `battlepoints` int(10) DEFAULT '500',
  `streak` int(10) DEFAULT '0',
  `exp` int(10) DEFAULT '0',
  `ingame` int(10) DEFAULT '0',
  `lastgame` int(10) DEFAULT '0',
  `bannedby` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `banreason` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `banrank` int(10) DEFAULT '0',
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;

INSERT INTO `user` (`id`, `game_acc`, `disc_id`, `vouched`, `vouchedby`, `vouchdate`, `rank`, `comment`, `division`, `wins`, `loss`, `draw`, `battlepoints`, `streak`, `exp`, `ingame`, `lastgame`, `bannedby`, `banreason`, `banrank`)
VALUES
	(1,'rubara','145474234934755328',1,'145474234934755328',NULL,7,'The creator',2,0,0,0,500,0,0,0,0,NULL,NULL,0);

/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table warns
# ------------------------------------------------------------

DROP TABLE IF EXISTS `warns`;

CREATE TABLE `warns` (
  `warnid` int(10) NOT NULL AUTO_INCREMENT,
  `disc_id` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
  `warnedby_id` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `warnreason` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
  `timer` bigint(20) DEFAULT NULL,
  `active` int(11) DEFAULT NULL,
  KEY `warnid` (`warnid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
