package at.htl.bibliotheksverwaltung.view;

import at.htl.bibliotheksverwaltung.controller.SceneManager;
import at.htl.bibliotheksverwaltung.database.DatabaseManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class KundeBearbeiten {

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
    private int customerId;

    public KundeBearbeiten(int customerId) {
        this.customerId = customerId;
    }

    private TextField createStyledTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle("-fx-border-color: black; -fx-background-color: white;");
        return tf;
    }

    public VBox getView() {
        root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        // Top-Bar
        HBox topBar = createTopBar("Kunde bearbeiten");
        root.getChildren().add(topBar);

        // Profile picture placeholder
        Label profileImage = new Label("\uD83D\uDC64");
        profileImage.setStyle("-fx-font-size: 80; -fx-text-fill: black;");
        VBox imageBox = new VBox(profileImage);
        imageBox.setPrefSize(120, 120);
        imageBox.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        imageBox.setAlignment(Pos.CENTER);

        // Input fields
        firstNameField = createStyledTextField("Vorname");
        lastNameField = createStyledTextField("Nachname");
        birthDayField = createStyledTextField("Tag");
        birthMonthField = createStyledTextField("Monat");
        birthYearField = createStyledTextField("Jahr");
        streetField = createStyledTextField("Straße");
        regionField = createStyledTextField("Ort");
        plzField = createStyledTextField("Plz");

        getCustomer();

        // Layouts
        VBox nameBox = new VBox(10, firstNameField, lastNameField);
        HBox birthBox = new HBox(10, birthDayField, birthMonthField, birthYearField);
        HBox addressBox = new HBox(10, streetField, regionField, plzField);

        VBox formFields = new VBox(15, nameBox, birthBox, addressBox);
        formFields.setAlignment(Pos.CENTER_LEFT);

        HBox formMain = new HBox(20, imageBox, formFields);
        formMain.setAlignment(Pos.CENTER);

        // Submit button
        Button changeButton = new Button("Ändern");
        changeButton.setPrefWidth(300);
        changeButton.setStyle(
                "-fx-background-color: #007BFF; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold;"
        );
        changeButton.setOnAction(e -> updateCustomer());

        Button backButton = new Button("Zurück");
        backButton.setPrefWidth(300);
        backButton.setStyle(
                "-fx-background-color: #FF0000; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold;"
        );
        backButton.setOnAction(e -> SceneManager.setView(new KundeAnlegen().getView()));

        infoLabel = new Label();
        infoLabel.setStyle("-fx-text-fill: #4682B4;");

        VBox formBox = new VBox(20, formMain, changeButton, backButton, infoLabel);
        formBox.setAlignment(Pos.CENTER);

        // Form Container Box
        VBox formContainer = new VBox(formBox);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setStyle("-fx-border-color: black; -fx-padding: 20;");

        root.getChildren().add(formContainer);
        return root;
    }

    private void getCustomer() {
        var customer = DatabaseManager.getInstance().getCustomerById(customerId);
        firstNameField.setText(customer.getFirstName());
        lastNameField.setText(customer.getLastName());
        birthDayField.setText(customer.getBirthDay());
        birthMonthField.setText(customer.getBirthMonth());
        birthYearField.setText(customer.getBirthYear());
        streetField.setText(customer.getStreet());
        plzField.setText(customer.getPlz());
        regionField.setText(customer.getRegion());
    }

    private void updateCustomer() {
        String first = firstNameField.getText().trim();
        String last = lastNameField.getText().trim();
        String birthDay = birthDayField.getText().trim();
        String birthMonth = birthMonthField.getText().trim();
        String birthYear = birthYearField.getText().trim();
        String street = streetField.getText().trim();
        String plz = plzField.getText().trim();
        String region = regionField.getText().trim();

        if (first.isEmpty() || last.isEmpty() || birthDay.isEmpty() || birthMonth.isEmpty()
                || birthYear.isEmpty() || street.isEmpty() || plz.isEmpty() || region.isEmpty()) {
            infoLabel.setText("Bitte alle Felder ausfüllen!");
            return;
        }

        if (!birthDay.matches("\\d{1,2}") || Integer.parseInt(birthDay) < 1 || Integer.parseInt(birthDay) > 31) {
            infoLabel.setText("Ungültiger Geburtstag (1–31).");
            return;
        }

        if (!birthMonth.matches("\\d{1,2}") || Integer.parseInt(birthMonth) < 1 || Integer.parseInt(birthMonth) > 12) {
            infoLabel.setText("Ungültiger Geburtsmonat (1–12).");
            return;
        }

        if (!birthYear.matches("\\d{4}") || Integer.parseInt(birthYear) < 1900 || Integer.parseInt(birthYear) > 2025) {
            infoLabel.setText("Ungültiges Geburtsjahr (1900–2025).");
            return;
        }

        if (!plz.matches("\\d+")) {
            infoLabel.setText("PLZ darf nur Zahlen enthalten.");
            return;
        }

        // All inputs valid — insert into DB
        DatabaseManager.getInstance().updateCustomer( customerId,first, last, Integer.parseInt(birthDay), Integer.parseInt(birthMonth), Integer.parseInt(birthYear), street, plz, region);
        infoLabel.setText("Kunde erfolgreich geändert!");
    }

    private HBox createTopBar(String titleText) {
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #4682B4; -fx-border-color: #ccc;");

        Label homeIcon = new Label("\u2302");
        homeIcon.setStyle("-fx-font-size: 20; -fx-text-fill: white;");
        homeIcon.setOnMouseClicked(e -> SceneManager.setView(new MainMenu().getView()));

        Label title = new Label(titleText);
        title.setStyle("-fx-font-size: 18; -fx-text-fill: white;");

        Label userIcon = new Label("\uD83D\uDC64");
        userIcon.setStyle("-fx-font-size: 20; -fx-text-fill: white;");

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        topBar.getChildren().addAll(homeIcon, spacer1, title, spacer2, userIcon);
        return topBar;
    }
}
