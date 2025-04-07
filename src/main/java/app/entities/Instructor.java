package app.entities;

import app.dto.InstructorDTO;
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
@Entity
@Table(name = "ski_lessons")
public class Instructor
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private String phone;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @OneToMany(mappedBy = "instructor", fetch = FetchType.EAGER, cascade = {CascadeType.ALL, CascadeType.REMOVE}, orphanRemoval = true)
    @ToString.Exclude
    @JsonManagedReference
    private List<SkiLesson> skiLessoons = new ArrayList<>();

    public Instructor(InstructorDTO instructorDTO)
    {
        this.id = instructorDTO.getId();
        this.firstName = instructorDTO.getFirstName();
        this.lastName = instructorDTO.getLastName();
        this.email = instructorDTO.getEmail();
        this.phone = instructorDTO.getPhone();
        this.yearsOfExperience = instructorDTO.getYearsOfExperience();
        this.skiLessoons = instructorDTO.getSkiLessoons();
    }

    public Instructor(String firstName, String lastName, String email, String phone, Integer yearsOfExperience)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.yearsOfExperience = yearsOfExperience;
    }

    public void addSkiLesson(SkiLesson skiLesson)
    {
        skiLessoons.add(skiLesson);
        skiLesson.setInstructor(this);
    }

    public void removeSkiLesson(SkiLesson skiLesson)
    {
        skiLessoons.remove(skiLesson);
        skiLesson.setInstructor(null);
    }
}
