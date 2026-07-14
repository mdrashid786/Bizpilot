ALTER TABLE business
ADD COLUMN owner_id BIGINT NOT NULL;

ALTER TABLE business
ADD CONSTRAINT fk_business_owner
FOREIGN KEY (owner_id)
REFERENCES users(id);