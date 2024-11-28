package com.app.freegamesapi.Controllers;

import com.app.freegamesapi.HelloApplication;
import com.app.freegamesapi.Helpers.AppPreferences;
import com.app.freegamesapi.Helpers.StageUtils;
import com.app.freegamesapi.Helpers.SceneUtils;
import com.app.freegamesapi.Helpers.ThemeUtils;
import com.app.freegamesapi.Models.Game;
import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.app.freegamesapi.Helpers.LinkUtils.openLink;

public class HomeController {

    @FXML
    private AnchorPane anpHome;

    @FXML
    private Button btnDetails;

    @FXML
    private Button btnRefresh;

    @FXML
    private CheckComboBox<String> ccbCategory;

    @FXML
    private ComboBox<String> cmbPlatform;

    @FXML
    private ComboBox<String> cmbSortBy;

    @FXML
    private ComboBox<String> cmbTheme;

    @FXML
    private Hyperlink hlkLinkedIn;

    @FXML
    private Hyperlink hlkRepository;

    @FXML
    private Hyperlink hlkGameUrl;

    @FXML
    private Hyperlink hlkSource;

    @FXML
    private ImageView imgThumbnail;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblDescription;

    @FXML
    private Label lblDeveloper;

    @FXML
    private Label lblGenre;

    @FXML
    private Label lblPlatform;

    @FXML
    private Label lblPublisher;

    @FXML
    private Label lblReleaseDate;

    @FXML
    private Label lblGamesCount;

    @FXML
    private ListView<Game> lsvGames;

    @FXML
    private TextField txtName;

    @FXML
    private ProgressIndicator pgiFetchingGameList;

    @FXML
    private Label lblFetchingGameList;

    private int selectedGameId;

    /**
     * Run when the controller is initialized
     */
    @FXML
    public void initialize() {
        // Call the method to populate the lists
        populateLists();

        // Set a standard selection for the sort by
        cmbSortBy.setValue("Alphabetical");

        // Set the actions for hyperlinks
        hlkLinkedIn.setOnAction(event -> openLink("https://linkedin.com/in/marcosmota5"));
        hlkRepository.setOnAction(event -> openLink("https://github.com/marcosmota5/FreeGamesExplorer"));
        hlkSource.setOnAction(event -> openLink("https://www.freetogame.com/"));

        // Add a listener to handle selection changes in the ListView
        lsvGames.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) { // Check if something is selected
                // Update the values from the selected game
                selectedGameId = newValue.getId();
                lblTitle.setText(newValue.getTitle());
                lblDescription.setText("                           " + newValue.getShortDescription()); // Extra spaces added to have enough space for the "description" title
                lblGenre.setText(newValue.getGenre());
                lblPlatform.setText(newValue.getPlatform());
                lblPublisher.setText(newValue.getPublisher());
                lblDeveloper.setText(newValue.getDeveloper());
                hlkGameUrl.setText(newValue.getGameUrl());
                hlkGameUrl.setOnAction(event -> openLink(newValue.getGameUrl()));

                // Format the release date using the system's current locale
                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.getDefault());

                // Create a formatted date string
                String formattedDate = newValue.getReleaseDate().format(formatter);

                // Set the formated date string to the label
                lblReleaseDate.setText(formattedDate);

                // Create an image element with the thumbnail url
                Image newImage = new Image(newValue.getThumbnail());

                // Set teh image of the thumbnail
                imgThumbnail.setImage(newImage);

                // Enable the details button
                btnDetails.setDisable(false);
            } else {
                selectedGameId = 0;
                lblTitle.setText("");
                lblDescription.setText("");
                lblGenre.setText("");
                lblPlatform.setText("");
                lblPublisher.setText("");
                lblDeveloper.setText("");
                lblReleaseDate.setText("");
                hlkGameUrl.setText("");
                hlkGameUrl.setOnAction(null); // Removes the event handler

                Image newImage = new Image(getClass().getResource("/no-image.png").toExternalForm());

                imgThumbnail.setImage(newImage);

                // Disable the details button as there's nothing to check
                btnDetails.setDisable(true);
            }
        });

        // Call the refresh list method
        refreshList(null);

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

        // Add a key press event listener to the txtName field
        txtName.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                refreshList(null); // Call refreshList when Enter is pressed
            }
        });
    }

    /**
     * Refresh the list of games
     *
     * @param event An event that can be handled in the function
     */
    @FXML
    void refreshList(ActionEvent event) {

        // Clear the list
        lsvGames.getItems().clear();

        // Get selected items from CheckComboBox (categories)
        List<String> categories = ccbCategory.getCheckModel().getCheckedItems();

        // Get selected value from ComboBox (platform)
        String platform = cmbPlatform.getSelectionModel().getSelectedItem();
        platform = (platform != null) ? platform : ""; // Default to empty string if no selection

        // Get selected value from ComboBox (sortBy)
        String sortBy = cmbSortBy.getSelectionModel().getSelectedItem();
        sortBy = (sortBy != null) ? sortBy : ""; // Default to empty string if no selection

        // Show progress and disable buttons immediately
        Platform.runLater(() -> {
            lblGamesCount.setText("Games count: 0");

            pgiFetchingGameList.setVisible(true);
            lblFetchingGameList.setVisible(true);

            btnRefresh.setDisable(true);
            btnDetails.setDisable(true);
        });

        // Call the asynchronous method and handle results
        Game.getGameList(categories, platform, sortBy, txtName.getText())
                .thenAccept(games -> {
                    // Update the ListView on the JavaFX Application Thread
                    Platform.runLater(() -> {

                        // Populate the ListView with the game list
                        lsvGames.getItems().setAll(games);

                        // Set the games count
                        lblGamesCount.setText("Games count: " + games.size());

                        // Make the progress invisible
                        pgiFetchingGameList.setVisible(false);
                        lblFetchingGameList.setVisible(false);

                        // Enable the buttons
                        btnRefresh.setDisable(false);
                        btnDetails.setDisable(false);
                    });
                })
                .exceptionally(ex -> {
                    // Handle exceptions
                    Platform.runLater(() -> {
                        System.err.println("Error fetching game list: " + ex.getMessage());
                        lblGamesCount.setText("Games count: 0");

                        // Make the progress invisible and re-enable buttons
                        pgiFetchingGameList.setVisible(false);
                        lblFetchingGameList.setVisible(false);

                        btnRefresh.setDisable(false);
                        btnDetails.setDisable(false);
                    });
                    return null;
                });
    }

    /**
     * Populate the lists used in the ComboBoxes
     */
    private void populateLists() {
        // Create the lists
        ObservableList<String> categoryList = FXCollections.observableArrayList("2D", "3D", "Action", "Action RPG", "Anime", "Battle Royale", "Card", "Fantasy", "Fighting", "First Person", "Flight", "Horror", "Low Spec", "Martial Arts", "Military", "MMO", "MMOFPS", "MMORPG", "MMORTS", "MMOTPS", "Moba", "Open World", "Permadeath", "Pixel", "PVE", "PVP", "Racing", "Sailing", "Sandbox", "Sci Fi", "Shooter", "Side Scroller", "Social", "Space", "Sports", "Strategy", "Superhero", "Survival", "Tank", "Third Person", "Top Down", "Tower Defense", "Turn Based", "Voxel", "Zombie");
        ObservableList<String> platformList = FXCollections.observableArrayList("", "Browser", "PC");
        ObservableList<String> sortByList = FXCollections.observableArrayList("", "Alphabetical", "Release Date");

        // Set the lists
        ccbCategory.getItems().addAll(categoryList);
        cmbPlatform.setItems(platformList);
        cmbSortBy.setItems(sortByList);
        cmbTheme.setItems(ThemeUtils.getThemes());
    }

    /**
     * Go to the details page
     *
     * @param event An event that can be handled in the function
     */
    @FXML
    void goToDetails(ActionEvent event) throws IOException {
        // If the selected game id is 0, return as there's no game to check
        if (selectedGameId == 0) {
            return;
        }

        // Get the stage from anchor
        Stage stage = (Stage)anpHome.getScene().getWindow();

        // Switch the scene
        SceneUtils.switchScene(anpHome, "game-details-view.fxml", selectedGameId);
    }

    /**
     * Open a modal window with the about
     *
     * @param event An event that can be handled in the function
     */
    @FXML
    void showAbout(ActionEvent event) throws IOException {
        // Open the Profile Detail window and pass the vehicle data to the controller
        StageUtils.openModalWindow(
                "about-view.fxml",
                HelloApplication.getPrimaryStage(),    // The owner stage (main window)
                "About",                        // The window title
                (AboutController controller) -> {  // Lambda expression for the controller
                }
        );
    }
}