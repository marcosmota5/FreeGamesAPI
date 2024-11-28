package com.app.freegamesapi.Helpers;

import com.app.freegamesapi.Controllers.HomeController;
import com.app.freegamesapi.Controllers.GameDetailsController;
import com.app.freegamesapi.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneUtils {

    /**
     * Switch the scene to the one received in the parameters.
     *
     * @param currentAnchorPane The current anchor pane.
     * @param fxml The fxml file of the new scene.
     */
    public static void switchScene(AnchorPane currentAnchorPane, String fxml) throws IOException  {
        AnchorPane nextAnchorPane=FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource(fxml)));
        currentAnchorPane.getChildren().removeAll();
        currentAnchorPane.getChildren().setAll(nextAnchorPane);
    }

    /**
     * Switch the scene to the game details page
     *
     * @param currentAnchorPane The current anchor pane.
     * @param fxml The fxml file of the new scene.
     * @param gameId Id of the game to be passed to the game details page.
     */
    public static void switchScene(AnchorPane currentAnchorPane, String fxml, int gameId) throws IOException  {
        // Use FXMLLoader to load the FXML file
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(HelloApplication.class.getResource(fxml)));

        // Load the new AnchorPane from the FXML file
        AnchorPane nextAnchorPane = loader.load();

        // Get the controller of the HomeController
        Object controller = loader.getController();

        // Pass the user instance if it's GameDetailsController
        if (controller instanceof GameDetailsController) {
            ((GameDetailsController)controller).setGameId(gameId); // Pass the game id to GameDetailsController
        }

        // Replace the current AnchorPane's children with the new one
        currentAnchorPane.getChildren().removeAll();
        currentAnchorPane.getChildren().setAll(nextAnchorPane);
    }
}