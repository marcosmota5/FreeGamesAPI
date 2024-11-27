package com.app.freegamesapi.Helpers;

import com.app.freegamesapi.Controllers.GameDetailsController;
import com.app.freegamesapi.Controllers.HomeController;
import com.app.freegamesapi.HelloApplication;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ThemeUtils {

    public static ObservableList<String> getThemes() {
        return FXCollections.observableArrayList("Light", "Dark");
    }


    public static void changeTheme(HomeController controller, String theme){
        // Change the theme according to the parameter
        if (theme.equals("Dark")) {
            Application.setUserAgentStylesheet(controller.getClass().getResource("/primer-dark.css").toExternalForm());
        } else {
            Application.setUserAgentStylesheet(controller.getClass().getResource("/primer-light.css").toExternalForm());
        }

        // Save the preferences
        AppPreferences.savePreferences(theme);
    }

    public static void changeTheme(GameDetailsController controller, String theme){
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
