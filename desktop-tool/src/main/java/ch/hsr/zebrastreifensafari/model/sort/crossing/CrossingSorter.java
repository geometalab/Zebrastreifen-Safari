package ch.hsr.zebrastreifensafari.model.sort.crossing;

import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public class CrossingSorter {

    private static CrossingSorter instance;
    private boolean node;
    private boolean numberOfRatings;
    private boolean status;

    private CrossingSorter() {
        node = false;
        numberOfRatings = false;
        status = false;
    }

    public static CrossingSorter getInstance() {
        if (instance == null) {
            instance = new CrossingSorter();
        }

        return instance;
    }

    public void sortByNode(List<Crossing> crossings) {
        Comparator<Crossing> comparator = (o1, o2) -> Long.compare(o1.getOsmNodeId(), o2.getOsmNodeId());
        Collections.sort(crossings, node ? comparator.reversed() : comparator);
        node = !node;
    }

    public void sortByNumberOfRatings(List<Crossing> crossings) {
        Comparator<Crossing> comparator = (o1, o2) -> Long.compare(o1.getRatingAmount(), o2.getRatingAmount());
        Collections.sort(crossings, numberOfRatings ? comparator.reversed() : comparator);
        numberOfRatings = !numberOfRatings;
    }

    public void sortByStatus(List<Crossing> crossings) {
        Comparator<Crossing> comparator = (o1, o2) -> Integer.compare(o1.getStatus(), o2.getStatus());
        Collections.sort(crossings, status ? comparator.reversed() : comparator);
        status = !status;
    }
}
