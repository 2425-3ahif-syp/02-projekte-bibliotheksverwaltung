package at.htl.bibliotheksverwaltung.view;

import at.htl.bibliotheksverwaltung.controller.SceneManager;
import at.htl.bibliotheksverwaltung.database.DatabaseManager;
import at.htl.bibliotheksverwaltung.model.Book;
import at.htl.bibliotheksverwaltung.model.Customer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatistikDetail {

    private VBox root;

    public VBox getView() {
        root = new VBox(20);
        root.setPadding(new Insets(20));

        HBox topBar = createTopBar("Statistik");
        root.getChildren().add(topBar);

        VBox cardContainer = new VBox(20);
        cardContainer.setPadding(new Insets(10));
        cardContainer.setAlignment(Pos.CENTER_LEFT);

        List<Book> books = DatabaseManager.getInstance().getAllBooks();
        int counter = 0;
        HBox hbox = null;
        for (int i = 5; i >= 1; i--) {
            for (int j = 0; j < books.size(); j++) {
                if (books.get(j).getRating() == i) {
                    String description = "Verfügbar";
                    if (books.get(j).isBorrowed()) {
                        description = "Ausgeliehen (bis " + books.get(j).getDueDate() + ")";
                    }
                    VBox card = createCard(
                            books.get(j).getTitle(),
                            description,
                            books.get(j).getRating()
                    );
                    switch (i) {
                        case 1:
                            card.setStyle("-fx-border-color: red; -fx-background-color: #f0f8ff;");

                            break;
                        case 2:
                            card.setStyle("-fx-border-color: orange; -fx-background-color: #f0f8ff;");

                            break;
                        case 3:
                            card.setStyle("-fx-border-color: yellow; -fx-background-color: #f0f8ff;");

                            break;
                        case 4:
                            card.setStyle("-fx-border-color: lightgreen; -fx-background-color: #f0f8ff;");

                            break;
                        case 5:
                            card.setStyle("-fx-border-color: #4682B4; -fx-background-color: #f0f8ff;");
                            break;
                        default:
                            break;
                    }
                    counter++;
                    if (counter == 1) {
                        hbox = new HBox(20);
                    }
                    hbox.getChildren().add(card);
                    if (counter == 3) {
                        cardContainer.getChildren().add(hbox);
                        counter = 0;
                    }
                }
            }
        }
        if (counter > 0) {
            cardContainer.getChildren().add(hbox);
        }

        ScrollPane scrollPane = new ScrollPane(cardContainer);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // "Back"-Button
        Button backBtn = new Button("Zurück");
        backBtn.setStyle(
                "-fx-background-color: #4682B4; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; "
        );
        backBtn.setOnAction(e -> SceneManager.setView(new Statistik().getView()));


        root.getChildren().addAll(scrollPane, backBtn);
        return root;
    }


    private VBox createCard(String title, String description, int rating) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setPrefWidth(250);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #4682B4;");

        Label descLabel = new Label(description);
        descLabel.setWrapText(true);

        Label starLabel = new Label(getStarString(rating));
        starLabel.setStyle("-fx-text-fill: orange; -fx-font-size: 16;");

        card.getChildren().addAll(titleLabel, descLabel, starLabel);
        return card;
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
