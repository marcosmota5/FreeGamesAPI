package com.app.freegamesapi.Controllers;

import com.google.gson.JsonObject;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class HomeController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Getting the game id 452 from api...");

        // Call the get data method
        getAPIData();

    }

    private void getAPIData() {
        // Create an HttpClient object
        HttpClient client = HttpClient.newHttpClient();

        // Create a GET request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.freetogame.com/api/game?id=452"))
                .build();

        // Send the request asynchronously
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    // Try to parse the JSON response of one single object
                    try {
                        // Get the json object by parsing the string
                        JsonObject gameObject = JsonParser.parseString(response).getAsJsonObject();

                        // Extract the game title from the json
                        String gameTitle = gameObject.get("title").getAsString();

                        // Update the text of the welcome text when the call is done
                        javafx.application.Platform.runLater(() -> welcomeText.setText(gameTitle));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }
}