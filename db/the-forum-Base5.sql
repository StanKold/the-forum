-- --------------------------------------------------------
-- Хост:                         127.0.0.1
-- Версия на сървъра:            10.5.18-MariaDB - mariadb.org binary distribution
-- ОС на сървъра:                Win64
-- HeidiSQL Версия:              11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Дъмп на структурата на БД forum
CREATE DATABASE IF NOT EXISTS `forum` /*!40100 DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci */;
USE `forum`;

-- Дъмп структура за таблица forum.contact
CREATE TABLE IF NOT EXISTS `contact` (
  `contact_id` int(11) NOT NULL AUTO_INCREMENT,
  `contact_text` text NOT NULL,
  `contact_email` text NOT NULL,
  `contact_name` text NOT NULL,
  `contact_date` date DEFAULT NULL,
  PRIMARY KEY (`contact_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Изнасянето на данните беше деселектирано.

-- Дъмп структура за таблица forum.dislikes
CREATE TABLE IF NOT EXISTS `dislikes` (
  `post_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  KEY `dislikes_posts_fk` (`post_id`),
  KEY `dislikes_users_fk` (`user_id`),
  CONSTRAINT `dislikes_posts_fk` FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`),
  CONSTRAINT `dislikes_users_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Изнасянето на данните беше деселектирано.

-- Дъмп структура за таблица forum.likes
CREATE TABLE IF NOT EXISTS `likes` (
  `post_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  KEY `Likes_user_fk` (`user_id`),
  KEY `likes_posts_fk` (`post_id`),
  CONSTRAINT `Likes_user_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `likes_posts_fk` FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Изнасянето на данните беше деселектирано.

-- Дъмп структура за таблица forum.phones
CREATE TABLE IF NOT EXISTS `phones` (
  `phone_id` int(11) NOT NULL AUTO_INCREMENT,
  `phone_number` varchar(30) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`phone_id`),
  UNIQUE KEY `phones_pk` (`user_id`),
  CONSTRAINT `phones_users_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Изнасянето на данните беше деселектирано.

-- Дъмп структура за таблица forum.posts
CREATE TABLE IF NOT EXISTS `posts` (
  `post_id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) NOT NULL,
  `content` varchar(8192) NOT NULL,
  `user_id` int(11) NOT NULL,
  `post_created` date DEFAULT NULL,
  PRIMARY KEY (`post_id`),
  KEY `posts_users_fk` (`user_id`),
  CONSTRAINT `posts_users_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Изнасянето на данните беше деселектирано.

-- Дъмп структура за таблица forum.post_tags
CREATE TABLE IF NOT EXISTS `post_tags` (
  `post_tag_id` int(11) NOT NULL AUTO_INCREMENT,
  `tag_id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  PRIMARY KEY (`post_tag_id`),
  UNIQUE KEY `post_tags_pk` (`post_tag_id`),
  KEY `post_tags_posts_fk` (`post_id`),
  KEY `post_tags_tags_fk` (`tag_id`),
  CONSTRAINT `post_tags_posts_fk` FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `post_tags_tags_fk` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`tag_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Изнасянето на данните беше деселектирано.

-- Дъмп структура за таблица forum.post_user_replies
CREATE TABLE IF NOT EXISTS `post_user_replies` (
  `reply_id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(500) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `post_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `created` date DEFAULT NULL,
  PRIMARY KEY (`reply_id`),
  KEY `post_user_replies_posts_fk` (`post_id`),
  KEY `post_user_replies_user_fk` (`user_id`),
  CONSTRAINT `post_user_replies_posts_fk` FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `post_user_replies_user_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- Изнасянето на данните беше деселектирано.

-- Дъмп структура за таблица forum.reactions
CREATE TABLE IF NOT EXISTS `reactions` (
  `reaction_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `post_id` int(11) NOT NULL,
  `type` varchar(50) NOT NULL,
  PRIMARY KEY (`reaction_id`),
  KEY `reactions_posts_fk` (`post_id`),
  KEY `reactions_users_fk` (`user_id`),
  CONSTRAINT `reactions_posts_fk` FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `reactions_users_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=193 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Изнасянето на данните беше деселектирано.

-- Дъмп структура за таблица forum.tags
CREATE TABLE IF NOT EXISTS `tags` (
  `tag_id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(50) NOT NULL,
  PRIMARY KEY (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Изнасянето на данните беше деселектирано.

-- Дъмп структура за таблица forum.users
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(60) NOT NULL,
  `username` varchar(50) NOT NULL,
  `avatar_url` varchar(500) NOT NULL DEFAULT 'none',
  `password` varchar(500) NOT NULL,
  `is_admin` tinyint(4) NOT NULL DEFAULT 0,
  `is_blocked` tinyint(4) NOT NULL DEFAULT 0,
  `created_on` date NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `users_pk2` (`email`),
  UNIQUE KEY `users_pk3` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Изнасянето на данните беше деселектирано.

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
