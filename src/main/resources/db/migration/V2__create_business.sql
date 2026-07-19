CREATE TABLE business
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    business_name VARCHAR(150) NOT NULL,

    owner_id BIGINT NOT NULL,

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

    tagline VARCHAR(200),

    business_hours TEXT,

    instagram_url VARCHAR(255),

    facebook_url VARCHAR(255),

    published BOOLEAN DEFAULT FALSE,

    created_at DATETIME,

    updated_at DATETIME,

    CONSTRAINT fk_business_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);