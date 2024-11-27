package com.app.freegamesapi.Helpers;

import com.app.freegamesapi.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class StageUtils {
    /**
     * Opens a new modal window with the specified FXML and passes data to the controller.
     * Blocks the parent window until the new window is closed.
     *
     * @param fxml The FXML file for the new view.
     * @param controllerMethodCall A method reference or lambda that accepts the controller.
     * @param ownerStage The parent stage (the window to block).
     * @throws IOException if the FXML cannot be loaded.
     */
    public static <T> void openModalWindow(String fxml, Stage ownerStage, String title, ControllerAction<T> controllerMethodCall) throws IOException {
        // Load the FXML for the new window
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(HelloApplication.class.getResource(fxml)));
        Parent root = fxmlLoader.load();

        // Get the controller for the new view
        T controller = fxmlLoader.getController();

        // Call the method on the controller (e.g., passing data)
        controllerMethodCall.call(controller);

        // Create a new Stage (window)
        Stage stage = new Stage();
        stage.setTitle(title);

        // Create a scene with the loaded FXML
        Scene scene = new Scene(root);
        stage.setScene(scene);

        // Set the modality to block the owner window
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(ownerStage);  // Block the owner stage

        // Show the modal window and wait for it to close before returning
        stage.showAndWait();
    }

    /**
     * Closes the stage (window) that contains the given node (e.g., AnchorPane).
     *
     * @param node The node inside the window to be closed (e.g., an AnchorPane).
     */
    public static void closeStage(Node node) {
        // Get the stage from the node
        Stage stage = (Stage) node.getScene().getWindow();
        if (stage != null) {
            // Close the stage
            stage.close();
        }
    }

    /**
     * Functional interface to pass actions on a controller.
     */
    @FunctionalInterface
    public interface ControllerAction<T> {
        void call(T controller);
    }
}