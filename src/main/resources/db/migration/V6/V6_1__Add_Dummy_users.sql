INSERT INTO store_manager.`user`
(id, password, name, resident_regist_no, phone_no, email, address, auth_cd, work_start_date, work_end_date, work_status_cd, bank_name, bank_account_no, month_salary, hour_wage, otp_no, delete_flag, created_by, created_date, last_updated_by, last_updated_date)
VALUES
('dummy_admin', '$2a$10$GVwmEUDc5JC4axuRf7pD8OL4Dj9n6iAD5rGCivsaSvJ3Msg.OOjO2', 'test5', '1111111111118', '12345678915', 'dummy6@dummy6.com', 'address6', 'ROLE_ADMIN', '1990-01-05', NULL, 'W', 'system', 'system', NULL, NULL, NULL, 'N', 'system', now(), 'system', now()),
('dummy_owner', '$2a$10$GVwmEUDc5JC4axuRf7pD8OL4Dj9n6iAD5rGCivsaSvJ3Msg.OOjO2', 'test6', '1111111111119', '12345678916', 'dummy7@dummy7.com', 'address7', 'ROLE_OWNER', '1990-01-05', NULL, 'W', 'system', 'system', NULL, NULL, NULL, 'N', 'system', now(), 'system', now()),
('dummy_manager', '$2a$10$GVwmEUDc5JC4axuRf7pD8OL4Dj9n6iAD5rGCivsaSvJ3Msg.OOjO2', 'test7', '1111111111120', '12345678917', 'dummy8@dummy8.com', 'address8', 'ROLE_MANAGER', '1990-01-05', NULL, 'W', 'system', 'system', NULL, NULL, NULL, 'N', 'system', now(), 'system', now()),
('dummy_partTimer', '$2a$10$GVwmEUDc5JC4axuRf7pD8OL4Dj9n6iAD5rGCivsaSvJ3Msg.OOjO2', 'test8', '1111111111121', '12345678918', 'dummy9@dummy9.com', 'address9', 'ROLE_PART_TIMER', '1990-01-05', NULL, 'W', 'system', 'system', NULL, NULL, NULL, 'N', 'system', now(), 'system', now());


INSERT INTO store_manager.user_store_branch_map
(user_id, branch_cd, created_by, created_date)
VALUES
('dummy_admin', 'DUMMY_BRCH1', 'system', now()),
('dummy_admin', 'DUMMY_BRCH2', 'system', now()),
('dummy_admin', 'DUMMY_BRCH3', 'system', now()),
('dummy_owner', 'DUMMY_BRCH1', 'system', now()),
('dummy_manager', 'DUMMY_BRCH1', 'system', now()),
('dummy_partTimer', 'DUMMY_BRCH1', 'system', now());
