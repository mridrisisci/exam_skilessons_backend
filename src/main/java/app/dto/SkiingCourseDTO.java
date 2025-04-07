package app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class SkiingCourseDTO
{
    private String title;
    private String description;
    private String level;
    private int durationMinutes;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private Integer instructorId; // remember to exclude from DB


}
