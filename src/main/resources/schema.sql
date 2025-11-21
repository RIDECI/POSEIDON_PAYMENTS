CREATE TABLE IF NOT EXISTS transactions (
    id VARCHAR(100) PRIMARY KEY,
    booking_id VARCHAR(100) NOT NULL,
    passenger_id VARCHAR(100) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    attempts INTEGER DEFAULT 0,
    error_message TEXT,
    receipt_code TEXT,
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS refunds (
    id VARCHAR(100) PRIMARY KEY,
    transaction_id VARCHAR(100),
    booking_id VARCHAR(100),
    passenger_id VARCHAR(100),
    refunded_amount NUMERIC(12,2),
    reason TEXT,
    request_at TIMESTAMP,
    status VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS payment_methods (
    id VARCHAR(100) PRIMARY KEY,
    user_id VARCHAR(100),
    alias VARCHAR(100),
    is_default BOOLEAN,
    is_active BOOLEAN,
    type VARCHAR(30)
);
