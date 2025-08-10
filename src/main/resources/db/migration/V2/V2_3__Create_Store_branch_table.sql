CREATE TABLE store_manager.store_branch (
    branch_cd varchar(20) NOT NULL COMMENT '지점 코드',
    branch_nm varchar(40) NOT NULL COMMENT '지점 이름',
    branch_desc varchar(100) NULL COMMENT '지점 설명',
    address varchar(200) NOT NULL COMMENT '위치',
    use_yn char(1) DEFAULT 'Y' NOT NULL COMMENT '사용 여부',
    sort_order INT UNSIGNED DEFAULT 1 NULL COMMENT '정렬 순서',
    created_by varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '생성자',
    created_date datetime NOT NULL COMMENT '생성일시',
    last_updated_by varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '마지막으로 업데이트한 사용자',
    last_updated_date datetime NOT NULL COMMENT '마지막으로 업데이트된 일시'
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_general_ci
COMMENT='매장 지점 정보';
