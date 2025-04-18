package at.htl.bibliotheksverwaltung.view;

import at.htl.bibliotheksverwaltung.controller.SceneManager;
import at.htl.bibliotheksverwaltung.database.DatabaseManager;
import at.htl.bibliotheksverwaltung.model.Book;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;
import java.util.stream.Collectors;

public class BuecherVerwalten {

    private VBox root;
    private TextField searchField;
    private VBox resultList;

    public VBox getView() {
        root = new VBox(20);
        root.setPadding(new Insets(20));

        // Top-Bar
        HBox topBar = createTopBar("Verwaltung");
        root.getChildren().add(topBar);

        // Suchleiste
        HBox searchBox = new HBox(10);
        searchField = new TextField();
        searchField.setPromptText("Eingabe...");
        searchField.setStyle("-fx-border-color: #4682B4; -fx-background-color: #f0f8ff;");  // Light blue background and blue border

        Button searchButton = new Button("Suchen");
        searchButton.setStyle(
                "-fx-background-color: #4682B4; " + // Steel Blue background
                        "-fx-text-fill: white; " +           // White text color
                        "-fx-font-size: 14px; " +            // Font size
                        "-fx-font-weight: bold; "            // Bold text
        );
        searchButton.setOnAction(e -> searchBooks());

        // "Buch Hinzufügen"
        Button addBookButton = new Button("Buch Hinzufügen");
        addBookButton.setStyle(
                "-fx-background-color: #4682B4; " + // Steel Blue background
                        "-fx-text-fill: white; " +           // White text color
                        "-fx-font-size: 14px; " +            // Font size
                        "-fx-font-weight: bold; "            // Bold text
        );
        addBookButton.setOnAction(e -> addNewBook());

        searchBox.getChildren().addAll(searchField, searchButton, addBookButton);

        // Ergebnisliste (mit ScrollPane)
        resultList = new VBox(10);
        ScrollPane scrollPane = new ScrollPane(resultList);
        scrollPane.setFitToWidth(true);

        root.getChildren().addAll(searchBox, scrollPane);

        // Initial alle Bücher
        searchBooks();

        return root;
    }

    private void searchBooks() {
        String query = searchField.getText().trim().toLowerCase();
        // Retrieve all books from the database and filter by title
        List<Book> found = DatabaseManager.getInstance().getAllBooks().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(query))
                .collect(Collectors.toList());

        updateResultList(found);
    }

    private void updateResultList(List<Book> books) {
        resultList.getChildren().clear();

        for (Book b : books) {
            HBox item = createBookItem(b);
            resultList.getChildren().add(item);
        }
    }

    private HBox createBookItem(Book book) {
        HBox container = new HBox(10);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(10));
        container.setStyle("-fx-border-color: #4682B4; -fx-background-color: #f0f8ff;");  // Light blue background with blue border

        Label title = new Label(book.getTitle());
        title.setPrefWidth(250);
        title.setStyle("-fx-font-size: 16px;");

        Label stars = new Label(getStarString(book.getRating()));
        stars.setStyle("-fx-text-fill: orange;");

        Label status = new Label();
        if (book.isBorrowed()) {
            status.setText("Ausgeliehen (bis " + book.getDueDate() + ")");
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button deleteBtn = new Button("Löschen");
        deleteBtn.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> deleteBook(book));

        container.getChildren().addAll(title, stars, status, spacer, deleteBtn);
        return container;
    }

    private void deleteBook(Book book) {
        DatabaseManager.getInstance().deleteBook(book);
        searchBooks(); // Liste aktualisieren
    }

    private void addNewBook() {
        DatabaseManager.getInstance().addBook("Neues Buch " + System.currentTimeMillis(), 3);
        searchBooks();
    }

    private String getStarString(int rating) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(i < rating ? "★" : "☆");
        }
        return sb.toString();
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
