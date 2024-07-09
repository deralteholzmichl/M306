package com.ubs.controller;

import com.ubs.Model.CombinedData;
import com.ubs.Model.esl.TimePeriod;
import com.ubs.Model.esl.ValueRow;
import com.ubs.Model.sdat.Observation;
import com.ubs.Model.sdat.ValidatedMeteredData;
import com.ubs.helper.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static javafx.scene.paint.Color.rgb;


public class MainSceneController {

    public Button exportbutton;
    public Button BarBtn;
    public Button LineBtn;
    public Button deleteBtn;
    public Rectangle rectangle;
    public Button closeButton;

    @FXML
    private DatePicker dateSelector;

    @FXML
    private Pane pane;

    @FXML
    private Text progressText;

    @FXML
    private ChoiceBox<String> selector;

    @FXML
    private Canvas canvas = new Canvas(815, 430);
    public List<BarChartEntry> barChartEntries;

    @FXML
    void delete(ActionEvent event) {
        pane.getChildren().remove(canvas);
    }

    @FXML
    void drawLine(ActionEvent event) {
        pane.getChildren().remove(canvas);
            DiagramObject data1 = LineViewGenerator(StaticData.combinedVerbrauch, "Verbrauch");
            DiagramObject data2 = LineViewGenerator(StaticData.combinedEinspeisen, "Einspeisen");
            System.out.println(dateSelector.getValue());
            if (dateSelector.getValue() != null) {
                String Date = dateSelector.getValue().toString();
                List<List<Double>> lines = new ArrayList<>();
                ArrayList<String> xList = new ArrayList<>();
                for (int i = 0; i < data1.xList.size(); i++) {
                    if (data1.xList.get(i).getFirst().equals(Date)) {
                        lines.add(data1.yList.get(i));
                        xList = data1.xList.get(i);
                        break;
                    }
                }
                for (int y = 0; y < data2.xList.size(); y++) {
                    if (data2.xList.get(y).getFirst().equals(Date)) {
                        lines.add(data2.yList.get(y));
                        break;
                    }
                }
                LocalTime time = LocalTime.of(0, 0);
                xList = new ArrayList<>();

                for (int i = 0; i < 13; i++) {
                    xList.add(time.toString());
                    time = time.plusHours(2);
                }
                if (lines.size() < 2) {
                    progressText.setText("No Data for Date");
                    return;
                } else {
                    canvas = StatisticDrawer.drawLineDiagram(lines, 280, 370, 90, true, true, xList, Interval.DAILY);
                    if (canvas == null) {
                        progressText.setText("Highest Value is 0");
                        return;
                    }
                    selector.setDisable(false);
                    pane.getChildren().add(canvas);
                    canvas.setLayoutY(5);
                    canvas.setLayoutX(200);
                    StaticData.diagramType = "LineChart";
                    StaticData.interval = "daily";
                    selector.setValue("daily");
                    System.out.println("Drawed Line Chart");
                    return;
                }
            }
            progressText.setText("Please select a Date");
    }
    void drawLineDiagramWithInterval(String drawInterval){
        pane.getChildren().remove(canvas);
        DiagramObject data1 = LineViewGenerator(StaticData.combinedVerbrauch,"Verbrauch");
        DiagramObject data2 = LineViewGenerator(StaticData.combinedEinspeisen,"Einspeisen");
        if (dateSelector.getValue() != null) {
            String Date = dateSelector.getValue().toString();
            ArrayList<String> dates = new ArrayList<>();
            LocalDate date = LocalDate.parse(Date);
            for (int i = 0; i < 7; i++) {
                dates.add(date.plusDays(i).toString());
            }

            List<List<Double>> lines = new ArrayList<>();
            List<Double> line = new ArrayList<>();
            ArrayList<String> xList = new ArrayList<>();
            ArrayList<String> usedDates = new ArrayList<>();

            for (int i = 0; i < data1.xList.size(); i++) {
                if (dates.contains(data1.xList.get(i).getFirst())) {
                    line.addAll(data1.yList.get(i));
                    if(!usedDates.contains(data1.xList.get(i).getLast())){
                        usedDates.add(data1.xList.get(i).getLast());
                    }
                }
            }
            lines.add(line);
            line = new ArrayList<>();
            for (int y = 0; y < data2.xList.size(); y++) {
                if (dates.contains(data2.xList.get(y).getFirst())) {
                    line.addAll(data2.yList.get(y));
                    if (!usedDates.contains(data2.xList.get(y).getLast())){
                        usedDates.add(data2.xList.get(y).getLast());
                    }
                }
            }
            lines.add(line);

            if (lines.size()<2) {
                progressText.setText("No Data for Date");
                return;
            }else {
                canvas = StatisticDrawer.drawLineDiagram(lines, 280,370, 90, true, true, dates,Interval.valueOf(drawInterval.toUpperCase()));
                if (canvas == null) {
                    progressText.setText("Highest Value is 0");
                    return;
                }
                canvas.setLayoutY(5);
                canvas.setLayoutX(200);
                pane.getChildren().add(canvas);
                StaticData.diagramType = "LineChart";
                StaticData.interval = drawInterval;
                System.out.println("Drawed Line Chart with interval");
                return;
            }
        }
        progressText.setText("select a Date");
    }

    @FXML
    void drawBar(ActionEvent event) {
        drawBarWithInterval(Interval.DAILY);
    }

    public void drawBarWithInterval(Interval interval){
        pane.getChildren().remove(canvas);
        if (dateSelector.getValue() != null) {
            String Date = dateSelector.getValue().toString();
            List<List<BarChartEntry>> barChartData =  new ArrayList<>();
            barChartData.add(BarChartViewGenerator(StaticData.combinedEinspeisen,interval,Date));
            barChartData.add(BarChartViewGenerator(StaticData.combinedVerbrauch,interval,Date));
            canvas = StatisticDrawer.drawBarChart(400, 300, 60, barChartData, interval);
            if (canvas == null) {
                progressText.setText("Highest Value is 0");
                System.out.println("Highest Value is 0");
                return;
            }
            pane.getChildren().remove(canvas);
            StaticData.diagramType = "BarChart";
            StaticData.interval = interval.toString().toLowerCase();
            pane.getChildren().add(canvas);
            progressText.setText(Date);
            selector.setValue(interval.toString().toLowerCase());
            canvas.setLayoutY(5);
            canvas.setLayoutX(200);
            selector.setDisable(false);
            return;
        }
        progressText.setText("Please select a Date");
    }
    public List<BarChartEntry> BarChartViewGenerator(List<CombinedData> BezugData, Interval interval, String datum){
        List<BarChartEntry> dhdd = new ArrayList<>();
        barChartEntries = new ArrayList<>();
        if (interval == Interval.DAILY){
            for (CombinedData c:BezugData){
                for (ValidatedMeteredData v: c.getValidatedMeteredData()){
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(v.getMeteringData().getInterval().getEndDateTime());
                    LocalDate localDateEnd = zonedDateTime.toLocalDate();
                    if (localDateEnd.toString().equals(datum)){
                        LocalTime startTime = LocalTime.of(0, 0);
                        int index = 0;
                        for (Observation o : v.getMeteringData().getObservations()) {
                            String EintragZeit;
                            if ((index + 1) % 8 == 0){
                                EintragZeit = startTime.toString();
                            }else {
                                EintragZeit = "";
                            }
                            BarChartEntry bce = new BarChartEntry(EintragZeit, o.getVolume());
                            startTime = startTime.plusMinutes(15);
                            index++;
                            dhdd.add(bce);
                        }
                    }
                }
            }
            if (!dhdd.isEmpty()) {
                return dhdd;
            }else{
                return new ArrayList<>();
            }
        } else if (interval == Interval.WEEKLY) {
            List<String> dates = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                dates.add(LocalDate.parse(datum).plusDays(i).toString());
            }
            for (CombinedData c:BezugData){
                for (ValidatedMeteredData v: c.getValidatedMeteredData()){
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(v.getMeteringData().getInterval().getEndDateTime());
                    LocalDate localDate = zonedDateTime.toLocalDate();
                    if (dates.contains(localDate.toString())){
                        int i = 0;
                        for (Observation o : v.getMeteringData().getObservations()) {
                            String date;
                            if (i==0) {
                                date = localDate.toString();
                            }else{
                                date = "";
                            }
                            BarChartEntry bce = new BarChartEntry(date, o.getVolume());
                            dhdd.add(bce);
                            i++;
                        }
                    }
                }
            }
            if (!dhdd.isEmpty()) {
                return dhdd;
            }else{
                return new ArrayList<>();
            }
        } else {
            for (int y = 0; y < BezugData.size(); y++) {
                CombinedData c = BezugData.get(y);
                if (c.getEslBillingData().getMeter().size() > 1) {
                    ArrayList<String> timePeriodsEnd = new ArrayList<>();
                    for (TimePeriod tp : c.getEslBillingData().getMeter().getFirst().getTimePeriod()) {
                        timePeriodsEnd.add(tp.getEnd());
                    }
                    for (int m = 1; m < c.getEslBillingData().getMeter().size(); m++) {
                        for (TimePeriod tp : c.getEslBillingData().getMeter().get(m).getTimePeriod()) {
                            c.getEslBillingData().getMeter().getFirst().getTimePeriod().add(tp);
                        }
                    }
                }
                List<TimePeriod> timePeriods = BezugData.get(y).getEslBillingData().getMeter().getFirst().getTimePeriod();
                List<String> usedTimePeriods = new ArrayList<>();
                for (TimePeriod tp : timePeriods) {
                    if (usedTimePeriods.contains(tp.getEnd())) {
                        timePeriods.remove(tp);
                    } else {
                        usedTimePeriods.add(tp.getEnd());
                    }
                }

                for (int t = timePeriods.size() - 1; t >= 0; t--) {
                    LocalDate timePeriodDate = LocalDateTime.parse(timePeriods.get(t).getEnd()).toLocalDate();
                    LocalDate previousTimePeriodDate;
                    ArrayList<Double> yAchsePunkte = new ArrayList<>();
                    ArrayList<String> xAchse = new ArrayList<>();
                    try {
                        previousTimePeriodDate = LocalDateTime.parse(BezugData.get(y).getEslBillingData().getMeter().getFirst().getTimePeriod().get(t + 1).getEnd()).toLocalDate();
                        xAchse.add(previousTimePeriodDate.toString());

                    } catch (Exception e) {
                        try {
                            previousTimePeriodDate = LocalDateTime.parse(BezugData.get(y - 1).getEslBillingData().getMeter().getFirst().getTimePeriod().getFirst().getEnd()).toLocalDate();
                            xAchse.add(previousTimePeriodDate.toString());
                        } catch (Exception e1) {
                            xAchse.add("");
                            previousTimePeriodDate = LocalDate.of(1900, 1, 1);
                        }
                    }
                    xAchse.add(LocalDateTime.parse(c.getEslBillingData().getMeter().getFirst().getTimePeriod().get(t).getEnd()).toLocalDate().toString());
                    Double stand = 0.0;
                    for (ValidatedMeteredData v : c.getValidatedMeteredData()) {
                        LocalDate validatedMetredDataDate = ZonedDateTime.parse(v.getMeteringData().getInterval().getEndDateTime()).toLocalDate();
                        LocalDate validatedMetredDataDateStart = ZonedDateTime.parse(v.getMeteringData().getInterval().getStartDateTime()).toLocalDate();
                        if (validatedMetredDataDate.isAfter(previousTimePeriodDate) && (validatedMetredDataDate.isBefore(timePeriodDate) || validatedMetredDataDate.equals(timePeriodDate))) {
                            //   if (validatedMetredDataDateStart.isAfter(previousTimePeriodDate)|| validatedMetredDataDateStart.equals(previousTimePeriodDate)) {
                            for (Observation o : v.getMeteringData().getObservations()) {
                                stand += Double.valueOf(o.getVolume());
                                yAchsePunkte.add(stand);
                            }
                            //     }
                        }
                    }
                }
            }
        }
        return new ArrayList<>();
    }
    public DiagramObject LineViewGenerator(List<CombinedData> BezugData, String fileArt){
        DiagramObject dhdd = new DiagramObject();
        ArrayList<Canvas> views =  new ArrayList<>();
            int i = 0;
            for (CombinedData c:BezugData) {
                double stand = 0.0;
                for (ValueRow vr : c.getEslBillingData().getMeter().getFirst().getTimePeriod().getLast().getValueRow()) {
                    if (fileArt.equals("Verbrauch")) {
                        if (vr.getObis().equals("1-1:1.8.1")) {
                            stand = Double.valueOf(vr.getValue());
                            break;
                        }
                    } else {
                        if (vr.getObis().equals("1-1:2.8.1")) {
                            stand = Double.valueOf(vr.getValue());
                            break;
                        }
                    }
                }
                for (ValueRow vr : c.getEslBillingData().getMeter().getFirst().getTimePeriod().getLast().getValueRow()) {
                    if (fileArt.equals("Verbrauch")) {
                        if (vr.getObis().equals("1-1:1.8.2")) {
                            stand += Double.valueOf(vr.getValue());
                            break;
                        }
                    } else {
                        if (vr.getObis().equals("1-1:2.8.1")) {
                            stand += Double.valueOf(vr.getValue());
                            break;
                        }
                    }
                }
                for (ValidatedMeteredData v : c.getValidatedMeteredData()) {
                    LocalDate validatedMetredDataDateEnd = ZonedDateTime.parse(v.getMeteringData().getInterval().getEndDateTime()).toLocalDate();
                    LocalDate validatedMetredDataDateStart = ZonedDateTime.parse(v.getMeteringData().getInterval().getStartDateTime()).toLocalDate();
                    ArrayList<Double> yAchsePunkte = new ArrayList<>();
                    ArrayList<String> xAchse = new ArrayList<>();

                    xAchse.add(validatedMetredDataDateStart.toString());
                    xAchse.add(validatedMetredDataDateEnd.toString());

                    for (Observation o : v.getMeteringData().getObservations()) {
                        stand += Double.valueOf(o.getVolume());
                        yAchsePunkte.add(stand);
                    }
                    dhdd.yList.add(yAchsePunkte);
                    dhdd.xList.add(xAchse);
                    i++;
                }
            }
        return dhdd;

    }
    public void initialize() {
        progressText.setFont(Font.font("Arial", 12));
        canvas.setLayoutY(150);
        canvas.setLayoutX(500);

        int width = 815;
        int height = 430;

        pane.setPrefWidth(width);
        pane.setMaxWidth(width);
        pane.setMaxHeight(height);
        pane.setPrefHeight(height);

        DropShadow shadowDate = new DropShadow();
        dateSelector.setOnMouseEntered(e -> dateSelector.setEffect(shadowDate));
        dateSelector.setOnMouseExited(e -> dateSelector.setEffect(null));

        selector.setValue("Interval");
        selector.setDisable(true);
        selector.getItems().addAll("daily", "weekly");

        //selector.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-family: Arial; -fx-font-size: 12px;");

        DropShadow SelectorShadow = new DropShadow();
        dateSelector.setStyle(
                "-fx-background-color: #FFFFFF; " + /* Hintergrundfarbe: Weiß für ein frisches Aussehen */
                        "-fx-text-fill: #333333; " + /* Textfarbe: Dunkles Grau für guten Kontrast */
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif; " + /* Schriftart */
                        "-fx-font-size: 12px; " + /* Schriftgröße */
                        "-fx-padding: 4px 8px; " + /* Innenabstand */
                        "-fx-border-color: #CCCCCC; " + /* Randfarbe: Helles Grau für dezente Trennung */
                        "-fx-border-width: 1px; " + /* Randbreite */
                        "-fx-border-radius: 4px; " /* Randradius: Leicht größer für modernere Kanten */
        );

        selector.setStyle(
                "-fx-background-color: #FFFFFF; " +
                        "-fx-text-fill: #333333; " +
                        "-fx-font-family: 'Segoe UI', Arial, sans-serif; " +
                        "-fx-font-size: 14px; " +
                        "-fx-padding: 4px 8px; " +
                        "-fx-border-color: #CCCCCC; " +
                        "-fx-border-width: 1px; " +
                        "-fx-border-radius: 4px; "
        );


        selector.setOnMouseEntered(e -> selector.setEffect(SelectorShadow));
        selector.setOnMouseExited(e -> selector.setEffect(null));


        Background blackBackground = createBackground(Color.BLACK);
        Background redBackground = createBackground(Color.rgb(148, 61, 52));
        Background greenBackground = createBackground(Color.rgb(58, 120, 56));
        Background gradientBackground = createGradientBackground(Color.rgb(58, 120, 56), Color.rgb(78, 160, 76));

        LineBtn.setBackground(blackBackground);
        deleteBtn.setBackground(redBackground);
        BarBtn.setBackground(blackBackground);
        exportbutton.setBackground(gradientBackground);

        LineBtn.setTextFill(Color.WHITE);
        deleteBtn.setTextFill(Color.WHITE);
        BarBtn.setTextFill(Color.WHITE);
        exportbutton.setTextFill(Color.WHITE);

        Font font = Font.font("Arial", 14);
        //dateSelector.setFont(font);
        LineBtn.setFont(font);
        deleteBtn.setFont(font);
        BarBtn.setFont(font);
        exportbutton.setFont(font);

        // Padding inside the buttons
        Insets padding = new Insets(10, 20, 10, 20);
        LineBtn.setPadding(padding);
        deleteBtn.setPadding(padding);
        BarBtn.setPadding(padding);
        exportbutton.setPadding(padding);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(5.0);
        shadow.setOffsetX(3.0);
        shadow.setOffsetY(3.0);
        shadow.setColor(Color.color(0.4, 0.4, 0.4));

        applyShadowEffect(LineBtn, shadow);
        applyShadowEffect(deleteBtn, shadow);
        applyShadowEffect(BarBtn, shadow);
        applyShadowEffect(exportbutton, shadow);

        selector.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (StaticData.diagramType.equals("LineChart")) {
                if (!newValue.equals(StaticData.interval)){
                    if (newValue.equals("weekly")) {
                        drawLineDiagramWithInterval(newValue.toString());
                    }else{
                        ActionEvent a = new ActionEvent();
                        drawLine(a);
                    }
                }
            }else if (StaticData.diagramType.equals("BarChart")){
                if (!newValue.equals(StaticData.interval)){
                    drawBarWithInterval(Interval.valueOf(newValue.toString().toUpperCase()));
                }
            }
        });
        Rectangle border = new Rectangle(canvas.getWidth(), canvas.getHeight());
        border.setStroke(Color.BLACK);
        border.setStrokeWidth(2);
        border.setFill(null);
        
        // Add shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        rectangle.setEffect(dropShadow);

        closeButton.setStyle(
                "-fx-background-color: #ff4c4c; " + /* Red background color */
                        "-fx-text-fill: white; " + /* White text color */
                        "-fx-font-size: 12px; " + /* Smaller font size */
                        "-fx-padding: 6px 12px; " + /* Reduced padding */
                        "-fx-border-radius: 2px; " + /* Even less rounded corners */
                        "-fx-background-radius: 2px; " + /* Even less rounded background */
                        "-fx-cursor: hand; " + /* Hand cursor */
                        "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.1), 3, 0, 0, 2);" /* Further reduced drop shadow effect */
        );
        closeButton.setOnMouseEntered(e -> closeButton.setStyle(
                "-fx-background-color: #ff1c1c; " + /* Darker red on hover */
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 12px; " +
                        "-fx-padding: 6px 12px; " +
                        "-fx-border-radius: 2px; " +
                        "-fx-background-radius: 2px; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.1), 3, 0, 0, 2);"
        ));
        closeButton.setOnMouseExited(e -> closeButton.setStyle(
                "-fx-background-color: #ff4c4c; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 12px; " +
                        "-fx-padding: 6px 12px; " +
                        "-fx-border-radius: 2px; " +
                        "-fx-background-radius: 2px; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.1), 3, 0, 0, 2);"
        ));
        closeButton.setOnMousePressed(e -> closeButton.setStyle(
                "-fx-background-color: #cc0000; " + /* Even darker red on press */
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 12px; " +
                        "-fx-padding: 6px 12px; " +
                        "-fx-border-radius: 2px; " +
                        "-fx-background-radius: 2px; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.1), 3, 0, 0, 2);"
        ));
        closeButton.setOnMouseReleased(e -> closeButton.setStyle(
                "-fx-background-color: #ff1c1c; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 12px; " +
                        "-fx-padding: 6px 12px; " +
                        "-fx-border-radius: 2px; " +
                        "-fx-background-radius: 2px; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.1), 3, 0, 0, 2);"
        ));

        closeButton.setOnAction(e -> System.exit(0)); // Close the application


        pane.getChildren().add(border);
    }

    public void export(ActionEvent actionEvent) throws IOException {
        Export export = new Export();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        Stage stage = new Stage();
        File selectedDirectory = directoryChooser.showDialog(stage);
        if (!selectedDirectory.getAbsolutePath().isEmpty()) {
            try {
                export.exportToCSV(StaticData.combinedEinspeisen, "Einspeisen", selectedDirectory.getPath());
                export.exportToCSV(StaticData.combinedVerbrauch, "Verbrauch", selectedDirectory.getPath());
                StaticData.exportInfoText = "Successfully exported";
            }catch (Exception e){
                e.printStackTrace();
                StaticData.exportInfoText = "Export failed + " + e.getMessage();
            }finally {
                SceneController sc = new SceneController();
                sc.showPopup(stage);
            }
        }
    }
    private Background createBackground(Color color) {
        return new Background(new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY));
    }

    private Background createGradientBackground(Color color1, Color color2) {
        return new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, null, new Stop(0, color1), new Stop(1, color2)),
                new CornerRadii(10), Insets.EMPTY));
    }

    private void applyShadowEffect(Button button, DropShadow shadow) {
        button.setOnMouseEntered(e -> button.setEffect(shadow));
        button.setOnMouseExited(e -> button.setEffect(null));
    }
}
