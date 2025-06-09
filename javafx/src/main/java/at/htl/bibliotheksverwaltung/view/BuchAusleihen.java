package at.htl.bibliotheksverwaltung.view;

import at.htl.bibliotheksverwaltung.controller.SceneManager;
import at.htl.bibliotheksverwaltung.database.DatabaseManager;
import at.htl.bibliotheksverwaltung.model.Book;
import at.htl.bibliotheksverwaltung.model.Customer;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

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

        // Initial alle verfügbaren Bücher
        searchBooks();
        return root;
    }

    private void searchBooks() {
        String query = searchField.getText().trim().toLowerCase();
        List<Book> available = DatabaseManager.getInstance().getAllBooks().stream()
                .filter(b -> !b.isBorrowed())
                .filter(b -> b.getTitle().toLowerCase().contains(query))
                .collect(Collectors.toList());
        updateResultList(available);
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

        HBox leftBox = new HBox(10, title, stars);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        container.setLeft(leftBox);

        Button borrowBtn = new Button("Ausleihen");
        borrowBtn.setStyle(
                "-fx-background-color: #4682B4; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold;"
        );
        borrowBtn.setOnAction(e -> borrowBook(book));

        // Button mittig-vertikal rechts platzieren
        BorderPane.setAlignment(borrowBtn, Pos.CENTER);
        container.setRight(borrowBtn);

        return container;
    }


    private void borrowBook(Book book) {
        // Dialog zur Kundenauswahl
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("Kunden auswählen");
        dialog.setHeaderText("Bitte wählen Sie den Kunden aus:");

        ComboBox<Customer> cb = new ComboBox<>(
                FXCollections.observableArrayList(
                        DatabaseManager.getInstance().getAllCustomers()
                )
        );
        cb.setVisibleRowCount(5);
        // damit in Liste & Button lesbar
        cb.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.toString());
            }
        });
        cb.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Customer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.toString());
            }
        });

        dialog.getDialogPane().setContent(cb);
        dialog.getDialogPane().getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        // OK-Button nur aktiv, wenn ein Kunde gewählt ist
        Button ok = (Button) dialog.getDialogPane()
                .lookupButton(ButtonType.OK);
        ok.disableProperty().bind(cb.valueProperty().isNull());

        dialog.setResultConverter(btn ->
                btn == ButtonType.OK ? cb.getValue() : null
        );

        dialog.showAndWait().ifPresent(c -> {
            book.setBorrowed(true);
            book.setDueDate(
                    LocalDate.now().plusDays(14)
                            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            );
            book.setCustomerId((int) c.getId());
            DatabaseManager.getInstance().updateBook(book);
            searchBooks();
        });
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
