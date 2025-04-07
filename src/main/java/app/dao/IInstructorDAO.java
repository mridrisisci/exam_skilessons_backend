package app.dao;

import app.entities.Instructor;
import app.entities.SkiLesson;

import java.util.List;

public interface IInstructorDAO {
    Instructor getInstructorById(Long id);
    Instructor addLesson(Instructor instructor, SkiLesson lesson);
    Instructor removeLesson(Instructor instructor, SkiLesson lesson);
    List<SkiLesson> getLessonsForInstructor(Instructor instructor);
}
