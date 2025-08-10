ALTER TABLE store_manager.sales ADD CONSTRAINT sales_store_branch_FK FOREIGN KEY (branch_cd) REFERENCES store_manager.store_branch(branch_cd);

ALTER TABLE store_manager.sales ADD CONSTRAINT user_sales_created_by FOREIGN KEY (created_by) REFERENCES store_manager.`user`(id);
ALTER TABLE store_manager.sales ADD CONSTRAINT user_sales_last_updated_by FOREIGN KEY (last_updated_by) REFERENCES store_manager.`user`(id);

ALTER TABLE store_manager.store_branch ADD CONSTRAINT user_store_branch_created_by FOREIGN KEY (created_by) REFERENCES store_manager.`user`(id);
ALTER TABLE store_manager.store_branch ADD CONSTRAINT user_store_branch_last_updated_by FOREIGN KEY (last_updated_by) REFERENCES store_manager.`user`(id);
