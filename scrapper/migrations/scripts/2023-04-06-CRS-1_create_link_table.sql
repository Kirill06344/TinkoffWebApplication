create table link
(
    id bigserial primary key,
    url text unique not null
);