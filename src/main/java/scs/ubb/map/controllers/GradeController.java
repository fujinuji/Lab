package scs.ubb.map.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import scs.ubb.map.controllers.alerts.MessageAlert;
import scs.ubb.map.domain.Grade;
import scs.ubb.map.domain.Homework;
import scs.ubb.map.domain.Student;
import scs.ubb.map.domain.controller.GradeEntityController;
import scs.ubb.map.handlers.GradeHandler;
import scs.ubb.map.handlers.HomeworkHandler;
import scs.ubb.map.services.service.GradeService;
import scs.ubb.map.services.service.HomeworkService;
import scs.ubb.map.services.service.StudentService;
import scs.ubb.map.utils.AcademicYear;
import scs.ubb.map.utils.Constants;
import scs.ubb.map.validators.ValidationException;
import scs.ubb.map.validators.controller.GradeControllerValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GradeController {

    private GradeService gradeService;
    private StudentService studentService;
    private HomeworkService homeworkService;

    private ObservableList<GradeEntityController> tableModel = FXCollections.observableArrayList();
    private ObservableList<Student> listViewModel = FXCollections.observableArrayList();
    private ObservableList<Homework> homeworkComboBoxList = FXCollections.observableArrayList();

    private Grade studentGrade = new Grade("", 0, LocalDate.now(), 1L, 0);
    private GradeHandler gradeHandler;
    private GradeControllerValidator gradeValidator = new GradeControllerValidator();

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
    private Pane gradeModificationPane;

    @FXML
    private Pane constraintsCheckBoxPane;

    @FXML
    private Pane absenceDatePane;

    @FXML
    private Pane teacherDatePane;

    @FXML
    private TextArea feedbackText;

    @FXML
    private TextField gradeTextField;

    @FXML
    private CheckBox absenceCheckBox;

    @FXML
    private CheckBox gradeDateInputCheckBox;

    @FXML
    private DatePicker teacherDatePicker;

    @FXML
    private Pane gradeSavePane;

    @FXML
    private Pane confirmGradePane;



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

        homeworkComboBox.setConverter((new StringConverter<Homework>() {
            @Override
            public String toString(Homework object) {
                if (object != null)
                    return object.getDescription();
                return null;
            }

            @Override
            public Homework fromString(String string) {
                return null;
            }
        }));

        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        studentGradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        homeworkColumn.setCellValueFactory(new PropertyValueFactory<>("homework"));
        presenationDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("teacher"));

        gradeTextField.setText("10");

        gradesTableView.setItems(tableModel);
        studentListView.setItems(listViewModel);
        homeworkComboBox.setItems(homeworkComboBoxList);
        noGradeConstraintsPaneHide();
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
        homeworkComboBox.setValue(HomeworkHandler.getCurrentHomework(homeworkService));
    }

    public void setServices(GradeService gradeService, StudentService studentService, HomeworkService homeworkService) {
        this.gradeService = gradeService;
        this.studentService = studentService;
        this.homeworkService = homeworkService;
        gradeHandler = new GradeHandler(homeworkService);
        updateModel();
        setHomeworkComboBox();
    }

    private void filterStudentsByName(String start) {
        List<Student> students = new ArrayList<>();
        studentService.findAll().forEach(students::add);
        listViewModel.setAll(students.stream()
                .filter(student -> student.getFirstName().toLowerCase().startsWith(start.toLowerCase()) ||
                        student.getLastName().toLowerCase().startsWith(start.toLowerCase()))
                .collect(Collectors.toList()));
    }

    private void setHomeworkComboBox() {
        List<Homework> homeworkList = new ArrayList<>();
        homeworkService.findAll().forEach(homeworkList::add);
        homeworkComboBoxList.setAll(homeworkList);
    }

    private void noGradeConstraintsPaneHide() {
        gradeModificationPane.setVisible(false);
        constraintsCheckBoxPane.setVisible(false);
        absenceDatePane.setVisible(false);
        teacherDatePane.setVisible(false);
        feedbackText.setWrapText(true);
        feedbackText.setPrefHeight(feedbackText.getPrefHeight() * 2);
    }

    private void reverse() {
        gradeModificationPane.setVisible(true);
        feedbackText.setPrefHeight(feedbackText.getPrefHeight() / 2);
    }

    private void showConstraintsCheckBox() {
        gradeModificationPane.setVisible(true);
        constraintsCheckBoxPane.setVisible(true);
        feedbackText.setPrefHeight(feedbackText.getPrefHeight() / 2);
    }

    @FXML
    public void studentListViewMouseClicked() {
    }

    @FXML
    public void handleClick() {
        studentGrade.setGrade(Float.parseFloat(gradeTextField.getText()));
        studentGrade.setTeacher("Teacher1");
        studentGrade.setHomeworkId(homeworkComboBox.getSelectionModel().getSelectedItem().getId());
        studentGrade.setStudentId(studentListView.getSelectionModel().getSelectedItem().getId());
        try {
            gradeValidator.studentAlreadyHasGrade(gradeService, studentListView.getSelectionModel().getSelectedItem(),
                    homeworkComboBox.getSelectionModel().getSelectedItem());

            gradeService.save(studentGrade, studentService, homeworkService, feedbackText.getText());
            updateModel();
            studentGrade = new Grade("", 0, LocalDate.now(), 1L, 0);
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    @FXML
    public void comboBoxSelectionChange(ActionEvent event) {
        Homework homework = homeworkComboBox.getSelectionModel().getSelectedItem();
        int currentWeek = AcademicYear.getInstance().getCurrentWeek();

        if (homework.getDeadlineWeek() < currentWeek) {
            String grade = gradeTextField.getText();
            Map<String, Object> constraints = new HashMap<>();
            studentGrade.setGrade(Float.parseFloat(grade));
            studentGrade.setHomeworkId(homework.getId());
            studentGrade.setDate(LocalDate.now());
            studentGrade = gradeHandler.getGradeWithConstraints(studentGrade, constraints);
            gradeTextField.setText("" + studentGrade.getGrade());
            showConstraintsCheckBox();
        } else {

        }
    }

    @FXML
    public void absenceCheckBoxOnAction(ActionEvent event) {
        if (absenceCheckBox.isSelected()) {
            absenceDatePane.setVisible(true);
        } else {
            absenceDatePane.setVisible(false);
        }
    }


    @FXML
    public void gradeDateInputCheckBoxOnAction(ActionEvent event) {
        if (gradeDateInputCheckBox.isSelected()) {
            teacherDatePane.setVisible(true);
        } else {
            teacherDatePane.setVisible(false);
        }
    }

    @FXML
    public void teacherDatePickerOnAction(ActionEvent event) {
        LocalDate date = teacherDatePicker.getValue();
        studentGrade.setDate(date);
        Map<String, Object> constraints = new HashMap<>();
        constraints.put("teacherFault", null);
        studentGrade = gradeHandler.getGradeWithConstraints(studentGrade, constraints);
        gradeTextField.setText("" + studentGrade.getGrade());
    }


    @FXML
    public void absenceDatePickerOnAction(ActionEvent event) {
        LocalDate date = teacherDatePicker.getValue();
        Homework homework = homeworkComboBox.getSelectionModel().getSelectedItem();
        int week = AcademicYear.getInstance().getSemesterWeek(date);

        if(homework.getDeadlineWeek() < week && homework.getStartWeek() < week) {
        }
    }
}

/*
    private ObservableList<Homework> homeworkComboBoxList = FXCollections.observableArrayList();

    initialize() {
        homeworkComboBox.setConverter((new StringConverter<Homework>() {
            @Override
            public String toString(Homework object) {
                if (object != null)
                    return object.getDescription();
                return null;
            }

            @Override
            public Homework fromString(String string) {
                return null;
            }
        }));

        homeworkComboBox.setItems(homeworkComboBoxList);
    }

    setService(....) {
        List<Homework> homeworkList = new ArrayList<>();
        homeworkService.findAll().forEach(homeworkList::add);
        homeworkComboBoxList.setAll(homeworkList);
    }
 */