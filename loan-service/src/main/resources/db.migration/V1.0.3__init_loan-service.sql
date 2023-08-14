use `banking_loan-service`;

DROP TABLE IF EXISTS  loans;
DROP TABLE IF EXISTS  loan_payment;

CREATE TABLE loans(
        loan_id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
        customer_id BIGINT NOT NULL,
        loan_due_date DATE,
        loan_amount DECIMAL(10,2) NOT NULL,
        loan_debt DECIMAL(10,2) NOT NULL
);

CREATE TABLE loan_payment(
        loan_payment_id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
        loan_payment_date DATE,
        payment_amount DECIMAL(10,2) NOT NULL,
        loan_id BIGINT,
        FOREIGN KEY (loan_id) REFERENCES loans(loan_id)
);


