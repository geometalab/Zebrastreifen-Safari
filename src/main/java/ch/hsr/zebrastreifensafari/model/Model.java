/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.zebrastreifensafari.model;

import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aeugster
 */
public class Model {

    private final List<Illumination> illuminations;
    private final List<SpatialClarity> spatialClaritys;
    private final List<Traffic> traffics;
    private List<User> users;
    private List<Crossing> crossings;
    private List<Rating> ratings;
    private boolean node = true, ratingAmount = true, status = true, user = true, traffic = true, clarity = true, illumination = true, comment = true, image = true, lastChanged = true;

    public Model() {
        ratings = new ArrayList<Rating>();
        illuminations = DataServiceLoader.getCrossingData().getIlluminations();
        spatialClaritys = DataServiceLoader.getCrossingData().getSpatialClarities();
        traffics = DataServiceLoader.getCrossingData().getTraffics();
        reloadCrossing();
        reloadUsers();
    }

    public void reloadCrossing() {
        crossings = DataServiceLoader.getCrossingData().getCrossings();
    }

    public void reloadRating(Crossing crossing) {
        ratings = DataServiceLoader.getCrossingData().getRatings(crossing);
    }

    public void reloadUsers() {
        users = DataServiceLoader.getCrossingData().getUsers();
    }

    public User getUser(String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }

        return null;
    }

    public Crossing getCrossing(int id) {
        for (Crossing crossing : crossings) {
            if (crossing.getId() == id) {
                return crossing;
            }
        }

        return null;
    }

    public Crossing getCrossing(long node) {
        for (Crossing crossing : crossings) {
            if (crossing.getOsmNodeId() == node) {
                return crossing;
            }
        }

        return null;
    }

    public Rating getRating(int id) {
        for (Rating rating : ratings) {
            if (rating.getId() == id) {
                return rating;
            }
        }

        return null;
    }

    public Illumination getIllumination(int id) {
        for (Illumination illumination : illuminations) {
            if (illumination.getId() == id) {
                return illumination;
            }
        }

        return null;
    }

    public SpatialClarity getSpatialClarity(int id) {
        for (SpatialClarity spatialClarity : spatialClaritys) {
            if (spatialClarity.getId() == id) {
                return spatialClarity;
            }
        }

        return null;
    }

    public Traffic getTraffic(int id) {
        for (Traffic traffic : traffics) {
            if (traffic.getId() == id) {
                return traffic;
            }
        }

        return null;
    }

    /**
     * @return the users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * @return the zebras
     */
    public List<Crossing> getCrossings() {
        return crossings;
    }

    /**
     * @return the ratings
     */
    public List<Rating> getRatings() {
        return ratings;
    }

    //<editor-fold desc="Sort methods">
    public void sortByNode() {
        if (node) {
            crossings = crossings.stream().sorted((o1, o2) -> Long.compare(o1.getOsmNodeId(), o2.getOsmNodeId())).collect(Collectors.toList());
            node = false;
        } else {
            crossings = crossings.stream().sorted((o1, o2) -> Long.compare(o2.getOsmNodeId(), o1.getOsmNodeId())).collect(Collectors.toList());
            node = true;
        }
    }

    public void sortByNumberOfRatings() {
        if (ratingAmount) {
            crossings = crossings.stream().sorted((o1, o2) -> Long.compare(o1.getRatingAmount(), o2.getRatingAmount())).collect(Collectors.toList());
            ratingAmount = false;
        } else {
            crossings = crossings.stream().sorted((o1, o2) -> Long.compare(o2.getRatingAmount(), o1.getRatingAmount())).collect(Collectors.toList());
            ratingAmount = true;
        }
    }

    public void sortByStatus() {
        if (status) {
            crossings = crossings.stream().sorted((o1, o2) -> Integer.compare(o1.getStatus(), o2.getStatus())).collect(Collectors.toList());
            status = false;
        } else {
            crossings = crossings.stream().sorted((o1, o2) -> Integer.compare(o2.getStatus(), o1.getStatus())).collect(Collectors.toList());
            status = true;
        }
    }

    public void sortByUser() {
        if (user) {
            ratings = ratings.stream().sorted(((o1, o2) -> o1.getUserId().getName().compareTo(o2.getUserId().getName()))).collect(Collectors.toList());
            user = false;
        } else {
            ratings = ratings.stream().sorted(((o1, o2) -> o2.getUserId().getName().compareTo(o1.getUserId().getName()))).collect(Collectors.toList());
            user = true;
        }
    }

    public void sortByTraffic() {
        if (traffic) {
            ratings = ratings.stream().sorted(((o1, o2) -> o1.getTrafficId().getValue().compareTo(o2.getTrafficId().getValue()))).collect(Collectors.toList());
            traffic = false;
        } else {
            ratings = ratings.stream().sorted(((o1, o2) -> o2.getTrafficId().getValue().compareTo(o1.getTrafficId().getValue()))).collect(Collectors.toList());
            traffic = true;
        }
    }

    public void sortByClarity() {
        if (clarity) {
            ratings = ratings.stream().sorted(((o1, o2) -> o1.getSpatialClarityId().getValue().compareTo(o2.getSpatialClarityId().getValue()))).collect(Collectors.toList());
            clarity = false;
        } else {
            ratings = ratings.stream().sorted(((o1, o2) -> o2.getSpatialClarityId().getValue().compareTo(o1.getSpatialClarityId().getValue()))).collect(Collectors.toList());
            clarity = true;
        }
    }

    public void sortByIllumination() {
        if (illumination) {
            ratings = ratings.stream().sorted(((o1, o2) -> o1.getIlluminationId().getValue().compareTo(o2.getIlluminationId().getValue()))).collect(Collectors.toList());
            illumination = false;
        } else {
            ratings = ratings.stream().sorted(((o1, o2) -> o2.getIlluminationId().getValue().compareTo(o1.getIlluminationId().getValue()))).collect(Collectors.toList());
            illumination = true;
        }
    }

    public void sortByComment() {
        if (comment) {
            ratings = ratings.stream().sorted(((o1, o2) -> {
                if (o1.getComment() == null) {
                    return 1;
                } else if (o2.getComment() == null) {
                    return -1;
                } else {
                    return o1.getComment().compareTo(o2.getComment());
                }
            })).collect(Collectors.toList());
            comment = false;
        } else {
            ratings = ratings.stream().sorted(((o1, o2) -> {
                if (o1.getComment() == null) {
                    return -1;
                } else if (o2.getComment() == null) {
                    return 1;
                } else {
                    return o2.getComment().compareTo(o1.getComment());
                }
            })).collect(Collectors.toList());
            comment = true;
        }
    }

    public void sortByImage() {
        if (image) {
            ratings = ratings.stream().sorted(((o1, o2) -> {
                if (o1.getImageWeblink() == null) {
                    return 1;
                } else if (o2.getImageWeblink() == null) {
                    return -1;
                } else {
                    return o1.getImageWeblink().compareTo(o2.getImageWeblink());
                }
            })).collect(Collectors.toList());
            image = false;
        } else {
            ratings = ratings.stream().sorted(((o1, o2) -> {
                if (o1.getImageWeblink() == null) {
                    return -1;
                } else if (o2.getImageWeblink() == null) {
                    return 1;
                } else {
                    return o2.getImageWeblink().compareTo(o1.getImageWeblink());
                }
            })).collect(Collectors.toList());
            image = true;
        }
    }

    public void sortByLastChanged() {
        if (lastChanged) {
            ratings = ratings.stream().sorted(((o1, o2) -> o1.getLastChanged().compareTo(o2.getLastChanged()))).collect(Collectors.toList());
            lastChanged = false;
        } else {
            ratings = ratings.stream().sorted(((o1, o2) -> o2.getLastChanged().compareTo(o1.getLastChanged()))).collect(Collectors.toList());
            lastChanged = true;
        }
    }
    //</editor-fold>
}
