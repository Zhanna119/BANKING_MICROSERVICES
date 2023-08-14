use `banking_account-service`;

INSERT INTO account(currency, current_balance, customer_id, iban)
VALUES
    ('USD', 1250.00, 1, 123456789),
    ('EUR', 2500.00, 2, 456789123),
    ('EUR', 3600.00, 1, 789123456);

INSERT INTO account_activity(description, transaction_amount, transaction_date, account_id)
VALUES
    ('description', 250.00, '2023-07-01', 1),
    ('description', 50.00, '2023-08-01', 2),
    ('description', 255.00, '2023-08-02', 1);

