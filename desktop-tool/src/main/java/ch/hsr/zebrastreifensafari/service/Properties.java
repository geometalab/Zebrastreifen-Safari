package ch.hsr.zebrastreifensafari.service;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 1.0
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
