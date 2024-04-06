BEGIN;

CREATE TABLE IF NOT EXISTS ticket
(
    id         INTEGER PRIMARY KEY AUTO_INCREMENT,
    number     INTEGER  NOT NULL,
    created_at DATETIME NOT NULL,
    service_id INTEGER  NOT NULL,
    CONSTRAINT FK_ticket_service FOREIGN KEY (service_id) REFERENCES service (id)
);

ALTER TABLE teller_station
    ADD COLUMN active BOOLEAN DEFAULT TRUE; # Ovo bi zapravo trebalo biti default false, ali ovako je lakse za testiranje

COMMIT;
