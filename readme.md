# Backend Java REST API - Exam
________
### Project overview
___
This backend system is a REST API for managing skilessons and instructors. It is built with Java 17 and uses Javalin as the web framework.

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
### Theoretical Questions
___
* (3.3.5): Why is PUT used for assigning an item to a student instead of POST?
Man bruger 'PUT' når man ønsker at opdatere en specifik resource - især når man kender det specifikke endpoint.
* Dertil er 'PUT' også idempotent. Så selvom at man kalder 'PUT' flere gande vi det kun opdatere resourcen man kalder den på den første gang.

### Endpoints 
___
* In the 'resources' folder, there is a <b>demo.http</b> file where all general endpoints are called with their respective HTTP methods.

#### * SkiLessons

* GET /api/lessons
* GET /api/lessons/{id}
* POST /api/lessons
* PUT /api/lessons/{id}
* DELETE /api/lessons/{id}

#### * Instructor

* GET /api/instructors
* GET /api/instructors/{id}
* POST /api/instructors
* PUT /api/instructors/{id}
* DELETE /api/instructors/{id}
* GET /api/instructors/{id}/lessons

Note that you must have your <b>secret token key</b> defined in your config.properties to execute various endpoints.


### REST Error Handling
___
The controller handles general error handling and returns a JSON with error messages. The controller also uses a 'try/catch' block to handle any errors that occur during request processing. If an error occurs, a JSON with error information and an appropriate HTTP status code is returned.
```json
{
    "status": "error",
    "message": "Error message"
}
```
Note that these error messages are also logged in the "debug.log" file in the "logs" folder.
The error messages  in JSON-format are structured as follows:


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
___________
Link for the external API: https://apiprovider.cphbusinessapps.dk/skilesson/

Following routes have been implemented:
* GET /api/courses/search/{level}
* GET /api/courses/search/duration/{level}
* GET /courses/search/instructor/{instructorId}

#### Eksempel på json respons første ovennævnte rute: 

```json
[
  {
    "title": "Basic Skiing Stance",
    "description": "Learn the proper stance for skiing to maintain balance and control.",
    "level": "beginner",
    "durationMinutes": 30,
    "createdAt": 1730310298.547000000,
    "updatedAt": 1730310298.547000000,
    "instructorId": null
  },
  {
    "title": "Snow Plow Stops",
    "description": "Learn the snow plow stop, an essential technique for beginners to slow down or stop.",
    "level": "beginner",
    "durationMinutes": 20,
    "createdAt": 1730310298.547000000,
    "updatedAt": 1730310298.547000000,
    "instructorId": null
  },
  {
    "title": "Emergency Falling Techniques",
    "description": "Learn how to fall safely to avoid injury and minimize risk during a fall.",
    "level": "beginner",
    "durationMinutes": 30,
    "createdAt": 1730310298.547000000,
    "updatedAt": 1730310298.547000000,
    "instructorId": null
  },
  {
    "title": "Freestyle Skiing Basics",
    "description": "Introduction to freestyle skiing, including jumps and tricks on terrain parks.",
    "level": "beginner",
    "durationMinutes": 40,
    "createdAt": 1730310298.547000000,
    "updatedAt": 1730310298.547000000,
    "instructorId": null
  }
]
```

### Testing
___________

This project contains RestAssured tests for the API endpoints. The tests are located in the `src/test/java` directory and are organized by package. Each test class corresponds to a specific controller or functionality.

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