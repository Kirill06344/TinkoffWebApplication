--liquibase formatted sql
--changeset kirill:6
alter table link add column intr_count  bigint;