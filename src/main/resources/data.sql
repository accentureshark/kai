-- Insert Organizations (Legal Persons)
INSERT INTO admin.person (id, type, name, display_name, business_name, tax_id, registration_number)
SELECT uuid_generate_v4(), 'LEGAL', 'Accenture', 'Accenture', 'Accenture PLC', '123456789', 'REG001'
WHERE NOT EXISTS (SELECT 1 FROM admin.person WHERE business_name = 'Accenture');

INSERT INTO admin.person (id, type, name, display_name, business_name, tax_id, registration_number)
SELECT uuid_generate_v4(), 'LEGAL', 'Tech Solutions Inc', 'Tech Solutions Inc', 'Tech Solutions Inc', '987654321', 'REG002'
WHERE NOT EXISTS (SELECT 1 FROM admin.person WHERE business_name = 'Tech Solutions Inc');

-- Insert Natural Persons
INSERT INTO admin.person (id, type, name, display_name, first_name, last_name, national_id, birth_date)
SELECT uuid_generate_v4(), 'NATURAL', 'John Doe', 'John Doe', 'John', 'Doe', '12345678A', '1990-01-15'
WHERE NOT EXISTS (SELECT 1 FROM admin.person WHERE first_name = 'John' AND last_name = 'Doe');

INSERT INTO admin.person (id, type, name, display_name, first_name, last_name, national_id, birth_date)
SELECT uuid_generate_v4(), 'NATURAL', 'Jane Smith', 'Jane Smith', 'Jane', 'Smith', '87654321B', '1985-03-22'
WHERE NOT EXISTS (SELECT 1 FROM admin.person WHERE first_name = 'Jane' AND last_name = 'Smith');

INSERT INTO admin.person (id, type, name, display_name, first_name, last_name, national_id, birth_date)
SELECT uuid_generate_v4(), 'NATURAL', 'Carlos Rodriguez', 'Carlos Rodriguez', 'Carlos', 'Rodriguez', '11223344C', '1988-07-10'
WHERE NOT EXISTS (SELECT 1 FROM admin.person WHERE first_name = 'Carlos' AND last_name = 'Rodriguez');

-- Insert Areas (will fail if org not present, so ensure orgs above are inserted first)
INSERT INTO admin.area (id, name, organization_id)
SELECT uuid_generate_v4(), 'Technology', p.id
FROM admin.person p
WHERE p.business_name = 'Accenture'
  AND NOT EXISTS (SELECT 1 FROM admin.area WHERE name = 'Technology' AND organization_id = p.id);

INSERT INTO admin.area (id, name, organization_id)
SELECT uuid_generate_v4(), 'Human Resources', p.id
FROM admin.person p
WHERE p.business_name = 'Accenture'
  AND NOT EXISTS (SELECT 1 FROM admin.area WHERE name = 'Human Resources' AND organization_id = p.id);

INSERT INTO admin.area (id, name, organization_id)
SELECT uuid_generate_v4(), 'Finance', p.id
FROM admin.person p
WHERE p.business_name = 'Accenture'
  AND NOT EXISTS (SELECT 1 FROM admin.area WHERE name = 'Finance' AND organization_id = p.id);

-- Insert Roles
INSERT INTO admin."role" (id, name, area_id)
SELECT uuid_generate_v4(), 'Developer', a.id
FROM admin.area a
WHERE a.name = 'Technology'
  AND NOT EXISTS (SELECT 1 FROM admin."role" WHERE name = 'Developer' AND area_id = a.id);

INSERT INTO admin."role" (id, name, area_id)
SELECT uuid_generate_v4(), 'Tech Lead', a.id
FROM admin.area a
WHERE a.name = 'Technology'
  AND NOT EXISTS (SELECT 1 FROM admin."role" WHERE name = 'Tech Lead' AND area_id = a.id);

INSERT INTO admin."role" (id, name, area_id)
SELECT uuid_generate_v4(), 'HR Manager', a.id
FROM admin.area a
WHERE a.name = 'Human Resources'
  AND NOT EXISTS (SELECT 1 FROM admin."role" WHERE name = 'HR Manager' AND area_id = a.id);

-- Insert Users
INSERT INTO admin."user" (id, email, password, active, role_id, person_id)
SELECT uuid_generate_v4(), 'john.doe@accenture.com',
  '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', true, r.id, p.id
FROM admin.person p, admin."role" r
WHERE p.first_name = 'John' AND p.last_name = 'Doe' AND r.name = 'Developer'
  AND NOT EXISTS (SELECT 1 FROM admin."user" WHERE email = 'john.doe@accenture.com');

INSERT INTO admin."user" (id, email, password, active, role_id, person_id)
SELECT uuid_generate_v4(), 'jane.smith@accenture.com',
  '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', true, r.id, p.id
FROM admin.person p, admin."role" r
WHERE p.first_name = 'Jane' AND p.last_name = 'Smith' AND r.name = 'HR Manager'
  AND NOT EXISTS (SELECT 1 FROM admin."user" WHERE email = 'jane.smith@accenture.com');

-- Insert Area Assignments
INSERT INTO admin.area_assignment (id, area_id, person_id, assigned_at, local_role)
SELECT uuid_generate_v4(), a.id, p.id, CURRENT_DATE, 'Senior Developer'
FROM admin.area a, admin.person p
WHERE a.name = 'Technology' AND p.first_name = 'John' AND p.last_name = 'Doe'
  AND NOT EXISTS (SELECT 1 FROM admin.area_assignment WHERE area_id = a.id AND person_id = p.id);

INSERT INTO admin.area_assignment (id, area_id, person_id, assigned_at, local_role)
SELECT uuid_generate_v4(), a.id, p.id, CURRENT_DATE, 'Junior Developer'
FROM admin.area a, admin.person p
WHERE a.name = 'Technology' AND p.first_name = 'Carlos' AND p.last_name = 'Rodriguez'
  AND NOT EXISTS (SELECT 1 FROM admin.area_assignment WHERE area_id = a.id AND person_id = p.id);