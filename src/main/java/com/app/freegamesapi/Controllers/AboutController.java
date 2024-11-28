package com.app.freegamesapi.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

import java.io.IOException;

import static com.app.freegamesapi.Helpers.LinkUtils.openLink;

public class AboutController {

    @FXML
    private Hyperlink hlkReference1;

    @FXML
    private Hyperlink hlkReference2;

    @FXML
    private Hyperlink hlkReference3;

    @FXML
    private Hyperlink hlkReference4;

    @FXML
    private Hyperlink hlkReference5;

    @FXML
    private Hyperlink hlkReference6;

    @FXML
    private Hyperlink hlkReference7;

    @FXML
    private Hyperlink hlkReference8;

    @FXML
    private Hyperlink hlkReference9;

    @FXML
    private Hyperlink hlkRepository;

    @FXML
    private ImageView imgGitHub;

    @FXML
    private ImageView imgLinkedIn;

    @FXML
    private ImageView imgPortfolio;

    /**
     * Run when the controller is initialized
     */
    @FXML
    public void initialize() {
        // Set the actions for hyperlinks
        hlkReference1.setOnAction(event -> openLink("https://www.flaticon.com/free-icons/landscape"));
        hlkReference2.setOnAction(event -> openLink("https://www.flaticon.com/free-icons/refresh"));
        hlkReference3.setOnAction(event -> openLink("https://www.flaticon.com/free-icons/more-details"));
        hlkReference4.setOnAction(event -> openLink("https://www.flaticon.com/free-icons/video-game"));
        hlkReference5.setOnAction(event -> openLink("https://www.flaticon.com/free-icons/back"));
        hlkReference6.setOnAction(event -> openLink("https://www.flaticon.com/free-icons/search"));
        hlkReference7.setOnAction(event -> openLink("https://www.flaticon.com/free-icons/linkedin"));
        hlkReference8.setOnAction(event -> openLink("https://www.flaticon.com/free-icons/github"));
        hlkReference9.setOnAction(event -> openLink("https://www.flaticon.com/free-icons/portfolio"));
        hlkRepository.setOnAction(event -> openLink("https://github.com/marcosmota5/FreeGamesExplorer"));

        // Use setOnMouseClicked on the image
        imgLinkedIn.setOnMouseClicked(event -> {
            openLink("https://www.linkedin.com/in/marcosmota5/");
        });

        imgGitHub.setOnMouseClicked(event -> {
            openLink("https://github.com/marcosmota5/");
        });

        imgPortfolio.setOnMouseClicked(event -> {
            openLink("https://marcosmota.tech/");
        });

        // Create the tooltips
        Tooltip tpLinkedIn = new Tooltip("LinkedIn profile");
        Tooltip tpGitHub = new Tooltip("GitHub profile");
        Tooltip tpPortfolio = new Tooltip("Portfolio");

        // Attach the Tooltip to the ImageView
        Tooltip.install(imgLinkedIn, tpLinkedIn);
        Tooltip.install(imgGitHub, tpGitHub);
        Tooltip.install(imgPortfolio, tpPortfolio);
    }
}
