CREATE TABLE business_category_data
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    business_id BIGINT NOT NULL,

    field_key VARCHAR(100),

    field_value TEXT,

    sort_order INT
);