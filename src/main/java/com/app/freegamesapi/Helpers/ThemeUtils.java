package com.app.freegamesapi.Helpers;

import com.app.freegamesapi.Controllers.GameDetailsController;
import com.app.freegamesapi.Controllers.HomeController;
import com.app.freegamesapi.HelloApplication;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ThemeUtils {

    /**
     * Get a list of the available themes of the application
     *
     * @return A list of string containing the available themes.
     */
    public static ObservableList<String> getThemes() {
        return FXCollections.observableArrayList("Light", "Dark");
    }

    /**
     * Change the theme of the application for a specific controller
     *
     * @param controller Controller that will be used for the context to get the theme file.
     * @param theme Theme that will be applied.
     */
    public static void changeTheme(Object controller, String theme){
        // Change the theme according to the parameter
        if (theme.equals("Dark")) {
            Application.setUserAgentStylesheet(controller.getClass().getResource("/primer-dark.css").toExternalForm());
        } else {
            Application.setUserAgentStylesheet(controller.getClass().getResource("/primer-light.css").toExternalForm());
        }

        // Save the preferences
        AppPreferences.savePreferences(theme);
    }
}