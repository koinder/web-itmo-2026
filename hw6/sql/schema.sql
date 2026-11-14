-- Схема БД проекта. Выполняется автоматически один раз — при создании пустой базы.
-- Каждое изменение структуры (CREATE TABLE, ALTER TABLE, индексы) выполните на своей базе
-- (Adminer или консоль) и допишите тот же SQL в конец этого файла.

CREATE TABLE `User` (
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `login`        VARCHAR(255) NOT NULL,
    `passwordSha`  VARCHAR(255) NOT NULL,
    `creationTime` DATETIME     NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_User_login` (`login`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
ALTER TABLE `User`
ADD COLUMN email VARCHAR(255) NULL;
ALTER TABLE User ADD CONSTRAINT uk_User_email UNIQUE(email);
CREATE TABLE `Event` (
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `userId`        BIGINT   NOT NULL,
    `type`  VARCHAR(6)   NOT NULL,
    `creationTime` DATETIME     NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
ALTER TABLE `Event`
ADD CONSTRAINT `fk_Event_userId`
FOREIGN KEY (`userId`) REFERENCES `User` (`id`);
CREATE TABLE `Talk` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    `sourceUserId` BIGINT NOT NULL,
    `targetUserId` BIGINT NOT NULL,
    `text` TEXT NOT NULL,
    `creationTime` DATETIME NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
ALTER TABLE `Talk`
ADD CONSTRAINT `fk_Talk_sourceUserId`
FOREIGN KEY (`sourceUserId`) REFERENCES `User` (`id`);
ALTER TABLE `Talk`
ADD CONSTRAINT `fk_Talk_targetUserId`
FOREIGN KEY (`targetUserId`) REFERENCES `User` (`id`);
CREATE TABLE `Article` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    `userId` BIGINT NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `text` TEXT NOT NULL,
    `creationTime` DATETIME NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
ALTER TABLE `Article`
ADD CONSTRAINT `fk_Article_userId`
FOREIGN KEY (`userId`) REFERENCES `User` (`id`);
ALTER TABLE `Article`
ADD COLUMN `hidden` BOOLEAN NOT NULL DEFAULT FALSE;