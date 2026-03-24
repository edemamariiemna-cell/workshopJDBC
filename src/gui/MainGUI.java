package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainGUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(
            getClass().getResource("/gui/Interface.fxml")
        );
        Scene scene = new Scene(root, 1300, 900);
        scene.getStylesheets().add(
            getClass().getResource("/gui/style.css").toExternalForm()
        );
        stage.setTitle("NutriTrack — Gestion Nutritionnelle");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}