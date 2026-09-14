CREATE TABLE app_users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(254) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(40) NOT NULL,
    CONSTRAINT chk_app_users_role CHECK (role IN ('CUSTOMER', 'ADMIN'))
);
