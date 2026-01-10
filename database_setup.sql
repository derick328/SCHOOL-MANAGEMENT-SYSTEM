-- ============================================
-- School Management System - Database Setup
-- ============================================

-- Create database
CREATE DATABASE school_management_db;

-- Connect to database
\c school_management_db;

-- Create user (optional, if not using default postgres user)
-- CREATE USER sms_user WITH PASSWORD 'your_secure_password_here';
-- GRANT ALL PRIVILEGES ON DATABASE school_management_db TO sms_user;

-- Enable UUID extension (if needed for future features)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================
-- Tables will be created automatically by Hibernate
-- This script is for manual database setup only
-- ============================================

-- Verify connection
SELECT version();

-- Check if database is ready
SELECT current_database(), current_user;

-- ============================================
-- Useful Queries for Development
-- ============================================

-- View all tables (after first run)
-- SELECT table_name FROM information_schema.tables WHERE table_schema = 'public';

-- View all users
-- SELECT * FROM users;

-- View audit logs
-- SELECT * FROM audit_logs ORDER BY created_at DESC LIMIT 10;

-- Count users by role
-- SELECT role, COUNT(*) FROM users GROUP BY role;

-- Find locked accounts
-- SELECT username, account_locked_until FROM users WHERE account_locked_until IS NOT NULL;

-- View recent login attempts
-- SELECT username, last_login_at FROM users ORDER BY last_login_at DESC;

-- ============================================
-- Performance Optimization (After Initial Setup)
-- ============================================

-- Add additional indexes if needed
-- CREATE INDEX idx_users_last_login ON users(last_login_at);
-- CREATE INDEX idx_audit_logs_timestamp ON audit_logs(created_at);

-- Analyze tables for query optimization
-- ANALYZE users;
-- ANALYZE audit_logs;

-- ============================================
-- Backup Commands (Run from command line)
-- ============================================

-- Backup database:
-- pg_dump -U postgres school_management_db > backup_$(date +%Y%m%d).sql

-- Restore database:
-- psql -U postgres school_management_db < backup_20260110.sql

-- ============================================
-- Monitoring Queries
-- ============================================

-- Check database size
-- SELECT pg_size_pretty(pg_database_size('school_management_db'));

-- Check table sizes
-- SELECT 
--   tablename,
--   pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) AS size
-- FROM pg_tables
-- WHERE schemaname = 'public'
-- ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;

-- Active connections
-- SELECT * FROM pg_stat_activity WHERE datname = 'school_management_db';

-- ============================================
-- Security Recommendations
-- ============================================

-- 1. Use strong passwords for database users
-- 2. Limit database user privileges (don't use superuser in production)
-- 3. Enable SSL connections in production
-- 4. Regularly backup database
-- 5. Monitor failed login attempts
-- 6. Keep PostgreSQL updated
-- 7. Configure pg_hba.conf for network access control

-- ============================================
-- Development vs Production Settings
-- ============================================

-- Development:
-- spring.jpa.hibernate.ddl-auto=update
-- spring.jpa.show-sql=true

-- Production:
-- spring.jpa.hibernate.ddl-auto=validate
-- spring.jpa.show-sql=false
-- Enable connection pooling
-- Configure proper logging levels

-- ============================================
-- End of Script
-- ============================================
