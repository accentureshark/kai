-- Insert sample data (only if tables are empty)
-- Organizations (Legal Persons)
INSERT INTO admin.person (id, type, display_name, business_name, tax_id, registration_number) 
VALUES 
    (uuid_generate_v4(), 'LEGAL', 'Accenture', 'Accenture PLC', '123456789', 'REG001'),
    (uuid_generate_v4(), 'LEGAL', 'Tech Solutions Inc', 'Tech Solutions Inc', '987654321', 'REG002')
ON CONFLICT (id) DO NOTHING;

-- Natural Persons
INSERT INTO admin.person (id, type, display_name, first_name, last_name, national_id, birth_date) 
VALUES 
    (uuid_generate_v4(), 'NATURAL', 'John Doe', 'John', 'Doe', '12345678A', '1990-01-15'),
    (uuid_generate_v4(), 'NATURAL', 'Jane Smith', 'Jane', 'Smith', '87654321B', '1985-03-22'),
    (uuid_generate_v4(), 'NATURAL', 'Carlos Rodriguez', 'Carlos', 'Rodriguez', '11223344C', '1988-07-10')
ON CONFLICT (id) DO NOTHING;

-- Get IDs for relationships (using a more robust approach)
WITH org_data AS (
    SELECT id as org_id FROM admin.person WHERE business_name = 'Accenture' LIMIT 1
),
person_data AS (
    SELECT 
        (SELECT id FROM admin.person WHERE first_name = 'John' AND last_name = 'Doe' LIMIT 1) as john_id,
        (SELECT id FROM admin.person WHERE first_name = 'Jane' AND last_name = 'Smith' LIMIT 1) as jane_id,
        (SELECT id FROM admin.person WHERE first_name = 'Carlos' AND last_name = 'Rodriguez' LIMIT 1) as carlos_id
)
-- Areas
INSERT INTO admin.area (id, name, organization_id)
SELECT 
    uuid_generate_v4(), 'Technology', org_data.org_id
FROM org_data
UNION ALL
SELECT 
    uuid_generate_v4(), 'Human Resources', org_data.org_id
FROM org_data
UNION ALL
SELECT 
    uuid_generate_v4(), 'Finance', org_data.org_id
FROM org_data
ON CONFLICT (id) DO NOTHING;

-- Roles
WITH area_data AS (
    SELECT 
        (SELECT id FROM admin.area WHERE name = 'Technology' LIMIT 1) as tech_area_id,
        (SELECT id FROM admin.area WHERE name = 'Human Resources' LIMIT 1) as hr_area_id
)
INSERT INTO admin.role (id, name, area_id)
SELECT uuid_generate_v4(), 'Developer', area_data.tech_area_id FROM area_data
UNION ALL
SELECT uuid_generate_v4(), 'Tech Lead', area_data.tech_area_id FROM area_data
UNION ALL
SELECT uuid_generate_v4(), 'HR Manager', area_data.hr_area_id FROM area_data
ON CONFLICT (id) DO NOTHING;

-- Users
WITH person_role_data AS (
    SELECT 
        (SELECT id FROM admin.person WHERE first_name = 'John' AND last_name = 'Doe' LIMIT 1) as john_id,
        (SELECT id FROM admin.person WHERE first_name = 'Jane' AND last_name = 'Smith' LIMIT 1) as jane_id,
        (SELECT id FROM admin.role WHERE name = 'Developer' LIMIT 1) as dev_role_id,
        (SELECT id FROM admin.role WHERE name = 'HR Manager' LIMIT 1) as hr_role_id
)
INSERT INTO admin.user (id, email, password, active, role_id, person_id)
SELECT 
    uuid_generate_v4(), 
    'john.doe@accenture.com', 
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', -- password: password
    true, 
    person_role_data.dev_role_id, 
    person_role_data.john_id
FROM person_role_data
UNION ALL
SELECT 
    uuid_generate_v4(), 
    'jane.smith@accenture.com', 
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', -- password: password
    true, 
    person_role_data.hr_role_id, 
    person_role_data.jane_id
FROM person_role_data
ON CONFLICT (email) DO NOTHING;

-- Area Assignments
WITH assignment_data AS (
    SELECT 
        (SELECT id FROM admin.area WHERE name = 'Technology' LIMIT 1) as tech_area_id,
        (SELECT id FROM admin.person WHERE first_name = 'John' AND last_name = 'Doe' LIMIT 1) as john_id,
        (SELECT id FROM admin.person WHERE first_name = 'Carlos' AND last_name = 'Rodriguez' LIMIT 1) as carlos_id
)
INSERT INTO admin.area_assignment (id, area_id, person_id, assigned_at, local_role)
SELECT 
    uuid_generate_v4(), 
    assignment_data.tech_area_id, 
    assignment_data.john_id, 
    CURRENT_DATE, 
    'Senior Developer'
FROM assignment_data
UNION ALL
SELECT 
    uuid_generate_v4(), 
    assignment_data.tech_area_id, 
    assignment_data.carlos_id, 
    CURRENT_DATE, 
    'Junior Developer'
FROM assignment_data
ON CONFLICT (area_id, person_id) DO NOTHING;