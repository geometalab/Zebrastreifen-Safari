package ch.hsr.zebrastreifensafari.service;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 08:17
 * @date : 24.11.2015
 */

public class Properties {

    private static ResourceBundle resourceBundle;

    private Properties() {
    }

    public static void setLanguage(Locale locale) {
        resourceBundle = ResourceBundle.getBundle("Bundle", locale);
        JOptionPane.setDefaultLocale(locale);
    }

    public static String get(String key) {
        return resourceBundle.getString(key);
    }
}
