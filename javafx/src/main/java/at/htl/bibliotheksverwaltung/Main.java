package at.htl.bibliotheksverwaltung;

import at.htl.bibliotheksverwaltung.controller.SceneManager;
import at.htl.bibliotheksverwaltung.database.DatabaseManager;
import at.htl.bibliotheksverwaltung.view.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Start and initialize the database
        DatabaseManager.getInstance();

        // Initialize SceneManager (creates scene and main layout)
        SceneManager.init(primaryStage);

        // Load initial view
        SceneManager.setView(new MainMenu().getView());

        primaryStage.setTitle("Bibliotheksverwaltung - Beispiel");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
