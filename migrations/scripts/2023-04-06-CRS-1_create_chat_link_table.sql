--liquibase formatted sql
--changeset kirill:3
create table if not exists chat_link
(
    chat_id bigint references chat(id) on update cascade on delete cascade,
    link_id bigint references  link(id) on update cascade on delete cascade,
    primary key (chat_id, link_id)
);