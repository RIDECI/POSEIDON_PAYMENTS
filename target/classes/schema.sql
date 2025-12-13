CREATE TABLE IF NOT EXISTS transactions (
    id VARCHAR(100) PRIMARY KEY,
    booking_id VARCHAR(100) NOT NULL,
    passenger_id VARCHAR(100) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    extra TEXT,
    status VARCHAR(30) NOT NULL,
    attempts INTEGER DEFAULT 0,
    error_message TEXT,
    receipt_code TEXT,
    created_at TIMESTAMP,

    CONSTRAINT uk_receipt_code UNIQUE (receipt_code),
    CONSTRAINT uk_booking_id UNIQUE (booking_id)
);

CREATE TABLE IF NOT EXISTS refunds (
    id VARCHAR(100) PRIMARY KEY,
    transaction_id VARCHAR(100),
    booking_id VARCHAR(100),
    passenger_id VARCHAR(100),
    refunded_amount NUMERIC(12,2),
    reason TEXT,
    status VARCHAR(30),
    external_reference VARCHAR(100),
    request_at TIMESTAMP,
    completed_at TIMESTAMP,

    CONSTRAINT fk_refunds_transaction
        FOREIGN KEY (transaction_id) REFERENCES transactions(id)
);


CREATE TABLE IF NOT EXISTS payment_methods (
    id VARCHAR(100) PRIMARY KEY,
    user_id VARCHAR(100),
    alias VARCHAR(100),
    is_default BOOLEAN,
    is_active BOOLEAN,
    type VARCHAR(30)
);


CREATE TABLE IF NOT EXISTS payment_receipt (
    id VARCHAR(100) PRIMARY KEY,
    transaction_id VARCHAR(100),
    receipt_code VARCHAR(50),
    passenger_id VARCHAR(36),
    driver_id VARCHAR(36),
    booking_id VARCHAR(36),
    amount NUMERIC(12,2) NOT NULL,
    payment_method VARCHAR(10),
    transaction_method VARCHAR(20),
    payment_details TEXT,
    issued_at TIMESTAMP,
    download_url VARCHAR(255),

    CONSTRAINT fk_receipt_transaction
        FOREIGN KEY (transaction_id) REFERENCES transactions(id),

    CONSTRAINT uk_receipt_code UNIQUE (receipt_code)
);


CREATE TABLE IF NOT EXISTS cash_payment_confirmation (
    id VARCHAR(100) PRIMARY KEY,
    transaction_id VARCHAR(36),
    booking_id VARCHAR(36),
    driver_id VARCHAR(36),
    passenger_id VARCHAR(36),
    amount NUMERIC(12,2) NOT NULL,
    confirmed BOOLEAN DEFAULT false,
    confirmed_at TIMESTAMP,
    observations TEXT,

    CONSTRAINT fk_cash_transaction
        FOREIGN KEY (transaction_id) REFERENCES transactions(id)
);


CREATE TABLE IF NOT EXISTS audit_logs (
    id VARCHAR(100) PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL,
    entity_id VARCHAR(100) NOT NULL,
    action VARCHAR(50) NOT NULL,
    user_id VARCHAR(100),
    user_name VARCHAR(200),
    description TEXT,
    previous_state TEXT,
    new_state TEXT,
    ip_address VARCHAR(50),
    timestamp TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS payment_suspensions (
    id VARCHAR(100) PRIMARY KEY,

    transaction_id VARCHAR(100) NOT NULL,
    reason VARCHAR(1000),
    status VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    expires_at TIMESTAMP,
    admin_id VARCHAR(100),
    CONSTRAINT fk_payment_suspension_transaction
        FOREIGN KEY (transaction_id) REFERENCES transactions(id)
);
CREATE TABLE IF NOT EXISTS credit_cards (
    id VARCHAR(100) PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    card_holder VARCHAR(200) NOT NULL,
    card_number VARCHAR(50) NOT NULL,     
    expiration VARCHAR(10) NOT NULL,       
    cvv VARCHAR(10) NOT NULL,              
    alias VARCHAR(100),
    is_default BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE
);
CREATE TABLE IF NOT EXISTS breb_keys (
    id VARCHAR(100) PRIMARY KEY,
    user_id VARCHAR(100) NOT NULL,
    value VARCHAR(255) NOT NULL,
    type VARCHAR(20) NOT NULL,   
    is_default BOOLEAN DEFAULT FALSE,

    CONSTRAINT uk_breb_value UNIQUE (value, user_id)
);
