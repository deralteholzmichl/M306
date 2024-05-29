package com.ubs.controller;

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
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.ArrayList;
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
            if (file.getPath().contains("EdmRegisterWertExport")){
                try {
                    convertedEslFiles.add(XmlFactory.convertToESLBillingData(file.getPath()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    convertedSdatFiles.add(XmlFactory.convertToValidatedMeteredData_12_13_14(file.getPath()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Files selected: " + selectedFiles.size());
        System.out.println("Sdat Files converted: " + convertedSdatFiles.size());
        System.out.println("Esl Files converted: " + convertedEslFiles.size());
    }
    public void initialize() {
        pane.setPrefWidth(500);
        pane.setPrefHeight(500);
    }

}
