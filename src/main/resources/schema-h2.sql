-- Example: Copy to a `schema-postgresql.sql` for Postgres-specific syntax
-- See `spring.datasource.platform` in `application.yml`
CREATE SCHEMA IF NOT EXISTS BASILISK;
CREATE TABLE IF NOT EXISTS BASILISK.BASILISKS(
  id IDENTITY PRIMARY KEY AUTO_INCREMENT,
  received_at TIMESTAMP AS CURRENT_TIMESTAMP(),
  word VARCHAR(32) NOT NULL,
  when TIMESTAMP NOT NULL
);
