package ch.hsr.zebrastreifensafari.service;

import ch.hsr.zebrastreifensafari.service.crossing.ICrossingDataService;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 15:34
 * @date : 08.10.2015
 */
public class DataServiceLoader {

    private static ICrossingDataService crossingDataService;

    private DataServiceLoader() {
    }

    public static ICrossingDataService getCrossingData() {
        return crossingDataService;
    }

    public static void provideCrossingData(ICrossingDataService crossingDataService) {
        DataServiceLoader.crossingDataService = crossingDataService;
    }
}
