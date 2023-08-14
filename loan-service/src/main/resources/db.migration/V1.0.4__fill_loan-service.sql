use `banking_loan-service`;

INSERT INTO loans(customer_id, loan_due_date, loan_amount, loan_debt)
VALUES
    (1, '2025-01-01', 5000.00, 2500.00),
    (2, '2030-01-01', 15000.00, 25000.00),
    (1, '2035-01-01', 55000.00, 25000.00);

INSERT INTO loan_payment(loan_payment_date, payment_amount, loan_id)
VALUES
    ('2023-09-01', 100.00, 1),
    ('2023-09-10', 1000.00, 2),
    ('2023-09-09', 150.00, 1);

