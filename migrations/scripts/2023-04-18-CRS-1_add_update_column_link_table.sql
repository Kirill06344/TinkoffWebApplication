--liquibase formatted sql
--changeset kirill:5
alter table link add column updated_at timestamp;