package ch.hsr.zebrastreifensafari.model.sort.crossing;

import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public class CrossingSorter {

    private static CrossingSorter instance;
    private CrossingLastSorted lastSorted;

    private CrossingSorter() {
        lastSorted = CrossingLastSorted.NOT_SORTED;
    }

    public static CrossingSorter getInstance() {
        if (instance == null) {
            instance = new CrossingSorter();
        }

        return instance;
    }

    public List<Crossing> sortByNode(List<Crossing> crossings) {
        if (lastSorted != CrossingLastSorted.NODE) {
            crossings = crossings.stream().sorted((o1, o2) -> Long.compare(o1.getOsmNodeId(), o2.getOsmNodeId())).collect(Collectors.toList());
        } else {
            crossings = crossings.stream().sorted((o1, o2) -> Long.compare(o2.getOsmNodeId(), o1.getOsmNodeId())).collect(Collectors.toList());
        }

        lastSorted = CrossingLastSorted.NODE;
        return crossings;
    }

    public List<Crossing> sortByNumberOfRatings(List<Crossing> crossings) {
        if (lastSorted != CrossingLastSorted.NUMBER_OF_RATINGS) {
            crossings = crossings.stream().sorted((o1, o2) -> Long.compare(o1.getRatingAmount(), o2.getRatingAmount())).collect(Collectors.toList());
        } else {
            crossings = crossings.stream().sorted((o1, o2) -> Long.compare(o2.getRatingAmount(), o1.getRatingAmount())).collect(Collectors.toList());
        }

        lastSorted = CrossingLastSorted.NUMBER_OF_RATINGS;
        return crossings;
    }

    public List<Crossing> sortByStatus(List<Crossing> crossings) {
        if (lastSorted != CrossingLastSorted.STATUS) {
            crossings = crossings.stream().sorted((o1, o2) -> Integer.compare(o1.getStatus(), o2.getStatus())).collect(Collectors.toList());
        } else {
            crossings = crossings.stream().sorted((o1, o2) -> Integer.compare(o2.getStatus(), o1.getStatus())).collect(Collectors.toList());
        }

        lastSorted = CrossingLastSorted.STATUS;
        return crossings;
    }
}
