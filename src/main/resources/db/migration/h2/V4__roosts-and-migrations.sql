CREATE TABLE IF NOT EXISTS BASILISK.ROOST
(
  id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS BASILISK.MIGRATION
(
  id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS BASILISK.ROOST_MIGRATION
(
  roost     BIGINT NOT NULL, -- REFERENCES BASILISK.ROOST (ID)
  migration BIGINT NOT NULL  -- REFERENCES BASILISK.MIGRATION (ID)
);
