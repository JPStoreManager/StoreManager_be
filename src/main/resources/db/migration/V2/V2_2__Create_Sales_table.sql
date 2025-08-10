CREATE TABLE store_manager.sales (
     branch_cd varchar(20) NOT NULL COMMENT '지점 코드',
     regist_date DATE NOT NULL COMMENT '등록일자',
     card_sales INT UNSIGNED DEFAULT 0 NULL COMMENT '카드 매출',
     cash_sales INT UNSIGNED NULL COMMENT '현금 매출',
     created_by varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '생성자',
     created_date datetime NOT NULL COMMENT '생성일시',
     last_updated_by varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '마지막으로 업데이트한 사용자',
     last_updated_date datetime NOT NULL COMMENT '마지막으로 업데이트된 일시'
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_general_ci
COMMENT='매출 정보';
