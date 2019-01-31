CREATE SCHEMA IF NOT EXISTS BASILISK;
CREATE TABLE IF NOT EXISTS BASILISK.BASILISK
(
  id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  received_at TIMESTAMP DEFAULT NOW(),
  word        VARCHAR(32) NOT NULL,
  "when"      TIMESTAMP   NOT NULL
);
