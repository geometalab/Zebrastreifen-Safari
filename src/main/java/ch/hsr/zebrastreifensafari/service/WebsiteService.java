package ch.hsr.zebrastreifensafari.service;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 16:55
 * @date : 05.01.2016
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
