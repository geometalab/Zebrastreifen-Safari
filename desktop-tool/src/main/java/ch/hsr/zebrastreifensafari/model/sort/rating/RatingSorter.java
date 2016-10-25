package ch.hsr.zebrastreifensafari.model.sort.rating;

import ch.hsr.zebrastreifensafari.jpa.entities.Rating;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public class RatingSorter {

    private static RatingSorter instance;
    private boolean user;
    private boolean traffic;
    private boolean clarity;
    private boolean illumination;
    private boolean comment;
    private boolean image;
    private boolean lastChanged;
    private boolean creationTime;

    private RatingSorter() {
        user = false;
        traffic = false;
        clarity = false;
        illumination = false;
        comment = false;
        image = false;
        lastChanged = false;
        creationTime = false;
    }

    public static RatingSorter getInstance() {
        if (instance == null) {
            instance = new RatingSorter();
        }

        return instance;
    }

    public void sortByUser(List<Rating> ratings) {
        Comparator<Rating> comparator = (o1, o2) -> o1.getUserId().getName().compareTo(o2.getUserId().getName());
        Collections.sort(ratings, user ? comparator.reversed() : comparator);
        user = !user;
    }

    public void sortByTraffic(List<Rating> ratings) {
        Comparator<Rating> comparator = (o1, o2) -> o1.getTrafficId().getValue().compareTo(o2.getTrafficId().getValue());
        Collections.sort(ratings, traffic ? comparator.reversed() : comparator);
        traffic = !traffic;
    }

    public void sortByClarity(List<Rating> ratings) {
        Comparator<Rating> comparator = (o1, o2) -> o1.getSpatialClarityId().getValue().compareTo(o2.getSpatialClarityId().getValue());
        Collections.sort(ratings, clarity ? comparator.reversed() : comparator);
        clarity = !clarity;
    }

    public void sortByIllumination(List<Rating> ratings) {
        Comparator<Rating> comparator = (o1, o2) -> o1.getIlluminationId().getValue().compareTo(o2.getIlluminationId().getValue());
        Collections.sort(ratings, illumination ? comparator.reversed() : comparator);
        illumination = !illumination;
    }

    public void sortByComment(List<Rating> ratings) {
        Comparator<Rating> comparator = (o1, o2) -> compareNullableValues(o1.getComment(), o2.getComment(), comment);
        Collections.sort(ratings, comment ? comparator.reversed() : comparator);
        comment = !comment;
    }

    public void sortByImage(List<Rating> ratings) {
        Comparator<Rating> comparator = (o1, o2) -> compareNullableValues(o1.getImageWeblink(), o2.getImageWeblink(), image);
        Collections.sort(ratings, image ? comparator.reversed() : comparator);
        image = !image;
    }

    public void sortByLastChanged(List<Rating> ratings) {
        Comparator<Rating> comparator = (o1, o2) -> o1.getLastChanged().compareTo(o2.getLastChanged());
        Collections.sort(ratings, lastChanged ? comparator.reversed() : comparator);
        lastChanged = !lastChanged;
    }

    public void sortByCreationTime(List<Rating> ratings) {
        Comparator<Rating> comparator = (o1, o2) -> compareNullableValues(o1.getCreationTime(), o2.getCreationTime(), creationTime);
        Collections.sort(ratings, creationTime ? comparator.reversed() : comparator);
        creationTime = !creationTime;
    }

    //todo doesn't work properly jet
    private <T extends Comparable<T>> int compareNullableValues(T value1, T value2, boolean sort) {
        if (value1 == null || value2 == null) {
            return sort ? 1 : -1;
        }

        return value1.compareTo(value2);
    }

}
