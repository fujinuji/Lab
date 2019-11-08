package scs.ubb.map;

import scs.ubb.map.domain.GradeDTO;
import scs.ubb.map.domain.Student;
import scs.ubb.map.repository.CrudRepository;
import scs.ubb.map.repository.files.StudentFileRepository;
import scs.ubb.map.repository.files.json.GradeJSONRepository;
import scs.ubb.map.repository.files.json.JSONRepository;
import scs.ubb.map.services.config.ApplicationContext;
import scs.ubb.map.validators.StudentValidator;

public class Main {
    public static void main(String[] args) {
        CrudRepository crudRepository = new StudentFileRepository(new StudentValidator(),
                ApplicationContext.getPROPERTIES().getProperty("student-data"));
        Student student = new Student("Prenume", "Nume", "Mail", 122);
        student.setId(2L);
        crudRepository.findAll().forEach(System.out::println);
        crudRepository.save(student);
        crudRepository.findAll().forEach(System.out::println);

        JSONRepository<GradeDTO> jsonRepository = new GradeJSONRepository("data/studentsGrades/");
        GradeDTO gradeDTO = new GradeDTO("Student", 5,10, 5, 6, "Perfect");
        jsonRepository.writeJson(gradeDTO);
    }
}
