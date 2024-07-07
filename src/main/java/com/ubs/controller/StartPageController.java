package com.ubs.controller;

import com.ubs.Model.CombinedData;
import com.ubs.Model.esl.ESLBillingData;
import com.ubs.Model.esl.Meter;
import com.ubs.Model.esl.TimePeriod;
import com.ubs.Model.sdat.ValidatedMeteredData;
import com.ubs.helper.StaticData;
import com.ubs.helper.XmlFactory;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

public class StartPageController {

    public Rectangle rectangle;
    public Button closeButton;
    @FXML
    private Button Button1;

    @FXML
    private Pane pane;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Text progressText;

    @FXML
    private Text progressText1;

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
                    Platform.runLater(() -> progressBar.setVisible(true));
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

                StaticData.combinedVerbrauch = BezugData;
                StaticData.combinedEinspeisen = EinspeisenData;

                System.out.println(EinspeisenData);
                System.out.println(BezugData);
                System.out.println("Files selected: " + selectedFiles.size());
                System.out.println("eindeutige Files: " + readedElements.size());
                System.out.println("Sdat Files converted: " + convertedSdatFiles.size());
                System.out.println("Esl Files converted: " + convertedEslFiles.size());
                //  Platform.runLater(() -> progressText.setText("Files converted: " + selectedFiles.size() + "/" + selectedFiles.size() + ":Finished"));
                Platform.runLater(() -> progressBar.setVisible(false));
                Platform.runLater(() -> Button1.setDisable(false));
                Platform.runLater(() -> {
                    try {
                        SceneController.getMainScene(event);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

            }catch (NullPointerException e) {
                Platform.runLater(() -> progressBar.setVisible(false));
                Platform.runLater(() -> progressText.setText("No Files selected"));
                Platform.runLater(() -> Button1.setDisable(false));
            }
        }).start();
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

    @FXML
    void initialize() {
        progressBar.setVisible(false);


        // Background color with gradient
        Background greenBackground = new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, null,
                        new Stop(0, Color.rgb(58, 120, 56)),
                        new Stop(1, Color.rgb(78, 160, 76))
                ),
                new CornerRadii(10), Insets.EMPTY));
        Button1.setBackground(greenBackground);

        // Text color and font
        Button1.setTextFill(Color.WHITE);
        Button1.setFont(Font.font("Arial", 14));

        // Padding inside the button
        Button1.setPadding(new Insets(10, 20, 10, 20));


        progressBar.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        progressBar.setStyle("-fx-accent: #3a7838;");

        progressText.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        progressText.setFill(Color.BLACK);

        rectangle.setFill(Color.WHITE);
        // Add shadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        rectangle.setEffect(dropShadow);
    }

    public void stop(ActionEvent actionEvent) {
    }
}
