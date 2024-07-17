--liquibase formatted sql

--changeset adsimka:1
CREATE TABLE message (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    image VARCHAR(255),
    tag VARCHAR(255),
    text VARCHAR(2048)
)

--changeset adsimka:2
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    roles VARCHAR(255)
)

--changeset adsimka:3
CREATE TABLE users (
    active BOOLEAN NOT NULL,
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    verification_token VARCHAR(255)
)

--changeset adsimka:4
ALTER TABLE IF EXISTS message
   ADD CONSTRAINT message_users_fk
   FOREIGN KEY (user_id)
   REFERENCES users

--changeset adsimka:5
ALTER TABLE IF EXISTS user_roles
   ADD CONSTRAINT user_roles_fk
   FOREIGN KEY (user_id)
   REFERENCES users
