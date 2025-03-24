package at.htl.bibliotheksverwaltung.controller;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Ein einfacher Manager für das Umschalten der Views.
 * Wir verwenden ein BorderPane und tauschen nur den center-Bereich aus.
 */
public class SceneManager {

    private static Stage primaryStage;
    private static BorderPane root;

    /**
     * Initialisiert das Hauptlayout und erstellt eine Scene.
     */
    public static void init(Stage stage) {
        primaryStage = stage;
        root = new BorderPane();

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Wechselt den center-Inhalt des BorderPane.
     */
    public static void setView(Node node) {
        root.setCenter(node);
    }
}
