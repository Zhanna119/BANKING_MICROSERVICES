use `banking_creditcard-service`;

DROP TABLE IF EXISTS  credit_cards;
DROP TABLE IF EXISTS  credit_card_payment;

CREATE TABLE credit_card(
        credit_card_id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
        available_limit DECIMAL(10,2) NOT NULL,
        number BIGINT NOT NULL,
        credit_limit DECIMAL(10,2) NOT NULL,
        customer_id BIGINT NOT NULL,
        debt DECIMAL(10,2) NOT NULL,
        expire_date DATE,
        min_payment_amount DECIMAL(10,2) NOT NULL
);

CREATE TABLE credit_card_payment(
        credit_card_payment_id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
        transaction_amount DECIMAL(10,2) NOT NULL,
        payment_date DATE,
        credit_card_id BIGINT NOT NULL,
        FOREIGN KEY (credit_card_id) REFERENCES credit_card(credit_card_id)
);

