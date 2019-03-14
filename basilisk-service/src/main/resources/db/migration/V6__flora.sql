CREATE SCHEMA IF NOT EXISTS FLORA;

CREATE TABLE IF NOT EXISTS FLORA.CHEF
(
  id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  received_at TIMESTAMP DEFAULT NOW(),
  name        VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS FLORA.RECIPE
(
  id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  received_at TIMESTAMP DEFAULT NOW(),
  name        VARCHAR NOT NULL UNIQUE,
  chef_id     BIGINT  NOT NULL
);

CREATE TABLE IF NOT EXISTS FLORA.SOURCE
(
  id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  received_at TIMESTAMP DEFAULT NOW(),
  name        VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS FLORA.INGREDIENT
(
  id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  received_at TIMESTAMP DEFAULT NOW(),
  name        VARCHAR NOT NULL,
  quantity    NUMERIC NOT NULL,
  chef_id     BIGINT  NOT NULL,
  recipe_id   BIGINT
);
