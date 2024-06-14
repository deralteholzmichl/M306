package com.ubs.controller;

import com.ubs.Model.CombinedData;
import com.ubs.Model.esl.ESLBillingData;
import com.ubs.Model.esl.Meter;
import com.ubs.Model.esl.TimePeriod;
import com.ubs.Model.esl.ValueRow;
import com.ubs.Model.sdat.Observation;
import com.ubs.Model.sdat.ValidatedMeteredData;
import com.ubs.Model.sdat.ValidatedMeteredData_12;
import com.ubs.helper.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.persistence.criteria.CriteriaBuilder;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

public class HelloController {
    private ArrayList<Canvas> ControllerViews;
    private ArrayList<Canvas> BarChartViews;
    @FXML
    private Text progressText;
    @FXML
    private Button Button1;
    @FXML
    private Pane pane;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button Button3;
    @FXML
    private Button Button4;
    @FXML
    void drawBar(ActionEvent event) {
        canvasIndex = 0;
        pane.getChildren().remove(canvas);
        ArrayList<BarChartEntry> sampleList = new ArrayList<>();
        sampleList.add(new BarChartEntry("Test1", 10));
        sampleList.add(new BarChartEntry("Test2", 0.4));
        sampleList.add(new BarChartEntry("Test3", 20));
        sampleList.add(new BarChartEntry("Test4", 5));

        canvas = StatisticDrawer.drawBarChart(200,200,40,sampleList);
        canvas.setLayoutY(100);
        pane.getChildren().add(canvas);

        pane.getChildren().remove(canvas);
        //Startwert der Grafik ist der erste Wert der jeweiligen Liste
        //MAX_HEIGHT: Wie gross die grafik sein soll
        //MAX_WIDTH: Wie breit die grafik sein soll
        //PADDING: Abstand von Rand zur Grafik(links/unten) für Beschriftung -> Mind.35 für Beschriftung
        //BESCHRIFTUNGX: Beschriftung der X-Achse
        //BESCHRIFTUNGY: Beschriftung der Y-Achse
        //   canvas = StatisticDrawer.drawLineDiagram(new ArrayList<>(List.of(4.0,26.0,10.0,4.0,7.9)),200.0,400.0,35.0,true,true,new ArrayList<>(List.of("12:00","12:15","12:30","12:45","13:00")));
        canvas = BarChartViews.get(canvasIndex);
        canvasIndex++;
        if (canvasIndex > BarChartViews.size() - 1){
            canvasIndex = 0;
        }
        canvas.setLayoutY(100);
        pane.getChildren().add(canvas);
    }
    @FXML
    void delete(ActionEvent event) {
        pane.getChildren().remove(canvas);
    }
    @FXML
    private Canvas canvas = new Canvas();
    int canvasIndex = 0;
    @FXML
    void draw(ActionEvent event) {
        pane.getChildren().remove(canvas);
        //Startwert der Grafik ist der erste Wert der jeweiligen Liste
        //MAX_HEIGHT: Wie gross die grafik sein soll
        //MAX_WIDTH: Wie breit die grafik sein soll
        //PADDING: Abstand von Rand zur Grafik(links/unten) für Beschriftung -> Mind.35 für Beschriftung
        //BESCHRIFTUNGX: Beschriftung der X-Achse
        //BESCHRIFTUNGY: Beschriftung der Y-Achse
     //   canvas = StatisticDrawer.drawLineDiagram(new ArrayList<>(List.of(4.0,26.0,10.0,4.0,7.9)),200.0,400.0,35.0,true,true,new ArrayList<>(List.of("12:00","12:15","12:30","12:45","13:00")));
        canvas = ControllerViews.get(canvasIndex);
        canvasIndex++;
        if (canvasIndex > ControllerViews.size() - 1){
            canvasIndex = 0;
        }
        canvas.setLayoutY(100);
        pane.getChildren().add(canvas);
    }
    @FXML
    private Button Button2;
    @FXML
    void onHelloButtonClick(ActionEvent event) throws JAXBException {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);
        List<ValidatedMeteredData> convertedSdatFiles = new ArrayList<>();
        List<ESLBillingData> convertedEslFiles = new ArrayList<>();
        progressBar.setVisible(true);
        Button1.setDisable(true);
        new Thread(() -> {
            int progressCount = 0;
            try{
            for(File file : selectedFiles) {
                progressCount++;
                if (file == null) {
                    Platform.runLater(() -> progressText.setText("No Files selected"));
                    Platform.runLater(() -> progressBar.setVisible(false));
                    Platform.runLater(() -> Button1.setDisable(false));

                    break;
                }
                if (!file.getPath().contains(".xml")) {
                    Platform.runLater(() -> progressText.setText("Only XML Files allowed"));
                    Platform.runLater(() -> progressBar.setVisible(false));
                    Platform.runLater(() -> Button1.setDisable(false));
                    return;
                }
                if (file.getPath().contains("EdmRegisterWertExport")) {
                    try {
                        convertedEslFiles.add(XmlFactory.convertToESLBillingData(file.getPath()));
                    } catch (Exception e) {
                        Platform.runLater(() -> progressText.setText("Error converting ESL Files"));
                        Platform.runLater(() -> progressBar.setVisible(false));
                        Platform.runLater(() -> Button1.setDisable(false));
                        e.printStackTrace();
                    }
                } else {
                    try {
                        convertedSdatFiles.add(XmlFactory.convertToValidatedMeteredData_12_13_14(file.getPath()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Platform.runLater(() -> progressText.setText("Error converting Sdat Files"));
                        Platform.runLater(() -> progressBar.setVisible(false));
                        Platform.runLater(() -> Button1.setDisable(false));
                    }
                }
                int currentProgress = progressCount;
                Platform.runLater(() -> progressBar.setProgress((double) currentProgress / selectedFiles.size()));
                Platform.runLater(() -> progressText.setText("Files converted: " + currentProgress + "/" + selectedFiles.size()));
            }

        Platform.runLater(() -> progressText.setText("Processing Data"));
        //Sdat Files werden einspeisung und bezug zugeordnet und Duplikate werden aussortiert
        List<String> readedElements = new ArrayList<>();
        List<ValidatedMeteredData> validatedMeteredDataListEinspeisen = new ArrayList<>();
        List<ValidatedMeteredData> validatedMeteredDataListBezug = new ArrayList<>();
        if (!convertedSdatFiles.isEmpty()){
            for (ValidatedMeteredData validatedMeteredData : convertedSdatFiles) {
                if (validatedMeteredData.getValidatedMeteredData_HeaderInformation().getInstanceDocument().getDocumentID().contains("ID742")) {
                    validatedMeteredDataListBezug.add(validatedMeteredData);
                } else {
                    validatedMeteredDataListEinspeisen.add(validatedMeteredData);
                }
            }
            ArrayList<ValidatedMeteredData> EindeutigeValidatedMeteredDataListEinspeisen = new ArrayList<>();
            ArrayList<ValidatedMeteredData> EindeutigeValidatedMeteredDataListBezug = new ArrayList<>();

            for (ValidatedMeteredData v: validatedMeteredDataListBezug){
                if (!readedElements.contains(v.getMeteringData().getInterval().getEndDateTime())){
                    EindeutigeValidatedMeteredDataListBezug.add(v);
                    readedElements.add(v.getMeteringData().getInterval().getEndDateTime());
                }
            }
            readedElements = new ArrayList<>();
            for (ValidatedMeteredData v: validatedMeteredDataListEinspeisen){
                if (!readedElements.contains(v.getMeteringData().getInterval().getEndDateTime())){
                    EindeutigeValidatedMeteredDataListEinspeisen.add(v);
                    readedElements.add(v.getMeteringData().getInterval().getEndDateTime());
                }
            }
            validatedMeteredDataListBezug = EindeutigeValidatedMeteredDataListBezug;
            validatedMeteredDataListEinspeisen = EindeutigeValidatedMeteredDataListEinspeisen;
        }



        //Duplikate werden aussortiert für Esl files
        readedElements = new ArrayList<>();
        List<ESLBillingData> eindeutigeESLFiles = new ArrayList<>();
        if (!convertedEslFiles.isEmpty()){
            for (ESLBillingData eslBillingData : convertedEslFiles) {
                if(!readedElements.contains(eslBillingData.getHeader().getCreated())){
                    readedElements.add(eslBillingData.getHeader().getCreated());
                    eindeutigeESLFiles.add(eslBillingData);
                }
            }
        }
        eindeutigeESLFiles.sort((o1, o2) -> o1.compareTo(o2.getHeader().getCreated()));
        validatedMeteredDataListEinspeisen.sort((o1, o2) -> o1.compareTo(o2.getMeteringData().getInterval().getEndDateTime()));
        validatedMeteredDataListBezug.sort((o1, o2) -> o1.compareTo(o2.getMeteringData().getInterval().getEndDateTime()));

        Set<String> removeDoubleTimePeriods = new HashSet<>();
        for (ESLBillingData eslBillingData : eindeutigeESLFiles) {
            for (Meter meter : eslBillingData.getMeter()) {
                Iterator<TimePeriod> iterator = meter.getTimePeriod().iterator();
                while (iterator.hasNext()) {
                    TimePeriod timePeriod = iterator.next();
                    if (removeDoubleTimePeriods.contains(timePeriod.getEnd())) {
                        iterator.remove();
                    } else {
                        removeDoubleTimePeriods.add(timePeriod.getEnd());
                    }
                }
            }
        }

        for (ESLBillingData eslBillingData: eindeutigeESLFiles) {
            eslBillingData.getMeter().removeIf(meter -> meter.getTimePeriod().isEmpty());
        }
        eindeutigeESLFiles.removeIf(eslBillingData -> eslBillingData.getMeter().isEmpty());

        List<CombinedData> BezugData = CombineData(eindeutigeESLFiles,validatedMeteredDataListBezug);
        List<CombinedData> EinspeisenData = CombineData(eindeutigeESLFiles,validatedMeteredDataListEinspeisen);
        LineViewGenerator(BezugData,Interval.DAILY);
        //date can be null if Interval is not DAILY
        BarChartViewGenerator(EinspeisenData, Interval.DAILY,"2022-06-29T22:00:00Z");

        System.out.println(EinspeisenData);
        System.out.println(BezugData);
        System.out.println("Files selected: " + selectedFiles.size());
        System.out.println("eindeutige Files: " + readedElements.size());
        System.out.println("Sdat Files converted: " + convertedSdatFiles.size());
        System.out.println("Esl Files converted: " + convertedEslFiles.size());
        Platform.runLater(() -> progressText.setText("Files converted: " + selectedFiles.size() + "/" + selectedFiles.size() + ":Finished"));
        Platform.runLater(() -> progressBar.setVisible(false));
        Platform.runLater(() -> Button1.setDisable(false));
        }catch (NullPointerException e) {
            Platform.runLater(() -> progressBar.setVisible(false));
            Platform.runLater(() -> progressText.setText("No Files selected"));
            Platform.runLater(() -> Button1.setDisable(false));
        }
        }).start();
    }
    public void BarChartViewGenerator(List<CombinedData> BezugData, Interval interval,String datum){
        List<BarChartEntry> dhdd = new ArrayList<>();
        ArrayList<Canvas> views =  new ArrayList<>();
        if (interval == Interval.DAILY){
            for (CombinedData c:BezugData){
                for (ValidatedMeteredData v: c.getValidatedMeteredData()){
                    System.out.println(v.getMeteringData().getInterval().getEndDateTime());
                    if (v.getMeteringData().getInterval().getEndDateTime().equals(datum)){
                        for (Observation o : v.getMeteringData().getObservations()) {
                            BarChartEntry bce = new BarChartEntry("", o.getVolume());
                            dhdd.add(bce);
                        }
                    }
                }
            }
            if (!dhdd.isEmpty()) {
                views.add(StatisticDrawer.drawBarChart(400, 300, 60, dhdd));
                System.out.println(views.size());
            }
        }else {
            for (int y = 0; y < BezugData.size(); y++) {
                CombinedData c = BezugData.get(y);
                if (c.getEslBillingData().getMeter().size() > 1) {
                    ArrayList<String> timePeriodsEnd = new ArrayList<>();
                    for (TimePeriod tp : c.getEslBillingData().getMeter().getFirst().getTimePeriod()) {
                        timePeriodsEnd.add(tp.getEnd());
                    }
                    for (int m = 1; m < c.getEslBillingData().getMeter().size(); m++) {
                        for (TimePeriod tp : c.getEslBillingData().getMeter().get(m).getTimePeriod()) {
                            //     if (!timePeriodsEnd.contains(tp.getEnd())){
                            c.getEslBillingData().getMeter().getFirst().getTimePeriod().add(tp);
                        }
                        //     }
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
                    /*
                    for (ValueRow v : c.getEslBillingData().getMeter().getFirst().getTimePeriod().get(t).getValueRow()) {
                        if (v.getObis().equals("1-1:1.6.1")) {
                            stand = Double.valueOf(v.getValue());
                            break;
                        }
                    }

                     */
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
                    BarChartEntry bce = new BarChartEntry(previousTimePeriodDate + "-" + timePeriodDate, stand);
                    dhdd.add(bce);
                }
            }
            views.add(StatisticDrawer.drawBarChart(400, 300, 60, dhdd));
        }
        if (!views.isEmpty()) {
            BarChartViews = views;
        }else{
            dhdd.add(new BarChartEntry("No Data for selected Date",0.0));
        }
    }

    public void LineViewGenerator(List<CombinedData> BezugData, Interval interval){
        DebugHelperDrawDiagram dhdd = new DebugHelperDrawDiagram();
        ArrayList<Canvas> views =  new ArrayList<>();
        if (interval == Interval.DAILY){
            for (CombinedData c:BezugData){
                double stand = 0.0;
                for (ValueRow vr : c.getEslBillingData().getMeter().getFirst().getTimePeriod().getLast().getValueRow()) {
                    if (vr.getObis().equals("1-1:1.8.1")) {
                        stand = Double.valueOf(vr.getValue());
                        break;
                    }
                }
                for (ValidatedMeteredData v: c.getValidatedMeteredData()){
                    LocalDate validatedMetredDataDateEnd = ZonedDateTime.parse(v.getMeteringData().getInterval().getEndDateTime()).toLocalDate();
                    LocalDate validatedMetredDataDateStart = ZonedDateTime.parse(v.getMeteringData().getInterval().getStartDateTime()).toLocalDate();
                    ArrayList<Double> yAchsePunkte = new ArrayList<>();
                    ArrayList<String> xAchse = new ArrayList<>();
                    xAchse.add(validatedMetredDataDateStart.toString());
                    xAchse.add(validatedMetredDataDateEnd.toString());
                    for (Observation o : v.getMeteringData().getObservations()){
                        stand += Double.valueOf(o.getVolume());
                        yAchsePunkte.add(stand);
                    }
                    dhdd.yList.add(yAchsePunkte);
                    dhdd.xList.add(xAchse);
                    views.add(StatisticDrawer.drawLineDiagram(yAchsePunkte, 400, 700, 60, true, true, xAchse));
                }
            }
        }else {


            for (int y = 0; y < BezugData.size(); y++) {
                CombinedData c = BezugData.get(y);
                if (c.getEslBillingData().getMeter().size() > 1) {
                    ArrayList<String> timePeriodsEnd = new ArrayList<>();
                    for (TimePeriod tp : c.getEslBillingData().getMeter().getFirst().getTimePeriod()) {
                        timePeriodsEnd.add(tp.getEnd());
                    }
                    for (int m = 1; m < c.getEslBillingData().getMeter().size(); m++) {
                        for (TimePeriod tp : c.getEslBillingData().getMeter().get(m).getTimePeriod()) {
                            //     if (!timePeriodsEnd.contains(tp.getEnd())){
                            c.getEslBillingData().getMeter().getFirst().getTimePeriod().add(tp);
                        }
                        //     }
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
                    for (ValueRow v : c.getEslBillingData().getMeter().getFirst().getTimePeriod().get(t).getValueRow()) {
                        if (v.getObis().equals("1-1:1.8.1")) {
                            stand = Double.valueOf(v.getValue());
                            break;
                        }
                    }
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
                    if (!yAchsePunkte.isEmpty()) {
                        dhdd.yList.add(yAchsePunkte);
                        dhdd.xList.add(xAchse);
                        views.add(StatisticDrawer.drawLineDiagram(yAchsePunkte, 400, 700, 60, true, true, xAchse));
                    }
                }
            }
        }
        ControllerViews = views;
    }



    public List<CombinedData> CombineData(List<ESLBillingData> eslBillingDataList, List<ValidatedMeteredData> validatedMeteredDataList){
        List<CombinedData> Data = new ArrayList<>();
        for (int esl = 0; esl < eslBillingDataList.size(); esl++){
            CombinedData C = new CombinedData();
            C.setEslBillingData(eslBillingDataList.get(esl));
            for (ValidatedMeteredData validatedMeteredData : validatedMeteredDataList) {
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(validatedMeteredData.getMeteringData().getInterval().getEndDateTime());
                LocalDate ValidatedMetredDataDate = zonedDateTime.toLocalDate();

                LocalDateTime ESLBillingDataDateTime = LocalDateTime.parse(eslBillingDataList.get(esl).getMeter().getFirst().getTimePeriod().getFirst().getEnd());
                LocalDate ESLBillingDataDate = ESLBillingDataDateTime.toLocalDate();
                LocalDateTime PreviouszonedDateTime;
                LocalDate PreviousMetredDataDate = LocalDate.of(2000,1,1);
                try {
                    PreviouszonedDateTime = LocalDateTime.parse(eslBillingDataList.get(esl -1).getMeter().getFirst().getTimePeriod().getFirst().getEnd());
                    PreviousMetredDataDate = PreviouszonedDateTime.toLocalDate();
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("Last Element");
                }
                if ((ValidatedMetredDataDate.isAfter(PreviousMetredDataDate) && (ValidatedMetredDataDate.isBefore(ESLBillingDataDate) || ValidatedMetredDataDate.equals(ESLBillingDataDate)))){
                    C.getValidatedMeteredData().add(validatedMeteredData);
                }
            }
            if (!C.getValidatedMeteredData().isEmpty()) {
                Data.add(C);
            }
        }
        return Data;
    }

    public void initialize() {
        progressText.setText("Please import Files");
        progressBar.setVisible(false);
        //Initialize the Pane
        pane.setPrefWidth(800);
        pane.setPrefHeight(700);
        Button1.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        Button2.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        Button3.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        Button4.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        Button1.setTextFill(Color.WHITE);
        Button2.setTextFill(Color.WHITE);
        Button3.setTextFill(Color.WHITE);
        Button4.setTextFill(Color.WHITE);
        Button1.setFont(new javafx.scene.text.Font("Arial", 12));
        Button2.setFont(new javafx.scene.text.Font("Arial", 12));
        Button3.setFont(new javafx.scene.text.Font("Arial", 12));
        Button4.setFont(new javafx.scene.text.Font("Arial", 12));
    }

}