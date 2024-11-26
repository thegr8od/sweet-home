-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema sweethome
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema sweethome
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sweethome` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `sweethome` ;

-- -----------------------------------------------------
-- Table `sweethome`.`houseinfos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sweethome`.`houseinfos` (
  `apt_seq` VARCHAR(20) NOT NULL,
  `sgg_cd` VARCHAR(5) NULL DEFAULT NULL,
  `umd_cd` VARCHAR(5) NULL DEFAULT NULL,
  `umd_nm` VARCHAR(20) NULL DEFAULT NULL,
  `jibun` VARCHAR(10) NULL DEFAULT NULL,
  `road_nm_sgg_cd` VARCHAR(5) NULL DEFAULT NULL,
  `road_nm` VARCHAR(20) NULL DEFAULT NULL,
  `road_nm_bonbun` VARCHAR(10) NULL DEFAULT NULL,
  `road_nm_bubun` VARCHAR(10) NULL DEFAULT NULL,
  `apt_nm` VARCHAR(40) NULL DEFAULT NULL,
  `build_year` INT NULL DEFAULT NULL,
  `latitude` VARCHAR(45) NULL DEFAULT NULL,
  `longitude` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`apt_seq`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sweethome`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sweethome`.`user` (
  `id` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NULL DEFAULT NULL,
  `email` VARCHAR(45) NULL DEFAULT NULL,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `age` VARCHAR(45) NULL DEFAULT NULL,
  `date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `token` VARCHAR(255) NULL DEFAULT NULL,
  `is_deleted` TINYINT(1) NULL DEFAULT '0',
  `deleted_at` DATETIME NULL DEFAULT NULL,
  `profile_image` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sweethome`.`aptboard`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sweethome`.`aptboard` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `apt_seq` VARCHAR(20) NOT NULL,
  `user_id` VARCHAR(45) NOT NULL,
  `title` VARCHAR(100) NOT NULL,
  `content` TEXT NOT NULL,
  `likes_count` INT NULL DEFAULT '0',
  `date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL DEFAULT NULL,
  `is_deleted` TINYINT(1) NULL DEFAULT '0',
  `deleted_at` DATETIME NULL DEFAULT NULL,
  `comments_count` INT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `apt_seq` (`apt_seq` ASC) VISIBLE,
  INDEX `user_id` (`user_id` ASC) VISIBLE,
  INDEX `likes_count` (`likes_count` DESC) VISIBLE,
  CONSTRAINT `aptboard_ibfk_1`
    FOREIGN KEY (`apt_seq`)
    REFERENCES `sweethome`.`houseinfos` (`apt_seq`),
  CONSTRAINT `aptboard_ibfk_2`
    FOREIGN KEY (`user_id`)
    REFERENCES `sweethome`.`user` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 52
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sweethome`.`aptboard_comments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sweethome`.`aptboard_comments` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `board_id` INT NOT NULL,
  `user_id` VARCHAR(45) NOT NULL,
  `content` TEXT NOT NULL,
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NULL DEFAULT NULL,
  `is_deleted` TINYINT(1) NULL DEFAULT '0',
  `deleted_at` DATETIME NULL DEFAULT NULL,
  `likes_count` INT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `board_id` (`board_id` ASC) VISIBLE,
  INDEX `user_id` (`user_id` ASC) VISIBLE,
  INDEX `idx_board_user` (`board_id` ASC, `user_id` ASC) VISIBLE,
  CONSTRAINT `aptboard_comments_ibfk_1`
    FOREIGN KEY (`board_id`)
    REFERENCES `sweethome`.`aptboard` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `aptboard_comments_ibfk_2`
    FOREIGN KEY (`user_id`)
    REFERENCES `sweethome`.`user` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 32
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sweethome`.`aptboard_comment_likes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sweethome`.`aptboard_comment_likes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `comment_id` INT NOT NULL,
  `user_id` VARCHAR(45) NOT NULL,
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `unique_comment_like` (`comment_id` ASC, `user_id` ASC) VISIBLE,
  INDEX `user_id` (`user_id` ASC) VISIBLE,
  CONSTRAINT `aptboard_comment_likes_ibfk_1`
    FOREIGN KEY (`comment_id`)
    REFERENCES `sweethome`.`aptboard_comments` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `aptboard_comment_likes_ibfk_2`
    FOREIGN KEY (`user_id`)
    REFERENCES `sweethome`.`user` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 29
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sweethome`.`aptboard_likes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sweethome`.`aptboard_likes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `board_id` INT NOT NULL,
  `user_id` VARCHAR(45) NOT NULL,
  `created_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `unique_like` (`board_id` ASC, `user_id` ASC) VISIBLE,
  INDEX `user_id` (`user_id` ASC) VISIBLE,
  CONSTRAINT `aptboard_likes_ibfk_1`
    FOREIGN KEY (`board_id`)
    REFERENCES `sweethome`.`aptboard` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `aptboard_likes_ibfk_2`
    FOREIGN KEY (`user_id`)
    REFERENCES `sweethome`.`user` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 35
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sweethome`.`common_data`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sweethome`.`common_data` (
  `no` INT NOT NULL AUTO_INCREMENT,
  `interest_rate` DECIMAL(5,2) NULL DEFAULT NULL,
  `date` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`no`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sweethome`.`dongcodes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sweethome`.`dongcodes` (
  `dong_code` VARCHAR(10) NOT NULL,
  `sido_name` VARCHAR(30) NULL DEFAULT NULL,
  `gugun_name` VARCHAR(30) NULL DEFAULT NULL,
  `dong_name` VARCHAR(30) NULL DEFAULT NULL,
  PRIMARY KEY (`dong_code`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sweethome`.`housedeals`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sweethome`.`housedeals` (
  `no` INT NOT NULL AUTO_INCREMENT,
  `apt_seq` VARCHAR(20) NULL DEFAULT NULL,
  `apt_dong` VARCHAR(40) NULL DEFAULT NULL,
  `floor` VARCHAR(3) NULL DEFAULT NULL,
  `deal_year` INT NULL DEFAULT NULL,
  `deal_month` INT NULL DEFAULT NULL,
  `deal_day` INT NULL DEFAULT NULL,
  `exclu_use_ar` DECIMAL(7,2) NULL DEFAULT NULL,
  `deal_amount` VARCHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`no`),
  INDEX `apt_seq_to_house_info_idx` (`apt_seq` ASC) VISIBLE,
  CONSTRAINT `apt_seq_to_house_info`
    FOREIGN KEY (`apt_seq`)
    REFERENCES `sweethome`.`houseinfos` (`apt_seq`))
ENGINE = InnoDB
AUTO_INCREMENT = 7084512
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sweethome`.`interest_api`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sweethome`.`interest_api` (
  `no` INT NOT NULL AUTO_INCREMENT,
  `apt_seq` VARCHAR(20) NULL DEFAULT NULL,
  `user_id` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`no`),
  UNIQUE INDEX `unique_apt_user` (`apt_seq` ASC, `user_id` ASC) VISIBLE,
  INDEX `apt_seq` (`apt_seq` ASC) VISIBLE,
  INDEX `user_id` (`user_id` ASC) VISIBLE,
  CONSTRAINT `interest_api_ibfk_1`
    FOREIGN KEY (`apt_seq`)
    REFERENCES `sweethome`.`houseinfos` (`apt_seq`),
  CONSTRAINT `interest_api_ibfk_2`
    FOREIGN KEY (`user_id`)
    REFERENCES `sweethome`.`user` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 114
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sweethome`.`qna`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sweethome`.`qna` (
  `idx` INT NOT NULL AUTO_INCREMENT,
  `userid` VARCHAR(45) NOT NULL,
  `title` VARCHAR(45) NOT NULL,
  `content` VARCHAR(2000) NOT NULL,
  `date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `answer` VARCHAR(45) NULL DEFAULT NULL,
  `answer_date` DATETIME NULL DEFAULT NULL,
  `is_deleted` TINYINT(1) NULL DEFAULT '0',
  `deleted_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`idx`),
  INDEX `userqna_idx` (`userid` ASC) VISIBLE,
  CONSTRAINT `userqna`
    FOREIGN KEY (`userid`)
    REFERENCES `sweethome`.`user` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 20
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sweethome`.`regional_data`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sweethome`.`regional_data` (
  `no` INT NOT NULL AUTO_INCREMENT,
  `sgg_cd` VARCHAR(5) NULL DEFAULT NULL,
  `gdp_growth` DECIMAL(5,2) NULL DEFAULT NULL,
  `pop_growth` DECIMAL(5,2) NULL DEFAULT NULL,
  PRIMARY KEY (`no`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `sweethome`.`search_log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sweethome`.`search_log` (
  `search_keyword` VARCHAR(100) NOT NULL,
  `search_count` INT NOT NULL DEFAULT '1',
  `date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`search_keyword`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

USE `sweethome`;

DELIMITER $$
USE `sweethome`$$
CREATE
DEFINER=`ssafy`@`%`
TRIGGER `sweethome`.`after_comment_delete`
AFTER DELETE ON `sweethome`.`aptboard_comments`
FOR EACH ROW
BEGIN
    UPDATE aptboard 
    SET comments_count = comments_count - 1 
    WHERE id = OLD.board_id;
END$$

USE `sweethome`$$
CREATE
DEFINER=`ssafy`@`%`
TRIGGER `sweethome`.`after_comment_insert`
AFTER INSERT ON `sweethome`.`aptboard_comments`
FOR EACH ROW
BEGIN
    UPDATE aptboard 
    SET comments_count = comments_count + 1 
    WHERE id = NEW.board_id;
END$$

USE `sweethome`$$
CREATE
DEFINER=`ssafy`@`%`
TRIGGER `sweethome`.`after_comment_like_delete`
AFTER DELETE ON `sweethome`.`aptboard_comment_likes`
FOR EACH ROW
BEGIN
    UPDATE aptboard_comments 
    SET likes_count = likes_count - 1 
    WHERE id = OLD.comment_id;
END$$

USE `sweethome`$$
CREATE
DEFINER=`ssafy`@`%`
TRIGGER `sweethome`.`after_comment_like_insert`
AFTER INSERT ON `sweethome`.`aptboard_comment_likes`
FOR EACH ROW
BEGIN
    UPDATE aptboard_comments 
    SET likes_count = likes_count + 1 
    WHERE id = NEW.comment_id;
END$$

USE `sweethome`$$
CREATE
DEFINER=`ssafy`@`%`
TRIGGER `sweethome`.`after_board_like_delete`
AFTER DELETE ON `sweethome`.`aptboard_likes`
FOR EACH ROW
BEGIN
    UPDATE aptboard 
    SET likes_count = likes_count - 1 
    WHERE id = OLD.board_id;
END$$

USE `sweethome`$$
CREATE
DEFINER=`ssafy`@`%`
TRIGGER `sweethome`.`after_board_like_insert`
AFTER INSERT ON `sweethome`.`aptboard_likes`
FOR EACH ROW
BEGIN
    UPDATE aptboard 
    SET likes_count = likes_count + 1 
    WHERE id = NEW.board_id;
END$$


DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
