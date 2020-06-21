# site3 데이터베이스가 이미 존재한다면 삭제
DROP DATABASE IF EXISTS site3;
# site 3 데이터베이스 생성
CREATE DATABASE site3;
USE site3;

# 테이블 목록 보여주기
SHOW TABLES;
DESC board;
DESC article;

# 테이블 내용 보기
SELECT *
FROM article;

SELECT *
FROM board;

SELECT *
FROM `member`;
# 테이블 초기화->데이터 1번부터시작함 삭제보다 이걸 추천 
TRUNCATE article;
TRUNCATE board;

# 삭제하기
DELETE FROM article;
DELETE FROM board;

CREATE TABLE `member` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    loginId CHAR(100) NOT NULL UNIQUE,
    loginPw CHAR(100) NOT NULL,
    `name` CHAR(100) NOT NULL
);

CREATE TABLE `board` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    `code` CHAR(100) NOT NULL UNIQUE,
    `name` CHAR(100) NOT NULL
);

CREATE TABLE `article` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    title CHAR(100) NOT NULL,
    `body` CHAR(100) NOT NULL,
    memberId INT(10) UNSIGNED NOT NULL,
    boardId INT(10) UNSIGNED NOT NULL,
    INDEX boardId (`boardId`)
);

CREATE TABLE `articleReply` (
    id INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
    regDate DATETIME NOT NULL,
    `body` CHAR(100) NOT NULL,
    memberId INT(10) UNSIGNED NOT NULL,
    articleId INT(10) UNSIGNED NOT NULL,
    INDEX articleId (`articleId`)
);
