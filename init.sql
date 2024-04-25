CREATE DATABASE IF NOT EXISTS llm_wx DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;;

USE llm_wx;

CREATE TABLE IF NOT EXISTS user_chat_history
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(100),
    content      TEXT,
    answer       MEDIUMTEXT,
    date_created DATETIME,
    msg_id       VARCHAR(32)
);