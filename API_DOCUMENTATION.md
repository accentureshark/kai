# KAI - Knowledge Access & Identity API

This project implements a complete CRUD API using Spring Boot, PostgreSQL, and Swagger documentation for managing users, organizations, areas, and roles.

## Features

- ✅ **Spring Boot 3.4.1** with Java 17
- ✅ **PostgreSQL** database integration
- ✅ **Spring Data JPA** for data persistence
- ✅ **Swagger/OpenAPI 3** documentation
- ✅ **Complete CRUD operations** for all entities
- ✅ **Validation** with Bean Validation
- ✅ **H2 database** support for testing
- ✅ **RESTful API design**
- ✅ **Pagination support**
- ✅ **Error handling**

## Data Model

The system manages the following entities:

### Core Entities
- **Person** (Abstract base class)
  - **NaturalPerson** - Individual people
  - **LegalPerson** - Legal entities
    - **Organization** - Companies/organizations
- **User** - System users linked to natural persons
- **Role** - User roles within areas
- **Area** - Organizational areas/departments
- **AreaAssignment** - Assignment of people to areas

### Relationships
- Users belong to NaturalPersons and have Roles
- Roles belong to Areas
- Areas belong to Organizations
- AreaAssignments link NaturalPersons to Areas

## API Endpoints

### Organizations (`/api/organizations`)
- `GET /api/organizations` - List all organizations (paginated)
- `GET /api/organizations/{id}` - Get organization by ID
- `GET /api/organizations/search?name={name}` - Search by name
- `GET /api/organizations/tax-id/{taxId}` - Get by tax ID
- `POST /api/organizations` - Create organization
- `PUT /api/organizations/{id}` - Update organization
- `DELETE /api/organizations/{id}` - Delete organization

### Areas (`/api/areas`)
- `GET /api/areas` - List all areas (paginated)
- `GET /api/areas/{id}` - Get area by ID
- `GET /api/areas/organization/{organizationId}` - Get areas by organization
- `GET /api/areas/search?name={name}` - Search by name
- `POST /api/areas` - Create area
- `PUT /api/areas/{id}` - Update area
- `DELETE /api/areas/{id}` - Delete area

### Users (`/api/users`)
- `GET /api/users` - List all users (paginated)
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/email/{email}` - Get user by email
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL 12+ (or use H2 for testing)

### Configuration

#### PostgreSQL Setup
1. Create database and user:
```sql
CREATE DATABASE kai_db;
CREATE USER kai_user WITH PASSWORD 'kai_pass';
GRANT ALL PRIVILEGES ON DATABASE kai_db TO kai_user;
```

2. Update `src/main/resources/application.yml` with your database settings.

#### H2 Testing Setup
Use the test profile for quick testing with H2 in-memory database:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

### Running the Application

1. **Clone and build:**
```bash
git clone <repository-url>
cd kai
mvn clean compile
```

2. **Run with PostgreSQL:**
```bash
mvn spring-boot:run
```

3. **Run with H2 (for testing):**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

### Accessing the Application

- **Application**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Documentation**: http://localhost:8080/api-docs
- **H2 Console** (test profile): http://localhost:8080/h2-console
- **Health Check**: http://localhost:8080/actuator/health

### Sample API Usage

#### Create an Organization
```bash
curl -X POST http://localhost:8080/api/organizations \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Accenture",
    "displayName": "Accenture PLC",
    "businessName": "Accenture Public Limited Company",
    "taxId": "123456789",
    "registrationNumber": "REG001"
  }'
```

#### Get All Organizations
```bash
curl http://localhost:8080/api/organizations
```

#### Create an Area
```bash
curl -X POST http://localhost:8080/api/areas \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Technology",
    "organizationId": "organization-uuid-here"
  }'
```

## Database Schema

The application automatically creates the database schema using Hibernate. For PostgreSQL setup, run the scripts in:
- `src/main/resources/schema.sql` - Database and schema setup
- `src/main/resources/data.sql` - Sample data (after schema creation)

## API Documentation

Full API documentation is available via Swagger UI at `/swagger-ui.html` when the application is running. The documentation includes:

- Interactive API testing
- Request/response schemas
- Parameter descriptions
- Example values
- Response codes

## Validation

The API includes comprehensive validation:
- Required field validation
- Email format validation
- Unique constraint validation
- Custom business rule validation

## Error Handling

The API returns appropriate HTTP status codes and error messages:
- `200 OK` - Successful GET/PUT requests
- `201 Created` - Successful POST requests
- `204 No Content` - Successful DELETE requests
- `400 Bad Request` - Validation errors or invalid data
- `404 Not Found` - Resource not found
- `409 Conflict` - Unique constraint violations
- `500 Internal Server Error` - Server errors

## Architecture

### Layers
- **Controller Layer** - REST endpoints with Swagger documentation
- **Service Layer** - Business logic and transaction management
- **Repository Layer** - Data access using Spring Data JPA
- **DTO Layer** - Data transfer objects for API communication
- **Entity Layer** - JPA entities representing the data model

### Key Design Patterns
- Repository Pattern
- DTO Pattern
- Builder Pattern
- Dependency Injection
- Transaction Management

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## Technology Stack

- **Spring Boot 3.4.1**
- **Spring Data JPA**
- **Spring Web**
- **Spring Validation**
- **PostgreSQL Driver**
- **H2 Database** (for testing)
- **Swagger/OpenAPI 3**
- **Lombok**
- **Jackson**
- **Maven**