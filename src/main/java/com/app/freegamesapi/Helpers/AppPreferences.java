package com.app.freegamesapi.Helpers;

import java.util.prefs.Preferences;

public class AppPreferences {

    // Define keys for the preferences
    private static final String PREF_KEY_THEME = "theme";

    // Get preferences for this application node
    private static Preferences preferences = Preferences.userNodeForPackage(Preferences.class);

    // Static method to save login and "Remember Me" state
    public static void savePreferences(String theme) {
        preferences.put(PREF_KEY_THEME, theme);
    }

    // Static method to load login and "Remember Me" state
    public static String[] loadPreferences() {
        String savedTheme = preferences.get(PREF_KEY_THEME, "");
        return new String[] { savedTheme };
    }

    // Static method to clear the saved preferences
    public static void clearPreferences() {
        preferences.remove(PREF_KEY_THEME);
    }
}