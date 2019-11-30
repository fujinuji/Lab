package scs.ubb.map.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import scs.ubb.map.domain.Student;
import scs.ubb.map.services.service.StudentService;
import scs.ubb.map.utils.events.StudentChangeEvent;
import scs.ubb.map.utils.observers.Observer;

import java.util.ArrayList;
import java.util.List;


public class StudentController implements Observer<StudentChangeEvent> {

    private StudentService service;
    private ObservableList<Student> model = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Student, String> lastNameColumn;
    @FXML
    private TableColumn<Student, String> firstNameColumn;
    @FXML
    private TableColumn<Student, Integer> groupColumn;
    @FXML
    private TableColumn<Student, String> emailColumn;
    @FXML
    private TableView<Student> studentsTable;

    @FXML
    private Button studentAddButton;
    @FXML
    private Button studentUpdateButton;
    @FXML
    private Button studentDeleteButton;

    @FXML
    public void initialize() {
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        studentsTable.setItems(model);
    }

    private void updateModel() {
        List<Student> students = new ArrayList<>();
        service.findAll().forEach(students::add);
        model.setAll(students);
    }

    public void setStudentService(StudentService studentService) {
        this.service = studentService;
    }

    @Override
    public void update(StudentChangeEvent studentChangeEvent) {
        updateModel();
    }
}
