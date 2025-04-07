package app.controllers;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.dto.HotelDTO;
import app.entities.Hotel;
import app.entities.Room;
import app.routes.Routes;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HotelResourceTest
{

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    final ObjectMapper objectMapper = new ObjectMapper();
    Hotel t1, t2;
    final Logger logger = LoggerFactory.getLogger(HotelResourceTest.class.getName());


    @BeforeAll
    static void setUpAll()
    {
        HotelController hotelController = new HotelController(emf);
        SecurityController securityController = new SecurityController(emf);
        Routes routes = new Routes(hotelController, securityController);
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
        try (EntityManager em = emf.createEntityManager())
        {
            //TestEntity[] entities = EntityPopulator.populate(genericDAO);
            t1 = new Hotel("TestEntityA");
            t2 = new Hotel("TestEntityB");
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Room ").executeUpdate();
            em.createQuery("DELETE FROM Hotel ").executeUpdate();

            em.persist(t1);
            em.persist(t2);
            em.getTransaction().commit();
        }
        catch (Exception e)
        {
            logger.error("Error setting up test", e);
        }
    }

    @Test
    void getAll()
    {
        given().when().get("/hotel").then().statusCode(200).body("size()", equalTo(2));
    }

    @Test
    void getById()
    {
        given().when().get("/hotel/" + t2.getId()).then().statusCode(200).body("id", equalTo(t2.getId().intValue()));
    }

    @Test
    void create()
    {
        try
        {
            // update this to use the new ENTITY
            String json = objectMapper.createObjectNode()
                    .put("name", "TestEntityC")
                        .toString();
            given().when()
                    .contentType("application/json")
                    .accept("application/json")
                    .body(json)
                    .post("/update_route_here")
                    .then()
                    .statusCode(201); // careful here - maybe change to 200
        } catch (Exception e)
        {
            logger.error("Error creating hotel", e);

            fail();
        }
    }

    @Test
    void update()
    {
        Hotel entity = new Hotel("New entity2");
        try
        {
            String json = objectMapper.writeValueAsString(new HotelDTO(entity));
            given().when()
                    .contentType("application/json")
                    .accept("application/json")
                    .body(json)
                    .put("/hotel/" + t1.getId()) // double check id
                    .then()
                    .statusCode(200)
                    .body("name", equalTo("New entity2"));
        } catch (JsonProcessingException e)
        {
            logger.error("Error updating entity", e);
            fail();
        }
    }

    @Test
    void delete()
    {
        given().when()
                .delete("/hotel/" + t1.getId())
                .then()
                .statusCode(204);
    }
}