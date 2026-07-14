CREATE TABLE refresh_token
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    user_id BIGINT NOT NULL,

    token VARCHAR(500),

    expiry_date DATETIME
);