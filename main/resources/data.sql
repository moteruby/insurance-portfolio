-- Insert default users (password is 'admin123' hashed with BCrypt)
-- BCrypt hash for 'admin123': $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

INSERT IGNORE INTO users (id, username, password, full_name, email, role, active, created_at, last_login)
VALUES 
(1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Администратор', 'admin@insurance.com', 'ADMIN', true, NOW(), NOW()),
(2, 'agent', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Страховой агент', 'agent@insurance.com', 'AGENT', true, NOW(), NOW()),
(3, 'analyst', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Аналитик', 'analyst@insurance.com', 'ANALYST', true, NOW(), NOW());
