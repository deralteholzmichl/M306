package com.ubs.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;

public class ExportInfo {

    public Text infoText;
    public Button okButton;

    public void ok(ActionEvent actionEvent) throws IOException {
        SceneController.getMainScene(actionEvent);
    }
    @FXML
    void initialize() {
        infoText.setText("Exported successfully");
    }
}
