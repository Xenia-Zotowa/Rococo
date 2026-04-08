-- Seed data for rococo-gateway

-- 1. Seed Artists
-- We use UUIDs that match the ones used in WireMock for consistency during testing
set @artist_id_renoir = UUID_TO_BIN('19bbbbb8-b687-4eec-8ba0-c8917c0a58a3', true);
set @artist_id_levitan = UUID_TO_BIN('5a486b2f-c361-459e-bd3f-60692a635ea9', true);

insert into `artist` (id, name, biography, photo)
values
(@artist_id_renoir, 'Ренуар', 'Французский живописец, график и скульптор, один из основных представителей импрессионизма.', NULL),
(@artist_id_levitan, 'Левитан', 'Русский живописец, один из крупнейших мастеров реалистического пейзажа.', NULL)
on duplicate key update name=values(name);

-- 2. Seed Museums
-- We need the ID from the country table. Let's find Russia's ID.
set @russia_id = (select id from `country` where name = 'Россия' limit 1);

set @museum_id_tretyakov = UUID_TO_BIN('3b785453-0d5b-4328-8380-5f226cb4dd5a', true);

insert into `museum` (id, title, description, city, photo, country_id)
values
(@museum_id_tretyakov, 'Третьяковка', 'Государственная Третьяковская галерея — российский государственный художественный музей.', 'Москва', NULL, @russia_id)
on duplicate key update title=values(title);

-- 3. Seed Paintings
set @painting_id_nude = UUID_TO_BIN('40433774-e548-4504-86ab-cafd25c6abca', true);
set @painting_id_levitan_peace = UUID_TO_BIN('9450ff00-0c8c-4a37-9eeb-a1d0b5c3d85b', true);

insert into `painting` (id, title, description, artist_id, museum_id, content)
values
(@painting_id_nude, 'Female nude', 'Картина «Обнаженная» была написана Пьером Ренуаром в 1876 году.', @artist_id_renoir, @museum_id_tretyakov, NULL),
(@painting_id_levitan_peace, 'Над вечным покоем', 'Пейзаж русского художника Исаака Левитана.', @artist_id_levitan, @museum_id_tretyakov, NULL)
on duplicate key update title=values(title);