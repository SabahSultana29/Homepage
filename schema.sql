-- Drop existing tables (for a clean setup)
DROP TABLE IF EXISTS credit_card_accounts CASCADE;
DROP TABLE IF EXISTS credit_card_offers CASCADE;
DROP TABLE IF EXISTS customers CASCADE;
DROP TABLE IF EXISTS email_log CASCADE;
DROP TABLE IF EXISTS printingshop_requests CASCADE;

-- Customers table
CREATE TABLE customers (
    customer_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15)
);

-- Credit Card Offers table
CREATE TABLE credit_card_offers (
    offer_id SERIAL PRIMARY KEY,
    offer_name VARCHAR(100) NOT NULL,
    discount_percent DECIMAL(5,2),
    valid_until DATE
);

-- Credit Card Accounts table
CREATE TABLE credit_card_accounts (
    account_id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    card_number VARCHAR(16) UNIQUE NOT NULL,
    card_type VARCHAR(50),
    credit_limit DECIMAL(12,2),
    offer_id INT,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    CONSTRAINT fk_offer FOREIGN KEY (offer_id) REFERENCES credit_card_offers(offer_id)
);

-- Email Log table
CREATE TABLE email_log (
    email_id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    subject VARCHAR(150),
    body TEXT,
    CONSTRAINT fk_email_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- Printing Shop Requests table
CREATE TABLE printingshop_requests (
    request_id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    document_name VARCHAR(100),
    pages INT,
    status VARCHAR(20),
    CONSTRAINT fk_request_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);


