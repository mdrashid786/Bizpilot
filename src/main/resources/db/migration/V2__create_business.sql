CREATE TABLE business
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    owner_id BIGINT NOT NULL,

    business_name VARCHAR(150) NOT NULL,

    slug VARCHAR(120) UNIQUE NOT NULL,

    category VARCHAR(50) NOT NULL,

    theme VARCHAR(100) NOT NULL,

    description TEXT,

    phone VARCHAR(20),

    whatsapp VARCHAR(20),

    email VARCHAR(120),

    address TEXT,

    google_map TEXT,

    logo VARCHAR(255),

    cover_image VARCHAR(255),

    published BOOLEAN DEFAULT FALSE,

    created_at DATETIME,

    updated_at DATETIME
);