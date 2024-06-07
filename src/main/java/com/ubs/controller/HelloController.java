package com.ubs.controller;

import com.ubs.Model.CombinedData;
import com.ubs.Model.esl.ESLBillingData;
import com.ubs.Model.sdat.ValidatedMeteredData;
import com.ubs.Model.sdat.ValidatedMeteredData_12;
import com.ubs.helper.BarChartEntry;
import com.ubs.helper.StatisticDrawer;
import com.ubs.helper.XmlFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HelloController {
    @FXML
    private Button Button1;
    @FXML
    private Pane pane;
    @FXML
    private Button Button3;
    @FXML
    private Button Button4;
    @FXML
    void drawBar(ActionEvent event) {
        pane.getChildren().remove(canvas);
        ArrayList<BarChartEntry> sampleList = new ArrayList<>();
        sampleList.add(new BarChartEntry("Test1", 10));
        sampleList.add(new BarChartEntry("Test2", 0.4));
        sampleList.add(new BarChartEntry("Test3", 20));
        sampleList.add(new BarChartEntry("Test4", 5));

        canvas = StatisticDrawer.drawBarChart(200,200,40,sampleList);
        canvas.setLayoutY(100);
        pane.getChildren().add(canvas);

    }
    @FXML
    void delete(ActionEvent event) {
        pane.getChildren().remove(canvas);
    }
    @FXML
    private Canvas canvas = new Canvas();

    @FXML
    void draw(ActionEvent event) {
        pane.getChildren().remove(canvas);
        //Startwert der Grafik ist der erste Wert der jeweiligen Liste
        //MAX_HEIGHT: Wie gross die grafik sein soll
        //MAX_WIDTH: Wie breit die grafik sein soll
        //PADDING: Abstand von Rand zur Grafik(links/unten) für Beschriftung -> Mind.35 für Beschriftung
        //BESCHRIFTUNGX: Beschriftung der X-Achse
        //BESCHRIFTUNGY: Beschriftung der Y-Achse
        canvas = StatisticDrawer.drawLineDiagram(new ArrayList<>(List.of(4.0,26.0,10.0,4.0,7.9)),200.0,400.0,35.0,true,true,new ArrayList<>(List.of("12:00","12:15","12:30","12:45","13:00")));
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
        for (File file : selectedFiles) {
            if (file == null) {
                break;
            }
            if (!file.getPath().contains(".xml")) {
                return;
            }
            if (file.getPath().contains("EdmRegisterWertExport")) {
                try {
                    convertedEslFiles.add(XmlFactory.convertToESLBillingData(file.getPath()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    convertedSdatFiles.add(XmlFactory.convertToValidatedMeteredData_12_13_14(file.getPath()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //Sdat Files werden einspeisung und bezug zugeordnet und Duplikate werden aussortiert
        List<String> readedElements = new ArrayList<>();
        List<ValidatedMeteredData> validatedMeteredDataListEinspeisen = new ArrayList<>();
        List<ValidatedMeteredData> validatedMeteredDataListBezug = new ArrayList<>();
        if (!convertedSdatFiles.isEmpty()){
            for (ValidatedMeteredData validatedMeteredData : convertedSdatFiles) {
                validatedMeteredData.getValidatedMeteredData_HeaderInformation();
                if(!readedElements.contains(validatedMeteredData.getValidatedMeteredData_HeaderInformation().getInstanceDocument().getCreation())){
                    readedElements.add(validatedMeteredData.getValidatedMeteredData_HeaderInformation().getInstanceDocument().getCreation());
                    if (validatedMeteredData.getValidatedMeteredData_HeaderInformation().getInstanceDocument().getDocumentID().contains("ID742")) {
                        validatedMeteredDataListBezug.add(validatedMeteredData);
                    } else {
                        validatedMeteredDataListEinspeisen.add(validatedMeteredData);
                    }
                }
            }
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
        validatedMeteredDataListEinspeisen.sort((o1, o2) -> o1.compareTo(o2.getValidatedMeteredData_HeaderInformation().getInstanceDocument().getCreation()));
        validatedMeteredDataListBezug.sort((o1, o2) -> o1.compareTo(o2.getValidatedMeteredData_HeaderInformation().getInstanceDocument().getCreation()));
        List<List<CombinedData>> BezugData = CombineData(eindeutigeESLFiles,validatedMeteredDataListBezug);
        List<List<CombinedData>> EinspeisenData = CombineData(eindeutigeESLFiles,validatedMeteredDataListEinspeisen);




        System.out.println(EinspeisenData);
        System.out.println(BezugData);
        System.out.println("Files selected: " + selectedFiles.size());
        System.out.println("eindeutige Files: " + readedElements.size());
        System.out.println("Sdat Files converted: " + convertedSdatFiles.size());
        System.out.println("Esl Files converted: " + convertedEslFiles.size());
    }
    public List<List<CombinedData>> CombineData(List<ESLBillingData> eslBillingDataList, List<ValidatedMeteredData> validatedMeteredDataList){
        List<List<CombinedData>> Data = new ArrayList<>();
        for (int esl = 0; esl < eslBillingDataList.size(); esl++){
            List<CombinedData> CombinedDataList = new ArrayList<>();
            for (int vMdLE = 0; vMdLE < validatedMeteredDataList.size(); vMdLE++){
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(validatedMeteredDataList.get(vMdLE).getValidatedMeteredData_HeaderInformation().getInstanceDocument().getCreation());
                LocalDate ValidatedMetredDataDate = zonedDateTime.toLocalDate();

                LocalDateTime ESLBillingDataDateTime = LocalDateTime.parse(eslBillingDataList.get(esl).getHeader().getCreated());
                LocalDate ESLBillingDataDate = ESLBillingDataDateTime.toLocalDate();

                LocalDateTime NextzonedDateTime;
                LocalDate NextValidatedMetredDataDate = LocalDate.now();
                try {
                    NextzonedDateTime = LocalDateTime.parse(eslBillingDataList.get(esl + 1).getHeader().getCreated());
                    NextValidatedMetredDataDate = NextzonedDateTime.toLocalDate();
                }catch (Exception e){
                    System.out.println(e);
                    System.out.println("Last Element");
                }
                if ((ValidatedMetredDataDate.isAfter(ESLBillingDataDate)|| ValidatedMetredDataDate.equals(ESLBillingDataDate)) && (ValidatedMetredDataDate.isBefore(NextValidatedMetredDataDate))){
                    CombinedData C = new CombinedData();
                    C.setEslBillingData(eslBillingDataList.get(esl));
                    C.setValidatedMeteredData(validatedMeteredDataList.get(vMdLE));
                    CombinedDataList.add(C);
                }

            }
            Data.add(CombinedDataList);
        }
        return Data;
    }




    public void initialize() {
        //Initialize the Pane
        pane.setPrefWidth(600);
        pane.setPrefHeight(500);
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
