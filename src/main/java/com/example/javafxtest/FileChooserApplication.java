package com.example.javafxtest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;

import java.io.IOException;

public class FileChooserApplication extends  Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FileChooserApplication.class.getResource("filechooser-view.fxml"));
        // fxmlLoader.setController();
        VBox loadedParent = fxmlLoader.load();
        Scene scene = new Scene(loadedParent, 700, 300);
        stage.setTitle("Pick files for rendering!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}