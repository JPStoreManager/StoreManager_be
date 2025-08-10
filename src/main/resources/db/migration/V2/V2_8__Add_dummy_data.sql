INSERT INTO store_manager.store_branch
(branch_cd, branch_nm, branch_desc, address, use_yn, sort_order, created_by, created_date, last_updated_by, last_updated_date)
VALUES
('DUMMY_BRCH', 'dummy branch', 'dummy branch', 'dummy', 'Y', 9999,
'system', '2025-07-13 00:00:00.000', 'system', '2025-07-13 00:00:00.000'),
('SEOCHO', '서초', '점핑파크 서초지점', '서초구', 'Y', 1,
'system', NOW(), 'system', NOW()),
('EUNGAE', '은계', '점핑파크 은계지점', '은계로', 'Y', 2,
'system', NOW(), 'system', NOW())
;