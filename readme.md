# Backend Java REST API - Exam
________
### Project overview
___
This backend system is a REST API for managing XX problems, tasks, and math groups. It is built with Java 17 and uses Javalin as the web framework.

### Tech Stack
_____
* Java 17: Programming language
* Javalin: Lightweight web framework
* Hibernate: ORM for database access
* PostgreSQL: Database
* JUnit 5: Testing framework
* REST Assured: API testing
* Testcontainers: Integration testing with containers
* Maven: Build tool
* Docker: Containerization

## Getting started
_____
### Setup
____
Remember to add a config.properties file in the resources folder. The config.properties file should contain the following variables:

```
DB_NAME=choose_db_name
DB_USERNAME=postgres
DB_PASSWORD=your_secret_pw
SECRET_KEY=your_secret_key
ISSUER="choose_issuer_name"
TOKEN_EXPIRE_TIME=1800000
```
The DB_NAME, DB_USERNAME, DB_PASSWORD, ISSUER, and TOKEN_EXPIRE_TIME properties should be filled in with the appropriate values. The SECRET_KEY property should be a minimum of 32 characters long.

### Project Structure
____
src/
├── main/
│   ├── java/app/
│   │   ├── config/       # Application and Hibernate configuration
│   │   ├── controllers/  # REST controllers handling HTTP requests
│   │   ├── dao/          # Data Access Objects for database operations
│   │   ├── dto/          # Data Transfer Objects for API requests/responses
│   │   ├── entities/     # JPA/Hibernate entity classes
│   │   ├── enums/        # Enumeration types
│   │   ├── exceptions/   # Custom exception classes
│   │   ├── routes/       # API route definitions
│   │   └── utils/        # Utility classes
│   └── resources/        # Configuration files and resources
└── test/
└── java/app/         # Test classes

### Theoretical Questions
___
* Question 1: 
* Question 2: 

### Endpoints 
___
* In the 'resources' folder, there is a <b>demo.http</b> file where all general endpoints are called with their respective HTTP methods.
* Additionally, there is also a <b>demoSecurity.http</b> file where all endpoints with protected access roles are called with their respective HTTP methods.

Note that you must have your <b>secret token key</b> defined in your config.properties to execute various endpoints.


### REST Error Handling
___
The controller handles general error handling and returns a JSON with error messages. The controller also uses a 'try/catch' block to handle any errors that occur during request processing. If an error occurs, a JSON with error information and an appropriate HTTP status code is returned.

Note that these error messages are also logged in the "debug.log" file in the "logs" folder.
The error messages are structured as follows:

```json
{
    "status": "error",
    "message": "Error message",
}
```
<details>
<summary>Error handling example</summary>
''' java
@Override
    public void getAll(Context ctx)
    {
        try
        {
            ctx.json(dao.getAll(Hotel.class));
        }
        catch (Exception ex)
        {
            logger.error("Error getting entities", ex);
            ErrorMessage error = new ErrorMessage("Error getting entities");
            ctx.status(404).json(error);
        }
    }
'''
</details>

### API documentation
___
* The API runs on port 7070
* Authentication uses JWT tokens
* Endpoints are secured with role-based access control
* Example HTTP requests are available in src/main/resources/demo.http

### External API

### Security
___

### Best Practices
_____________
#### 1. Code Organization:
* Follow the existing package structure
* Keep controllers thin, business logic in services
* Use DTOs for API requests/responses

#### 2. Database Access:
* Use DAOs for database operations
* Create entity classes for database tables
* Use transactions for data modifications

#### 3. Testing:
* Write unit tests for all new functionality
* Use REST Assured for API testing
* Use Testcontainers for integration tests

#### 4. Security:
* Validate all user input
* Use proper authentication and authorization
* Don't expose sensitive information in responses