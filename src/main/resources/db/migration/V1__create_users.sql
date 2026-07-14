CREATE TABLE users
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    full_name VARCHAR(120),

    email VARCHAR(120) UNIQUE,

    phone VARCHAR(20),

    password VARCHAR(255),

    role VARCHAR(20),

    enabled BOOLEAN,

    created_at DATETIME
);