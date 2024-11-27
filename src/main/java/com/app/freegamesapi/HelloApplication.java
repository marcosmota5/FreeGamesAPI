package com.app.freegamesapi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    // Static variable to hold the primary stage reference
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {

        // Store the primary stage reference in the static variable
        primaryStage = stage;

        // Create the loader
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));

        // Create the scene
        Scene scene = new Scene(fxmlLoader.load(), 1280, 768);

        // Set the application title
        stage.setTitle("Free Games API");

        // Set the icon for the application
        Image icon = new Image(HelloApplication.class.getResourceAsStream("/app-icon.png"));
        stage.getIcons().add(icon);

        // Set the scene and show the stage
        stage.setScene(scene);
        stage.show();
    }

    // Static method to return the primary stage
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}