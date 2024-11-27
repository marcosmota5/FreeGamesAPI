package com.app.freegamesapi.Controllers;

import com.app.freegamesapi.Models.Game;
import com.google.gson.JsonObject;
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
import org.controlsfx.control.CheckComboBox;

import java.awt.*;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeController {

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
    private ListView<Game> lsvGames;

    @FXML
    private TextField txtName;

    @FXML
    public void initialize() {
        // Call the method to populate the lists
        populateLists();

        // Set the actions for hyperlinks
        hlkLinkedIn.setOnAction(event -> openLink("https://linkedin.com/in/marcosmota5"));
        hlkRepository.setOnAction(event -> openLink("https://github.com/marcosmota5/FreeGamesAPI"));
        hlkSource.setOnAction(event -> openLink("https://www.freetogame.com/"));

        // Add a listener to handle selection changes in the ListView
        lsvGames.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) { // Check if something is selected
                // Update the values from the selected game
                lblTitle.setText(newValue.getTitle());
                lblDescription.setText("                           " + newValue.getShortDescription());
                lblGenre.setText(newValue.getGenre());
                lblPlatform.setText(newValue.getPlatform());
                lblPublisher.setText(newValue.getPublisher());
                lblDeveloper.setText(newValue.getDeveloper());
                hlkGameUrl.setText(newValue.getGameUrl());
                hlkGameUrl.setOnAction(event -> openLink(newValue.getGameUrl()));

                // Format the release date using the system's current locale
                DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.getDefault());
                String formattedDate = newValue.getReleaseDate().format(formatter);

                lblReleaseDate.setText(formattedDate);

                Image newImage = new Image(newValue.getThumbnail());

                imgThumbnail.setImage(newImage);
            } else {
                lblTitle.setText(""); // Clear the label if no selection is made
                lblDescription.setText(""); // Clear the label if no selection is made
                lblGenre.setText(""); // Clear the label if no selection is made
                lblPlatform.setText(""); // Clear the label if no selection is made
                lblPublisher.setText(""); // Clear the label if no selection is made
                lblDeveloper.setText(""); // Clear the label if no selection is made
                lblReleaseDate.setText(""); // Clear the label if no selection is made
                hlkGameUrl.setText("");
                hlkGameUrl.setOnAction(null); // Removes the event handler

                Image newImage = new Image(getClass().getResource("/no-image.png").toExternalForm());

                imgThumbnail.setImage(newImage);
            }
        });

        refreshList(null);
    }

    @FXML
    void refreshList(ActionEvent event) {

        // Get selected items from CheckComboBox (categories)
        List<String> categories = ccbCategory.getCheckModel().getCheckedItems();

        // Get selected value from ComboBox (platform)
        String platform = cmbPlatform.getSelectionModel().getSelectedItem();
        platform = (platform != null) ? platform : ""; // Default to empty string if no selection

        // Get selected value from ComboBox (sortBy)
        String sortBy = cmbSortBy.getSelectionModel().getSelectedItem();
        sortBy = (sortBy != null) ? sortBy : ""; // Default to empty string if no selection

        // Call the asynchronous method and handle results
        Game.getGameList(categories, platform, sortBy, txtName.getText())
                .thenAccept(games -> {
                    // Update the ListView on the JavaFX Application Thread
                    Platform.runLater(() -> {
                        lsvGames.getItems().setAll(games); // Populate the ListView with the game list
                    });
                })
                .exceptionally(ex -> {
                    // Handle exceptions
                    Platform.runLater(() -> {
                        System.err.println("Error fetching game list: " + ex.getMessage());
                    });
                    return null;
                });

    }

    private void populateLists() {
        // Create the lists
        ObservableList<String> categoryList = FXCollections.observableArrayList("MMORPG", "Shooter", "Strategy", "Moba", "Racing", "Sports", "Social", "Sandbox", "Open World", "Survival", "PVP", "PVE", "Pixel", "Voxel", "Zombie", "Turn Based", "First Person", "Third Person", "Top Down", "Tank", "Space", "Sailing", "Side Scroller", "Superhero", "Permadeath", "Card", "Battle Royale", "MMO", "MMOFPS", "MMOTPS", "3D", "2D", "Anime", "Fantasy", "Sci Fi", "Fighting", "Action RPG", "Action", "Military", "Martial Arts", "Flight", "Low Spec", "Tower Defense", "Horror", "MMORTS");
        ObservableList<String> platformList = FXCollections.observableArrayList("", "Browser", "PC");
        ObservableList<String> sortByList = FXCollections.observableArrayList("", "Alphabetical", "Release Date");

        // Set the lists
        ccbCategory.getItems().addAll(categoryList);
        cmbPlatform.setItems(platformList);
        cmbSortBy.setItems(sortByList);
    }

    private void openLink(String url) {
        try {
            // Check if Desktop is supported
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(url));
                }
            } else {
                System.err.println("Desktop is not supported. Cannot open the link.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}