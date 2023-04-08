create table if not exists chat_link
(
    chat_id bigint primary key references chat(id) on update cascade on delete cascade,
    link_id bigint primary key references  link(id) on update cascade on delete cascade
);