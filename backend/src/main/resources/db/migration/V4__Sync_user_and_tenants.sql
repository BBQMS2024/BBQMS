BEGIN;

ALTER TABLE User
    ADD tenant_id BIGINT;

ALTER TABLE User
    ADD CONSTRAINT FK_user_tenant FOREIGN KEY (tenant_id) REFERENCES tenant (id);

INSERT INTO tenant_logo (id, base64_logo)
VALUES (1, null);

INSERT INTO tenant (code, name, hq_address, font, welcome_message, logo_id)
VALUES ('DFLT', 'Default', 'Default', 'Arial', 'Welcome to BBQMS', 1);

COMMIT;
