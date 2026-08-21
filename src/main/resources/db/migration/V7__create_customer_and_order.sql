CREATE TABLE customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    business_id BIGINT NOT NULL,
    name VARCHAR(120) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    total_orders INT NOT NULL DEFAULT 0,
    total_spent DOUBLE NOT NULL DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT uq_customer_business_phone UNIQUE (business_id, phone),
    CONSTRAINT fk_customer_business FOREIGN KEY (business_id) REFERENCES business(id)
);

CREATE TABLE customer_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    business_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    customer_name VARCHAR(120) NOT NULL,
    customer_phone VARCHAR(20) NOT NULL,
    items_json TEXT NOT NULL,
    total_amount DOUBLE NOT NULL,
    dining_in BOOLEAN,
    table_number VARCHAR(50),
    created_at DATETIME,
    updated_at DATETIME,
    CONSTRAINT fk_order_business FOREIGN KEY (business_id) REFERENCES business(id),
    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES customer(id)
);