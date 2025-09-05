
-- Insert Customers
INSERT INTO customers (name, email, phone) VALUES
('Sabah Sultana', 'sabah@example.com', '9876543210'),
('Aryan Sharma', 'aryan@example.com', '9123456780'),
('Abhishek kale', 'abhishek@example.com', '9988776655');

-- Insert Credit Card Offers
INSERT INTO credit_card_offers (offer_name, description,annual_fee) VALUES
('Festival Offer', 'Enjoy festival discounts on all purchases', 499.99),
('Welcome Offer','New users get premium benefits', 299.00),
('Premium Cashback', 'Get amazing cashback deals!', 999.00);

-- Insert Credit Card Accounts
INSERT INTO credit_card_accounts (customer_id, card_number, card_type, credit_limit, offer_id) VALUES
(1, '1234567890123456', 'Platinum', 200000.00, 1),
(2, '9876543210987654', 'Gold', 150000.00, 2),
(3, '1111222233334444', 'Silver', 100000.00, NULL);

-- Insert Email Logs
INSERT INTO email_log (id , customer_id , card_number, message,sent_at) VALUES
(1,1, '1223445667897', 'Your credit card has, been created' ,'2025-09-02 16:30:00'),
(2,2 , '12234556778998', 'Email sent for customer 2', '2025-09-02 16:45:00');
-- Insert Printing Shop Requests
INSERT INTO printingShop_requests (account_id, status,details, request_time) VALUES
(1, 'PENDING' , 'Platinum Credit card request' , CURRENT_TIMESTAMP),
(2, 'SHIPPED', 'Gold Credit card Replacement', CURRENT_TIMESTAMP),
(3, 'DELIVERED', 'Silver Credit card has delivered successfully' , CURRENT_TIMESTAMP);

--Insert into transactions table
INSERT INTO transactions
(customer_id, name, dob, email, phone, credit_score, product, validity_period, credit_limit, status, approval_date, processed_by, application_timeline)
VALUES
(1001, 'Abhishek Kale', '2001-08-12', 'abhishek@example.com', '+91-9876543210', 782, 'Platinum Rewards Card', '2025-2030', '₹2,00,000', 'Approved', '2025-09-02', 'System Auto-Decision', 'Application Submitted'),

(1002, 'Aryan Sharma', '1998-05-21', 'aryan@example.com', '+91-9988776655', 810, 'Gold Credit Card', '2025-2030', '₹1,20,000', 'Pending', '2025-09-03', 'Sales User1', 'Application Processing'),

(1003, 'Sabah Sultana', '1995-02-15', 'saba@example.com', '+91-9123456789', 650, 'Basic Cashback Card', '2025-2030', '₹60,000', 'Rejected', '2025-09-01', 'Sales User2', 'Credit Card Offered');
