package at.htl.bibliotheksverwaltung.view;

import at.htl.bibliotheksverwaltung.controller.SceneManager;
import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class Statistik {

    private VBox root;

    public VBox getView() {
        root = new VBox(20);
        root.setPadding(new Insets(20));

        // Top-Bar
        HBox topBar = createTopBar("Statistik");
        root.getChildren().add(topBar);

        // Balkendiagramm
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Bücher");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Beliebtheit (z.B. Sterne)");
        // Bereich auf 0 bis 5 festlegen
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(5);
        yAxis.setTickUnit(0.5);

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Beliebteste Bücher");

        // Change colors of the BarChart
        barChart.setStyle("-fx-background-color: #f0f8ff;"); // Light blue background for the chart

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Bewertung");

        // Beispielwerte
        series.getData().add(new XYChart.Data<>("Don Quijote", 5));
        series.getData().add(new XYChart.Data<>("Herr der Ringe 1", 4));
        series.getData().add(new XYChart.Data<>("Harry Potter\nund die Heiligtümer des Todes", 3));
        series.getData().add(new XYChart.Data<>("Der Verdacht", 2));
        series.getData().add(new XYChart.Data<>("Don Carlos", 1));

        barChart.getData().add(series);

        // "Details"-Button
        Button detailsBtn = new Button("Details");
        detailsBtn.setStyle(
                "-fx-background-color: #4682B4; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; "
        );
        detailsBtn.setOnAction(e -> SceneManager.setView(new StatistikDetail().getView()));

        root.getChildren().addAll(barChart, detailsBtn);

        return root;
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
