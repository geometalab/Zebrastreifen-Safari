package ch.hsr.zebrastreifensafari.service;

import ch.hsr.zebrastreifensafari.service.crossing.ICrossingDataService;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 1.0
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
