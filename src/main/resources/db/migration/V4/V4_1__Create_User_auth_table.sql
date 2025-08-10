CREATE TABLE `user_auth` (
    `auth_cd` varchar(20) NOT NULL COMMENT '권한 코드',
    `auth_nm` varchar(30) NOT NULL COMMENT '권한 이름',
    `auth_desc` varchar(200) DEFAULT NULL COMMENT '권한 설명',
    `use_yn` char(1) NOT NULL DEFAULT 'Y' COMMENT '사용 여부',
    `sort_order` int(10) unsigned NOT NULL DEFAULT 1 COMMENT '정렬 순서',
    `created_by` varchar(30) NOT NULL COMMENT '생성자',
    `created_date` datetime NOT NULL COMMENT '생성일시',
    `last_updated_by` varchar(30) NOT NULL COMMENT '마지막으로 업데이트한 사용자',
    `last_updated_date` datetime NOT NULL COMMENT '마지막으로 업데이트된 일시',
    PRIMARY KEY (`auth_cd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='사용자 권한 테이블';

INSERT INTO store_manager.user_auth
(auth_cd, auth_nm, auth_desc, use_yn, sort_order, created_by, created_date, last_updated_by, last_updated_date)
VALUES
('ROLE_ADMIN', 'Administrator', '관리자 권한', 'Y', 1, 'system', NOW(), 'system', NOW()),
('ROLE_MANAGER', 'Store Manager', '매장 매니저 권한', 'Y', 3, 'system', NOW(), 'system', NOW()),
('ROLE_OWNER', 'Store Owner', '매장 소유주 권한', 'Y', 2, 'system', NOW(), 'system', NOW()),
('ROLE_PART_TIMER', 'Store Part Time Employer', '매장 아르바이트생 권한', 'Y', 4, 'system', NOW(), 'system', NOW());
