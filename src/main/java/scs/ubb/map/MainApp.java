package scs.ubb.map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import scs.ubb.map.controllers.StudentController;
import scs.ubb.map.repository.files.StudentFileRepository;
import scs.ubb.map.services.config.Config;
import scs.ubb.map.services.service.StudentService;
import scs.ubb.map.validators.repository.StudentValidator;

public class MainApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader studentLoader = new FXMLLoader();
        studentLoader.setLocation(getClass().getResource("/views/studentView.fxml"));
        AnchorPane root = studentLoader.load();
        primaryStage.setScene(new Scene(root));

        StudentController controller = studentLoader.getController();
        controller.setStudentService(new StudentService(new StudentFileRepository(new StudentValidator(),
                Config.getProperties().getProperty("student-data"))));
        primaryStage.setWidth(800);
        primaryStage.show();
    }
}
