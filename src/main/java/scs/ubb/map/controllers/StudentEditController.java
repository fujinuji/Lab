package scs.ubb.map.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import scs.ubb.map.controllers.alerts.MessageAlert;
import scs.ubb.map.domain.Student;
import scs.ubb.map.domain.controller.StudentControllerEntity;
import scs.ubb.map.services.service.StudentService;
import scs.ubb.map.utils.Generator;
import scs.ubb.map.validators.ValidationException;
import scs.ubb.map.validators.controller.StudentControllerValidator;

import java.util.ArrayList;
import java.util.List;

public class StudentEditController {

    private StudentService service;
    private Stage stage;
    private Student data;
    private StudentControllerValidator validator = new StudentControllerValidator();

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
    private Label idValidationLabel;

    @FXML
    private Label firstNameValidationLabel;

    @FXML
    private Label lastNameValidationLabel;

    @FXML
    private Label groupValidationLabel;

    @FXML
    private Label emailValidationLabel;

    @FXML
    public void initialize() {
        studentEmailField.setPromptText("email@example.com");

        studentIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                validator.validateId(newValue);
                idValidationLabel.setTextFill(Color.GREEN);
                idValidationLabel.setText("OK");
            } catch (ValidationException e) {
                idValidationLabel.setText(e.getMessage());
                idValidationLabel.setTextFill(Color.RED);
            }
        });

        studentFirstNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                validator.validateFirstName(newValue);
                firstNameValidationLabel.setTextFill(Color.GREEN);
                firstNameValidationLabel.setText("OK");
            } catch (ValidationException e) {
                firstNameValidationLabel.setText(e.getMessage());
                firstNameValidationLabel.setTextFill(Color.RED);
            }
        });

        studentLastNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                validator.validateLastName(newValue);
                lastNameValidationLabel.setTextFill(Color.GREEN);
                lastNameValidationLabel.setText("OK");
            } catch (ValidationException e) {
                lastNameValidationLabel.setText(e.getMessage());
                lastNameValidationLabel.setTextFill(Color.RED);
            }
        });

        studentGroupField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                validator.validateGroup(newValue);
                groupValidationLabel.setTextFill(Color.GREEN);
                groupValidationLabel.setText("OK");
            } catch (ValidationException e) {
                groupValidationLabel.setText(e.getMessage());
                groupValidationLabel.setTextFill(Color.RED);
            }
        });

        studentEmailField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                validator.validateEmail(newValue);
                emailValidationLabel.setTextFill(Color.GREEN);
                emailValidationLabel.setText("OK");
            } catch (ValidationException e) {
                emailValidationLabel.setText(e.getMessage());
                emailValidationLabel.setTextFill(Color.RED);
            }
        });

        studentEmailField.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                handleSave();
            }
        });
    }

    public void setService(StudentService service, Stage stage, Student data) {
        this.service = service;
        this.data = data;
        this.stage = stage;

        studentIdField.setEditable(false);
        if (data != null) {
            fillFields(data);
        } else {
            List<Student> students = new ArrayList<>();
            service.findAll().forEach(students::add);
            studentIdField.setText("" + Generator.generateStudentId(students));
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
        try {
            validator.validate(new StudentControllerEntity(studentIdField.getText(),
                    studentFirstNameField.getText(),
                    studentLastNameField.getText(),
                    studentGroupField.getText(),
                    studentEmailField.getText()));

            Student student = new Student(studentLastNameField.getText(),
                    studentFirstNameField.getText(),
                    studentEmailField.getText(),
                    Integer.parseInt(studentGroupField.getText()));
            student.setId(Long.parseLong(studentIdField.getText()));

            if (this.data == null) {
                saveStudent(student);
            } else {
                updateStudent(student);
            }
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }

    }

    private void saveStudent(Student student) {
        try {
            Student data = service.save(student);

            if (data == null) {
                stage.close();
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Save student", "Student has been saved");
            }
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    private void updateStudent(Student student) {
        try {
            Student data = service.update(student);

            if (data == null) {
                stage.close();
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Save student", "Student has been saved");
            }
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }
}
