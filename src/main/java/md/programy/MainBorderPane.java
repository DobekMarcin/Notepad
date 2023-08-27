package md.programy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import md.programy.controller.NotepadController;

public class MainBorderPane extends Application {

    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/MainBorderPane.fxml"));
        Pane borderPane = loader.load();
        NotepadController notepadController=loader.getController();
        notepadController.setPrimaryStage(primaryStage);
        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Notatnik");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
