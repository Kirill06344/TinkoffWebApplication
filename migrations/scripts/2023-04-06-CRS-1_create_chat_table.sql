--liquibase formatted sql
--changeset kirill:1
create table if not exists chat
(
    id bigint primary key check (id > 0)
);