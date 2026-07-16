CREATE TABLE business_category_data
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    row_id VARCHAR(36) NOT NULL,

    business_id BIGINT NOT NULL,

    field_key VARCHAR(100),

    field_value TEXT,

    sort_order INT,

    created_at DATETIME,

    updated_at DATETIME
);