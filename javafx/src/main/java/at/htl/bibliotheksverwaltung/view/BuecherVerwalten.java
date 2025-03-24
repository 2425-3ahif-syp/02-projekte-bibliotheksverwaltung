package at.htl.bibliotheksverwaltung.view;

import at.htl.bibliotheksverwaltung.controller.SceneManager;
import at.htl.bibliotheksverwaltung.model.Book;
import at.htl.bibliotheksverwaltung.model.DataStore;
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
        Button searchButton = new Button("Suchen");
        searchButton.setOnAction(e -> searchBooks());

        // "Buch Hinzufügen"
        Button addBookButton = new Button("Buch Hinzufügen");
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

        // Alle Bücher, deren Titel den Suchbegriff enthält
        List<Book> found = DataStore.books.stream()
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
        container.setStyle("-fx-border-color: #ccc; -fx-background-color: #fafafa;");

        Label title = new Label(book.getTitle());
        title.setPrefWidth(250);

        Label stars = new Label(getStarString(book.getRating()));
        stars.setStyle("-fx-text-fill: orange;");

        // Zeigt "Ausgeliehen (bis ...)" oder nichts
        Label status = new Label();
        if (book.isBorrowed()) {
            status.setText("Ausgeliehen (bis " + book.getDueDate() + ")");
        }

        // "Löschen"-Button
        Button deleteBtn = new Button("Löschen");
        deleteBtn.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> deleteBook(book));

        container.getChildren().addAll(title, stars, status, deleteBtn);
        return container;
    }

    private void deleteBook(Book book) {
        DataStore.books.remove(book);
        searchBooks(); // Liste aktualisieren
    }

    private void addNewBook() {
        // Beispiel: Füge ein neues Buch hinzu
        DataStore.addBook("Neues Buch " + System.currentTimeMillis(), 3);
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
        topBar.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc;");

        Label homeIcon = new Label("\u2302");
        homeIcon.setStyle("-fx-font-size: 20;");
        homeIcon.setOnMouseClicked(e -> SceneManager.setView(new MainMenu().getView()));

        Label title = new Label(titleText);
        title.setStyle("-fx-font-size: 18;");

        Label userIcon = new Label("\uD83D\uDC64");
        userIcon.setStyle("-fx-font-size: 20;");

        topBar.getChildren().addAll(homeIcon, title, userIcon);
        return topBar;
    }
}
