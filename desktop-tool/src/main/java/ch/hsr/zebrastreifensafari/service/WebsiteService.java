package ch.hsr.zebrastreifensafari.service;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 1.0
 */
public class WebsiteService {

    private WebsiteService() {
    }

    public static void openWebsite(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                Runtime.getRuntime().exec("xdg-open " + url);
            } catch (IOException ioex) {
                ioex.printStackTrace();
            }
        }
    }
}
