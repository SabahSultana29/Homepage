-- Insert Customers
INSERT INTO customers (first_name, last_name, email, phone) VALUES
('Sabah', 'Sultana', 'sabah@example.com', '9876543210'),
('Rahul', 'Sharma', 'rahul@example.com', '9123456780'),
('Ananya', 'Rao', 'ananya@example.com', '9988776655');

-- Insert Credit Card Offers
INSERT INTO credit_card_offers (offer_name, discount_percent, valid_until) VALUES
('Festival Offer', 10.00, '2025-12-31'),
('Welcome Offer', 15.00, '2025-09-30'),
('Premium Cashback', 20.00, '2026-01-15');

-- Insert Credit Card Accounts
INSERT INTO credit_card_accounts (customer_id, card_number, card_type, credit_limit, offer_id) VALUES
(1, '1234567890123456', 'Platinum', 200000.00, 1),
(2, '9876543210987654', 'Gold', 150000.00, 2),
(3, '1111222233334444', 'Silver', 100000.00, NULL);

-- Insert Email Logs
INSERT INTO email_log (customer_id, subject, body) VALUES
(1, 'Welcome to Credit Card Service', 'Dear Sabah, your card has been activated.'),
(2, 'Offer Alert!', 'Hello Rahul, you have received a Welcome Offer.'),
(3, 'Statement Available', 'Hi Ananya, your monthly statement is ready.');

-- Insert Printing Shop Requests
INSERT INTO printingshop_requests (customer_id, document_name, pages, status) VALUES
(1, 'Credit Card Statement Jan.pdf', 5, 'Completed'),
(2, 'Application Form.pdf', 3, 'Pending'),
(3, 'Offer Letter.pdf', 2, 'In Progress');
