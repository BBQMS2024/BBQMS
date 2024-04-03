BEGIN;

CREATE TABLE IF NOT EXISTS service
(
    id        INTEGER PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    tenant_id INTEGER      NOT NULL,
    CONSTRAINT FK_service_tenant FOREIGN KEY (tenant_id) REFERENCES tenant (id)
);

CREATE TABLE IF NOT EXISTS branch
(
    id        INTEGER PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    tenant_id INTEGER      NOT NULL,
    CONSTRAINT FK_branch_tenant FOREIGN KEY (tenant_id) REFERENCES tenant (id)
);

CREATE TABLE IF NOT EXISTS display
(
    id        INTEGER PRIMARY KEY AUTO_INCREMENT,
    name      VARCHAR(255) NOT NULL,
    branch_id INTEGER      NOT NULL,
    CONSTRAINT FK_display_branch FOREIGN KEY (branch_id) REFERENCES branch (id)
);

CREATE TABLE IF NOT EXISTS teller_station
(
    id         INTEGER PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(255) NOT NULL,
    display_id INTEGER      NULL,
    branch_id  INTEGER      NOT NULL,
    CONSTRAINT FK_teller_station_display FOREIGN KEY (display_id) REFERENCES display (id),
    CONSTRAINT FK_teller_station_branch FOREIGN KEY (branch_id) REFERENCES branch (id)
);

CREATE TABLE IF NOT EXISTS branch_group
(
    id   INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS branch_group_branch
(
    id              INTEGER PRIMARY KEY AUTO_INCREMENT,
    branch_group_id INTEGER NOT NULL,
    branch_id       INTEGER NOT NULL,
    CONSTRAINT FK_branchgroupbranch_branchgroup FOREIGN KEY (branch_group_id) REFERENCES branch_group (id),
    CONSTRAINT FK_branchgroupbranch_branch FOREIGN KEY (branch_id) REFERENCES branch (id)
);

CREATE TABLE IF NOT EXISTS branch_group_service
(
    id              INTEGER PRIMARY KEY AUTO_INCREMENT,
    branch_group_id INTEGER NOT NULL,
    service_id      INTEGER NOT NULL,
    CONSTRAINT FK_branchgroupservice_branchgroup FOREIGN KEY (branch_group_id) REFERENCES branch_group (id),
    CONSTRAINT FK_branchgroupservice_service FOREIGN KEY (service_id) REFERENCES service (id)
);

CREATE TABLE IF NOT EXISTS teller_station_service
(
    id                INTEGER PRIMARY KEY AUTO_INCREMENT,
    teller_station_id INTEGER NOT NULL,
    service_id        INTEGER NOT NULL,
    CONSTRAINT FK_tellerstationservice_tellerstation FOREIGN KEY (teller_station_id) REFERENCES teller_station (id),
    CONSTRAINT FK_tellerstationservice_service FOREIGN KEY (service_id) REFERENCES service (id)
);

COMMIT;
