package com.app.freegamesapi.Controllers;

import com.google.gson.JsonObject;
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
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class HomeController {

    @FXML
    private Button btnDetails;

    @FXML
    private Button btnRefresh;

    @FXML
    private ChoiceBox<String> ccbCategory;

    @FXML
    private ComboBox<String> cmbPlatform;

    @FXML
    private ComboBox<String> cmbSortBy;

    @FXML
    private Hyperlink hlkSource;

    @FXML
    private ImageView imgThumbnail;

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
    private ListView<?> lsvGames;

    @FXML
    private TextField txtName;

    @FXML
    protected void onHelloButtonClick() {

    }

}