--liquibase formatted sql

--changeset nurlybek:insert-1
insert into roles(name) values ('role_admin'),
                               ('role_user'),
                               ('role_manager');