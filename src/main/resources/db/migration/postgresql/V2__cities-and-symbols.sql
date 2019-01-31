CREATE TABLE IF NOT EXISTS BASILISK.CITY
(
  id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS BASILISK.SYMBOL
(
  id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR NOT NULL UNIQUE,
  city BIGINT  NOT NULL REFERENCES BASILISK.CITY (ID)
);
