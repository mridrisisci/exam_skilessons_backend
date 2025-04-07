package app.dao;

import app.entities.Instructor;
import app.entities.SkiLesson;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class SkiLessonDAO extends GenericDAO implements ISkiLessonDAO {

    public SkiLessonDAO(EntityManagerFactory emf) {
        super(emf);
    }

    public List<SkiLesson> getAllSkiLessons() {
        return super.getAll(SkiLesson.class);
    }

    public SkiLesson getSkiLessonById(Long id) {
        return super.getById(SkiLesson.class, id);
    }

    public SkiLesson createSkiLesson(SkiLesson skiLesson) {
        return super.create(skiLesson);
    }

    public SkiLesson updateSkiLesson(SkiLesson skiLesson) {
        return super.update(skiLesson);
    }

    public void deleteSkiLesson(Long id) {
        super.delete(SkiLesson.class, id);
    }

    @Override
    public SkiLesson addInstructorToLesson(SkiLesson skiLesson, Instructor instructor) {
        skiLesson.setInstructor(instructor); // Assuming there's a setInstructor method in SkiLesson class
        return update(skiLesson); // Updating the SkiLesson with the new instructor
    }

    @Override
    public SkiLesson removeInstructorFromLesson(SkiLesson skiLesson, Instructor instructor) {
        skiLesson.setInstructor(null); // Assuming there's a setInstructor method in SkiLesson class
        SkiLesson updatedEntity = update(skiLesson);
        return updatedEntity;
    }

    @Override
    public List<SkiLesson> getLessonsForInstructor(Instructor instructor) {
        return instructor.getSkiLessoons(); // Assuming Instructor has a getSkiLessoons() method
    }

    // New methods
    public void assignInstructorToSkiLesson(int lessonId, int instructorId) {
        SkiLesson lesson = getById(SkiLesson.class, (long) lessonId);
        Instructor instructor = getById(Instructor.class, (long) instructorId);

        if (lesson != null && instructor != null) {
            lesson.setInstructor(instructor);
            update(lesson); // Updating the lesson with the new instructor
        }
    }
}
