
CREATE MATERIALIZED VIEW product_price
AS
  SELECT y.product_id AS product_id, y.price AS price, y.end AS end
  FROM (
      SELECT
        x.product_id AS product_id,
        x.price AS price,
        x.end AS end,
        ROW_NUMBER() OVER (PARTITION BY product_id ORDER BY x.end) AS row_num
      FROM (
          SELECT
            pp.product_id AS product_id,
            pp.price AS price,
            LEAD(start) OVER (PARTITION BY pp.product_id ORDER BY pp.start) AS end
           FROM product_pricing pp
           ORDER BY product_id, start
       ) x
       WHERE x.end IS NULL OR x.end > now()
   ) y
   WHERE y.row_num = 1
WITH DATA;

CREATE UNIQUE INDEX product_price_idx ON product_price(product_id);

CREATE FUNCTION refresh_product_price()
  RETURNS TRIGGER LANGUAGE plpgsql
  AS $$
    BEGIN REFRESH MATERIALIZED VIEW CONCURRENTLY product_price;
    RETURN NULL;
  END $$;

CREATE TRIGGER refresh_product_price_after_insert
  AFTER INSERT
  ON product_pricing
  FOR EACH STATEMENT
  EXECUTE PROCEDURE refresh_product_price();
