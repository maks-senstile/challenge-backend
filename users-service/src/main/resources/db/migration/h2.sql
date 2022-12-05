-- drop table addresses;
-- drop table users;

create table if not exists users (
    id                  int primary key not null,
    first_name          TEXT   not null
);

create table if not exists addresses (
    id                  int primary key not null,
    user_id             int    not null,
    address_line        TEXT   not null,
    city                TEXT   not null,
    country             TEXT   not null,
    postal_code         TEXT   not null,

    foreign key (user_id) references users(id)
);

merge into users key(id) values ( 1, 'Вася' );
merge into addresses key(id) values ( 1, 1, 'Ленина 10', 'Moscow', 'KG', '111555' );