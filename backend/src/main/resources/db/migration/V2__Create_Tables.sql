BEGIN;

CREATE TABLE tenant
(
    id              INTEGER AUTO_INCREMENT NOT NULL,
    code            VARCHAR(255)          NOT NULL,
    name            VARCHAR(255)          NULL,
    hq_address      VARCHAR(255)          NULL,
    font            VARCHAR(255)          NOT NULL,
    welcome_message VARCHAR(255)          NOT NULL,
    logo_id         BIGINT                NOT NULL,
    CONSTRAINT pk_tenant PRIMARY KEY (id)
);

CREATE TABLE tenant_logo
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    base64_logo VARCHAR(255)          NULL,
    CONSTRAINT pk_tenantlogo PRIMARY KEY (id)
);

ALTER TABLE tenant
    ADD CONSTRAINT uc_tenant_logo UNIQUE (logo_id);

ALTER TABLE tenant
    ADD CONSTRAINT uc_tenant_code UNIQUE (code);

ALTER TABLE tenant
    ADD CONSTRAINT fk_tenant_on_logo FOREIGN KEY (logo_id) REFERENCES tenant_logo (id);

COMMIT;
