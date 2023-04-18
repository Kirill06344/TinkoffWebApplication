--liquibase formatted sql
--changeset kirill:4
alter table link add column checked_at timestamp;