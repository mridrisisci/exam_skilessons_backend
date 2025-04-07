package app.dao;

import app.entities.Instructor;
import app.entities.SkiLesson;

import java.util.List;

public interface ISkiLessonDAO {
    SkiLesson getSkiLessonById(Long id);
    SkiLesson addInstructorToLesson(SkiLesson skiLesson, Instructor instructor);
    SkiLesson removeInstructorFromLesson(SkiLesson skiLesson, Instructor instructor);
    List<SkiLesson> getLessonsForInstructor(Instructor instructor);
}
