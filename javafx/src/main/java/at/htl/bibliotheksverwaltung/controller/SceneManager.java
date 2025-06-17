package at.htl.bibliotheksverwaltung.controller;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage primaryStage;
    private static BorderPane root;

    public static void init(Stage stage) {
        primaryStage = stage;
        root = new BorderPane();

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
    }

    public static void setView(Node node) {
        root.setCenter(node);
    }
}
