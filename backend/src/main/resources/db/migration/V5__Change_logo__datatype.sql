BEGIN;

ALTER TABLE tenant_logo
    MODIFY base64_logo LONGTEXT;

COMMIT;
