BEGIN;

INSERT INTO tenant_logo (base64_logo) VALUES ('dummy_logo_1');
INSERT INTO tenant_logo (base64_logo) VALUES ('dummy_logo_2');
INSERT INTO tenant_logo (base64_logo) VALUES ('dummy_logo_3');

INSERT INTO tenant (code, name, hq_address, font, welcome_message, logo_id)
VALUES ('T001', 'Tenant 1', 'HQ Address 1', 'Arial', 'Welcome to Tenant 1', 1);

INSERT INTO tenant (code, name, hq_address, font, welcome_message, logo_id)
VALUES ('T002', 'Tenant 2', 'HQ Address 2', 'Times New Roman', 'Welcome to Tenant 2', 2);

INSERT INTO tenant (code, name, hq_address, font, welcome_message, logo_id)
VALUES ('T003', 'Tenant 3', 'HQ Address 3', 'Calibri', 'Welcome to Tenant 3', 3);

COMMIT;