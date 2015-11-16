package ch.hsr.zebrastreifensafari.service;

import ch.hsr.zebrastreifensafari.service.crossing.ICrossingDataService;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 15:34
 * @date : 08.10.2015
 */
public class DataServiceLoader {

    private static ICrossingDataService crossingDataService;
    private static ResourceBundle resourceBundle;

    private DataServiceLoader() {
    }

    public static ICrossingDataService getCrossingData() {
        return crossingDataService;
    }

    public static void provideCrossingData(ICrossingDataService crossingDataService) {
        DataServiceLoader.crossingDataService = crossingDataService;
    }

    public static void provideResourceBundle(Locale locale) {
        resourceBundle = ResourceBundle.getBundle("Bundle", locale);
    }

    public static String getBundleString(String key) {
        return resourceBundle.getString(key);
    }
}
