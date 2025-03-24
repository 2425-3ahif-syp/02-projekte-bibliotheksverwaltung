package at.htl.bibliotheksverwaltung.view;

import at.htl.bibliotheksverwaltung.controller.SceneManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MainMenu {

    public VBox getView() {
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);

        Button rueckgabeBtn = new Button("Buch Rückgabe");
        Button statistikBtn = new Button("Statistik");
        Button verwaltungBtn = new Button("Verwaltung");
        Button ausleihenBtn = new Button("Buch Ausleihen");
        Button kundeAnlegenBtn = new Button("Kunde Anlegen");

        rueckgabeBtn.setPrefWidth(200);
        statistikBtn.setPrefWidth(200);
        verwaltungBtn.setPrefWidth(200);
        ausleihenBtn.setPrefWidth(200);
        kundeAnlegenBtn.setPrefWidth(200);

        // Navigation
        rueckgabeBtn.setOnAction(e -> SceneManager.setView(new BuchRueckgabe().getView()));
        statistikBtn.setOnAction(e -> SceneManager.setView(new Statistik().getView()));
        verwaltungBtn.setOnAction(e -> SceneManager.setView(new BuecherVerwalten().getView()));
        ausleihenBtn.setOnAction(e -> SceneManager.setView(new BuchAusleihen().getView()));
        kundeAnlegenBtn.setOnAction(e -> SceneManager.setView(new KundeAnlegen().getView()));

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
