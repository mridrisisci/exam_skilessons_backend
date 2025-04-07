package app.entities;

import app.dto.SkiLessonDTO;
import app.enums.LessonLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "ski_lessons")
public class SkiLesson
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "end_time")
    private LocalDateTime endTime;

    private Double longitude;
    private Double latitude;
    private String name;
    private Integer price;

    @Enumerated(EnumType.STRING)
    private LessonLevel level;

    @ManyToOne
    @ToString.Exclude
    @JsonManagedReference
    @JoinColumn(name = "instructor_id")
    private Instructor instructor = new Instructor();

    public SkiLesson(SkiLessonDTO skiLessonDTO)
    {
        this.id = skiLessonDTO.getId();
        this.startTime = skiLessonDTO.getStartTime();
        this.endTime = skiLessonDTO.getEndTime();
        this.longitude = skiLessonDTO.getLongitude();
        this.latitude = skiLessonDTO.getLatitude();
        this.name = skiLessonDTO.getName();
        this.price = skiLessonDTO.getPrice();
        this.level = skiLessonDTO.getLevel();
        this.instructor = skiLessonDTO.getInstructor();
    }

    public SkiLesson(LocalDateTime startTime, LocalDateTime endTime, Double longitude, Double latitude, String name, Integer price, LessonLevel level)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        this.price = price;
        this.level = level;
    }

}
