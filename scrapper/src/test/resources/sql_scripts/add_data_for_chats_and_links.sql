INSERT INTO chat(id) values (1) on conflict do nothing ;
INSERT INTO chat(id) values (2) on conflict do nothing;
INSERT INTO chat(id) values (3) on conflict do nothing;

INSERT INTO link(id, url) values (1, 'https://stackoverflow.com/questions/22234') on conflict do nothing;
INSERT INTO link(id, url) values (3, 'https://stackoverflow.com/questions/22235') on conflict do nothing;

