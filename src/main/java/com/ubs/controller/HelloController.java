package com.ubs.controller;

import com.ubs.Model.esl.ESLBillingData;
import com.ubs.Model.sdat.ValidatedMeteredData;
import com.ubs.Model.sdat.ValidatedMeteredData_12;
import com.ubs.helper.XmlFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML
    private Label welcomeText;

    @FXML
    void onHelloButtonClick(ActionEvent event) throws JAXBException {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(stage);
        List<ValidatedMeteredData> convertedSdatFiles = new ArrayList<>();
        List<ESLBillingData> convertedEslFiles = new ArrayList<>();
        for (File file : selectedFiles) {
            if (file == null) {
                return;
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
        System.out.println("Files converted: " + convertedSdatFiles.size());
        System.out.println("Files converted: " + convertedEslFiles.size());
    }

}
