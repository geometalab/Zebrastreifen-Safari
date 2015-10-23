package main.java.ch.hsr.zebrastreifensafari.service;

import main.java.ch.hsr.zebrastreifensafari.service.crossing.ICrossingDataService;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 15:34
 * @date : 08.10.2015
 */
public class DataServiceLoader {

    private static ICrossingDataService CrossingDataService;

    private DataServiceLoader() {
    }

    public static ICrossingDataService getCrossingData() {
        return CrossingDataService;
    }

    public static void provideCrossingData(ICrossingDataService CrossingDataService) {
        DataServiceLoader.CrossingDataService = CrossingDataService;
    }
}
