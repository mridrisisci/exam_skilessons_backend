package app.controllers;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
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
            /*doctor1 = populator.getDoctors().get(0);
            doctor2 = populator.getDoctors().get(1);
            doctor3 = populator.getDoctors().get(2);

            appointment1 = populator.getAppointments().get(0);
            appointment2 = populator.getAppointments().get(1);
            appointment3 = populator.getAppointments().get(2);
            appointment4 = populator.getAppointments().get(3);
            appointment5 = populator.getAppointments().get(4);
            appointment6 = populator.getAppointments().get(5);*/
    }

    @Test
    void getAll()
    {
        given().when().get("doctors/all").then().statusCode(200).body("size()", equalTo(3));
    }

    @Test
    void getById()
    {
        //given().when().get("/doctors/" + doctor2.getId()).then().statusCode(200).body("name", equalTo(doctor2.getName()));
    }

    @Test
    void create()
    {
        try
        {
            // update this to use the new ENTITY
            String json = objectMapper.createObjectNode()
                .put("name", "TestEntityC")
                .put("yearOfGraduation", "2015-01-01")
                .put("dateOfBirth", "2000-01-02")
                .put("clinicName", "Test Clinic")
                .toString();
            given().when()
                    .contentType("application/json")
                    .accept("application/json")
                    .body(json)
                    .post("/doctors")
                    .then()
                    .statusCode(200); // careful here - maybe change to 200
        } catch (Exception e)
        {
            logger.error("Error creating hotel", e);

            fail();
        }
    }

    @Test
    void update()
    {
        try
        {
            String json = objectMapper.createObjectNode()
                .put("name", "entity is updated")
                .put("clinicName", "Test Clinic")
                .put("dateOfBirth", "3000-01-02")
                .put("yearOfGraduation", "2020-01-01")
                    .toString();
            given().when()
                    .contentType("application/json")
                    .accept("application/json")
                    .body(json)
                    //.put("/doctors/" + doctor2.getId()) // double check id
                    .then()
                    .statusCode(200)
                    .body("name", equalTo("entity is updated"));
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
                //.delete("/doctors/" + doctor2.getId())
                .then()
                .statusCode(204);
    }
}