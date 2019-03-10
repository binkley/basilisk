-- This is unfortunate.  We never want to use scripts like this in database or
-- integration tests; however, contract tests cross the HTTP client-server
-- boundary, so there is no nice way to ask the controller to rollback.

-- noinspection SqlWithoutWhereForFile
DELETE
FROM BASILISK.BASILISK;
DELETE
FROM FLORA.INGREDIENT;
DELETE
FROM FLORA.RECIPE;
