create table if not exists chat_link
(
    id bigint primary key,
    chat_id bigint references chat(id) on update cascade on delete cascade,
    link_id bigint references  link(id) on update cascade on delete cascade
);