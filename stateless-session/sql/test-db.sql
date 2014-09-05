DROP DATABASE IF EXISTS `stateless_session`;
CREATE DATABASE  IF NOT EXISTS `stateless_session`;
USE `stateless_session`;

DROP TABLE IF EXISTS `sites`;
CREATE TABLE `sites` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `display_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `site_code` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(255) NOT NULL,
  `encrypted_password` varchar(255) NOT NULL,
  `password_salt` varchar(255) NOT NULL,
  `site_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_site_user` (`site_id`, `login`),
  CONSTRAINT `FK_USER_SITE_ID` FOREIGN KEY (`site_id`) REFERENCES `sites` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS roles;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `users_roles`;
CREATE TABLE `users_roles` (
  `users_id` int(11) DEFAULT NULL,
  `roles_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`users_id`,`roles_id`),
  KEY `FK_USER_ID` (`users_id`),
  KEY `FK_ROLE_ID` (`roles_id`),
  CONSTRAINT `FK_USER_ID` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ROLE_ID` FOREIGN KEY (`roles_id`) REFERENCES `roles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


INSERT INTO `sites`
(`id`,`display_name`,`site_code`)
VALUES
(1,'wds','wds'),(2,'xerox','xerox');

INSERT INTO `users`
(`id`,`login`,`encrypted_password`,`password_salt`,`site_id`)
VALUES
(1,'admin','8048470a0bbd8ec332c8d400e23cdb88ffca278256fa78ef96283b22147ccad6169233edaf04948dbe202ac85f9b4a813b1b9250767e7d89b627b7f8d1a62a49',
'4f1b574119e452d351a521ae1917211832412a',1),
(2,'moderator','72683289d7dfc5560a604b181faa04b4b8147c885314c466a7e252f7e140c7c1bded30b53fb52b6695427e988ba05a967a005e7950dd3d97c7b03176e5d80d94',
'54352b4f2a5317305659ad2d311b48463d2c10',2),
(3,'agent','e416de4102b266e2ae04824e3b5c52ac029064cc1cf42f3a5a18467c78bdc8000a673371fa4bc1110da6352295683a7a06000c28ec25fc4ee0d15e8fed5ff37b',
'3f24251c33464c48e1816212f334ba40223423',1);

INSERT INTO `roles`
(`id`,`name`)
VALUES
(1,'SITE_ADMIN'),
(2,'MODERATOR'),
(3,'AGENT');

INSERT INTO `users_roles`
(`users_id`,`roles_id`)
VALUES
(1,1),
(2,2),
(3,3);