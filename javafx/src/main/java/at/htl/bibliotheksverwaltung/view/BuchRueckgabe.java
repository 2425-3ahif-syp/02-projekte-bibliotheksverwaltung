package at.htl.bibliotheksverwaltung.view;

import at.htl.bibliotheksverwaltung.controller.SceneManager;
import at.htl.bibliotheksverwaltung.database.DatabaseManager;
import at.htl.bibliotheksverwaltung.model.Book;
import at.htl.bibliotheksverwaltung.model.Customer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.util.List;
import java.util.stream.Collectors;

public class BuchRueckgabe {

    private VBox root;
    private TextField searchField;
    private VBox resultList;

    public VBox getView() {
        root = new VBox(20);
        root.setPadding(new Insets(20));

        HBox topBar = createTopBar("Buch Rückgabe");
        root.getChildren().add(topBar);

        HBox searchBox = new HBox(10);
        searchField = new TextField();
        searchField.setPromptText("Harry Potter...");
        searchField.setStyle("-fx-border-color: #4682B4; -fx-background-color: #f0f8ff;");
        searchField.setOnAction(e -> searchBooks());

        Button searchButton = new Button("Suchen");
        searchButton.setStyle(
                "-fx-background-color: #4682B4; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; "
        );
        searchButton.setOnAction(e -> searchBooks());

        searchBox.getChildren().addAll(searchField, searchButton);

        resultList = new VBox(10);
        root.getChildren().addAll(searchBox, resultList);

        searchBooks();
        return root;
    }

    private void searchBooks() {
        String query = searchField.getText().trim().toLowerCase();
        List<Book> borrowed = DatabaseManager.getInstance().getAllBooks().stream()
                .filter(Book::isBorrowed)
                .filter(b -> b.getTitle().toLowerCase().contains(query))
                .collect(Collectors.toList());
        updateResultList(borrowed);
    }

    private void updateResultList(List<Book> books) {
        resultList.getChildren().clear();
        for (Book b : books) {
            resultList.getChildren().add(createBookItem(b));
        }
    }

    private BorderPane createBookItem(Book book) {
        BorderPane container = new BorderPane();
        container.setPadding(new Insets(10));
        container.setStyle("-fx-border-color: #4682B4; -fx-background-color: #f0f8ff;");

        Label title = new Label(book.getTitle());
        title.setPrefWidth(300);
        title.setStyle("-fx-font-size: 16px;");

        Label stars = new Label(getStarString(book.getRating()));
        stars.setStyle("-fx-text-fill: orange;");

        Label status = new Label("Ausgeliehen (bis " + book.getDueDate() + ")");
        status.setStyle("-fx-font-size: 14px;");

        int customerId = book.getCustomerId();
        Label customerLabel;
        if (customerId > 0) {
            Customer c = DatabaseManager.getInstance().getCustomerById(customerId);
            if (c != null) {
                customerLabel = new Label(
                        "von " + c.getFirstName() + " " + c.getLastName() +
                                " (ID " + c.getId() + ")"
                );
            } else {
                customerLabel = new Label("von unbekannter Kunde (ID " + customerId + ")");
            }
        } else {
            customerLabel = new Label("kein Kunde zugewiesen");
        }
        customerLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");

        HBox leftBox = new HBox(10, title, stars, status, customerLabel);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        container.setLeft(leftBox);

        Button returnBtn = new Button("Rückgabe");
        returnBtn.setStyle(
                "-fx-background-color: #4682B4; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold;"
        );
        returnBtn.setOnAction(e -> {
            book.setBorrowed(false);
            book.setDueDate("");
            book.setCustomerId(0);
            DatabaseManager.getInstance().updateBook(book);
            searchBooks();
        });

        BorderPane.setAlignment(returnBtn, Pos.CENTER);
        container.setRight(returnBtn);

        return container;
    }

    private String getStarString(int rating) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) sb.append(i < rating ? "★" : "☆");
        return sb.toString();
    }

    private HBox createTopBar(String titleText) {
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #4682B4; -fx-border-color: #ccc;");

        Label homeIcon = new Label("\u2302");
        homeIcon.setStyle("-fx-font-size: 20; -fx-text-fill: white;");
        homeIcon.setOnMouseClicked(e ->
                SceneManager.setView(new MainMenu().getView()));

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
