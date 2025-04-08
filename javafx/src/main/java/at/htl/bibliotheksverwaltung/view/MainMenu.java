package at.htl.bibliotheksverwaltung.view;

import at.htl.bibliotheksverwaltung.controller.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MainMenu {

    public VBox getView() {
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);

        // Creating the buttons
        Button rueckgabeBtn = new Button("Buch Rückgabe");
        Button statistikBtn = new Button("Statistik");
        Button verwaltungBtn = new Button("Verwaltung");
        Button ausleihenBtn = new Button("Buch Ausleihen");
        Button kundeAnlegenBtn = new Button("Kunde Anlegen");

        // Set preferred width and button styles
        Button[] buttons = {rueckgabeBtn, statistikBtn, verwaltungBtn, ausleihenBtn, kundeAnlegenBtn};
        for (Button button : buttons) {
            button.setPrefWidth(200);
            button.setStyle(
                    "-fx-background-color: #4682B4; " +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 14px; " +
                            "-fx-font-weight: bold; "
            );
        }

        // Navigation actions for each button
        rueckgabeBtn.setOnAction(e -> SceneManager.setView(new BuchRueckgabe().getView()));
        statistikBtn.setOnAction(e -> SceneManager.setView(new Statistik().getView()));
        verwaltungBtn.setOnAction(e -> SceneManager.setView(new BuecherVerwalten().getView()));
        ausleihenBtn.setOnAction(e -> SceneManager.setView(new BuchAusleihen().getView()));
        kundeAnlegenBtn.setOnAction(e -> SceneManager.setView(new KundeAnlegen().getView()));

        // Add buttons to the VBox
        vbox.getChildren().addAll(
                rueckgabeBtn,
                statistikBtn,
                verwaltungBtn,
                ausleihenBtn,
                kundeAnlegenBtn
        );

        return vbox;
    }
}
