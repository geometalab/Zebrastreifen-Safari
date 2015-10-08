package ch.hsr.zebrastreifensafari.service;

import ch.hsr.zebrastreifensafari.service.zebracrossing.IZebracrossingDataService;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 15:34
 * @date : 08.10.2015
 */

public class DataServiceLoader {

    private static IZebracrossingDataService zebracrossingDataService;

    private DataServiceLoader() {
    }

    public static IZebracrossingDataService getZebraData() {
        return zebracrossingDataService;
    }

    public static void provideZebraData(IZebracrossingDataService zebracrossingDataService) {
        DataServiceLoader.zebracrossingDataService = zebracrossingDataService;
    }
}
