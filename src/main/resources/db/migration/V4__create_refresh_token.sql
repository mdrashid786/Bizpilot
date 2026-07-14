CREATE TABLE refresh_token (

    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    token VARCHAR(500) NOT NULL UNIQUE,

    user_id BIGINT NOT NULL,

    device_id VARCHAR(200) NOT NULL,

    device_name VARCHAR(200) NOT NULL,

    ip_address VARCHAR(100) NOT NULL,

    expiry_date DATETIME NOT NULL,

    revoked BOOLEAN NOT NULL DEFAULT FALSE,

    created_at DATETIME,

    updated_at DATETIME,

    CONSTRAINT fk_refresh_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);