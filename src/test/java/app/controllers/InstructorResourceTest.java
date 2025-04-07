package app.controllers;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.entities.Instructor;
import app.entities.SkiLesson;
import app.routes.Routes;
import app.utils.Populator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;

class InstructorResourceTest
{

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    final ObjectMapper objectMapper = new ObjectMapper();
    final Logger logger = LoggerFactory.getLogger(InstructorController.class.getName());
    Instructor instructor1, instructor2, instructor3;
    SkiLesson lesson1, lesson2, lesson3, lesson4, lesson5, lesson6;



    @BeforeAll
    static void setUpAll()
    {
        InstructorController instructorController = new InstructorController(emf);
        SecurityController securityController = new SecurityController(emf);
        Routes routes = new Routes(instructorController, securityController);
        ApplicationConfig
                .getInstance()
                .initiateServer()
                .setRoute(routes.getRoutes())
                .handleException()
                .setApiExceptionHandling()
                .checkSecurityRoles()
                .startServer(7078);
        RestAssured.baseURI = "http://localhost:7078/api";
    }

    @BeforeEach
    void setUp()
    {
        Populator populator = new Populator();

            populator.populate(emf);
            instructor1 = populator.getInstructors().get(0);
            instructor2 = populator.getInstructors().get(1);
            instructor3 = populator.getInstructors().get(2);

            lesson1 = populator.getSkiLessons().get(0);
            lesson2 = populator.getSkiLessons().get(1);
            lesson3 = populator.getSkiLessons().get(2);
            lesson4 = populator.getSkiLessons().get(3);
            lesson5 = populator.getSkiLessons().get(4);
            lesson6 = populator.getSkiLessons().get(5);
    }

    @Test
    void getAll()
    {
        given().when().get("instructor/all").then().statusCode(200).body("size()", equalTo(3));
    }

    @Test
    void getById()
    {
        given().when().get("/instructor/" + instructor2.getId()).then().statusCode(200).body("firstName", equalTo(instructor2.getFirstName()));
    }

    @Test
    void create()
    {
        try
        {
            // update this to use the new ENTITY
            String json = objectMapper.createObjectNode()
                .put("firstName", "TestEntityC")
                .put("lastName", "Margrethe")
                .put("email", "test@mail.net")
                .put("phone", "99999999")
                .put("yearsOfExperience", 5)
                .toString();
            given().when()
                    .contentType("application/json")
                    .accept("application/json")
                    .body(json)
                    .post("/instructor")
                    .then()
                    .statusCode(200); // careful here - maybe change to 200
        } catch (Exception e)
        {
            logger.error("Error creating instructor", e);
            fail();
        }
    }

    @Test
    void update()
    {
        try
        {
            String json = objectMapper.createObjectNode()
                .put("firstName", "entity is updated its first name")
                .put("lastName", "Test last name")
                .put("email", "test@test.com")
                .put("phone", "1337")
                    .toString();
            given().when()
                    .contentType("application/json")
                    .accept("application/json")
                    .body(json)
                    .put("/instructor/" + instructor2.getId()) // double check id
                    .then()
                    .statusCode(200)
                    .body("firstName", equalTo("entity is updated its first name"));
        } catch (Exception e)
        {
            logger.error("Error updating entity", e);
            fail();
        }
    }

    @Test
    void delete()
    {
        given().when()
                .delete("/instructor/" + instructor2.getId())
                .then()
                .statusCode(204);
    }
}