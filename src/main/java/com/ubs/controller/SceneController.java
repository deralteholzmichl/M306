package com.ubs.controller;

import com.ubs.helper.ScenePath;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class SceneController {

    private static double x;
    private static double y;
    private static Parent main;
    public static void getInitialScene(Stage stage) throws IOException {
        main = FXMLLoader.load((SceneController.class.getResource(ScenePath.PreviewWindow.getPath())));
        Scene scene = new Scene(main);
        controlDrag(stage);
        stage.setTitle("Start");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(false);
        stage.setFullScreen(false);
        stage.setScene(scene);
        stage.show();
    }
    public static void controlDrag(Stage stage) {
        main.setOnMousePressed(event -> {
            x = stage.getX() - event.getScreenX();
            y = stage.getY() - event.getScreenY();
        });
        main.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + x);
            stage.setY(event.getScreenY() + y);
        });
    }

    public static void getMainScene(ActionEvent event) throws IOException {
        changeScreen(event, ScenePath.MainScene.getPath());
    }
    public static void getStartPage(Stage stage) throws IOException {
        main = FXMLLoader.load((SceneController.class.getResource(ScenePath.StartPage.getPath())));
        Scene scene = new Scene(main);
        stage.centerOnScreen();
        controlDrag(stage);
        stage.setTitle("Start");
        stage.setScene(scene);
        stage.setFullScreen(false);
        stage.show();
    }

    public static void refreshMenu(ActionEvent event) throws IOException {
        changeScreen(event, ScenePath.Login.getPath());
    }

    private static void changeScreen(ActionEvent event, String path) throws IOException {
        main = FXMLLoader.load(SceneController.class.getResource(path));
        Scene visitScene = new Scene(main);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.centerOnScreen();
        controlDrag(window);
        window.setScene(visitScene);
        window.show();
    }

    public static void close(ActionEvent actionEvent) {
        Node  source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
}

