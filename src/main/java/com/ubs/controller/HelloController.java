package com.ubs.controller;

import com.ubs.Model.CombinedData;
import com.ubs.Model.esl.ESLBillingData;
import com.ubs.Model.esl.Meter;
import com.ubs.Model.esl.TimePeriod;
import com.ubs.Model.esl.ValueRow;
import com.ubs.Model.sdat.Observation;
import com.ubs.Model.sdat.ValidatedMeteredData;
import com.ubs.helper.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.persistence.criteria.CriteriaBuilder;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;

public class HelloController {
    public DebugHelperDrawDiagram LineWievEntrys1;
    public DebugHelperDrawDiagram LineWievEntrys2;

    public List<BarChartEntry> barChartEntries;
    public List<CombinedData> combinedEinspeisen;
    public List<CombinedData> combinedVerbrauch;
    public DatePicker dateSelector;
    public ChoiceBox selector;
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
        pane.getChildren().remove(canvas);
        drawBarWithInterval(Interval.DAILY);
    }
    public void drawBarWithInterval(Interval interval){
        pane.getChildren().remove(canvas);
        if (dateSelector.getValue() != null) {
            String Date = dateSelector.getValue().toString();
            List<List<BarChartEntry>> barChartData =  new ArrayList<>();
            barChartData.add(BarChartViewGenerator(combinedEinspeisen,interval,Date));
            barChartData.add(BarChartViewGenerator(combinedVerbrauch,interval,Date));
            canvas = StatisticDrawer.drawBarChart(650, 400, 60, barChartData, interval);
            if (canvas == null) {
                progressText.setText("No Data for selected Date");
                return;
            }
            pane.getChildren().remove(canvas);
            StaticData.diagramType = "BarChart";
            StaticData.interval = interval.toString().toLowerCase();
            canvas.setLayoutY(100);
            pane.getChildren().add(canvas);
            progressText.setText(Date);
            selector.setValue(interval.toString().toLowerCase());
            return;
        }
        progressText.setText("Please select a valid Date");
    }




    @FXML
    void delete(ActionEvent event) {
        Export e = new Export();
        e.exportToCSV(combinedVerbrauch,"Verbrauch");
        e.exportToCSV(combinedEinspeisen,"Einspeisen");
        pane.getChildren().remove(canvas);
    }
    @FXML
    private Canvas canvas = new Canvas();

    void drawLineDiagramWithInterval(String drawInterval){
        pane.getChildren().remove(canvas);
        DebugHelperDrawDiagram data1 = LineViewGenerator(combinedVerbrauch,"Verbrauch");
        DebugHelperDrawDiagram data2 = LineViewGenerator(combinedEinspeisen,"Einspeisen");
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
                    if (usedDates.contains(data1.xList.get(i).getLast())){
                        xList.addAll(List.of("",""));
                    }else {
                        xList.addAll(data1.xList.get(i));
                        usedDates.add(data1.xList.get(i).getLast());
                    }
                }
            }
            lines.add(line);
            line = new ArrayList<>();
            for (int y = 0; y < data2.xList.size(); y++) {
                if (dates.contains(data2.xList.get(y).getFirst())) {
                    line.addAll(data2.yList.get(y));
                    if (usedDates.contains(data2.xList.get(y).getLast())){
                        xList.addAll(List.of("",""));
                    }else {
                        xList.addAll(data2.xList.get(y));
                        usedDates.add(data2.xList.get(y).getLast());
                    }
                }
            }
            lines.add(line);

            if (lines.size()<2) {
                progressText.setText("No Data for selected Date");
                return;
            }else {
                canvas = StatisticDrawer.drawLineDiagram(lines, 400, 500, 100, true, true, xList,Interval.valueOf(drawInterval.toUpperCase()));
                if (canvas == null) {
                    progressText.setText("No Diagram for selected Date");
                    return;
                }
                canvas.setLayoutY(100);
                pane.getChildren().add(canvas);
                StaticData.diagramType = "LineChart";
                StaticData.interval = drawInterval;
                return;
            }

        }
        progressText.setText("Please select a valid Date");

    }

    int canvasIndex = 0;
    @FXML
    void draw(ActionEvent event) {
        pane.getChildren().remove(canvas);
        DebugHelperDrawDiagram data1 = LineViewGenerator(combinedVerbrauch,"Verbrauch");
        DebugHelperDrawDiagram data2 = LineViewGenerator(combinedEinspeisen,"Einspeisen");
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
            if (lines.size()<2) {
                progressText.setText("No Data for selected Date");
                return;
            }else {
                canvas = StatisticDrawer.drawLineDiagram(lines, 400, 500, 100, true, true, xList, Interval.DAILY);
                if (canvas == null) {
                    progressText.setText("No Diagram for selected Date");
                    return;
                }
                pane.getChildren().remove(canvas);
                canvas.setLayoutY(100);
                pane.getChildren().add(canvas);
                StaticData.diagramType = "LineChart";
                StaticData.interval = "daily";
                selector.setValue("daily");
                return;
            }

        }
        progressText.setText("Please select a valid Date");

        //Startwert der Grafik ist der erste Wert der jeweiligen Liste
        //MAX_HEIGHT: Wie gross die grafik sein soll
        //MAX_WIDTH: Wie breit die grafik sein soll
        //PADDING: Abstand von Rand zur Grafik(links/unten) für Beschriftung -> Mind.35 für Beschriftung
        //BESCHRIFTUNGX: Beschriftung der X-Achse
        //BESCHRIFTUNGY: Beschriftung der Y-Achse
     //   canvas = StatisticDrawer.drawLineDiagram(new ArrayList<>(List.of(4.0,26.0,10.0,4.0,7.9)),200.0,400.0,35.0,true,true,new ArrayList<>(List.of("12:00","12:15","12:30","12:45","13:00")));
       /*
        canvas = ControllerViews.get(canvasIndex);
        canvasIndex++;
        if (canvasIndex > ControllerViews.size() - 1){
            canvasIndex = 0;
        }
        canvas.setLayoutY(100);
        pane.getChildren().add(canvas);

        */
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




        this.combinedVerbrauch = BezugData;
        this.combinedEinspeisen = EinspeisenData;
        //LineViewGenerator(BezugData,Interval.DAILY);

        //date can be null if Interval is not DAILY
        //BarChartViewGenerator(EinspeisenData, Interval.DAILY,"2022-06-29T22:00:00Z");

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
    public List<BarChartEntry> BarChartViewGenerator(List<CombinedData> BezugData,Interval interval,String datum){
        List<BarChartEntry> dhdd = new ArrayList<>();
        ArrayList<Canvas> views =  new ArrayList<>();
        barChartEntries = new ArrayList<>();
        if (interval == Interval.DAILY){
            for (CombinedData c:BezugData){
                for (ValidatedMeteredData v: c.getValidatedMeteredData()){
                    System.out.println(v.getMeteringData().getInterval().getEndDateTime());
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(v.getMeteringData().getInterval().getEndDateTime());
                    LocalDate localDate = zonedDateTime.toLocalDate();
                    if (localDate.toString().equals(datum)){
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
                    /*
                    BarChartEntry bce = new BarChartEntry(previousTimePeriodDate + "-" + timePeriodDate, stand);
                    dhdd.add(bce);
                     */
                }
            }
            //views.add(StatisticDrawer.drawBarChart(400, 300, 60, dhdd));
        }
        /*
        if (!views.isEmpty()) {
            BarChartViews = views;
        }else{
            dhdd.add(new BarChartEntry("No Data for selected Date",0.0));
        }

         */
        return new ArrayList<>();
    }

    public DebugHelperDrawDiagram LineViewGenerator(List<CombinedData> BezugData,String fileArt){
        DebugHelperDrawDiagram dhdd = new DebugHelperDrawDiagram();
        ArrayList<Canvas> views =  new ArrayList<>();
        if (true){
            int i = 0;
            for (CombinedData c:BezugData){
                double stand = 0.0;
                for (ValueRow vr : c.getEslBillingData().getMeter().getFirst().getTimePeriod().getLast().getValueRow()) {
                    if (fileArt.equals("Verbrauch")) {
                        if (vr.getObis().equals("1-1:1.8.1")) {
                            stand = Double.valueOf(vr.getValue());
                            break;
                        }
                    }else{
                            if (vr.getObis().equals("1-1:1.8.2")) {
                                stand = Double.valueOf(vr.getValue());
                                break;
                            }
                    }
                }
                for (ValueRow vr : c.getEslBillingData().getMeter().getFirst().getTimePeriod().getLast().getValueRow()) {
                    if (fileArt.equals("Einspeisen")){
                        if (vr.getObis().equals("1-1:2.8.1")) {
                            stand += Double.valueOf(vr.getValue());
                            break;
                        }
                    }else{
                        if (vr.getObis().equals("1-1:2.8.2")) {
                            stand += Double.valueOf(vr.getValue());
                            break;
                        }
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
                    i++;
                    // views.add(StatisticDrawer.drawLineDiagram(yAchsePunkte, 400, 700, 60, true, true, xAchse));
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
                    for (ValueRow v : c.getEslBillingData().getMeter().getFirst().getTimePeriod().get(t).getValueRow()) {
                        if (v.getObis().equals("1-1:1.8.2")) {
                            stand += Double.parseDouble(v.getValue());
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
                        //  views.add(StatisticDrawer.drawLineDiagram(yAchsePunkte, 400, 700, 60, true, true, xAchse));
                    }
                }
            }
        }
        return dhdd;
        //ControllerViews = views;
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
                LocalDateTime NextZonedDateTime;
                LocalDate NextMetredDataDate = LocalDate.of(2100,1,1);
                try {
                    NextZonedDateTime = LocalDateTime.parse(eslBillingDataList.get(esl +1).getMeter().getFirst().getTimePeriod().getFirst().getEnd());
                    NextMetredDataDate = NextZonedDateTime.toLocalDate();
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("Last Element");
                }
                if (((ValidatedMetredDataDate.isAfter(ESLBillingDataDate) || ValidatedMetredDataDate.equals(ESLBillingDataDate)) && (ValidatedMetredDataDate.isBefore(NextMetredDataDate)))){
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
        selector.getItems().addAll("hourly", "daily", "weekly");
        selector.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
              if (StaticData.diagramType.equals("LineChart")) {
                  if (!newValue.equals(StaticData.interval)){
                      if (newValue.equals("weekly")) {
                          drawLineDiagramWithInterval(newValue.toString());
                      }else{
                          ActionEvent a = new ActionEvent();
                         
                           draw(a);
                      }
                  }
              }else if (StaticData.diagramType.equals("BarChart")){
                  if (!newValue.equals(StaticData.interval)){
                      drawBarWithInterval(Interval.valueOf(newValue.toString().toUpperCase()));
                  }
              }
        });
    }
}