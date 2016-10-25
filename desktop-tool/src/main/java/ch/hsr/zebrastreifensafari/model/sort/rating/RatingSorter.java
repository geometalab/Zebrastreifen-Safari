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

    private static boolean user = false;
    private static boolean traffic = false;
    private static boolean clarity = false;
    private static boolean illumination = false;
    private static boolean comment = false;
    private static boolean image = false;
    private static boolean lastChanged = false;
    private static boolean creationTime = false;

    public static void sortByUser(List<Rating> ratings) {
        Comparator<Rating> comparator = (o1, o2) -> o1.getUserId().getName().compareTo(o2.getUserId().getName());
        Collections.sort(ratings, user ? comparator.reversed() : comparator);
        user = !user;
    }

    public static void sortByTraffic(List<Rating> ratings) {
        Comparator<Rating> comparator = (o1, o2) -> o1.getTrafficId().getValue().compareTo(o2.getTrafficId().getValue());
        Collections.sort(ratings, traffic ? comparator.reversed() : comparator);
        traffic = !traffic;
    }

    public static void sortByClarity(List<Rating> ratings) {
        Comparator<Rating> comparator = (o1, o2) -> o1.getSpatialClarityId().getValue().compareTo(o2.getSpatialClarityId().getValue());
        Collections.sort(ratings, clarity ? comparator.reversed() : comparator);
        clarity = !clarity;
    }

    public static void sortByIllumination(List<Rating> ratings) {
        Comparator<Rating> comparator = (o1, o2) -> o1.getIlluminationId().getValue().compareTo(o2.getIlluminationId().getValue());
        Collections.sort(ratings, illumination ? comparator.reversed() : comparator);
        illumination = !illumination;
    }

    public static void sortByComment(List<Rating> ratings) {
        Comparator<Rating> comparator = (o1, o2) -> compareNullableValues(o1.getComment(), o2.getComment());
        Collections.sort(ratings, comment ? comparator.reversed() : comparator);
        comment = !comment;
    }

    public static void sortByImage(List<Rating> ratings) {
        Comparator<Rating> comparator = (o1, o2) -> compareNullableValues(o1.getImageWeblink(), o2.getImageWeblink());
        Collections.sort(ratings, image ? comparator.reversed() : comparator);
        image = !image;
    }

    public static void sortByLastChanged(List<Rating> ratings) {
        Comparator<Rating> comparator = (o1, o2) -> o1.getLastChanged().compareTo(o2.getLastChanged());
        Collections.sort(ratings, lastChanged ? comparator.reversed() : comparator);
        lastChanged = !lastChanged;
    }

    public static void sortByCreationTime(List<Rating> ratings) {
        Comparator<Rating> comparator = (o1, o2) -> compareNullableValues(o1.getCreationTime(), o2.getCreationTime());
        Collections.sort(ratings, creationTime ? comparator.reversed() : comparator);
        creationTime = !creationTime;
    }

    private static  <T extends Comparable<T>> int compareNullableValues(T value1, T value2) {
        if (value1 == null && value2 == null) {
            return 0;
        }

        if (value1 == null) {
            return 1;
        }

        if (value2 == null) {
            return -1;
        }

        return value1.compareTo(value2);
    }

}
