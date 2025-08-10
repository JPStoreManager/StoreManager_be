ALTER TABLE store_manager.`user` MODIFY COLUMN auth_cd varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '권한 코드';
ALTER TABLE store_manager.`user` ADD CONSTRAINT user_user_auth_FK FOREIGN KEY (auth_cd) REFERENCES store_manager.user_auth(auth_cd);
