package com.app.freegamesapi.Helpers;

import java.awt.*;
import java.net.URI;

public class LinkUtils {
    public static void openLink(String url) {
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
