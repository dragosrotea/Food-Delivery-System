ALTER TABLE app_users
    DROP CONSTRAINT chk_app_users_role;

ALTER TABLE app_users
    ADD CONSTRAINT chk_app_users_role
        CHECK (role IN ('CUSTOMER', 'DRIVER', 'ADMIN'));

ALTER TABLE orders
    ADD COLUMN driver_id BIGINT,
    ADD COLUMN version BIGINT NOT NULL DEFAULT 0;

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_driver
        FOREIGN KEY (driver_id)
        REFERENCES app_users(id);

CREATE INDEX idx_orders_driver_id ON orders(driver_id);
CREATE INDEX idx_orders_available_delivery
    ON orders(status, driver_id);
