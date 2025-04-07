package app.controllers;

import app.config.HibernateConfig;
import app.dao.CrudDAO;
import app.dao.InstructorDAO;
import app.dto.ErrorMessage;
import app.dto.InstructorDTO;
import app.dto.SkiLessonDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public void assignInstructorToSkiingCourse(Context ctx)
    {
        try
        {
            // get ids
            String courseIdParam = ctx.pathParam("courseId");
            String instructorIdParam = ctx.pathParam("instructorId");
            int courseId = Integer.parseInt(courseIdParam);
            int instructorId = Integer.parseInt(instructorIdParam);

            // Fetch the course and instructor from the database
            SkiLesson course = dao.getById(SkiLesson.class, courseId);
            Instructor instructor = dao.getById(Instructor.class, instructorId);

            // Check if the course and instructor exist
            if (course == null || instructor == null)
            {
                ctx.status(404).json(new ErrorMessage("Course or instructor not found"));
                return;
            }

            // Assign the instructor to the course
            course.setInstructor(instructor);
            dao.update(course);
            ctx.status(200).json(new InstructorDTO(instructor));
        } catch (Exception e)
        {
            logger.error("Error assigning instructor to skiing course", e);
            ErrorMessage error = new ErrorMessage("Error assigning instructor to skiing course");
            ctx.status(404).json(error);
        }
    }

    public void getOverviewOFSkiingCourseByInstructor(Context ctx)
    {
        List<SkiingCourseDTO> skiingCourses;
        List<SkiingCourseDTO> filteredSkiingCourses;

        try
        {
            String json = dataAPIReader.getDataFromClient(BASE_URL);
            JsonNode node = objectMapper.readValue(json, JsonNode.class);
            skiingCourses = objectMapper.convertValue(node, new TypeReference<List<SkiingCourseDTO>>()
            {
            });

            // get instructor id
            String param = ctx.pathParam("instructorId");
            Integer instructorId = Integer.parseInt(param);

            // filter courses based on instructorId
            filteredSkiingCourses = skiingCourses.stream()
                .filter(course -> course.getInstructorId() != null && course.getInstructorId().equals(instructorId))
                .collect(Collectors.toList());

            List<String> newList = skiingCourses.stream()
                .map(course -> course.getTitle())
                .collect(Collectors.toList());

            // convert to proper json response
            Map<String, Object> response = new HashMap<>();
            response.put("instructorId", instructorId);
            response.put("Course title", newList);

            ctx.json(response);

        } catch (Exception e)
        {
            logger.error("Error getting overview of skiing courses by instructor", e);
            ErrorMessage error = new ErrorMessage("Error getting overview of skiing courses by instructor");
            ctx.status(404).json(error);
        }
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

    public void getTotalDurationForASkiingCourse(Context ctx)
    {
        List<SkiingCourseDTO> skiingCourses;
        try
        {
            String json = dataAPIReader.getDataFromClient(BASE_URL);
            JsonNode node = objectMapper.readValue(json, JsonNode.class);
            String param = ctx.pathParam("level");
            skiingCourses = objectMapper.convertValue(node, new TypeReference<List<SkiingCourseDTO>>()
            {
            });

            // filter courses based on level
            List<SkiingCourseDTO> filteredCourse = skiingCourses.stream()
                .filter(course -> course.getLevel().equalsIgnoreCase(param))
                .collect(Collectors.toList());

            // calculate total duration
            int totalDuration = skiingCourses.stream()
                .mapToInt(SkiingCourseDTO::getDurationMinutes)
                .sum();

            // convert to a proper json response
            Map<String, Object> response = new HashMap<>();
            response.put("level", param);
            response.put("totalDuration", totalDuration);

            ctx.json(response);

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
            ctx.json(dao.getAll(SkiLesson.class));
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
            SkiLessonDTO foundEntity = new SkiLessonDTO(dao.getById(SkiLesson.class, id));
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
            SkiLessonDTO incomingInstructor = ctx.bodyAsClass(SkiLessonDTO.class);
            SkiLesson entity = new SkiLesson(incomingInstructor);
            SkiLesson createdEntity = dao.create(entity);
            for (SkiLesson lesson : entity.getInstructor().getSkiLessoons())
            {
                lesson.setInstructor(createdEntity.getInstructor());
                dao.update(lesson);
            }
            ctx.json(new InstructorDTO(createdEntity.getInstructor()));
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
            SkiLessonDTO incomingEntity = ctx.bodyAsClass(SkiLessonDTO.class);
            SkiLesson entityToUpdate = dao.getById(SkiLesson.class, id);
            if (incomingEntity.getEndTime() != null)
            {
                entityToUpdate.setEndTime(incomingEntity.getEndTime());
            }
            if (incomingEntity.getInstructor() != null)
            {
                entityToUpdate.setInstructor(incomingEntity.getInstructor());
            }
            SkiLesson updatedEntity = dao.update(entityToUpdate);
            SkiLessonDTO returnedEntity = new SkiLessonDTO(updatedEntity);
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
            SkiLesson lesson = dao.getById(SkiLesson.class, id);
            context.json(lesson.getInstructor().getSkiLessoons());
        } catch (Exception ex)
        {
            logger.error("Error getting lessons", ex);
            ErrorMessage error = new ErrorMessage("Error getting lessons");
            context.status(404).json(error);
        }
    }
}
