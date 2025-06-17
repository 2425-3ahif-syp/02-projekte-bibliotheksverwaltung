package at.htl.bibliotheksverwaltung.view;

import at.htl.bibliotheksverwaltung.controller.SceneManager;
import at.htl.bibliotheksverwaltung.database.DatabaseManager;
import at.htl.bibliotheksverwaltung.model.Book;
import at.htl.bibliotheksverwaltung.model.Customer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

public class KundenAnzeigen {

    private VBox root;
    private TextField searchField;
    private VBox resultList;

    public VBox getView() {
        root = new VBox(20);
        root.setPadding(new Insets(20));

        HBox topBar = createTopBar("Liste der Kunden");
        root.getChildren().add(topBar);

        HBox searchBox = new HBox(10);
        searchField = new TextField();
        searchField.setPromptText("Kunden Namen eingeben...");
        searchField.setStyle("-fx-border-color: #4682B4; -fx-background-color: #f0f8ff;");
        searchField.setOnAction(e -> searchCustomers());

        Button searchButton = new Button("Suchen");
        searchButton.setStyle(
                "-fx-background-color: #4682B4; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; "
        );
        searchButton.setOnAction(e -> searchCustomers());
        Button newCustomerButton = new Button("Kunde Anlegen");
        newCustomerButton.setStyle(
                "-fx-background-color: #4682B4; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold;"
        );


        newCustomerButton.setOnAction(e -> {
            SceneManager.setView(new KundeAnlegen().getView());
        });
        Button editButton = new Button("Kunde bearbeiten");
        editButton.setPrefWidth(300);
        editButton.setStyle(
                "-fx-background-color: #4682B4; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold;"
        );
        editButton.setOnAction(e -> SceneManager.setView(new KundenIdUeberpruefen().getView()));

        searchBox.getChildren().addAll(searchField, searchButton, newCustomerButton,editButton);

        resultList = new VBox(10);

        root.getChildren().addAll(searchBox, resultList);

        searchCustomers();
        return root;
    }

    private void searchCustomers() {
        String query = searchField.getText().trim().toLowerCase();

        List<Customer> matchingCustomers = DatabaseManager.getInstance().getAllCustomers().stream()
                .filter(c ->
                        String.valueOf(c.getId()).contains(query) ||
                                (c.getLastName() != null && c.getLastName().toLowerCase().contains(query)) ||
                                (c.getFirstName() != null && c.getFirstName().toLowerCase().contains(query)) ||
                                (c.getBirthYear() != null && c.getBirthYear().toLowerCase().contains(query))
                )
                .collect(Collectors.toList());

        updateResultList(matchingCustomers);
    }




    private void updateResultList(List<Customer> customers) {
        resultList.getChildren().clear();

        for (Customer c : customers) {
            HBox item = createCustomerItem(c);
            resultList.getChildren().add(item);
        }
    }


    private HBox createCustomerItem(Customer customer) {
        HBox container = new HBox(20);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(10));
        container.setStyle("-fx-border-color: #4682B4; -fx-background-color: #f0f8ff;");

        Label idLabel = new Label("ID: " + customer.getId());
        idLabel.setPrefWidth(80);
        idLabel.setStyle("-fx-font-size: 14px;");

        Label nameLabel = new Label("Name: " + safe(customer.getFirstName()) + " " + safe(customer.getLastName()));
        nameLabel.setPrefWidth(200);
        nameLabel.setStyle("-fx-font-size: 16px;");

        Label birthLabel = new Label("Geburtsdatum: " + safe(customer.getBirthYear()));
        birthLabel.setStyle("-fx-font-size: 14px;");

        Button deleteButton = new Button("Löschen");
        deleteButton.setStyle(
                "-fx-background-color: #B22222; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold;"
        );
        deleteButton.setOnAction(e -> {
            DatabaseManager.getInstance().deleteCustomer(customer.getId());
            searchCustomers(); // Aktualisiere Liste nach dem Löschen
        });

        container.getChildren().addAll(idLabel, nameLabel, birthLabel,deleteButton);
        return container;
    }

    private String safe(String text) {
        return text == null ? "(leer)" : text;
    }



    private void returnBook(Book book) {
        book.setBorrowed(false);
        book.setDueDate("");
        DatabaseManager.getInstance().updateBook(book);
        searchCustomers();
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
