-- Create schema if not exists
CREATE SCHEMA IF NOT EXISTS admin;

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create tables based on the JPA entities
-- Note: JPA will actually create the tables, but this script ensures proper constraints and indexes

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_person_type ON admin.person(type);
CREATE INDEX IF NOT EXISTS idx_user_email ON admin.user(email);
CREATE INDEX IF NOT EXISTS idx_area_assignment_area_person ON admin.area_assignment(area_id, person_id);