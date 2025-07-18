-- PostgreSQL setup script for KAI database

-- Create database and user (run as postgres user)
-- CREATE DATABASE kai_db;
-- CREATE USER kai_user WITH PASSWORD 'kai_pass';
-- GRANT ALL PRIVILEGES ON DATABASE kai_db TO kai_user;

-- Connect to kai_db and run the following:

-- Create schema
CREATE SCHEMA IF NOT EXISTS admin;

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tables will be created automatically by Hibernate/JPA

-- Create indexes for performance (after tables are created)
-- These can be run after the application starts
-- CREATE INDEX IF NOT EXISTS idx_person_type ON admin.person(type);
-- CREATE INDEX IF NOT EXISTS idx_user_email ON admin."user"(email);
-- CREATE INDEX IF NOT EXISTS idx_area_assignment_area_person ON admin.area_assignment(area_id, person_id);

-- Note: Some table names like "user" and "role" might need to be quoted in PostgreSQL as they are reserved words