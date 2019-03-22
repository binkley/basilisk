CREATE SCHEMA X;

CREATE TABLE X.TOP
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE X.MIDDLE
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    mid INT NOT NULL
);

CREATE TABLE X.BOTTOM
(
    middle_id BIGINT  NOT NULL,
    foo       VARCHAR NOT NULL
);

CREATE TABLE X.TOP_MIDDLE
(
    top_id    BIGINT NOT NULL,
    middle_id BIGINT NOT NULL
);
