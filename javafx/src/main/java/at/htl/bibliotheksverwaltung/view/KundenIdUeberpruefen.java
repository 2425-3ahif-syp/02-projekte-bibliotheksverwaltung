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

public class KundenIdUeberpruefen {


    private VBox root;
    private TextField idField;
    private TextField idConfirmField;
    private Label infoLabel;
    private Integer customerId;

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

        // Input fields
        idField = createStyledTextField("ID");
        idConfirmField = createStyledTextField("ID bestätigen");

        // Layouts
        VBox idBox = new VBox(10, idField, idConfirmField);

        // Submit button
        Button confirmButton = new Button("ID bestätigen");
        confirmButton.setPrefWidth(300);
        confirmButton.setStyle(
                "-fx-background-color: #007BFF; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold;"
        );
        confirmButton.setOnAction(e -> confirm(idField.getText(), idConfirmField.getText()));

        Button backButton = new Button("Kunden-Liste anzeigen");
        backButton.setPrefWidth(300);
        backButton.setStyle(
                "-fx-background-color: #007BFF; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold;"
        );
        backButton.setOnAction(e -> SceneManager.setView(new KundenAnzeigen().getView()));


        infoLabel = new Label();
        infoLabel.setStyle("-fx-text-fill: #4682B4;");

        VBox formBox = new VBox(20, idBox, confirmButton,backButton, infoLabel);
        formBox.setAlignment(Pos.CENTER);

        // Form Container Box
        VBox formContainer = new VBox(formBox);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setStyle("-fx-border-color: black; -fx-padding: 20;");

        root.getChildren().add(formContainer);
        return root;
    }

    private boolean checkID(String id, String confirmId) {
        DatabaseManager.getInstance().printAllCustomerIDs();
        if (id == null || id.isEmpty() || confirmId == null || confirmId.isEmpty()) {
            infoLabel.setText("Bitte alle Felder ausfüllen!");
            return false;
        }
        if (!id.equals(confirmId)) {
            infoLabel.setText("Die beiden IDs stimmen nicht überein!");
            return false;
        }
        if (!id.trim().matches("\\d+")) {
            infoLabel.setText("Nur Ziffern sind in der ID erlaubt!");
            return false;
        }
        if (!DatabaseManager.getInstance().customerExists(Integer.parseInt(id.trim()))) {
            infoLabel.setText("Es gibt keinen Kunden mit dieser ID!");
            return false;
        }
        return true;
    }

    private void confirm(String id, String confirmId) {
        if (checkID(id, confirmId)){
            customerId = Integer.parseInt(idField.getText().trim());
            SceneManager.setView(new KundeBearbeiten(customerId).getView());
        }
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
