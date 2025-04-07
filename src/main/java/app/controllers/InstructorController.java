package app.controllers;

import app.config.HibernateConfig;
import app.dao.CrudDAO;
import app.dao.InstructorDAO;
import app.dto.ErrorMessage;
import app.dto.InstructorDTO;
import app.entities.Instructor;
import app.entities.SkiLesson;
import app.utils.Populator;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstructorController implements IController {
    private final CrudDAO dao;
    private static final Logger logger = LoggerFactory.getLogger(InstructorController.class);
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    public InstructorController(EntityManagerFactory emf) {
        dao = new InstructorDAO(emf);
    }

    public InstructorController(CrudDAO dao) {
        this.dao = dao;
    }

    public void populateDB(EntityManagerFactory emf)
    {
        Populator populator = new Populator();
        try (EntityManager em = emf.createEntityManager())
        {
            populator.populate(emf);
            logger.info("Populated database with dummy data");
        } catch (Exception e)
        {
            logger.error("Error populating database: " + e.getMessage());
        }
    }

    @Override
    public void getAll(Context ctx) {
        try {
            ctx.json(dao.getAll(Instructor.class));
        } catch (Exception ex) {
            logger.error("Error getting entities", ex);
            ErrorMessage error = new ErrorMessage("Error getting entities");
            ctx.status(404).json(error);
        }
    }

    @Override
    public void getById(Context ctx) {
        try {
            long id = ctx.pathParamAsClass("id", Long.class)
                .check(i -> i > 0, "id must be at least 0")
                .getOrThrow((validation) -> new BadRequestResponse("Invalid id"));
            InstructorDTO foundEntity = new InstructorDTO(dao.getById(Instructor.class, id));
            ctx.json(foundEntity);

        } catch (Exception ex) {
            logger.error("Error getting entity", ex);
            ErrorMessage error = new ErrorMessage("No entity with that id");
            ctx.status(404).json(error);
        }
    }

    @Override
    public void create(Context ctx) {
        try {
            InstructorDTO incomingInstructor = ctx.bodyAsClass(InstructorDTO.class);
            Instructor entity = new Instructor(incomingInstructor);
            Instructor createdEntity = dao.create(entity);
            for (SkiLesson lesson : entity.getSkiLessoons()) {
                lesson.setInstructor(createdEntity);
                dao.update(lesson);
            }
            ctx.json(new InstructorDTO(createdEntity));
        } catch (Exception ex) {
            logger.error("Error creating entity", ex);
            ErrorMessage error = new ErrorMessage("Error creating entity");
            ctx.status(400).json(error);
        }
    }

    public void update(Context ctx) {
        try {
            long id = ctx.pathParamAsClass("id", Long.class)
                .check(i -> i > 0, "id must be at least 0")
                .getOrThrow((valiappor) -> new BadRequestResponse("Invalid id"));
            InstructorDTO incomingEntity = ctx.bodyAsClass(InstructorDTO.class);
            Instructor entityToUpdate = dao.getById(Instructor.class, id);
            if (incomingEntity.getFirstName() != null) {
                entityToUpdate.setFirstName(incomingEntity.getFirstName());
            }
            if (incomingEntity.getLastName() != null) {
                entityToUpdate.setLastName(incomingEntity.getLastName());
            }
            Instructor updatedEntity = dao.update(entityToUpdate);
            InstructorDTO returnedEntity = new InstructorDTO(updatedEntity);
            ctx.json(returnedEntity);
        } catch (Exception ex) {
            logger.error("Error updating entity", ex);
            ErrorMessage error = new ErrorMessage("Error updating entity. " + ex.getMessage());
            ctx.status(400).json(error);
        }
    }

    public void delete(Context ctx) {
        try {
            long id = ctx.pathParamAsClass("id", Long.class)
                .check(i -> i > 0, "id must be at least 0")
                .getOrThrow((valiappor) -> new BadRequestResponse("Invalid id"));
            dao.delete(Instructor.class, id);
            ctx.status(204);
        } catch (Exception ex) {
            logger.error("Error deleting entity", ex);
            ErrorMessage error = new ErrorMessage("Error deleting entity");
            ctx.status(400).json(error);
        }
    }

    public void getLessons(@NotNull Context context) {
        try {
            long id = context.pathParamAsClass("id", Long.class)
                .check(i -> i > 0, "id must be at least 0")
                .getOrThrow((valiappor) -> new BadRequestResponse("Invalid id"));
            Instructor instructor = dao.getById(Instructor.class, id);
            context.json(instructor.getSkiLessoons());
        } catch (Exception ex) {
            logger.error("Error getting lessons", ex);
            ErrorMessage error = new ErrorMessage("Error getting lessons");
            context.status(404).json(error);
        }
    }
}
