CREATE SCHEMA X;

CREATE TABLE X.NEAR
(
    code VARCHAR NOT NULL PRIMARY KEY
);

CREATE TABLE X.TOP
(
    code              VARCHAR NOT NULL PRIMARY KEY,
    -- Example of mandatory shared relationship
    name              VARCHAR NOT NULL UNIQUE,
    -- Example of optional shared relationships
    planned_near_code VARCHAR REFERENCES X.NEAR (code)
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
    mid       INT     NOT NULL
);

CREATE TABLE X.BOTTOM
(
    -- One bottom to each middle; to each middle, many bottoms
    middle_code VARCHAR NOT NULL REFERENCES X.MIDDLE (code),
    foo         VARCHAR NOT NULL,
    UNIQUE (middle_code, foo)
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
