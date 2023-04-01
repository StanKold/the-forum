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

-- Дъмп данни за таблица forum.contact: ~4 rows (приблизително)
/*!40000 ALTER TABLE `contact` DISABLE KEYS */;
INSERT INTO `contact` (`contact_id`, `contact_text`, `contact_email`, `contact_name`, `contact_date`) VALUES
	(1, 'new test by t-shirt ', 'new@test', 'new test', '2023-03-26'),
	(2, 'atafag af agag  f ', 'stttattt', 'test name', '2023-03-27'),
	(5, 'Your message will be deleted as not important, unless you need one of our T-shirt\'s "bg-tati"', '123456789012', '123456', '2023-03-31'),
	(6, 'iskam da rabotiii taq forma ', 'stanimir@abv', 'stanimir ', '2023-03-31');
/*!40000 ALTER TABLE `contact` ENABLE KEYS */;

-- Дъмп данни за таблица forum.dislikes: ~0 rows (приблизително)
/*!40000 ALTER TABLE `dislikes` DISABLE KEYS */;
INSERT INTO `dislikes` (`post_id`, `user_id`) VALUES
	(1, 1);
/*!40000 ALTER TABLE `dislikes` ENABLE KEYS */;

-- Дъмп данни за таблица forum.likes: ~12 rows (приблизително)
/*!40000 ALTER TABLE `likes` DISABLE KEYS */;
INSERT INTO `likes` (`post_id`, `user_id`) VALUES
	(1, 3),
	(2, 4),
	(3, 2),
	(5, 1),
	(3, 3),
	(1, 4),
	(5, 2),
	(6, 1),
	(8, 2),
	(5, 3),
	(7, 4),
	(11, 3);
/*!40000 ALTER TABLE `likes` ENABLE KEYS */;

-- Дъмп данни за таблица forum.phones: ~2 rows (приблизително)
/*!40000 ALTER TABLE `phones` DISABLE KEYS */;
INSERT INTO `phones` (`phone_id`, `phone_number`, `user_id`) VALUES
	(1, '0888888888', 1),
	(2, '0899999999', 2);
/*!40000 ALTER TABLE `phones` ENABLE KEYS */;

-- Дъмп данни за таблица forum.posts: ~26 rows (приблизително)
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
	(9, 'power socket', 'wall power socket is making noice and smells starnge? is it late for insurance?', 5, '2023-03-08'),
	(10, 'power test10', 'wall power socket is making noice and smells starnge? is it late for insurance?', 4, '2023-03-08'),
	(11, 'test post11', ' test post 10 content just for test.', 5, '2023-03-10'),
	(12, 'test post no num', ' test post no num any more content just for test.', 5, '2023-03-10'),
	(13, 'update post from', 'no Error messages when title or content empty ', 1, '2023-03-23'),
	(14, 'it worked shit ', 'that shit does work, i didn\'t believe it ', 1, '2023-03-23'),
	(15, 'fagaggag sgsfs ', 'adhsfsjg sdjh aha dhadf aha ddh adh aa ', 1, '2023-03-23'),
	(16, 'daha dsgjh fhmg ', 'ag agadg adgadga dgadgf adgag adgdafa ffzfafadgdcgsfgsfg s', 1, '2023-03-23'),
	(17, 'new title post17', 'problem/situation as good as possible, so you get fast and full responses and comments!', 1, '2023-03-23'),
	(18, 'new title23', 'Use meaningful and short title for your post, content should describe the problem/situation as good as possible, so you get fast and full responses and comments! ', 1, '2023-03-23'),
	(19, 'asgahaadh', 'responses and comments! responses and comments! responses and comments! responses and comments! responses and comments! responses and comments! responses and comments! ', 1, '2023-03-23'),
	(20, 'update 24.03', 'edit this morning update post admin2  24.03 edit this morning update post admin2  24.03 edit this morning update post admin2  24.03 edit this \r\n dddasdad addalllllllllll\r\n', 1, '2023-03-24'),
	(21, 'kitchen sink ', 'can anyone share opinion what brand should I looking for, or what to avoid ', 2, '2023-03-29'),
	(22, 'Table legs ', 'Legs of the tables are making marks on th floor, how to remove the marks from the ceramic tails and how to avoid repetition ', 3, '2023-03-29'),
	(23, 'TV mount ', 'tv mount is getting out of the wall constantly, I am afraid I can find th tv broken one day ', 4, '2023-03-29'),
	(24, 'Bed noice ', 'Bed mechanical parts for lifting up the mattrace is making noice and shakes, can I make something to make it stop ', 2, '2023-03-29'),
	(25, 'Dimable lights ', 'I both a dimable swithes for all of the rooms, Issue is that led light go from 90% to 100% only, I cannot use them as needed. With NMH there is no issues.   ', 4, '2023-03-29'),
	(26, 'AC darin ', 'My AC - daikin - have a missing hole for drain, now the drops are going on the building facade, Where and what should i by to fix it ', 3, '2023-03-29');
/*!40000 ALTER TABLE `posts` ENABLE KEYS */;

-- Дъмп данни за таблица forum.post_tags: ~21 rows (приблизително)
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
	(13, 8, 9),
	(14, 1, 5),
	(15, 2, 6),
	(16, 3, 4),
	(17, 4, 2),
	(18, 5, 1),
	(19, 1, 20),
	(20, 27, 20),
	(21, 28, 13),
	(22, 29, 13);
/*!40000 ALTER TABLE `post_tags` ENABLE KEYS */;

-- Дъмп данни за таблица forum.post_user_replies: ~33 rows (приблизително)
/*!40000 ALTER TABLE `post_user_replies` DISABLE KEYS */;
INSERT INTO `post_user_replies` (`reply_id`, `content`, `post_id`, `user_id`, `created`) VALUES
	(1, 'very interesting, let me know when fix it', 1, 3, '2023-03-01'),
	(2, 'why are you asking for so simple thing', 3, 3, '2023-03-05'),
	(3, 'I have the same issue', 4, 2, '2023-03-05'),
	(4, 'Did you sole it already', 2, 3, '2023-03-05'),
	(5, 'I can help you, call me', 4, 2, '2023-03-05'),
	(6, 'Just by something from the store and fix it', 1, 2, '2023-03-05'),
	(7, 'Can you let it as it is? its very expensive', 3, 1, '2023-03-05'),
	(8, 'I have experience and can fix it for you, will cost 50$', 2, 3, '2023-03-08'),
	(9, 'if there is such, they can count on me! I will be there, hard every weekend.', 6, 1, '2023-03-08'),
	(10, 'try add comment localhost:8080', 1, 1, '2023-03-08'),
	(11, 'second comment localhost:8080', 1, 1, '2023-03-08'),
	(12, 'comment agggggaaaing becouse of bug', 1, 1, '2023-03-08'),
	(13, 'and the comments adding form is working too ', 14, 1, '2023-03-09'),
	(14, 'new comment', 13, 1, '2023-03-09'),
	(15, 'additional comment ', 13, 1, '2023-03-09'),
	(16, 'comment', 18, 1, '2023-03-09'),
	(17, 'kokoookoko', 5, 1, '2023-03-09'),
	(18, 'can i touch ? ', 6, 1, '2023-03-10'),
	(19, 'm;hlugkufufuik ', 1, 1, '2023-03-10'),
	(20, 'agadgagagad dg gaSgf as', 3, 1, '2023-03-10'),
	(21, 'dali raboti ', 20, 1, '2023-03-10'),
	(22, 'khfl;fkhlhv,jhvjk', 1, 1, '2023-03-15'),
	(23, 'today ', 1, 1, '2023-03-16'),
	(24, 'comment \r\n', 20, 1, '2023-03-17'),
	(25, 'sfadaf ad aga ag', 1, 1, '2023-03-17'),
	(26, 'sggadggadgfaag', 20, 1, '2023-03-26'),
	(27, 'gggff', 6, 1, '2023-03-26'),
	(28, 'ggggh', 1, 1, '2023-03-26'),
	(29, 'comments new today ', 6, 1, '2023-03-26'),
	(30, 'take  schot ceramics - thats the best ! they have lots of type and size. ', 21, 1, '2023-03-29'),
	(31, 'yes, if they are dirty you can wash. you can try by yourself with dry shampoo for textile or soap, water and sponge. or else go to car wash they know how   ', 7, 1, '2023-03-29'),
	(32, 'take a forklift, they are solid, no worries if something heavy on the road. ', 8, 1, '2023-03-30'),
	(34, 'this shit should  be deleted by admins, are they drunk ? ', 15, 6, '2023-03-31');
/*!40000 ALTER TABLE `post_user_replies` ENABLE KEYS */;

-- Дъмп данни за таблица forum.reactions: ~24 rows (приблизително)
/*!40000 ALTER TABLE `reactions` DISABLE KEYS */;
INSERT INTO `reactions` (`reaction_id`, `user_id`, `post_id`, `type`) VALUES
	(2, 3, 1, 'like'),
	(3, 2, 1, 'like'),
	(4, 2, 2, 'like'),
	(5, 3, 4, 'like'),
	(6, 3, 2, 'like'),
	(8, 2, 4, 'like'),
	(9, 4, 2, 'dislike'),
	(10, 5, 3, 'dislike'),
	(11, 5, 4, 'dislike'),
	(12, 5, 5, 'dislike'),
	(13, 4, 1, 'like'),
	(14, 4, 6, 'dislike'),
	(15, 1, 15, 'dislike'),
	(16, 1, 14, 'dislike'),
	(17, 1, 3, 'like'),
	(18, 1, 2, 'dislike'),
	(19, 1, 5, 'like'),
	(20, 1, 18, 'like'),
	(187, 1, 21, 'like'),
	(188, 1, 7, 'like'),
	(189, 1, 8, 'dislike'),
	(190, 1, 11, 'dislike'),
	(191, 1, 1, 'like'),
	(192, 6, 15, 'dislike');
/*!40000 ALTER TABLE `reactions` ENABLE KEYS */;

-- Дъмп данни за таблица forum.tags: ~28 rows (приблизително)
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
INSERT INTO `tags` (`tag_id`, `content`) VALUES
	(1, 'new'),
	(2, 'important'),
	(3, 'car repair'),
	(4, 'house repair'),
	(5, 'leak'),
	(6, 'ac/dc'),
	(7, 'used car'),
	(8, 'power'),
	(9, '220v'),
	(10, 'poser'),
	(11, 'nosuch'),
	(12, 'drago'),
	(13, 'miro'),
	(14, 'nikola'),
	(15, 'neww'),
	(16, 'workiiing'),
	(17, 'once again'),
	(18, 'workiing'),
	(20, 'addd'),
	(21, 'add'),
	(22, 'delete'),
	(23, 'deeba'),
	(24, 'added'),
	(25, 'top'),
	(26, 'bbbbb'),
	(27, 'tagg'),
	(28, 'new tag '),
	(29, 'error');
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;

-- Дъмп данни за таблица forum.users: ~6 rows (приблизително)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`user_id`, `first_name`, `last_name`, `email`, `username`, `avatar_url`, `password`, `is_admin`, `is_blocked`, `created_on`) VALUES
	(1, 'admin2', 'admin3', 'admin2@abv.bg', 'admin2', 'http:127', '$2a$10$GcHjn/kJY8tBakwwFIJLjOu//tmy6b53zU71WAqVHTDDNXTTN/B/C', 1, 0, '2023-03-26'),
	(2, 'user', 'user', 'user@abv.bg', 'user2', 'http:127', '$2a$10$cdrkgwmTUJntviX.XcqcCeF.ryeVXW0vpbJ0ggmto9ZdhqK/b2zsG', 0, 0, '2023-02-21'),
	(3, 'regular', 'JOhnsons', 'regular@abv.bg', 'regular', 'http:127', '$2a$10$atk7jLCzqBE1kV.shK9KI.TJaN/HDxEs2G1mMWXnZP4FkminLPR0C', 0, 0, '2023-03-26'),
	(4, 'test', 'test', 'testuser@abv.bg', 'test1', 'none', '$2a$10$MHUUF70HUvhfp2J.jg4aNe91UcjfXSIWzLUdCinJ414uG4xz1z8Si', 0, 0, '2023-03-08'),
	(5, 'test2', 'test2', 'testuser2@abv.bg', 'test2', 'none', '$2a$10$ew9U3IPv2uXuamG6kvq9VeDvK1zVaGN9EGWqi75m6HMviUDYNVsVm', 0, 0, '2023-03-08'),
	(6, 'badGay ', 'badGay ', 'badGay1@abv', 'badGay ', '/assets/avatars/328454498_507713451271472_4366547811544314727_n.jpg', '$2a$10$H3QcvnvlHZ0FhrkJHZ2htuwTtze7csD0CsmV6WdvikEIIjShlsKG2', 0, 1, '2023-03-31');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
