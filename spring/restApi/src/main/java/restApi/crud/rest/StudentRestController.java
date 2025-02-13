package restApi.crud.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import restApi.crud.entity.Student;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentRestController {

    @GetMapping("/students")
    public List<Student> getStudents() {
        List<Student> theStudents = new ArrayList<Student>();

        theStudents.add(new Student("John", "Doe"));
        theStudents.add(new Student("Jana", "Doe"));
        theStudents.add(new Student("lani", "Doe"));

        return theStudents;

    }
}
