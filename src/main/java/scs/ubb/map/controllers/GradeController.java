package scs.ubb.map.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import scs.ubb.map.domain.Grade;
import scs.ubb.map.domain.Homework;
import scs.ubb.map.domain.Student;
import scs.ubb.map.domain.controller.GradeEntityController;
import scs.ubb.map.services.service.GradeService;
import scs.ubb.map.services.service.HomeworkService;
import scs.ubb.map.services.service.StudentService;
import scs.ubb.map.utils.Constants;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GradeController {

    private GradeService gradeService;
    private StudentService studentService;
    private HomeworkService homeworkService;

    private ObservableList<GradeEntityController> tableModel = FXCollections.observableArrayList();
    private ObservableList<Student> listViewModel = FXCollections.observableArrayList();

    @FXML
    private ListView<Student> studentListView;

    @FXML
    private TableColumn<GradeEntityController, String> studentNameColumn;

    @FXML
    private TableColumn<GradeEntityController, String> studentGradeColumn;

    @FXML
    private TableColumn<GradeEntityController, String> homeworkColumn;

    @FXML
    private TableColumn<GradeEntityController, String> presenationDateColumn;

    @FXML
    private TableColumn<GradeEntityController, String> deadlineColumn;

    @FXML
    private TableColumn<GradeEntityController, String> teacherColumn;

    @FXML
    private TableView<GradeEntityController> gradesTableView;

    @FXML
    private TextField studentSearchBar;

    @FXML
    private ComboBox<Homework> homeworkComboBox;

    @FXML
    public void initialize() {
        studentListView.setCellFactory((ListView<Student> lv) -> {
                    return new ListCell<Student>() {
                        @Override
                        public void updateItem(Student student, boolean empty) {
                            super.updateItem(student, empty);
                            if (empty) {
                                setText(null);
                            } else {
                                setText(student.getFirstName() + " " + student.getLastName());
                            }
                        }
                    };
                }
        );

        studentSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.equals("")) {
                        filterStudentsByName(newValue);
                    } else {
                        filterStudentsByName("1");
                    }
                }
        );

        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        studentGradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        homeworkColumn.setCellValueFactory(new PropertyValueFactory<>("homework"));
        presenationDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("teacher"));

        gradesTableView.setItems(tableModel);
        studentListView.setItems(listViewModel);
    }

    public void updateModel() {
        List<Grade> grades = new ArrayList<>();
        gradeService.findAll().forEach(grades::add);

        List<GradeEntityController> entities = grades.stream()
                .map(grade -> {
                    Student student = studentService.findOne(grade.getStudentId());
                    Homework homework = homeworkService.findOne(grade.getHomeworkId());

                    return new GradeEntityController(grade.getId(),
                            student.getFirstName() + " " + student.getLastName(),
                            String.valueOf(grade.getGrade()),
                            homework.getDescription(),
                            grade.getDate().format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)),
                            String.valueOf(homework.getDeadlineWeek()),
                            grade.getTeacher());
                })
                .collect(Collectors.toList());
        tableModel.setAll(entities);
    }

    public void setServices(GradeService gradeService, StudentService studentService, HomeworkService homeworkService) {
        this.gradeService = gradeService;
        this.studentService = studentService;
        this.homeworkService = homeworkService;
        updateModel();
    }

    public void filterStudentsByName(String start) {
        List<Student> students = new ArrayList<>();
        studentService.findAll().forEach(students::add);
        listViewModel.setAll(students.stream()
                .filter(student -> student.getFirstName().startsWith(start) || student.getLastName().startsWith(start))
                .collect(Collectors.toList()));
    }
}
