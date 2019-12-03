package scs.ubb.map.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scs.ubb.map.controllers.alerts.MessageAlert;
import scs.ubb.map.domain.Student;
import scs.ubb.map.services.service.StudentService;
import scs.ubb.map.utils.events.StudentChangeEvent;
import scs.ubb.map.utils.observers.Observer;

import java.io.IOException;
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

        studentsTable.setRowFactory(tv -> {
            TableRow<Student> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (! row.isEmpty())) {
                    Student student = row.getItem();
                    showMessageTaskEditDialog(student);
                }
            });
            return  row;
        });

        studentsTable.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.DELETE)) {
                Student student = studentsTable.getSelectionModel().getSelectedItem();
                if (student != null) {
                    service.delete(student.getId());
                }
            }
        });
    }

    private void updateModel() {
        List<Student> students = new ArrayList<>();
        Student student = new Student("Nume", "Prenume", "Mail", 222);
        student.setId(2L);
        service.save(student);
        service.findAll().forEach(students::add);
        model.setAll(students);
    }

    public void setStudentService(StudentService studentService) {
        this.service = studentService;
        service.addObserver(this);
        updateModel();
    }

    @Override
    public void update(StudentChangeEvent studentChangeEvent) {
        updateModel();
    }

    @FXML
    public void handleAddStudent() {
        showMessageTaskEditDialog(null);
    }

    @FXML
    public void handleUpdateStudent() {
        Student student = studentsTable.getSelectionModel().getSelectedItem();

        if (student == null) {
            MessageAlert.showErrorMessage(null, "You have select a student");
        } else {
            showMessageTaskEditDialog(student);
        }
    }

    @FXML
    public void handleDeleteStudent() {
        Student student = studentsTable.getSelectionModel().getSelectedItem();
        service.delete(student.getId());
    }

    public void showMessageTaskEditDialog(Student messageTask) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/editStudentView.fxml"));

            AnchorPane root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Student");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            StudentEditController studentEdiController = loader.getController();
            studentEdiController.setService(service, dialogStage, messageTask);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
