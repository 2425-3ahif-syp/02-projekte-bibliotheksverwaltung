package at.htl.bibliotheksverwaltung.view;

import at.htl.bibliotheksverwaltung.controller.SceneManager;
import at.htl.bibliotheksverwaltung.model.Book;
import at.htl.bibliotheksverwaltung.database.DatabaseManager;
import at.htl.bibliotheksverwaltung.view.MainMenu;
import at.htl.bibliotheksverwaltung.view.StatistikDetail;
import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Window;

import java.util.*;
import java.util.stream.Collectors;

public class Statistik {

    private VBox root;

    public VBox getView() {
        root = new VBox(20);
        root.setPadding(new Insets(20));

        HBox topBar = createTopBar("Statistik");
        root.getChildren().add(topBar);

        List<Book> books = DatabaseManager.getInstance().getAllBooks();

        Map<Integer, List<String>> bookData = books.stream()
                .filter(b -> b.getRating() >= 1 && b.getRating() <= 5)
                .collect(Collectors.groupingBy(
                        Book::getRating,
                        Collectors.mapping(Book::getTitle, Collectors.toList())
                ));

        double avg = books.stream()
                .filter(b -> b.getRating() >= 1 && b.getRating() <= 5)
                .mapToInt(Book::getRating)
                .average().orElse(0);

        Label avgLabel = new Label(String.format("Durchschnittsbewertung: %.2f", avg));
        avgLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #4682B4;");
        root.getChildren().add(avgLabel);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Bewertung");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Anzahl Bücher");
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(
                Math.max(5, bookData.values().stream().mapToInt(List::size).max().orElse(1))
        );
        yAxis.setTickUnit(1);

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Bücher nach Bewertung");
        barChart.setStyle("-fx-background-color: #f0f8ff;");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Anzahl Bücher");

        for (int rating = 1; rating <= 5; rating++) {
            int count = bookData.getOrDefault(rating, Collections.emptyList()).size();
            series.getData().add(new XYChart.Data<>(String.valueOf(rating), count));
        }
        barChart.getData().add(series);
        root.getChildren().add(barChart);

        Button detailsBtn = new Button("Details");
        detailsBtn.setStyle(
                "-fx-background-color: #4682B4; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; "
        );
        detailsBtn.setOnAction(e -> SceneManager.setView(new StatistikDetail().getView()));

        root.getChildren().add(detailsBtn);

        return root;
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