--liquibase formatted sql

--changeset adsimka:1
INSERT INTO users (username, email, password, active)
VALUES ('admin', 'admin@mail.ru','admin', true);

--changeset adsimka:2
INSERT INTO user_roles (user_id, roles)
VALUES (1, 'USER'),
       (1, 'ADMIN');