CREATE SCHEMA FLORA;

CREATE TABLE FLORA.CHEF
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    received_at TIMESTAMP DEFAULT NOW(),
    code        VARCHAR NOT NULL UNIQUE,
    name        VARCHAR NOT NULL UNIQUE
);

CREATE TABLE FLORA.RECIPE
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    received_at TIMESTAMP DEFAULT NOW(),
    code        VARCHAR NOT NULL UNIQUE,
    name        VARCHAR NOT NULL UNIQUE,
    chef_id     BIGINT  NOT NULL
);

CREATE TABLE FLORA.SOURCE
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    code        VARCHAR NOT NULL UNIQUE,
    received_at TIMESTAMP DEFAULT NOW(),
    name        VARCHAR NOT NULL UNIQUE
);

CREATE TABLE FLORA.LOCATION
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    code        VARCHAR NOT NULL UNIQUE,
    received_at TIMESTAMP DEFAULT NOW(),
    name        VARCHAR NOT NULL UNIQUE
);

CREATE TABLE FLORA.INGREDIENT
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    received_at TIMESTAMP DEFAULT NOW(),
    code        VARCHAR NOT NULL UNIQUE,
    source_id   BIGINT  NOT NULL,
    name        VARCHAR NOT NULL,
    quantity    NUMERIC NOT NULL,
    chef_id     BIGINT  NOT NULL,
    recipe_id   BIGINT
);

CREATE TABLE FLORA.SOURCE_LOCATION
(
    source_id   BIGINT NOT NULL,
    location_id BIGINT NOT NULL
);
