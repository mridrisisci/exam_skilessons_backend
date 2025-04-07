package app.dto;

import app.entities.Instructor;
import app.entities.SkiLesson;
import app.enums.LessonLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SkiLessonDTO
{
    private Integer id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double longitude;
    private Double latitude;
    private String name;
    private Integer price;
    private LessonLevel level;
    private Instructor instructor = new Instructor();

    public SkiLessonDTO(SkiLesson skiLesson)
    {
        this.startTime = skiLesson.getStartTime();
        this.endTime = skiLesson.getEndTime();
        this.longitude = skiLesson.getLongitude();
        this.latitude = skiLesson.getLatitude();
        this.name = skiLesson.getName();
        this.price = skiLesson.getPrice();
        this.level = skiLesson.getLevel();
        this.instructor = skiLesson.getInstructor();
    }


}
