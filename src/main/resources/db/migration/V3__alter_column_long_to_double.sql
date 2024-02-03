ALTER TABLE selling
    ALTER COLUMN count
        TYPE double precision
        USING count::double precision;
ALTER TABLE product
    ALTER COLUMN count
        TYPE double precision
        USING count::double precision;
