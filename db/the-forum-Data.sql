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

-- Дъмп данни за таблица forum.phones: ~2 rows (приблизително)
/*!40000 ALTER TABLE `phones` DISABLE KEYS */;
INSERT INTO `phones` (`phone_id`, `phone_number`, `user_id`) VALUES
	(1, '0888888888', 1),
	(2, '0899999999', 2);
/*!40000 ALTER TABLE `phones` ENABLE KEYS */;

-- Дъмп данни за таблица forum.posts: ~9 rows (приблизително)
/*!40000 ALTER TABLE `posts` DISABLE KEYS */;
INSERT INTO `posts` (`post_id`, `title`, `content`, `user_id`, `post_created`) VALUES
	(1, 'Car door repair', 'how to fix not locking door of a car ?', 2, '2023-02-21'),
	(2, 'Leaking sunroof', 'how should I stop sunroof from leaking ?', 3, '2023-02-23'),
	(3, 'Bath tube hole', 'Anyone knows how can I fix a small hole in the bath tube?', 2, '2023-02-24'),
	(4, 'Wardrobe door tilt', 'Hello, one of my wardrobe doors is tilt, hinges are twisted, what should I do ?', 2, '2023-02-28'),
	(5, 'changed title 5', 'changed new title and content for post 5', 3, '2023-03-02'),
	(6, 'Boob car wash', 'Hello, is there near the town one of those nice car wash where a nude girl is playing with the hose, bubbles and spunge ', 3, '2023-03-02'),
	(7, 'carrrrrr', 'Is it a good idea to wash the car seats? any advice ?', 4, '2023-03-02'),
	(8, 'new car', 'I need a small city car, electric preffered, could anyone offer used for less than $8k ?', 4, '2023-03-03'),
	(9, 'power socket', 'wall power socket is making noice and smells starnge? is it late for insurance?', 5, '2023-03-08');
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;

-- Дъмп данни за таблица forum.post_tags: ~12 rows (приблизително)
/*!40000 ALTER TABLE `post_tags` DISABLE KEYS */;
INSERT INTO `post_tags` (`post_tag_id`, `tag_id`, `post_id`) VALUES
	(1, 1, 2),
	(2, 2, 3),
	(3, 3, 1),
	(4, 4, 2),
	(5, 1, 3),
	(7, 3, 4),
	(8, 4, 4),
	(9, 5, 2),
	(10, 6, 8),
	(11, 7, 8),
	(12, 8, 9),
	(13, 8, 9);
/*!40000 ALTER TABLE `post_tags` ENABLE KEYS */;

-- Дъмп данни за таблица forum.post_user_replies: ~9 rows (приблизително)
/*!40000 ALTER TABLE `post_user_replies` DISABLE KEYS */;
INSERT INTO `post_user_replies` (`reply_id`, `content`, `post_id`, `user_id`) VALUES
	(1, 'very interesting, let me know when fix it', 1, 3),
	(2, 'why are you asking for so simple thing', 3, 3),
	(3, 'I have the same issue', 4, 2),
	(4, 'Did you sole it already', 2, 3),
	(5, 'I can help you, call me', 4, 2),
	(6, 'Just by something from the store and fix it', 1, 2),
	(7, 'Can you let it as it is? its very expensive', 3, 1),
	(8, 'I have experience and can fix it for you, will cost 50$', 2, 3),
	(9, 'if there is such, they can count on me! I will be there, hard every weekend.', 6, 1);
/*!40000 ALTER TABLE `post_user_replies` ENABLE KEYS */;

-- Дъмп данни за таблица forum.reactions: ~14 rows (приблизително)
/*!40000 ALTER TABLE `reactions` DISABLE KEYS */;
INSERT INTO `reactions` (`reaction_id`, `user_id`, `post_id`, `type`) VALUES
	(1, 1, 1, 'like'),
	(2, 3, 1, 'like'),
	(3, 2, 1, 'like'),
	(4, 2, 2, 'like'),
	(5, 3, 2, 'like'),
	(6, 3, 2, 'like'),
	(7, 1, 3, 'like'),
	(8, 2, 4, 'like'),
	(9, 4, 2, 'dislike'),
	(10, 5, 3, 'dislike'),
	(11, 5, 4, 'dislike'),
	(12, 5, 5, 'dislike'),
	(13, 4, 6, 'like'),
	(14, 4, 6, 'dislike');
/*!40000 ALTER TABLE `reactions` ENABLE KEYS */;

-- Дъмп данни за таблица forum.tags: ~8 rows (приблизително)
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
INSERT INTO `tags` (`tag_id`, `content`) VALUES
	(1, 'new'),
	(2, 'important'),
	(3, 'car repair'),
	(4, 'house repair'),
	(5, 'leak'),
	(6, 'ac/dc'),
	(7, 'used car'),
	(8, 'power');
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;

-- Дъмп данни за таблица forum.users: ~5 rows (приблизително)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`user_id`, `first_name`, `last_name`, `email`, `username`, `avatar_url`, `password`, `is_admin`, `is_blocked`, `created_on`) VALUES
	(1, 'admin2', 'admin2', 'admin2@abv.bg', 'admin2', 'http:127', '$2a$10$hegev8psTniTW1OujCcXPON4B2VycpyJowrqiy0tsxTvpcb8czKbO', 1, 0, '2023-02-20'),
	(2, 'user', 'user', 'user@abv.bg', 'user2', 'http:127', '$2a$10$cdrkgwmTUJntviX.XcqcCeF.ryeVXW0vpbJ0ggmto9ZdhqK/b2zsG', 0, 0, '2023-02-21'),
	(3, 'regular', 'regular', 'regular@abv.bg', 'regular', 'http:127', '$2a$10$dQHaN3LCFB70rdXJfsF2h.Bh3Sdar/Wtinr/.WsC.aHmloL4paoIi', 0, 0, '2023-02-21'),
	(4, 'test', 'test', 'testuser@abv.bg', 'test1', 'none', '$2a$10$MHUUF70HUvhfp2J.jg4aNe91UcjfXSIWzLUdCinJ414uG4xz1z8Si', 0, 0, '2023-03-08'),
	(5, 'test2', 'test2', 'testuser2@abv.bg', 'test2', 'none', '$2a$10$ew9U3IPv2uXuamG6kvq9VeDvK1zVaGN9EGWqi75m6HMviUDYNVsVm', 0, 0, '2023-03-08');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
