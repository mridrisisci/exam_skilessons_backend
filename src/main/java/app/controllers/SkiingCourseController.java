package app.controllers;

import app.config.HibernateConfig;
import app.dao.CrudDAO;
import app.dao.InstructorDAO;
import app.dto.ErrorMessage;
import app.dto.InstructorDTO;
import app.dto.SkiingCourseDTO;
import app.entities.Instructor;
import app.entities.SkiLesson;
import app.utils.DataAPIReader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SkiingCourseController implements IController
{
    private final CrudDAO dao;
    private DataAPIReader dataAPIReader;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String BASE_URL = "https://apiprovider.cphbusinessapps.dk/skilesson/";
    private static final Logger logger = LoggerFactory.getLogger(SkiingCourseController.class);
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    public SkiingCourseController(EntityManagerFactory emf, DataAPIReader dataAPIReader)
    {
        dao = new InstructorDAO(emf);
        this.objectMapper.registerModule(new JavaTimeModule());
        this.dataAPIReader = dataAPIReader;
    }

    public SkiingCourseController(CrudDAO dao, DataAPIReader dataAPIReader)
    {
        this.dao = dao;
        this.objectMapper.registerModule(new JavaTimeModule());
        this.dataAPIReader = dataAPIReader;
    }

    public void searchSkiingCourseByLevel(Context ctx)
    {
        List<SkiingCourseDTO> skiingCourses;
        List<SkiingCourseDTO> filteredSkiingCourses = new ArrayList<>();
        try
        {
            String json = dataAPIReader.getDataFromClient(BASE_URL);
            JsonNode node = objectMapper.readValue(json, JsonNode.class);
            skiingCourses = objectMapper.convertValue(node, new TypeReference<List<SkiingCourseDTO>>()
            {
            });
            String param = ctx.pathParam("level");
            filteredSkiingCourses = skiingCourses.stream()
                .filter(course -> course.getLevel().equalsIgnoreCase(param))
                .collect(Collectors.toList());
            ctx.json(filteredSkiingCourses);
        } catch (Exception e)
        {
            logger.error("Error getting skiing courses", e);
            ErrorMessage error = new ErrorMessage("Error getting skiing courses");
            ctx.status(404).json(error);
        }
    }

    public void getTotalDurationOfSkiingCourses(Context ctx)
    {
        List<SkiingCourseDTO> skiingCourses;
        try
        {
            String json = dataAPIReader.getDataFromClient(BASE_URL);
            JsonNode node = objectMapper.readValue(json, JsonNode.class);
            skiingCourses = objectMapper.convertValue(node, new TypeReference<List<SkiingCourseDTO>>() {});
            int totalDuration = skiingCourses.stream()
                .mapToInt(SkiingCourseDTO::getDurationMinutes)
                .sum();
            ctx.json(totalDuration);

        } catch (Exception e)
        {
            logger.error("Error getting total duration of skiing courses", e);
            ErrorMessage error = new ErrorMessage("Error getting total duration of skiing courses");
            ctx.status(404).json(error);
        }
    }

    @Override
    public void getAll(Context ctx)
    {
        try
        {
            ctx.json(dao.getAll(Instructor.class));
        } catch (Exception ex)
        {
            logger.error("Error getting entities", ex);
            ErrorMessage error = new ErrorMessage("Error getting entities");
            ctx.status(404).json(error);
        }
    }

    @Override
    public void getById(Context ctx)
    {
        try
        {
            long id = ctx.pathParamAsClass("id", Long.class)
                .check(i -> i > 0, "id must be at least 0")
                .getOrThrow((validation) -> new BadRequestResponse("Invalid id"));
            InstructorDTO foundEntity = new InstructorDTO(dao.getById(Instructor.class, id));
            ctx.json(foundEntity);

        } catch (Exception ex)
        {
            logger.error("Error getting entity", ex);
            ErrorMessage error = new ErrorMessage("No entity with that id");
            ctx.status(404).json(error);
        }
    }

    @Override
    public void create(Context ctx)
    {
        try
        {
            InstructorDTO incomingInstructor = ctx.bodyAsClass(InstructorDTO.class);
            Instructor entity = new Instructor(incomingInstructor);
            Instructor createdEntity = dao.create(entity);
            for (SkiLesson lesson : entity.getSkiLessoons())
            {
                lesson.setInstructor(createdEntity);
                dao.update(lesson);
            }
            ctx.json(new InstructorDTO(createdEntity));
        } catch (Exception ex)
        {
            logger.error("Error creating entity", ex);
            ErrorMessage error = new ErrorMessage("Error creating entity");
            ctx.status(400).json(error);
        }
    }

    public void update(Context ctx)
    {
        try
        {
            long id = ctx.pathParamAsClass("id", Long.class)
                .check(i -> i > 0, "id must be at least 0")
                .getOrThrow((valiappor) -> new BadRequestResponse("Invalid id"));
            InstructorDTO incomingEntity = ctx.bodyAsClass(InstructorDTO.class);
            Instructor entityToUpdate = dao.getById(Instructor.class, id);
            if (incomingEntity.getFirstName() != null)
            {
                entityToUpdate.setFirstName(incomingEntity.getFirstName());
            }
            if (incomingEntity.getLastName() != null)
            {
                entityToUpdate.setLastName(incomingEntity.getLastName());
            }
            Instructor updatedEntity = dao.update(entityToUpdate);
            InstructorDTO returnedEntity = new InstructorDTO(updatedEntity);
            ctx.json(returnedEntity);
        } catch (Exception ex)
        {
            logger.error("Error updating entity", ex);
            ErrorMessage error = new ErrorMessage("Error updating entity. " + ex.getMessage());
            ctx.status(400).json(error);
        }
    }

    public void delete(Context ctx)
    {
        try
        {
            long id = ctx.pathParamAsClass("id", Long.class)
                .check(i -> i > 0, "id must be at least 0")
                .getOrThrow((valiappor) -> new BadRequestResponse("Invalid id"));
            dao.delete(Instructor.class, id);
            ctx.status(204);
        } catch (Exception ex)
        {
            logger.error("Error deleting entity", ex);
            ErrorMessage error = new ErrorMessage("Error deleting entity");
            ctx.status(400).json(error);
        }
    }

    public void getLessons(@NotNull Context context)
    {
        try
        {
            long id = context.pathParamAsClass("id", Long.class)
                .check(i -> i > 0, "id must be at least 0")
                .getOrThrow((valiappor) -> new BadRequestResponse("Invalid id"));
            Instructor instructor = dao.getById(Instructor.class, id);
            context.json(instructor.getSkiLessoons());
        } catch (Exception ex)
        {
            logger.error("Error getting lessons", ex);
            ErrorMessage error = new ErrorMessage("Error getting lessons");
            context.status(404).json(error);
        }
    }
}
