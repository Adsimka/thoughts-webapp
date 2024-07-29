--liquibase formatted sql

--changeset adsimka:1
CREATE TABLE user_subscriptions (
    channel_id BIGINT NOT NULL REFERENCES users,
    subscriber_id BIGINT NOT NULL REFERENCES users,
    PRIMARY KEY (channel_id, subscriber_id)
)