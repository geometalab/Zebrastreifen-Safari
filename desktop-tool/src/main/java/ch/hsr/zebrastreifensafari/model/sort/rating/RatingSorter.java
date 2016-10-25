package ch.hsr.zebrastreifensafari.model.sort.rating;

import ch.hsr.zebrastreifensafari.jpa.entities.Rating;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public class RatingSorter {

    private static RatingSorter instance;
    private RatingLastSorted lastSorted;

    private RatingSorter() {
        lastSorted = RatingLastSorted.NOT_SORTED;
    }

    public static RatingSorter getInstance() {
        if (instance == null) {
            instance = new RatingSorter();
        }

        return instance;
    }

    public List<Rating> sortByUser(List<Rating> ratings) {
        if (lastSorted != RatingLastSorted.USER) {
            ratings = ratings.stream().sorted((o1, o2) -> o1.getUserId().getName().compareTo(o2.getUserId().getName())).collect(Collectors.toList());
        } else {
            ratings = ratings.stream().sorted((o1, o2) -> o2.getUserId().getName().compareTo(o1.getUserId().getName())).collect(Collectors.toList());
        }

        lastSorted = RatingLastSorted.USER;
        return ratings;
    }

    public List<Rating> sortByTraffic(List<Rating> ratings) {
        if (lastSorted != RatingLastSorted.TRAFFIC) {
            ratings = ratings.stream().sorted((o1, o2) -> o1.getTrafficId().getValue().compareTo(o2.getTrafficId().getValue())).collect(Collectors.toList());
        } else {
            ratings = ratings.stream().sorted((o1, o2) -> o2.getTrafficId().getValue().compareTo(o1.getTrafficId().getValue())).collect(Collectors.toList());
        }

        lastSorted = RatingLastSorted.TRAFFIC;
        return ratings;
    }

    public List<Rating> sortByClarity(List<Rating> ratings) {
        if (lastSorted != RatingLastSorted.CLARITY) {
            ratings = ratings.stream().sorted((o1, o2) -> o1.getSpatialClarityId().getValue().compareTo(o2.getSpatialClarityId().getValue())).collect(Collectors.toList());
        } else {
            ratings = ratings.stream().sorted((o1, o2) -> o2.getSpatialClarityId().getValue().compareTo(o1.getSpatialClarityId().getValue())).collect(Collectors.toList());
        }

        lastSorted = RatingLastSorted.CLARITY;
        return ratings;
    }

    public List<Rating> sortByIllumination(List<Rating> ratings) {
        if (lastSorted != RatingLastSorted.ILLUMINATION) {
            ratings = ratings.stream().sorted((o1, o2) -> o1.getIlluminationId().getValue().compareTo(o2.getIlluminationId().getValue())).collect(Collectors.toList());
        } else {
            ratings = ratings.stream().sorted((o1, o2) -> o2.getIlluminationId().getValue().compareTo(o1.getIlluminationId().getValue())).collect(Collectors.toList());
        }

        lastSorted = RatingLastSorted.ILLUMINATION;
        return ratings;
    }

    public List<Rating> sortByComment(List<Rating> ratings) {
        ratings = ratings.stream().sorted(((o1, o2) -> {
            if (o1.getComment() == null) {
                return sorter(lastSorted != RatingLastSorted.COMMENT);
            }

            if (o2.getComment() == null) {
                return sorter(lastSorted == RatingLastSorted.COMMENT);
            }

            return lastSorted != RatingLastSorted.COMMENT ? o1.getComment().compareTo(o2.getComment()) : o2.getComment().compareTo(o1.getComment());
        })).collect(Collectors.toList());

        lastSorted = RatingLastSorted.COMMENT;
        return ratings;
    }

    public List<Rating> sortByImage(List<Rating> ratings) {
        ratings = ratings.stream().sorted(((o1, o2) -> {
            if (o1.getImageWeblink() == null) {
                return sorter(lastSorted != RatingLastSorted.IMAGE);
            }

            if (o2.getImageWeblink() == null) {
                return sorter(lastSorted == RatingLastSorted.IMAGE);
            }

            return lastSorted != RatingLastSorted.IMAGE ? o1.getImageWeblink().compareTo(o2.getImageWeblink()) : o2.getImageWeblink().compareTo(o1.getImageWeblink());
        })).collect(Collectors.toList());


        lastSorted = RatingLastSorted.IMAGE;
        return ratings;
    }

    public List<Rating> sortByLastChanged(List<Rating> ratings) {
        if (lastSorted != RatingLastSorted.LAST_CHANGED) {
            ratings = ratings.stream().sorted((o1, o2) -> o1.getLastChanged().compareTo(o2.getLastChanged())).collect(Collectors.toList());
        } else {
            ratings = ratings.stream().sorted((o1, o2) -> o2.getLastChanged().compareTo(o1.getLastChanged())).collect(Collectors.toList());
        }


        lastSorted = RatingLastSorted.LAST_CHANGED;
        return ratings;
    }

    public List<Rating> sortByCreationTime(List<Rating> ratings) {
        ratings = ratings.stream().sorted(((o1, o2) -> {
            if (o1.getCreationTime() == null) {
                return sorter(lastSorted != RatingLastSorted.CREATION_TIME);
            }

            if (o2.getCreationTime() == null) {
                return sorter(lastSorted == RatingLastSorted.CREATION_TIME);
            }

            return lastSorted != RatingLastSorted.CREATION_TIME ? o1.getCreationTime().compareTo(o2.getCreationTime()) : o2.getCreationTime().compareTo(o1.getCreationTime());
        })).collect(Collectors.toList());


        lastSorted = RatingLastSorted.CREATION_TIME;
        return ratings;
    }

    private int sorter(boolean sort) {
        return sort ? 1 : -1;
    }
}
