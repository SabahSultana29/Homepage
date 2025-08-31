- Insert Customers
INSERT INTO customers (name, email) VALUES 
('Sabah Sultana', 'sabah@example.com'),
('John Doe', 'john@example.com');

-- Insert Credit Card Offers
INSERT INTO credit_card_offers (card_type, credit_limit, description) VALUES 
('Gold', 50000, 'Gold card with medium credit limit'),
('Platinum', 100000, 'Premium Platinum card with high limit');

-- Insert a Credit Card Account (linked to Sabah Sultana, customer_id = 1)
INSERT INTO credit_card_accounts (customer_id, card_number, card_type, credit_limit, status) VALUES 
(1, '1234567890123456', 'Gold', 50000, 'ACTIVE');

-- Insert Email Logs
INSERT INTO email_log (customer_id, email, message) VALUES 
(1, 'sabah@example.com', 'Your Gold Credit Card has been created successfully!');

-- Insert Print Shop Requests
INSERT INTO printing_shop_requests (account_id, request_status) VALUES 
(1, 'PENDING');