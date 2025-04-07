package app.dao;

import app.entities.Instructor;
import app.entities.SkiLesson;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;


public class InstructorDAO extends GenericDAO implements IInstructorDAO {

    public InstructorDAO(EntityManagerFactory emf) {
        super(emf);
    }

    public List<Instructor> getAllInstructors() {
        return super.getAll(Instructor.class);
    }

    public Instructor getInstructorById(Long id) {
        return super.getById(Instructor.class, id);
    }

    public Instructor createInstructor(Instructor instructor) {
        return super.create(instructor);
    }

    public Instructor updateEntity(Instructor instructor) {
        return super.update(instructor);
    }

    public void deleteInstructor(Long id) {
        super.delete(Instructor.class, id);
    }

    @Override
    public Instructor addLesson(Instructor instructor, SkiLesson lesson) {
        instructor.getSkiLessoons().add(lesson);  // Assuming there's an add method in Instructor class
        return update(instructor);
    }

    @Override
    public Instructor removeLesson(Instructor instructor, SkiLesson lesson) {
        instructor.getSkiLessoons().remove(lesson);  // Assuming there's a remove method in Instructor class
        Instructor updatedEntity = update(instructor);
        delete(lesson);  // Assuming SkiLesson needs to be deleted
        return updatedEntity;
    }

    @Override
    public List<SkiLesson> getLessonsForInstructor(Instructor instructor) {
        return instructor.getSkiLessoons();
    }

    // New methods
    public void addInstructorToSkiLesson(int lessonId, int instructorId) {
        SkiLesson lesson = getById(SkiLesson.class, (long) lessonId);
        Instructor instructor = getById(Instructor.class, (long) instructorId);

        if (lesson != null && instructor != null) {
            lesson.setInstructor(instructor);
            update(lesson);  // Updating the lesson with the new instructor
        }
    }


}
