--liquibase formatted sql

--changeset numberless:create_1
CREATE TABLE market.AUTHORS
(
    ID                BIGSERIAL PRIMARY KEY,
    FIRST_NAME        VARCHAR(100) NOT NULL,
    LAST_NAME         VARCHAR(100) NOT NULL,
    DATE_OF_BIRTH     DATE         NOT NULL,
    CITIZENSHIP       VARCHAR(100),
    LANGUAGE_OF_WORKS VARCHAR(100)
);

CREATE TABLE market.books
(
    ID          BIGSERIAL PRIMARY KEY,
    NAME        VARCHAR(100) NOT NULL,
    AUTHOR_ID   BIGINT,
    YEAR        INT          NOT NULL,
    LANGUAGE    VARCHAR(50)  NOT NULL,
    PRICE NUMERIC(10, 2) NOT NULL,
    CURRENCY    VARCHAR(3)   NOT NULL,
    DESCRIPTION TEXT,
    QUANTITY    INT          NOT NULL,

    CONSTRAINT fk_book_author
        FOREIGN KEY (AUTHOR_ID)
            REFERENCES authors (ID)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);




