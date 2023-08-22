use `banking_creditcard-service`;

INSERT INTO credit_cards(available_limit, number, credit_limit, customer_id, debt, expire_date, min_payment_amount)
VALUES
    (1250.00, 123456789, 200.00, 1, 2000.00, '2025-07-01', 100.00),
    (1250.00, 456789123, 250.00, 2, 1000.00, '2024-07-01', 100.00),
    (1250.00, 789123456, 300.00, 1, 2500.00, '2025-07-01', 150.00);

INSERT INTO credit_card_payment(transaction_amount, payment_date, credit_card_id)
VALUES
    (250.00, '2023-08-01', 1),
    (350.00, '2023-07-01', 2),
    (450.00, '2023-06-01', 1);
