package at.htl.bibliotheksverwaltung;

import at.htl.bibliotheksverwaltung.controller.SceneManager;
import at.htl.bibliotheksverwaltung.database.DatabaseManager;
import at.htl.bibliotheksverwaltung.view.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseManager.getInstance();

        SceneManager.init(primaryStage);

        SceneManager.setView(new MainMenu().getView());

        primaryStage.setTitle("Bibliotheksverwaltung - Beispiel");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
