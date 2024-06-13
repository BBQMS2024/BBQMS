BEGIN;

# Add deleted column to allow for soft deletion
ALTER TABLE ticket
    ADD COLUMN deleted BOOL DEFAULT FALSE;

COMMIT;
