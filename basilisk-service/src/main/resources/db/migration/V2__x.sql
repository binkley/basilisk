CREATE SCHEMA X;

CREATE FUNCTION X.update_sequence_number()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    IF NEW.sequence_number = 0 THEN
        NEW.sequence_number = OLD.sequence_number;
    ELSIF NEW.sequence_number < OLD.sequence_number THEN
        RAISE EXCEPTION 'Upsert is out of date: % (current is %)',
            NEW.sequence_number, OLD.sequence_number
            USING ERRCODE = 'STALE';
    END IF;

    RETURN NEW;
END;
$$;

CREATE FUNCTION update_timestamp()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS
$$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$;

CREATE TABLE X.NEAR
(
    code            VARCHAR     NOT NULL PRIMARY KEY,
    sequence_number BIGINT      NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TRIGGER near_sequence_number_trigger
    BEFORE UPDATE
    ON X.NEAR
    FOR EACH ROW
EXECUTE PROCEDURE X.update_sequence_number();

CREATE TRIGGER near_updated_at_trigger
    BEFORE UPDATE
    ON X.NEAR
    FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();

CREATE FUNCTION X.upsert_near(_code X.NEAR.code%TYPE,
                              _sequence_number X.NEAR.sequence_number%TYPE)
    RETURNS SETOF X.NEAR
    ROWS 1
    LANGUAGE plpgsql
AS
$$
BEGIN
    RETURN QUERY INSERT INTO X.NEAR (code, sequence_number)
        VALUES (_code, _sequence_number)
        ON CONFLICT (code) DO UPDATE
            SET sequence_number = excluded.sequence_number RETURNING *;
END;
$$;

CREATE TABLE X.SIDE
(
    code            VARCHAR     NOT NULL PRIMARY KEY,
    sequence_number BIGINT      NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TRIGGER side_sequence_number_trigger
    BEFORE UPDATE
    ON X.SIDE
    FOR EACH ROW
EXECUTE PROCEDURE X.update_sequence_number();

CREATE TRIGGER side_updated_at_trigger
    BEFORE UPDATE
    ON X.SIDE
    FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();

CREATE FUNCTION X.upsert_side(_code X.SIDE.code%TYPE,
                              _sequence_number X.SIDE.sequence_number%TYPE)
    RETURNS SETOF X.SIDE
    ROWS 1
    LANGUAGE plpgsql
AS
$$
BEGIN
    RETURN QUERY INSERT INTO X.SIDE (code, sequence_number)
        VALUES (_code, _sequence_number)
        ON CONFLICT (code) DO UPDATE
            SET sequence_number = excluded.sequence_number RETURNING *;
END;
$$;

CREATE TABLE X.TOP
(
    code              VARCHAR     NOT NULL PRIMARY KEY,
    name              VARCHAR     NOT NULL UNIQUE,
    planned_near_code VARCHAR REFERENCES X.NEAR (code),
    sequence_number   BIGINT      NOT NULL,
    created_at        TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TRIGGER top_sequence_number_trigger
    BEFORE UPDATE
    ON X.TOP
    FOR EACH ROW
EXECUTE PROCEDURE X.update_sequence_number();

CREATE TRIGGER top_updated_at_trigger
    BEFORE UPDATE
    ON X.TOP
    FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();

CREATE FUNCTION X.upsert_top(_code X.TOP.code%TYPE,
                             _name X.TOP.name%TYPE,
                             _planned_near_code X.TOP.planned_near_code%TYPE,
                             _sequence_number X.TOP.sequence_number%TYPE)
    RETURNS SETOF X.TOP
    ROWS 1
    LANGUAGE plpgsql
AS
$$
BEGIN
    RETURN QUERY INSERT INTO X.TOP (code, name, planned_near_code,
                                    sequence_number)
        VALUES (_code, _name, _planned_near_code, _sequence_number)
        ON CONFLICT (code) DO UPDATE
            SET (name, planned_near_code, sequence_number)
                = (excluded.name, excluded.planned_near_code,
                   excluded.sequence_number) RETURNING *;
END;
$$;

CREATE TABLE X.KIND
(
    code            VARCHAR     NOT NULL PRIMARY KEY,
    coolness        NUMERIC     NOT NULL,
    sequence_number BIGINT      NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TRIGGER kind_sequence_number_trigger
    BEFORE UPDATE
    ON X.KIND
    FOR EACH ROW
EXECUTE PROCEDURE X.update_sequence_number();

CREATE TRIGGER kind_updated_at_trigger
    BEFORE UPDATE
    ON X.KIND
    FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();

CREATE FUNCTION X.upsert_kind(_code X.KIND.code%TYPE,
                              _coolness X.KIND.coolness%TYPE,
                              _sequence_number X.KIND.sequence_number%TYPE)
    RETURNS SETOF X.KIND
    ROWS 1
    LANGUAGE plpgsql
AS
$$
BEGIN
    RETURN QUERY INSERT INTO X.KIND (code, coolness, sequence_number)
        VALUES (_code, _coolness, _sequence_number)
        ON CONFLICT (code) DO UPDATE
            SET (coolness, sequence_number)
                = (excluded.coolness, excluded.sequence_number) RETURNING *;
END;
$$;

CREATE TABLE X.MIDDLE
(
    code            VARCHAR     NOT NULL PRIMARY KEY,
    -- Example of optional relationship
    kind_code       VARCHAR REFERENCES X.KIND (code),
    mid             INT         NOT NULL,
    sequence_number BIGINT      NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TRIGGER middle_sequence_number_trigger
    BEFORE UPDATE
    ON X.MIDDLE
    FOR EACH ROW
EXECUTE PROCEDURE X.update_sequence_number();

CREATE TRIGGER middle_updated_at_trigger
    BEFORE UPDATE
    ON X.MIDDLE
    FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();

CREATE FUNCTION X.upsert_middle(_code X.MIDDLE.code%TYPE,
                                _kind_code X.MIDDLE.kind_code%TYPE,
                                _mid X.MIDDLE.mid%TYPE,
                                _sequence_number X.MIDDLE.sequence_number%TYPE)
    RETURNS SETOF X.MIDDLE
    ROWS 1
    LANGUAGE plpgsql
AS
$$
BEGIN
    RETURN QUERY INSERT INTO X.MIDDLE (code, kind_code, mid, sequence_number)
        VALUES (_code, _kind_code, _mid, _sequence_number)
        ON CONFLICT (code) DO UPDATE
            SET (kind_code, mid, sequence_number)
                = (excluded.kind_code, excluded.mid,
                   excluded.sequence_number) RETURNING *;
END;
$$;

CREATE TABLE X.BOTTOM
(
    -- One bottom to each middle; to each middle, many bottoms
    middle_code VARCHAR NOT NULL REFERENCES X.MIDDLE (code),
    foo         VARCHAR NOT NULL,
    UNIQUE (middle_code, foo)
);

CREATE TABLE X.TOP_SIDE
(
    top_code  VARCHAR NOT NULL REFERENCES X.TOP (code),
    side_code VARCHAR NOT NULL REFERENCES X.SIDE (code),
    UNIQUE (top_code)
);

CREATE TABLE X.TOP_MIDDLE
(
    top_code    VARCHAR NOT NULL REFERENCES X.TOP (code),
    middle_code VARCHAR NOT NULL REFERENCES X.MIDDLE (code),
    UNIQUE (top_code, middle_code)
);

CREATE TABLE X.TOP_NEAR
(
    top_code  VARCHAR NOT NULL REFERENCES X.TOP (code),
    near_code VARCHAR NOT NULL REFERENCES X.NEAR (code),
    UNIQUE (top_code, near_code)
);

CREATE TABLE X.MIDDLE_SIDE
(
    middle_code VARCHAR NOT NULL REFERENCES X.MIDDLE (code),
    side_code   VARCHAR NOT NULL REFERENCES X.SIDE (code),
    UNIQUE (middle_code)
);

CREATE TABLE X.MIDDLE_NEAR
(
    middle_code VARCHAR NOT NULL REFERENCES X.MIDDLE (code),
    near_code   VARCHAR NOT NULL REFERENCES X.NEAR (code),
    UNIQUE (middle_code, near_code)
);

CREATE TABLE X.KIND_NEAR
(
    kind_code VARCHAR NOT NULL REFERENCES X.KIND (code),
    near_code VARCHAR NOT NULL REFERENCES X.NEAR (code),
    UNIQUE (kind_code, near_code)
);
