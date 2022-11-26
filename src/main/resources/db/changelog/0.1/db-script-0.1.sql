
CREATE TABLE IF NOT EXISTS product_pricing (
  id SERIAL PRIMARY KEY,
  product_id BIGINT NOT NULL,
  price DECIMAL(7, 2) NOT NULL,
  start TIMESTAMPTZ NOT NULL
);
