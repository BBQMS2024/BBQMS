# Create user classes and roles to be used for authorization

BEGIN;

CREATE TABLE IF NOT EXISTS user
(
    id           INTEGER PRIMARY KEY AUTO_INCREMENT,
    email        VARCHAR(255) UNIQUE,
    password     VARCHAR(255),
    phone_number VARCHAR(255) UNIQUE,
    oauth        BOOLEAN,
    tfa_secret   VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS role
(
    id   INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) UNIQUE
);

CREATE TABLE IF NOT EXISTS user_role
(
    id      INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id INTEGER,
    role_id INTEGER,
    CONSTRAINT FK_userrole_user FOREIGN KEY (user_id) REFERENCES User (id),
    CONSTRAINT FK_userrole_role FOREIGN KEY (role_id) REFERENCES Role (id)
);

CREATE INDEX email_idx ON User (email);
CREATE INDEX phone_number_idx ON User (phone_number);

INSERT INTO role(name)
values ('ROLE_SUPER_ADMIN'),
       ('ROLE_STAFF_ADMIN'),
       ('ROLE_BRANCH_ADMIN');

COMMIT;
