-- Drop existing tables (for a clean setup)
DROP TABLE IF EXISTS credit_card_accounts CASCADE;
DROP TABLE IF EXISTS credit_card_offers CASCADE;
DROP TABLE IF EXISTS customers CASCADE;
DROP TABLE IF EXISTS email_log CASCADE;
DROP TABLE IF EXISTS printingShop_requests CASCADE;

-- Customers table
CREATE TABLE customers (
    customer_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15)
);

-- Credit Card Offers table
CREATE TABLE credit_card_offers (
    offer_id SERIAL PRIMARY KEY,
    offer_name VARCHAR(100) NOT NULL,
    description TEXT,
    annual_fee DECIMAL(10,2)
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
    id SERIAL PRIMARY KEY,
    customer_id INTEGER ,
    card_number VARCHAR(65),
    message TEXT,
    sent_at TIMESTAMP

);

-- Printing Shop Requests table
CREATE TABLE printingShop_requests (
   id BIGSERIAL PRIMARY KEY ,
   account_id BIGINT NOT NULL,
   status VARCHAR(50) NOT NULL,
   details VARCHAR(1000),
   request_time TIMESTAMP NOT NULL ,
   CONSTRAINT fk_request_customer FOREIGN KEY (account_id)
   REFERENCES customers(customer_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);


--Transaction Table
CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    credit_score INT NOT NULL,
    product VARCHAR(100) NOT NULL,
    validity_period VARCHAR(20) NOT NULL,
    credit_limit VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    approval_date DATE NOT NULL,
    processed_by VARCHAR(100),
    application_timeline VARCHAR(100)
);