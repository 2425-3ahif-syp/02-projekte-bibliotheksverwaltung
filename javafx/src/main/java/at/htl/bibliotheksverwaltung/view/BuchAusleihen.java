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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class BuchAusleihen {

    private VBox root;
    private TextField searchField;
    private VBox resultList;

    public VBox getView() {
        root = new VBox(20);
        root.setPadding(new Insets(20));

        // Top-Bar
        HBox topBar = createTopBar("Buch Ausleihen");
        root.getChildren().add(topBar);

        // Suchleiste
        HBox searchBox = new HBox(10);
        searchField = new TextField();
        searchField.setPromptText("Buchtitel...");
        Button searchButton = new Button("Suchen");
        searchButton.setOnAction(e -> searchBooks());

        searchBox.getChildren().addAll(searchField, searchButton);

        resultList = new VBox(10);

        root.getChildren().addAll(searchBox, resultList);

        // Initial alle verfügbaren Bücher
        searchBooks();

        return root;
    }

    private void searchBooks() {
        String query = searchField.getText().trim().toLowerCase();
        // Retrieve all books from the database and filter for available (not borrowed)
        List<Book> available = DatabaseManager.getInstance().getAllBooks().stream()
                .filter(b -> !b.isBorrowed())
                .filter(b -> b.getTitle().toLowerCase().contains(query))
                .collect(Collectors.toList());

        updateResultList(available);
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

        Button borrowBtn = new Button("Ausleihen");
        borrowBtn.setOnAction(e -> borrowBook(book));

        container.getChildren().addAll(title, stars, borrowBtn);
        return container;
    }

    private void borrowBook(Book book) {
        // Mark book as borrowed and set due date to 14 days from now
        book.setBorrowed(true);
        book.setDueDate(LocalDate.now().plusDays(14) // changeable after decision on how long you can borrow a book
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        DatabaseManager.getInstance().updateBook(book);
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

        topBar.getChildren().addAll(homeIcon, home, spacer1, title, spacer2, profile, userIcon);
        return topBar;
    }
}
