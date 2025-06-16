package at.htl.bibliotheksverwaltung.view;

import at.htl.bibliotheksverwaltung.controller.SceneManager;
import at.htl.bibliotheksverwaltung.database.DatabaseManager;
import at.htl.bibliotheksverwaltung.model.Book;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class BuchBearbeiten {

    private VBox root;
    private TextField titleField;
    private Spinner<Integer> ratingSpinner;
    private Book book;

    public BuchBearbeiten(Book book) {
        this.book = book;
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

        // Überschrift + Zurück-Button
        HBox topBar = createTopBar("Buch bearbeiten");
        root.getChildren().add(topBar);

        // Eingabefelder
        titleField = createStyledTextField("Titel");
        titleField.setText(book.getTitle());

        ratingSpinner = new Spinner<>(0, 10, book.getRating());
        ratingSpinner.setEditable(true);

        VBox form = new VBox(10,
                new Label("Titel:"),
                titleField,
                new Label("Bewertung:"),
                ratingSpinner
        );
        form.setPadding(new Insets(10));
        root.getChildren().add(form);

        // Speicher-Button
        Button saveButton = new Button("Speichern");
        saveButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white;");
        saveButton.setOnAction(e -> {
            book.setTitle(titleField.getText());
            book.setRating(ratingSpinner.getValue());
            DatabaseManager.getInstance().updateBook(book);
            SceneManager.setView(new BuecherVerwalten().getView());
        });

        root.getChildren().add(saveButton);

        return root;
    }

    private HBox createTopBar(String titleText) {
        Label title = new Label(titleText);
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button backButton = new Button("Zurück");
        backButton.setStyle("-fx-background-color: #4682B4; -fx-text-fill: white;");
        backButton.setOnAction(e -> SceneManager.setView(new BuecherVerwalten().getView()));
        return new HBox(10, title, spacer, backButton);
    }
}
