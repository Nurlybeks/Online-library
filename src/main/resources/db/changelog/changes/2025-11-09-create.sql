--liquibase formatted sql

--changeset nurlybek:create_1
create schema if not exists market;

create table market.roles
(
    id          bigserial primary key,
    name        varchar(255) not null,
    description varchar(255)
);

create table market.users
(
    id bigserial primary key,
    username varchar(255) not null unique ,
    password_hash varchar(255) not null ,
    full_name varchar(255) not null ,
    birthdate date
);

create table market.user_roles(
                                  user_id bigint not null ,
                                  role_id bigint not null ,
                                  foreign key (user_id) references market.users(id) on delete cascade ,
                                  foreign key (role_id) references market.roles(id) on delete cascade
)
