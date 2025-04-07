package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.controllers.InstructorController;
import app.controllers.SecurityController;
import app.controllers.SkiingCourseController;
import app.routes.Routes;
import app.utils.DataAPIReader;
import jakarta.persistence.EntityManagerFactory;


public class Main
{
    private final static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();


    public static void main(String[] args)
    {
        DataAPIReader dataAPIReader = new DataAPIReader();
        SkiingCourseController skiingCourseController = new SkiingCourseController(emf, dataAPIReader);
        InstructorController instructorController = new InstructorController(emf);
        SecurityController securityController = new SecurityController(emf);
        Routes routes = new Routes(instructorController, securityController, skiingCourseController);

        ApplicationConfig
                .getInstance()
                .initiateServer()
                .setRoute(routes.getRoutes())
                .handleException()
                .setApiExceptionHandling()
                .checkSecurityRoles()
                .startServer(7070);
    }
}