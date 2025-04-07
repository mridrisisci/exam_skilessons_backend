package app.utils;


import app.config.HibernateConfig;
import app.entities.Instructor;
import app.entities.SkiLesson;
import app.enums.LessonLevel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Populator
{

    // initialize instance variables here
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private Logger logger = LoggerFactory.getLogger(Populator.class);

    Instructor instructor1, instructor2, instructor3;
    SkiLesson lesson1, lesson2, lesson3, lesson4, lesson5, lesson6;

    public Populator()
    {
        // initialize dummy objects here

        instructor1 = new Instructor(null,
            "johnson",
            "johnson@example.com",
            "39393939",
            5);

        instructor2 = new Instructor(
            null,
            "smith",
            "smith@example.com",
            "29292929",
            10);

        instructor3 = new Instructor(
            null,
            "jane",
            "jane@example.com",
            "19191919",
            7);


        lesson1 = new SkiLesson(
            LocalDateTime.of(2025, 4, 7, 9, 0),
            LocalDateTime.of(2025, 4, 7, 11, 0),
            45.123456,
            12.654321,
            "Beginner Skiing",
            100,
            LessonLevel.BEGINNER);

        lesson2 = new SkiLesson(
            LocalDateTime.of(2025, 4, 7, 12, 0),
            LocalDateTime.of(2025, 4, 7, 14, 0),
            46.123456,
            13.654321,
            "Advanced Skiing",
            150,
            LessonLevel.ADVANCED);

        lesson3 = new SkiLesson(
            LocalDateTime.of(2025, 4, 7, 15, 0),
            LocalDateTime.of(2025, 4, 7, 17, 0),
            47.123456,
            14.654321,
            "Intermediate Skiing",
            120,
            LessonLevel.INTERMEDIATE);

        lesson4 = new SkiLesson(
            LocalDateTime.of(2025, 4, 8, 9, 0),
            LocalDateTime.of(2025, 4, 8, 11, 0),
            48.123456,
            15.654321,
            "Advanced Skiing",
            140,
            LessonLevel.ADVANCED);

        lesson5 = new SkiLesson(
            LocalDateTime.of(2025, 4, 8, 12, 0),
            LocalDateTime.of(2025, 4, 8, 14, 0),
            49.123456,
            16.654321,
            "Beginner Skiing",
            110,
            LessonLevel.BEGINNER);

        lesson6 = new SkiLesson(
            LocalDateTime.of(2025, 4, 8, 15, 0),
            LocalDateTime.of(2025, 4, 8, 17, 0),
            50.123456,
            17.654321,
            "Intermediate Skiing",
            130,
            LessonLevel.INTERMEDIATE);


    }


    public Map<String, Object> getEntites()
    {
        Map<String, Object> entities = new HashMap<>();
        return entities;
    }

    public void populate(EntityManagerFactory emf)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();

            // Clear existing data (if any)
            em.createQuery("DELETE FROM Instructor ").executeUpdate();
            em.createQuery("DELETE FROM SkiLesson ").executeUpdate();

            // Persist Doctor objects
            em.persist(instructor1);
            em.persist(instructor2);
            em.persist(instructor3);

            // Add appointments to doctors (bi-directional mapping)
            instructor1.addSkiLesson(lesson1);
            instructor1.addSkiLesson(lesson2);

            instructor2.addSkiLesson(lesson3);
            instructor2.addSkiLesson(lesson4);

            instructor2.addSkiLesson(lesson5);
            instructor3.addSkiLesson(lesson6);

            // Persist Appointment objects
            em.persist(lesson1);
            em.persist(lesson2);
            em.persist(lesson3);
            em.persist(lesson4);
            em.persist(lesson5);
            em.persist(lesson6);

            // Merge the doctors to ensure appointments are updated
            instructor1 = em.merge(instructor1);
            instructor2 = em.merge(instructor2);
            instructor3 = em.merge(instructor3);

            // Commit transaction
            em.getTransaction().commit();
        }
        catch (Exception e)
        {
            logger.error("Error populating database", e);
        }
    }


    // immutable ?
    public List<Instructor> getInstructors()
    {
        return List.of(instructor1, instructor2, instructor3);
    }
    public List<SkiLesson> getSkiLessons()
    {
        return List.of(lesson1, lesson2, lesson3, lesson4, lesson5, lesson6);
    }


}