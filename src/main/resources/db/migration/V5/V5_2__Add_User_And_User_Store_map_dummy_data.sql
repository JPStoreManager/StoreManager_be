-- user
INSERT INTO store_manager.`user`
(id, password, name, resident_regist_no, phone_no, email, address, auth_cd, work_start_date, work_end_date, work_status_cd, bank_name, bank_account_no, month_salary, hour_wage, otp_no, delete_flag, created_by, created_date, last_updated_by, last_updated_date)
VALUES
('dummy1', '$2a$10$KFuPNo6WTWJ/mbtHP4FThegk/AEyMe9.OrQzGBW5cbIpmFYeQSmEW', 'test1', '1111111111113', '12345678910', 'dummy1@dummy1.com', 'address1', 'ROLE_ADMIN', '1990-01-01', NULL, 'W', 'system', 'system', NULL, NULL, NULL, 'N', 'system', '2025-07-13 00:00:00.000', 'system', '2025-07-13 00:00:00.000'),
('dummy2', '$2a$10$KFuPNo6WTWJ/mbtHP4FThegk/AEyMe9.OrQzGBW5cbIpmFYeQSmEW', 'test2', '1111111111114', '12345678911', 'dummy2@dummy2.com', 'address2', 'ROLE_OWNER', '1990-01-02', NULL, 'W', 'system', 'system', NULL, NULL, NULL, 'N', 'system', '2025-07-13 00:00:00.000', 'system', '2025-07-13 00:00:00.000'),
('dummy3', '$2a$10$KFuPNo6WTWJ/mbtHP4FThegk/AEyMe9.OrQzGBW5cbIpmFYeQSmEW', 'test3', '1111111111115', '12345678912', 'dummy3@dummy3.com', 'address3', 'ROLE_PART_TIMER', '1990-01-03', NULL, 'W', 'system', 'system', NULL, NULL, NULL, 'N', 'system', '2025-07-13 00:00:00.000', 'system', '2025-07-13 00:00:00.000'),
('dummy4', '$2a$10$KFuPNo6WTWJ/mbtHP4FThegk/AEyMe9.OrQzGBW5cbIpmFYeQSmEW', 'test4', '1111111111116', '12345678913', 'dummy4@dummy4.com', 'address4', 'ROLE_MANAGER', '1990-01-04', NULL, 'W', 'system', 'system', NULL, NULL, NULL, 'Y', 'system', '2025-07-13 00:00:00.000', 'system', '2025-07-13 00:00:00.000'),
('dummy5', '$2a$10$KFuPNo6WTWJ/mbtHP4FThegk/AEyMe9.OrQzGBW5cbIpmFYeQSmEW', 'test5', '1111111111117', '12345678914', 'dummy5@dummy5.com', 'address5', 'ROLE_PART_TIMER', '1990-01-05', NULL, 'W', 'system', 'system', NULL, NULL, NULL, 'Y', 'system', '2025-07-13 00:00:00.000', 'system', '2025-07-13 00:00:00.000');

-- store_branch
DELETE FROM store_manager.store_branch
WHERE branch_cd='DUMMY_BRCH';
INSERT INTO store_manager.store_branch
(branch_cd, branch_nm, branch_desc, address, use_yn, sort_order, created_by, created_date, last_updated_by, last_updated_date)
VALUES
('DUMMY_BRCH1', 'dummy branch1', 'dummy branch1', 'address1', 'Y', 9999, 'system', '2025-07-13 00:00:00.000', 'system', '2025-07-13 00:00:00.000'),
('DUMMY_BRCH2', 'dummy branch2', 'dummy branch2', 'address2', 'Y', 9999, 'system', '2025-07-13 00:00:00.000', 'system', '2025-07-13 00:00:00.000'),
('DUMMY_BRCH3', 'dummy branch3', 'dummy branch3', 'address3', 'Y', 9999, 'system', '2025-07-13 00:00:00.000', 'system', '2025-07-13 00:00:00.000'),
('DUMMY_BRCH4', 'dummy branch4', 'dummy branch4', 'address4', 'N', 9999, 'system', '2025-07-13 00:00:00.000', 'system', '2025-07-13 00:00:00.000');

-- user_store_branch_map
INSERT INTO store_manager.user_store_branch_map
(user_id, branch_cd, created_by, created_date)
VALUES
('dummy1', 'DUMMY_BRCH1', 'system', now()),
('dummy1', 'DUMMY_BRCH2', 'system', now()),
('dummy1', 'DUMMY_BRCH3', 'system', now()),
('dummy2', 'DUMMY_BRCH2', 'system', now()),
('dummy4', 'DUMMY_BRCH1', 'system', now()),
('dummy2', 'DUMMY_BRCH4', 'system', now());