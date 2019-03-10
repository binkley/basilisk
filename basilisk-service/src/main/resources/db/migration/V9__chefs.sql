CREATE TABLE IF NOT EXISTS FLORA.CHEF
(
  id          BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  received_at TIMESTAMP DEFAULT NOW(),
  code        VARCHAR(32) NOT NULL UNIQUE,
  name        VARCHAR(32) NOT NULL UNIQUE
);
