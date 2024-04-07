BEGIN;

ALTER TABLE branch_group
ADD tenant_id INTEGER,
ADD CONSTRAINT FK_branchgroup_tenant FOREIGN KEY (tenant_id) REFERENCES tenant (id);

COMMIT;
