CREATE TABLE store_manager.common_code (
   grp_cd varchar(20) NOT NULL COMMENT '그룹 코드',
   grp_cd_desc varchar(100) NOT NULL COMMENT '그룹 코드 설명',
   cd varchar(30) NOT NULL COMMENT '코드',
   cd_val varchar(100) NOT NULL COMMENT '코드값',
   cd_desc varchar(200) NULL COMMENT '코드 설명',
   use_yn char(1) DEFAULT 'Y' NOT NULL COMMENT '사용 여부',
   sort_order INT UNSIGNED DEFAULT 1 NOT NULL COMMENT '정렬 순서',
   created_by varchar(100) NOT NULL COMMENT '생성자',
   created_date DATETIME NOT NULL COMMENT '생성일시',
   last_updated_by varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '마지막으로 업데이트한 사용자',
   last_updated_date datetime NOT NULL COMMENT '마지막으로 업데이트된 일시',
   CONSTRAINT common_code_pk PRIMARY KEY (grp_cd,cd,cd_val),
   CONSTRAINT common_code_user_FK FOREIGN KEY (created_by) REFERENCES store_manager.`user`(id),
   CONSTRAINT common_code_user_FK_1 FOREIGN KEY (last_updated_by) REFERENCES store_manager.`user`(id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_general_ci
COMMENT='공통 코드 테이블';
