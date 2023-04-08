create table if not exists link
(
    id bigserial primary key,
    url text unique not null
);