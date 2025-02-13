package restApi.crud.rest;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restApi.crud.entity.Student;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentRestController {

    private List<Student> theStudents;


    @PostConstruct
    public void loadData() {
        theStudents = new ArrayList<>();

        theStudents.add(new Student("John", "Doe"));
        theStudents.add(new Student("Jana", "Doe"));
        theStudents.add(new Student("lani", "Doe"));
    }

    @GetMapping("/students")
    public List<Student> getStudents() {

        return theStudents;

    }

    @GetMapping("/students/{studentId}")
    public Student getStudent(@PathVariable int studentId) {

        return theStudents.get(studentId);
    }
}
