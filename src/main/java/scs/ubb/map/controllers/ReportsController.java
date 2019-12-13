package scs.ubb.map.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import scs.ubb.map.domain.Homework;
import scs.ubb.map.domain.Student;
import scs.ubb.map.domain.StudentLabDTO;
import scs.ubb.map.services.service.GradeService;
import scs.ubb.map.services.service.HomeworkService;
import scs.ubb.map.services.service.StudentService;
import scs.ubb.map.utils.ReportsService;

import java.util.List;

public class ReportsController {

    private ReportsService reportsService;

    @FXML
    private TextArea dataTextArea;

    public void initialize() {
        dataTextArea.setEditable(false);
    }

    public void setService(StudentService studentService, HomeworkService homeworkService, GradeService gradeService) {
        reportsService = new ReportsService(studentService, homeworkService, gradeService);
    }

    public void averageGradeStudentsOnAction(ActionEvent event) {
        List<StudentLabDTO> studentLabDTOList = reportsService.getLabGrades();
        String text = "";
        for(StudentLabDTO studentLabDTO : studentLabDTOList) {
            Student student = studentLabDTO.getStudent();
            text += student.getFirstName() + " " + student.getLastName() + " => " + studentLabDTO.getGrade() + "\n";
        }

        dataTextArea.setText(text);
    }

    public void studentsInExamOnAction(ActionEvent event) {
        List<Student> students = reportsService.getStudentsWhichCanTakeTheExam();
        String text = "";

        for(Student student : students) {
            text += student.getFirstName() + " " + student.getLastName() + "\n";
        }
        dataTextArea.setText(text);
    }

    public void hardestGradeOnAction(ActionEvent event) {
        Homework hardestHomework = reportsService.getHardestHomework();
        dataTextArea.setText(hardestHomework.getDescription());
    }

    public void allHomeworksOnAction(ActionEvent event) {
        List<Student> students = reportsService.getStudentsWithMaxGrade();
        String text = "";

        for(Student student : students) {
            text += student.getFirstName() + " " + student.getLastName() + "\n";
        }
        dataTextArea.setText(text);
    }
}
