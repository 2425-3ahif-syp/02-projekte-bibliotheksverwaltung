package at.htl.bibliotheksverwaltung;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import at.htl.bibliotheksverwaltung.controller.SceneManager;
import at.htl.bibliotheksverwaltung.view.MainMenu;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // SceneManager initialisieren (legt ein BorderPane + Scene an)
        SceneManager.init(primaryStage);

        // Hauptmenü als Start-Ansicht setzen
        SceneManager.setView(new MainMenu().getView());

        primaryStage.setTitle("Bibliotheksverwaltung - Beispiel");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
