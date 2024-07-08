package com.ubs.controller;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class PreviewWindowController {

    public Pane pane;

    @FXML
    public void initialize() {
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(this::handlePauseFinished);
        pause.play();
    }

    private void handlePauseFinished(ActionEvent actionEvent) {
        try {
            SceneController.getStartPage((Stage) pane.getScene().getWindow());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
