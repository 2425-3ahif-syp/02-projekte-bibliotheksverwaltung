package at.htl.bibliotheksverwaltung.view;

import at.htl.bibliotheksverwaltung.controller.SceneManager;
import at.htl.bibliotheksverwaltung.model.Book;
import at.htl.bibliotheksverwaltung.model.DataStore;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

public class BuchRueckgabe {

    private VBox root;
    private TextField searchField;
    private VBox resultList;

    public VBox getView() {
        root = new VBox(20);
        root.setPadding(new Insets(20));

        // Top-Bar
        HBox topBar = createTopBar("Buch Rückgabe");
        root.getChildren().add(topBar);

        // Suchleiste
        HBox searchBox = new HBox(10);
        searchField = new TextField();
        searchField.setPromptText("Harry Potter...");
        Button searchButton = new Button("Suchen");
        searchButton.setOnAction(e -> searchBooks());

        searchBox.getChildren().addAll(searchField, searchButton);

        resultList = new VBox(10);

        root.getChildren().addAll(searchBox, resultList);

        // Initial: Alle ausgeliehenen Bücher anzeigen
        searchBooks();
        return root;
    }

    private void searchBooks() {
        String query = searchField.getText().trim().toLowerCase();

        List<Book> borrowed = DataStore.books.stream()
                .filter(Book::isBorrowed)
                .filter(b -> b.getTitle().toLowerCase().contains(query))
                .collect(Collectors.toList());

        updateResultList(borrowed);
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
        title.setPrefWidth(300);

        Label stars = new Label(getStarString(book.getRating()));
        stars.setStyle("-fx-text-fill: orange;");

        Label status = new Label("Ausgeliehen (bis " + book.getDueDate() + ")");

        Button returnBtn = new Button("Rückgabe");
        returnBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        returnBtn.setOnAction(e -> returnBook(book));

        container.getChildren().addAll(title, stars, status, returnBtn);
        return container;
    }

    private void returnBook(Book book) {
        book.setBorrowed(false);
        book.setDueDate("");
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

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        topBar.getChildren().addAll(homeIcon, spacer1, title, spacer2, userIcon);
        return topBar;
    }
}
