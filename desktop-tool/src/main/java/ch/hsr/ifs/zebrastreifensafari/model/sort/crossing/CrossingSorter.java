package ch.hsr.ifs.zebrastreifensafari.model.sort.crossing;

import ch.hsr.ifs.zebrastreifensafari.jpa.entities.Crossing;

import java.util.Comparator;
import java.util.List;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public class CrossingSorter {

    private static boolean node = false;
    private static boolean numberOfRatings = false;
    private static boolean status = false;

    public static void sortByNode(List<Crossing> crossings) {
        Comparator<Crossing> comparator = (o1, o2) -> Long.compare(o1.getOsmNodeId(), o2.getOsmNodeId());
        crossings.sort(node ? comparator.reversed() : comparator);
        node = !node;
    }

    public static void sortByNumberOfRatings(List<Crossing> crossings) {
        Comparator<Crossing> comparator = (o1, o2) -> Long.compare(o1.getRatingAmount(), o2.getRatingAmount());
        crossings.sort(numberOfRatings ? comparator.reversed() : comparator);
        numberOfRatings = !numberOfRatings;
    }

    public static void sortByStatus(List<Crossing> crossings) {
        Comparator<Crossing> comparator = (o1, o2) -> Integer.compare(o1.getStatus(), o2.getStatus());
        crossings.sort(status ? comparator.reversed() : comparator);
        status = !status;
    }
}
