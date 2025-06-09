package at.htl.bibliotheksverwaltung.view;

import at.htl.bibliotheksverwaltung.controller.SceneManager;
import at.htl.bibliotheksverwaltung.database.DatabaseManager;
import at.htl.bibliotheksverwaltung.model.Book;
import at.htl.bibliotheksverwaltung.model.Customer;
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
    private ComboBox<String> statusFilter;
    private ComboBox<Customer> customerFilter;

    public VBox getView() {
        root = new VBox(20);
        root.setPadding(new Insets(20));

        // Top-Bar
        HBox topBar = createTopBar("Verwaltung");
        root.getChildren().add(topBar);

        // Suchleiste + Filter
        HBox searchBox = new HBox(10);
        searchField = new TextField();
        searchField.setPromptText("Eingabe...");
        searchField.setStyle("-fx-border-color: #4682B4; -fx-background-color: #f0f8ff;");

        Button searchButton = new Button("Suchen");
        searchButton.setStyle(
                "-fx-background-color: #4682B4; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; "
        );
        searchButton.setOnAction(e -> searchBooks());

        // Status-Filter
        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("Alle", "Verfügbar", "Ausgeborgt");
        statusFilter.setValue("Alle");
        statusFilter.setOnAction(e -> {
            updateCustomerFilterVisibility();
            searchBooks();
        });

        // Kunden-Filter (nur sichtbar bei "Ausgeborgt")
        customerFilter = new ComboBox<>();
        customerFilter.setPromptText("Kunde wählen");
        customerFilter.setVisible(false);
        customerFilter.setMinWidth(150);
        updateCustomerList();
        customerFilter.setOnAction(e -> searchBooks());

        // "Buch Hinzufügen"
        Button addBookButton = new Button("Buch Hinzufügen");
        addBookButton.setStyle(
                "-fx-background-color: #4682B4; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; "
        );
        addBookButton.setOnAction(e -> addNewBook());

        searchBox.getChildren().addAll(searchField, searchButton, statusFilter, customerFilter, addBookButton);

        // Ergebnisliste (mit ScrollPane)
        resultList = new VBox(10);
        ScrollPane scrollPane = new ScrollPane(resultList);
        scrollPane.setFitToWidth(true);

        root.getChildren().addAll(searchBox, scrollPane);

        // Initial alle Bücher
        searchBooks();

        return root;
    }

    private void updateCustomerList() {
        customerFilter.getItems().clear();
        customerFilter.getItems().add(null); // Für "alle Kunden"
        customerFilter.getItems().addAll(DatabaseManager.getInstance().getAllCustomers());
    }

    private void updateCustomerFilterVisibility() {
        boolean show = "Ausgeborgt".equals(statusFilter.getValue());
        customerFilter.setVisible(show);
        if (!show) {
            customerFilter.setValue(null);
        }
    }

    private void searchBooks() {
        String query = searchField.getText().trim().toLowerCase();
        String status = statusFilter.getValue();
        Customer selectedCustomer = customerFilter.getValue();

        List<Book> found = DatabaseManager.getInstance().getAllBooks().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(query))
                .filter(b -> {
                    if ("Verfügbar".equals(status)) return !b.isBorrowed();
                    if ("Ausgeborgt".equals(status)) return b.isBorrowed();
                    return true;
                })
                .filter(b -> {
                    if ("Ausgeborgt".equals(status) && selectedCustomer != null) {
                        return b.getCustomerId() == selectedCustomer.getId();
                    }
                    return true;
                })
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
        container.setStyle("-fx-border-color: #4682B4; -fx-background-color: #f0f8ff;");

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
        searchBooks();
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