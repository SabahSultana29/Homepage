
-- Schema for Credit Card Management System
-- Customers Table
CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

-- Credit Card Offers Table
CREATE TABLE credit_card_offers (
    id SERIAL PRIMARY KEY,
    card_type VARCHAR(50) NOT NULL,
    credit_limit NUMERIC(15,2) NOT NULL,
    description TEXT
);

-- Credit Card Accounts Table
CREATE TABLE credit_card_accounts (
    id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    card_number VARCHAR(20) UNIQUE NOT NULL,
    card_type VARCHAR(50) NOT NULL,
    credit_limit NUMERIC(15,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

-- Email Log Table
CREATE TABLE email_log (
    id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    email VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

-- Print Shop Requests Table
CREATE TABLE printing_shop_requests (
    id SERIAL PRIMARY KEY,
    account_id INT NOT NULL,
    request_status VARCHAR(20) NOT NULL,
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_id) REFERENCES credit_card_accounts(id) ON DELETE CASCADE
);
