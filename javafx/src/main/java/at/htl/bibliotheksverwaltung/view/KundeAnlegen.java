package at.htl.bibliotheksverwaltung.view;

import at.htl.bibliotheksverwaltung.controller.SceneManager;
import at.htl.bibliotheksverwaltung.model.DataStore;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class KundeAnlegen {

    private VBox root;
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField birthDayField;
    private TextField birthMonthField;
    private TextField birthYearField;
    private TextField streetField;
    private TextField plzField;
    private TextField regionField;
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

        HBox nameBox = new HBox(10, firstNameField, lastNameField);
        nameBox.setAlignment(Pos.CENTER_LEFT);

        birthDayField = new TextField();
        birthDayField.setPromptText("Geburtstag");
        birthMonthField = new TextField();
        birthMonthField.setPromptText("Geburtsmonat");
        birthYearField = new TextField();
        birthYearField.setPromptText("Geburtsjahr");

        HBox birthBox = new HBox(10, birthDayField, birthMonthField, birthYearField);
        birthBox.setAlignment(Pos.CENTER_LEFT);

        streetField = new TextField();
        streetField.setPromptText("Straße");
        plzField = new TextField();
        plzField.setPromptText("PLZ");
        regionField = new TextField();
        regionField.setPromptText("Region");

        HBox addressBox = new HBox(10, streetField, plzField, regionField);
        addressBox.setAlignment(Pos.CENTER_LEFT);

        Button addButton = new Button("Hinzufügen");
        addButton.setOnAction(e -> addCustomer());

        infoLabel = new Label();

        VBox formBox = new VBox(10, firstNameField, lastNameField, birthBox, addressBox, regionField, addButton, infoLabel);
        formBox.setAlignment(Pos.CENTER_LEFT);

        root.getChildren().add(formBox);
        return root;
    }

    private void addCustomer() {
        String first = firstNameField.getText().trim();
        String last = lastNameField.getText().trim();
        String birthDay = birthDayField.getText().trim();
        String birthMonth = birthMonthField.getText().trim();
        String birthYear = birthYearField.getText().trim();
        String street = streetField.getText().trim();
        String plz = plzField.getText().trim();
        String region = regionField.getText().trim();

        if (first.isEmpty() || last.isEmpty() || birthDay.isEmpty() || birthMonth.isEmpty() || birthYear.isEmpty() || street.isEmpty() || plz.isEmpty() || region.isEmpty()) {
            infoLabel.setText("Bitte alle Felder ausfüllen!");
            return;
        }

        DataStore.addCustomer(first, last, birthDay, birthMonth, birthYear, street, plz, region);
        infoLabel.setText("Kunde erfolgreich hinzugefügt!");

        // Felder leeren
        firstNameField.clear();
        lastNameField.clear();
        birthDayField.clear();
        birthMonthField.clear();
        birthYearField.clear();
        streetField.clear();
        plzField.clear();
        regionField.clear();
    }

    private HBox createTopBar(String titleText) {
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc;");

        Label homeIcon = new Label("\u2302");
        homeIcon.setStyle("-fx-font-size: 20;");
        homeIcon.setOnMouseClicked(e -> SceneManager.setView(new MainMenu().getView()));

        Label home = new Label("Home");
        home.setStyle("-fx-font-size: 18;");

        Label title = new Label(titleText);
        title.setStyle("-fx-font-size: 18;");

        Label profile = new Label("Profil");
        profile.setStyle("-fx-font-size: 18;");

        Label userIcon = new Label("\uD83D\uDC64");
        userIcon.setStyle("-fx-font-size: 20;");

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        topBar.getChildren().addAll(homeIcon,home , spacer1, title, spacer2, profile, userIcon);
        return topBar;
    }
}