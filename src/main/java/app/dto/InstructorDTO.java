package app.dto;

import app.entities.Instructor;
import app.entities.SkiLesson;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InstructorDTO
{

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Integer yearsOfExperience;
    private List<SkiLesson> skiLessoons = new ArrayList<>();

    public InstructorDTO(Instructor instructor)
    {
        this.firstName = instructor.getFirstName();
        this.lastName = instructor.getLastName();
        this.email = instructor.getEmail();
        this.phone = instructor.getPhone();
        this.yearsOfExperience = instructor.getYearsOfExperience();
        this.skiLessoons = instructor.getSkiLessoons();
    }
}
