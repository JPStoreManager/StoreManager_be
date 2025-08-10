ALTER TABLE store_manager.sales ADD comment varchar(300) NULL COMMENT '비고';
ALTER TABLE store_manager.sales CHANGE comment comment varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL NULL COMMENT '비고' AFTER cash_sales;
