package com.app.freegamesapi.Controllers;

import com.app.freegamesapi.Helpers.AppPreferences;
import com.app.freegamesapi.Helpers.SceneUtils;
import com.app.freegamesapi.Helpers.ThemeUtils;
import com.app.freegamesapi.Models.Game;
import com.app.freegamesapi.Models.SystemRequirements;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Locale;


import static com.app.freegamesapi.Helpers.LinkUtils.openLink;

public class GameDetailsController {

    @FXML
    private AnchorPane anpGameDetails;

    @FXML
    private Button btnBack;

    @FXML
    private HBox hbxImages;

    @FXML
    private Hyperlink hlkGameUrl;

    @FXML
    private Hyperlink hlkLinkedIn;

    @FXML
    private Hyperlink hlkRepository;

    @FXML
    private Hyperlink hlkSource;

    @FXML
    private ImageView imgCloseEnlarged;

    @FXML
    private ImageView imgEnlarged;

    @FXML
    private ImageView imgThumbnail;

    @FXML
    private ComboBox<String> cmbTheme;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblDeveloper;

    @FXML
    private Label lblGenre;

    @FXML
    private Label lblGraphics;

    @FXML
    private Label lblMemory;

    @FXML
    private Label lblOS;

    @FXML
    private Label lblPlatform;

    @FXML
    private Label lblProcessor;

    @FXML
    private Label lblPublisher;

    @FXML
    private Label lblReleaseDate;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblStorage;

    @FXML
    private Label lblTitle;

    @FXML
    private ProgressIndicator pgiFetchingGameDetails;

    @FXML
    private Label lblFetchingGameDetails;

    @FXML
    private Pane pneEnlarged;

    private int gameId;

    @FXML
    public void initialize() {

        // Set the actions for hyperlinks
        hlkLinkedIn.setOnAction(event -> openLink("https://linkedin.com/in/marcosmota5"));
        hlkRepository.setOnAction(event -> openLink("https://github.com/marcosmota5/FreeGamesAPI"));
        hlkSource.setOnAction(event -> openLink("https://www.freetogame.com/"));

        // Use setOnMouseClicked
        imgCloseEnlarged.setOnMouseClicked(event -> {
            pneEnlarged.setVisible(false);
        });

        // Set the theme list
        cmbTheme.setItems(ThemeUtils.getThemes());

        // Load the saved login from preferences and display it
        String[] preferences = AppPreferences.loadPreferences();

        // Set the initial theme
        cmbTheme.setValue(preferences[0]);

        // Call the method to set the theme
        ThemeUtils.changeTheme(this, cmbTheme.getValue());

        // Add a listener to detect when the selection changes
        cmbTheme.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) { // Ensure a valid selection
                ThemeUtils.changeTheme(this, newValue); // Call the changeTheme method with the selected value
            }
        });
    }

    @FXML
    void getGame(ActionEvent event) {

        // Show progress and disable buttons immediately
        Platform.runLater(() -> {
            pgiFetchingGameDetails.setVisible(true);
            lblFetchingGameDetails.setVisible(true);

            btnBack.setDisable(true);
        });

        // Call the asynchronous method and handle results
        Game.getGameById(gameId)
                .thenAccept(game -> {
                    // Update the ListView on the JavaFX Application Thread
                    Platform.runLater(() -> {

                        // Set the values
                        setGameData(game);

                        // Make the progress invisible
                        pgiFetchingGameDetails.setVisible(false);
                        lblFetchingGameDetails.setVisible(false);

                        // Enable the buttons
                        btnBack.setDisable(false);
                    });
                })
                .exceptionally(ex -> {
                    // Handle exceptions
                    Platform.runLater(() -> {
                        System.err.println("Error fetching game details: " + ex.getMessage());

                        // Make the progress invisible and re-enable buttons
                        pgiFetchingGameDetails.setVisible(false);
                        lblFetchingGameDetails.setVisible(false);

                        btnBack.setDisable(false);
                    });
                    return null;
                });
    }

    private void setGameData(Game game) {
        // If the game is not null, set the values
        if (game != null) {
            // Update the values from the selected game
            lblTitle.setText(game.getTitle());
            lblDescription.setText("                    " + game.getDescription());
            Tooltip tooltip = new Tooltip(game.getDescription());
            lblDescription.setTooltip(tooltip);
            lblGenre.setText(game.getGenre());
            lblPlatform.setText(game.getPlatform());
            lblPublisher.setText(game.getPublisher());
            lblDeveloper.setText(game.getDeveloper());
            lblStatus.setText(game.getStatus());
            hlkGameUrl.setText(game.getGameUrl());
            hlkGameUrl.setOnAction(event -> openLink(game.getGameUrl()));

            setSystemRequirements(game.getSystemRequirements());

            // Format the release date using the system's current locale
            DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.getDefault());
            String formattedDate = game.getReleaseDate().format(formatter);

            lblReleaseDate.setText(formattedDate);

            Image newImage = new Image(game.getThumbnail());

            imgThumbnail.setImage(newImage);

            setScreenshots(game.getScreenshots());
        } else {
            lblTitle.setText("");
            lblDescription.setText("");
            lblGenre.setText("");
            lblPlatform.setText("");
            lblPublisher.setText("");
            lblDeveloper.setText("");
            lblStatus.setText("");
            lblReleaseDate.setText("");
            hlkGameUrl.setText("");
            hlkGameUrl.setOnAction(null); // Removes the event handler

            setSystemRequirements(null);

            Image newImage = new Image(getClass().getResource("/no-image.png").toExternalForm());

            imgThumbnail.setImage(newImage);
        }
    }

    private void setSystemRequirements(SystemRequirements requirements) {

        // Clear the values
        lblOS.setText("");
        lblProcessor.setText("");
        lblMemory.setText("");
        lblGraphics.setText("");
        lblStorage.setText("");

        // If the requirements are not null, set the values
        if (requirements != null) {
            lblOS.setText(requirements.getOs());
            lblProcessor.setText(requirements.getProcessor());
            lblMemory.setText(requirements.getMemory());
            lblGraphics.setText(requirements.getGraphics());
            lblStorage.setText(requirements.getStorage());
        }
    }

    public void setGameId(int gameId) {
        // Set the current user property
        this.gameId = gameId;

        getGame(null);
    }

    @FXML
    void goBack(ActionEvent event) throws IOException {
        // Get the stage from anchor
        Stage stage = (Stage)anpGameDetails.getScene().getWindow();

        // Switch the scene
        SceneUtils.switchScene(anpGameDetails, "home-view.fxml");
    }

    public void setScreenshots(List<String> screenshotUrls) {
        hbxImages.getChildren().clear(); // Clear any existing thumbnails

        if (screenshotUrls == null || screenshotUrls.isEmpty()) {
            imgEnlarged.setImage(null); // No images to display
            return;
        }

        // Load the first image as the default enlarged image
        Image firstImage = new Image(screenshotUrls.get(0));
        imgEnlarged.setImage(firstImage);

        // Add thumbnails to the HBox
        for (String url : screenshotUrls) {
            //ImageView thumbnail = createThumbnail(url);
            hbxImages.getChildren().add(createThumbnail(url));
        }
    }

    private StackPane createThumbnail(String url) {
        // Create an ImageView for the thumbnail
        ImageView thumbnail = new ImageView(new Image(url));
        thumbnail.setPreserveRatio(true);
        thumbnail.setFitHeight(225); // Set thumbnail height

        // Change the cursor to a hand and add a border on hover
        thumbnail.setOnMouseEntered(event -> {
            thumbnail.setStyle("-fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgb(0,118,184), 10, 0.5, 0, 0);");
            thumbnail.setFitHeight(230); // Increase height on hover
        });

        // Reset style and height when hover ends
        thumbnail.setOnMouseExited(event -> {
            thumbnail.setStyle(""); // Reset border
            thumbnail.setFitHeight(225); // Reset height
        });

        // Set click action to update the enlarged image and show the enlarged pane
        thumbnail.setOnMouseClicked(event -> {
            Image clickedImage = new Image(url);
            imgEnlarged.setImage(clickedImage); // Update the enlarged image
            pneEnlarged.setVisible(true);
        });

        // Wrap the ImageView in a StackPane and add margins
        StackPane wrapper = new StackPane(thumbnail);
        StackPane.setMargin(thumbnail, new Insets(10, 7, 10, 7)); // Add 10px margins around the thumbnail

        // Ensure the StackPane background is transparent
        wrapper.setStyle("-fx-background-color: transparent;");

        // Return the StackPane
        return wrapper;
    }

}