package scs.ubb.map.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import scs.ubb.map.controllers.alerts.MessageAlert;
import scs.ubb.map.domain.Student;
import scs.ubb.map.services.service.StudentService;

public class StudentEditController {

    private StudentService service;
    private Stage stage;
    private Student data;

    @FXML
    private TextField studentIdField;

    @FXML
    private TextField studentFirstNameField;

    @FXML
    private TextField studentLastNameField;

    @FXML
    private TextField studentGroupField;

    @FXML
    private TextField studentEmailField;

    @FXML
    public void initialize(){
        studentEmailField.setPromptText("email@example.com");
    }

    public void setService(StudentService service, Stage stage, Student data) {
        this.service = service;
        this.data = data;
        this.stage = stage;

        if(data != null) {
            fillFields(data);
            studentIdField.setEditable(false);
        }
    }

    private void fillFields(Student student) {
        studentIdField.setText("" + student.getId());
        studentFirstNameField.setText(student.getFirstName());
        studentLastNameField.setText(student.getLastName());
        studentGroupField.setText("" + student.getGroup());
        studentEmailField.setText(student.getEmail());
    }

    @FXML
    public void handleSave() {


        Student student = new Student(studentLastNameField.getText(),
                                    studentFirstNameField.getText(),
                                    studentEmailField.getText(),
                                    Integer.parseInt(studentGroupField.getText()));
        student.setId(Long.parseLong(studentIdField.getText()));

        if(this.data == null) {
            saveStudent(student);
        } else {
            updateStudent(student);
        }
    }

    private void saveStudent(Student student) {
        try {
            Student data = service.save(student);

            if(data == null) {
                stage.close();
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Save student", "Student has been saved");
            }
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    private void updateStudent(Student student) {
        try {
            Student data = service.update(student);

            if(data == null) {
                stage.close();
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Save student", "Student has been saved");
            }
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }
}
