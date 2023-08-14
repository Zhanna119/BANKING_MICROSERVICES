use `banking_account-service`;

CREATE TABLE account(
                        account_id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                        currency VARCHAR(50) NOT NULL,
                        current_balance DECIMAL(10,2) NOT NULL,
                        customer_id BIGINT NOT NULL,
                        iban VARCHAR(50) NOT NULL
);

CREATE TABLE account_activity(
                        account_activity_id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                        description VARCHAR(255) NOT NULL,
                        transaction_amount DECIMAL(10,2) NOT NULL,
                        transaction_date DATE,
                        account_id BIGINT NOT NULL,
                        FOREIGN KEY (account_id) REFERENCES account(account_id)
);

