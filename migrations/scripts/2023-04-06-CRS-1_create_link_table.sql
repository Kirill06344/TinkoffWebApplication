--liquibase formatted sql
--changeset kirill:2
create table if not exists link
(
    id bigserial primary key,
    url text unique not null
);