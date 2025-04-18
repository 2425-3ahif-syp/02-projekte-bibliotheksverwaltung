package at.htl.bibliotheksverwaltung.view;

import at.htl.bibliotheksverwaltung.controller.SceneManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class StatistikDetail {

    private VBox root;

    public VBox getView() {
        root = new VBox(20);
        root.setPadding(new Insets(20));

        // Top-Bar
        HBox topBar = createTopBar("Statistik");
        root.getChildren().add(topBar);

        // ScrollPane mit horizontalem HBox, um 3 "Karten" anzuzeigen
        HBox cardContainer = new HBox(20);
        cardContainer.setPadding(new Insets(10));
        cardContainer.setAlignment(Pos.CENTER_LEFT);

        // Karte 1
        VBox card1 = createCard(
                "Don Quijote",
                "file:./donquijote.jpg", // Beispiel: eigenes Bild, hier nur Platzhalter
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ...",
                5
        );

        // Karte 2
        VBox card2 = createCard(
                "Herr der Ringe",
                "file:./herrderringe.jpg",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ...",
                4
        );

        // Karte 3
        VBox card3 = createCard(
                "Harry Potter",
                "file:./harrypotter.jpg",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ...",
                5
        );

        cardContainer.getChildren().addAll(card1, card2, card3);

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



        root.getChildren().addAll(scrollPane,backBtn);
        return root;
    }





    private VBox createCard(String title, String imagePath, String description, int rating) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setPrefWidth(200);
        card.setStyle("-fx-border-color: #4682B4; -fx-background-color: #f0f8ff;");  // Light blue background with a blue border

        // Bild (Platzhalter oder echtes Bild)
        ImageView imageView = new ImageView();
        try {
            Image img = new Image(imagePath, 200, 120, true, true);
            imageView.setImage(img);
        } catch (Exception e) {
            // Falls Bild nicht geladen werden kann, ignorieren wir das
            imageView.setFitWidth(200);
            imageView.setFitHeight(120);
            imageView.setStyle("-fx-background-color: #ddd; -fx-border-color: #999;");
        }

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #4682B4;"); // Blue text

        Label descLabel = new Label(description);
        descLabel.setWrapText(true);

        Label starLabel = new Label(getStarString(rating));
        starLabel.setStyle("-fx-text-fill: orange; -fx-font-size: 16;");

        card.getChildren().addAll(imageView, titleLabel, descLabel, starLabel);
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
        topBar.setStyle("-fx-background-color: #4682B4; -fx-border-color: #ccc;"); // Blue background for top bar

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
