CREATE SCHEMA X;

CREATE TABLE X.TOP
(
    code VARCHAR NOT NULL PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE X.KIND
(
    code     VARCHAR NOT NULL PRIMARY KEY,
    coolness NUMERIC NOT NULL
);

CREATE TABLE X.MIDDLE
(
    code      VARCHAR NOT NULL PRIMARY KEY,
    kind_code VARCHAR,
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
    middle_code VARCHAR NOT NULL
);
