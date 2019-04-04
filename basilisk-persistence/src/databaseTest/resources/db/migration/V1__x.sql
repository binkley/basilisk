CREATE SCHEMA X;

CREATE TABLE X.SIDE
(
    code VARCHAR     NOT NULL PRIMARY KEY,
    time TIMESTAMPTZ NOT NULL
);

CREATE TABLE X.TOP
(
    code      VARCHAR NOT NULL PRIMARY KEY,
    -- Example of mandatory shared relationship
    side_code VARCHAR NOT NULL REFERENCES X.SIDE (code),
    name      VARCHAR NOT NULL UNIQUE
);

CREATE TABLE X.KIND
(
    code     VARCHAR NOT NULL PRIMARY KEY,
    coolness NUMERIC NOT NULL
);

CREATE TABLE X.MIDDLE
(
    code      VARCHAR NOT NULL PRIMARY KEY,
    -- Example of optional relationship
    kind_code VARCHAR REFERENCES X.KIND (code),
    -- Example of optional shared relationship
    side_code VARCHAR REFERENCES X.SIDE (code),
    mid       INT     NOT NULL
);

CREATE TABLE X.BOTTOM
(
    -- One bottom to each middle; to each middle, many bottoms
    middle_code VARCHAR NOT NULL,
    foo         VARCHAR NOT NULL,
    UNIQUE (middle_code, foo)
);

CREATE TABLE X.TOP_MIDDLE
(
    top_code    VARCHAR NOT NULL,
    middle_code VARCHAR NOT NULL,
    UNIQUE (top_code, middle_code)
);
