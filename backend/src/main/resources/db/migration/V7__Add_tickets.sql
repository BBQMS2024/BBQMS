BEGIN;

CREATE TABLE IF NOT EXISTS ticket
(
    id         INTEGER PRIMARY KEY AUTO_INCREMENT,
    number     INTEGER  NOT NULL,
    created_at DATETIME NOT NULL,
    service_id INTEGER  NOT NULL,
    branch_id  INTEGER  NOT NULL,
    CONSTRAINT FK_ticket_service FOREIGN KEY (service_id) REFERENCES service (id),
    CONSTRAINT FK_ticket_branch FOREIGN KEY (branch_id) REFERENCES branch (id)
);

ALTER TABLE teller_station
    ADD COLUMN active BOOLEAN DEFAULT TRUE; # This should be actually be false, but this way it's easier to test.

COMMIT;
