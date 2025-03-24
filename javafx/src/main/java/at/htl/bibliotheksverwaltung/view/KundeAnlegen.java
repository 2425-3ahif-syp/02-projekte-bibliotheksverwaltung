package at.htl.bibliotheksverwaltung.view;

import at.htl.bibliotheksverwaltung.controller.SceneManager;
import at.htl.bibliotheksverwaltung.model.DataStore;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class KundeAnlegen {

    private VBox root;
    private TextField firstNameField;
    private TextField lastNameField;
    private Label infoLabel;

    public VBox getView() {
        root = new VBox(20);
        root.setPadding(new Insets(20));

        // Top-Bar
        HBox topBar = createTopBar("Kunden anlegen");
        root.getChildren().add(topBar);

        // Formular
        firstNameField = new TextField();
        firstNameField.setPromptText("Vorname");
        lastNameField = new TextField();
        lastNameField.setPromptText("Nachname");

        Button addButton = new Button("Hinzufügen");
        addButton.setOnAction(e -> addCustomer());

        infoLabel = new Label();

        VBox formBox = new VBox(10, firstNameField, lastNameField, addButton, infoLabel);
        formBox.setAlignment(Pos.CENTER_LEFT);

        root.getChildren().add(formBox);
        return root;
    }

    private void addCustomer() {
        String first = firstNameField.getText().trim();
        String last = lastNameField.getText().trim();

        if (first.isEmpty() || last.isEmpty()) {
            infoLabel.setText("Bitte Vor- und Nachname eingeben!");
            return;
        }

        DataStore.addCustomer(first, last);
        infoLabel.setText("Kunde erfolgreich hinzugefügt!");

        // Felder leeren
        firstNameField.clear();
        lastNameField.clear();
    }

    private HBox createTopBar(String titleText) {
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc;");

        Label homeIcon = new Label("\u2302"); // Home
        homeIcon.setStyle("-fx-font-size: 20;");
        homeIcon.setOnMouseClicked(e -> SceneManager.setView(new MainMenu().getView()));

        Label title = new Label(titleText);
        title.setStyle("-fx-font-size: 18;");

        Label userIcon = new Label("\uD83D\uDC64"); // User
        userIcon.setStyle("-fx-font-size: 20;");

        topBar.getChildren().addAll(homeIcon, title, userIcon);
        return topBar;
    }
}
