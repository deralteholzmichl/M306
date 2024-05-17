package com.ubs.helper;

public enum ScenePath {

    Login("/com/ubs.controller/hello-view.fxml");

    private final String path;

    private ScenePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
