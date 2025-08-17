CREATE TABLE `user_store_branch_map` (
    `user_id` varchar(100) NOT NULL COMMENT '계정 아이디',
    `branch_cd` varchar(20) NOT NULL COMMENT '지점 코드',
    `created_by` varchar(30) NOT NULL COMMENT '생성자',
    `created_date` datetime NOT NULL COMMENT '생성일시',
    PRIMARY KEY (`user_id`,`branch_cd`),
    KEY `user_store_branch_map_user_id_IDX` (`user_id`) USING BTREE,
    KEY `user_store_branch_map_branch_cd_IDX` (`branch_cd`) USING BTREE,
    CONSTRAINT `user_store_branch_map_store_branch_FK` FOREIGN KEY (`branch_cd`) REFERENCES `store_branch` (`branch_cd`),
    CONSTRAINT `user_store_branch_map_user_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='user 테이블과 store_branch 테이블의 매핑 테이블';