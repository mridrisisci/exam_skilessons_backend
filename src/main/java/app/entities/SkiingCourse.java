package app.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SkiingCourse
{
    private String title;
    private String description;
    private String level;
    private int durationMinutes;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

}
