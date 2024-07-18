--liquibase formatted sql

--changeset adsimka:1
CREATE EXTENSION IF NOT EXISTS pgcrypto;

--changeset adsimka:2
UPDATE users SET password = crypt(password, gen_salt('bf', 8));