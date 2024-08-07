package com.ubs.helper;

public enum ScenePath {

    Login("/com/ubs/hello-view.fxml"),
    StartPage("/com/ubs/startPage.fxml"),
    MainScene("/com/ubs/MainScene.fxml"),
    PreviewWindow("/com/ubs/PreviewWindow.fxml"),
    ExportInfo("/com/ubs/exportInfo.fxml");

    private final String path;

    ScenePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
