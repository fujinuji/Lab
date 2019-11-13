package scs.ubb.map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import scs.ubb.map.repository.CrudRepository;
import scs.ubb.map.repository.files.GradeFileRepository;
import scs.ubb.map.repository.files.HomeworkFileRepository;
import scs.ubb.map.repository.files.StudentFileRepository;
import scs.ubb.map.repository.files.json.GradeJSONRepository;
import scs.ubb.map.repository.files.json.JSONRepository;
import scs.ubb.map.services.config.Config;
import scs.ubb.map.services.service.GradeService;
import scs.ubb.map.services.service.HomeworkService;
import scs.ubb.map.services.service.Service;
import scs.ubb.map.services.service.StudentService;
import scs.ubb.map.utils.AcademicYear;
import scs.ubb.map.validators.repository.GradeValidator;
import scs.ubb.map.validators.repository.HomeworkValidator;
import scs.ubb.map.validators.repository.StudentValidator;

@RunWith(JUnit4.class)
public class GradeServiceTest {
    private AcademicYear academicYear;
    private CrudRepository studentRepo;
    private CrudRepository homeworkRepo;
    private CrudRepository gradeRepo;

    private JSONRepository jsonRepository = new GradeJSONRepository("data/studentsGrades/");

    private Service studentService;
    private Service homeworkService;
    private Service gradeService;

    @Before
    public void before() {
        academicYear = new AcademicYear(Config.getProperties().getProperty("year-data"));

        studentRepo = new StudentFileRepository(new StudentValidator(),
                Config.getProperties().getProperty("student-data"));
        homeworkRepo = new HomeworkFileRepository(new HomeworkValidator(),
                Config.getProperties().getProperty("homework-data"));
        gradeRepo = new GradeFileRepository(new GradeValidator(),
                Config.getProperties().getProperty("grade-data"));

        studentService = new StudentService(studentRepo);
        homeworkService = new HomeworkService(homeworkRepo);
        gradeService = new GradeService(gradeRepo, jsonRepository);
        System.out.println("Aici");
    }

    @Test
    public void testNimic() {
        System.out.println("Aici2");
    }
}
