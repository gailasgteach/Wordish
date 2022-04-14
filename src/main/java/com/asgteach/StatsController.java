package com.asgteach;

import com.asgteach.model.GameStatus;
import com.asgteach.model.WordStats;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class StatsController {

    @FXML
    private VBox rootContainer;
    @FXML
    private Label statPlayed;
    @FXML
    private Label statPercent;
    @FXML
    private Label statCurrent;
    @FXML
    private Label statMax;
    @FXML
    private Button switchButton;

    @FXML
    private StackPane pane;
    @FXML
    private BarChart<Number, String> barChart;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private CategoryAxis yAxis;

    private final ObservableList<BarChart.Series<Number, String>> bcData
            = FXCollections.observableArrayList();
    private static final GameStatus gameStatus = GameStatus.getInstance();
    private final WordStats ws = gameStatus.getWordStats();
    private ChangeListener<Scene> changeListener;

    @FXML
    private void switchToWordish() throws IOException {
        pane.sceneProperty().removeListener(changeListener);
        WordishApp.setRoot("wordish");
    }

    public void initialize() {
        barChart.setAnimated(false);
//        switchButton.setText(new String(Character.toChars(Integer.parseInt("F0156", 16))));
//        switchButton.setStyle("-fx-font-family: \"Material Design Icons\"; -fx-font-size: 28");
        barChart.setTitle("Guess Distribution");
        barChart.setLegendVisible(false);
        xAxis.setTickLabelsVisible(false);

        statPlayed.setText(String.valueOf(ws.getGamesPlayed()));
        statPercent.setText(String.format("%4.0f", ws.getWinPercentage()));
        statCurrent.setText(String.valueOf(ws.getCurrentStreak()));
        statMax.setText(String.valueOf(ws.getMaxStreak()));
        barChart.setData(installBarChartData());
        barChart.setHorizontalGridLinesVisible(false);
        barChart.setVerticalGridLinesVisible(false);

        pane.sceneProperty().addListener(changeListener
                = (ObservableValue<? extends Scene> observableScene,
                        Scene oldScene, Scene newScene) -> {
                    if (oldScene == null && newScene != null) {
                        // Approach 2
                        // WritableImage image = pane.snapshot(new SnapshotParameters(), null);
                        // fixLabels();
                        // Approach 1
                        new Timeline(
                                new KeyFrame(Duration.millis(50), ae -> fixLabels()))
                                .play();
                    }
                });

    }

    private ObservableList<XYChart.Series<Number, String>> installBarChartData() {
        XYChart.Series<Number, String> series = new XYChart.Series<>();
        ws.getGuessDistribution().keySet().forEach(key -> {
            series.getData().add(0, new BarChart.Data<>(
                    ws.getGuessDistribution().get(key),
                    String.valueOf(key)));
        });
        bcData.add(series);
        return bcData;
    }

    public void fixLabels() {
        // this code only works after the scene is completely rendered
        System.out.println("fixLabels1");
        List<Label> mylabels = pane.getChildren().stream()
                .filter(sc -> sc instanceof Label)
                .map(Label.class::cast)
                .collect(Collectors.toList());
        mylabels.stream().forEach(l -> {
            l.toFront();
            l.getStyleClass().add("chartlabel");
        });
        // find chart area Node
        Node chartArea = barChart.lookup(".chart-plot-background");
        XYChart.Series<Number, String> series = barChart.getData().get(0);

        IntStream.range(0, series.getData().size())
                .forEach(i -> {
                    Data<Number, String> data = series.getData().get(i);
                    Node node = data.getNode();
                    if (data.getYValue().equals(String.valueOf(ws.getThisGameGuesses()))) {
                        node.setStyle("-fx-bar-fill: -fx-match-color;");
                        mylabels.get(i).setStyle("-fx-background-color: -fx-match-color;");
                    }
                    // Place the label on the bar
                    // the stackpane centers the labels first
                    Bounds nodeBounds = node.localToScene(chartArea.getBoundsInLocal());
                    // Get the display position along xAxis/yAxis for a given X/Yvalue. 
                    double displayPositionX = xAxis.getDisplayPosition(data.getXValue());
                    double displayPositionY = yAxis.getDisplayPosition(data.getYValue());
                    mylabels.get(i).setText(data.getXValue().toString());
                    if (displayPositionX == 0.0) {
                        mylabels.get(i).setTranslateX(20 - (nodeBounds.getWidth() / 2));
                        //mylabels.get(i).setTranslateX(-53 - (nodeBounds.getWidth() / 2));
                    } else {
                        mylabels.get(i).setTranslateX(displayPositionX - 4 - ((nodeBounds.getWidth()) / 2));
                        //mylabels.get(i).setTranslateX(displayPositionX - 78 - ((nodeBounds.getWidth()) / 2));
                    }
                    mylabels.get(i).setTranslateY(displayPositionY + 21);
                    mylabels.get(i).setVisible(true);
                });
    }
}
