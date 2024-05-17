package com.ubs.controller;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
         SceneController.getInitialScene(stage);
    }
    @Override
    public void init() throws Exception {
    }
    @Override
    public void stop() throws Exception {
    }

    public static void main(String[] args) {
        launch();
    }

}