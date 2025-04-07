package app.routes;

import app.config.HibernateConfig;
import app.controllers.InstructorController;
import app.controllers.SkiingCourseController;
import com.fasterxml.jackson.databind.ObjectMapper;
import app.controllers.SecurityController;
import app.enums.Roles;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes
{
    private final InstructorController instructorController;
    private final SecurityController securityController;
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private final SkiingCourseController skiingCourseController;

    public Routes(InstructorController instructorController, SecurityController securityController, SkiingCourseController skiingCourseController)
    {
        this.instructorController = instructorController;
        this.securityController = securityController;
        this.skiingCourseController = skiingCourseController;
    }

    public  EndpointGroup getRoutes()
    {
        return () -> {
            path("instructor", instructorRoutes());
            path("auth", authRoutes());
            path("protected", protectedRoutes());
            path("courses", skiingCoursesRoutes());
        };
    }

    public EndpointGroup instructorRoutes()
    {
        return () -> {
            get("/all", instructorController::getAll);
            post("/", instructorController::create);
            get("/{id}", instructorController::getById);
            put("/{id}", instructorController::update);
            delete("/{id}", instructorController::delete);
            post("/populate", (ctx) -> instructorController.populateDB(emf));
        };
    }

    private EndpointGroup skiingCoursesRoutes()
    {
        return () -> {
            get("/search/{level}", (ctx) -> skiingCourseController.searchSkiingCourseByLevel(ctx));
            get("/search/duration/{level}", (ctx) -> skiingCourseController.getTotalDurationForASkiingCourse(ctx));
        };
    }

    private  EndpointGroup authRoutes()
    {
        return () -> {
            get("/test", ctx->ctx.json(jsonMapper.createObjectNode().put("msg",  "Hello from Open")), Roles.ANYONE);
            get("/healthcheck", securityController::healthCheck, Roles.ANYONE);
            post("/login", securityController::login, Roles.ANYONE);
            post("/register", securityController::register, Roles.ANYONE);
            get("/verify", securityController::verify , Roles.ANYONE);
            get("/tokenlifespan", securityController::timeToLive , Roles.ANYONE);
        };
    }

    private  EndpointGroup protectedRoutes()
    {
        return () -> {
            get("/user_demo",(ctx)->ctx.json(jsonMapper.createObjectNode().put("msg",  "Hello from USER Protected")), Roles.USER);
            get("/admin_demo",(ctx)->ctx.json(jsonMapper.createObjectNode().put("msg",  "Hello from ADMIN Protected")), Roles.ADMIN);
        };
    }

}
