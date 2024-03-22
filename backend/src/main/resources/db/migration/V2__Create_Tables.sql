BEGIN;

CREATE TABLE `role`
(
    id     BIGINT AUTO_INCREMENT NOT NULL,
    `role` VARCHAR(255)          NULL,
    CONSTRAINT pk_role PRIMARY KEY (id)
);

CREATE TABLE tenant
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    code            VARCHAR(255)          NULL,
    name            VARCHAR(255)          NULL,
    hq_address      VARCHAR(255)          NULL,
    font            VARCHAR(255)          NULL,
    welcome_message VARCHAR(255)          NULL,
    logo_id         BIGINT                NULL,
    CONSTRAINT pk_tenant PRIMARY KEY (id)
);

CREATE TABLE tenant_logo
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    base64_logo VARCHAR(255)          NULL,
    CONSTRAINT pk_tenantlogo PRIMARY KEY (id)
);

CREATE TABLE user
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    email        VARCHAR(255)          NULL,
    phone_number VARCHAR(255)          NULL,
    auth_method  VARCHAR(255)          NULL,
    tenant_id    BIGINT                NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL
);

ALTER TABLE tenant
    ADD CONSTRAINT uc_tenant_logo UNIQUE (logo_id);

ALTER TABLE user
    ADD CONSTRAINT uc_user_tenant UNIQUE (tenant_id);

ALTER TABLE tenant
    ADD CONSTRAINT FK_TENANT_ON_LOGO FOREIGN KEY (logo_id) REFERENCES tenant_logo (id);

ALTER TABLE user
    ADD CONSTRAINT FK_USER_ON_TENANT FOREIGN KEY (tenant_id) REFERENCES tenant (id);

ALTER TABLE user_role
    ADD CONSTRAINT fk_userrole_on_role FOREIGN KEY (role_id) REFERENCES `role` (id);

ALTER TABLE user_role
    ADD CONSTRAINT fk_userrole_on_user FOREIGN KEY (user_id) REFERENCES user (id);

COMMIT;