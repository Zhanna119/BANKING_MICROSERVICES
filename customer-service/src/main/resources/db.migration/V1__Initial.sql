use bank;

DROP TABLE IF EXISTS customers;

CREATE TABLE customers(
    customer_id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name varchar(50) not null,
    surname varchar(50) not null,
    customer_identity_number BIGINT NOT NULL
);