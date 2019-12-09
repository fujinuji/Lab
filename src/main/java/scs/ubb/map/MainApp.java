package scs.ubb.map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.xml.sax.SAXException;
import scs.ubb.map.controllers.GradeController;
import scs.ubb.map.controllers.StudentController;
import scs.ubb.map.domain.Grade;
import scs.ubb.map.domain.Homework;
import scs.ubb.map.domain.Student;
import scs.ubb.map.repository.CrudRepository;
import scs.ubb.map.repository.HomeworkRepository;
import scs.ubb.map.repository.StudentRepository;
import scs.ubb.map.repository.files.GradeFileRepository;
import scs.ubb.map.repository.files.HomeworkFileRepository;
import scs.ubb.map.repository.files.StudentFileRepository;
import scs.ubb.map.repository.files.json.GradeJSONRepository;
import scs.ubb.map.repository.xml.StudentXMLRepository;
import scs.ubb.map.services.config.Config;
import scs.ubb.map.services.service.GradeService;
import scs.ubb.map.services.service.HomeworkService;
import scs.ubb.map.services.service.StudentService;
import scs.ubb.map.utils.AcademicYear;
import scs.ubb.map.validators.repository.GradeValidator;
import scs.ubb.map.validators.repository.HomeworkValidator;
import scs.ubb.map.validators.repository.StudentValidator;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AcademicYear a = new AcademicYear(Config.getProperties().getProperty("year-data"));
        gradeScene(primaryStage);
        primaryStage.setWidth(850);
        primaryStage.show();
    }

    public void studentScene(Stage primaryStage) throws Exception {
        FXMLLoader studentLoader = new FXMLLoader();
        studentLoader.setLocation(getClass().getResource("/views/studentView.fxml"));
        AnchorPane root = studentLoader.load();
        primaryStage.setScene(new Scene(root));

        StudentController controller = studentLoader.getController();
        controller.setStudentService(new StudentService(new StudentXMLRepository(new StudentValidator(),
                Config.getProperties().getProperty("student-data-xml"))));
    }

    public void gradeScene(Stage primaryStage) throws Exception{
        FXMLLoader gradeLoader = new FXMLLoader();
        gradeLoader.setLocation(getClass().getResource("/views/studentsGradeView.fxml"));
        AnchorPane root = gradeLoader.load();
        primaryStage.setScene(new Scene(root));

        CrudRepository<Long, Student> studentRepository = new StudentXMLRepository(new StudentValidator(),
                Config.getProperties().getProperty("student-data-xml"));
        CrudRepository<Integer, Homework> homeworkRepository = new HomeworkFileRepository(new HomeworkValidator(),
                Config.getProperties().getProperty("homework-data"));
        CrudRepository<String, Grade> gradeRepository = new GradeFileRepository(new GradeValidator(studentRepository, homeworkRepository),
                Config.getProperties().getProperty("grade-data"));

        StudentService studentService = new StudentService(studentRepository);
        HomeworkService homeworkService = new HomeworkService(homeworkRepository);
        GradeService gradeService = new GradeService(gradeRepository,
                new GradeJSONRepository(Config.getProperties().getProperty("student-grades-json")));

        GradeController controller = gradeLoader.getController();
        controller.setServices(gradeService, studentService, homeworkService);
    }
}
