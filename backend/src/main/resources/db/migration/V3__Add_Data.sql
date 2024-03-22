BEGIN;

-- INSERT statements for the `role` table
INSERT INTO `role` (`role`) VALUES ('Admin');
INSERT INTO `role` (`role`) VALUES ('User');
INSERT INTO `role` (`role`) VALUES ('Guest');

INSERT INTO tenant_logo (base64_logo) VALUES ('dummy_logo_1');
INSERT INTO tenant_logo (base64_logo) VALUES ('dummy_logo_2');
INSERT INTO tenant_logo (base64_logo) VALUES ('dummy_logo_3');

INSERT INTO tenant (code, name, hq_address, font, welcome_message, logo_id)
VALUES ('T001', 'Tenant 1', 'HQ Address 1', 'Arial', 'Welcome to Tenant 1', 1);

INSERT INTO tenant (code, name, hq_address, font, welcome_message, logo_id)
VALUES ('T002', 'Tenant 2', 'HQ Address 2', 'Times New Roman', 'Welcome to Tenant 2', 2);

INSERT INTO tenant (code, name, hq_address, font, welcome_message, logo_id)
VALUES ('T003', 'Tenant 3', 'HQ Address 3', 'Calibri', 'Welcome to Tenant 3', 3);

INSERT INTO user (email, phone_number, auth_method, tenant_id)
VALUES ('user1@example.com', '1234567890', 'email', 1);

INSERT INTO user (email, phone_number, auth_method, tenant_id)
VALUES ('user2@example.com', '9876543210', 'phone', 2);

INSERT INTO user (email, phone_number, auth_method, tenant_id)
VALUES ('user3@example.com', '5555555555', 'email', 3);

INSERT INTO user_role (role_id, user_id) VALUES (1, 1);
INSERT INTO user_role (role_id, user_id) VALUES (1, 2);
INSERT INTO user_role (role_id, user_id) VALUES (2, 2);
INSERT INTO user_role (role_id, user_id) VALUES (3, 3);

COMMIT;