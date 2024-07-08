package com.ubs.controller;

import com.ubs.helper.StaticData;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;

public class ExportInfo {

    public Button okButton;
    public Label label;
    public AnchorPane pane;

    public void ok(ActionEvent actionEvent) throws IOException {
        SceneController.close(actionEvent);
    }
    @FXML
    void initialize() {
        label.setText(StaticData.exportInfoText);
        Background blueBackground = new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, null,
                        new Stop(0, Color.rgb(70, 130, 180)), // Startfarbe: Stahlblau
                        new Stop(1, Color.rgb(25, 25, 112))   // Endfarbe: Mitternachtsblau
                ),
                new CornerRadii(10), Insets.EMPTY));
        okButton.setBackground(blueBackground);

        // Text color and font
        okButton.setTextFill(Color.WHITE);
        okButton.setFont(Font.font("Arial", 14));
        // Padding inside the button
        okButton.setPadding(new Insets(10, 20, 10, 20));
        pane.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-padding: 10;-fx-background-color: white");
    }
}
