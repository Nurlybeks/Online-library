--liquibase formatted sql

--changeset nurlybek:insert-1
INSERT INTO ROLES(NAME) VALUES ('ROLE_ADMIN'),
                               ('ROLE_USER'),
                               ('ROLE_MANAGER');