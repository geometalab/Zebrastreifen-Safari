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
    private boolean nodeSort;
    private boolean ratingAmountSort;
    private boolean statusSort;
    private boolean userSort;
    private boolean trafficSort;
    private boolean claritySort;
    private boolean illuminationSort;
    private boolean commentSort;
    private boolean imageSort;
    private boolean lastChangedSort;
    private boolean creationTimeSort;

    public Model() {
        ratings = new ArrayList<Rating>();
        illuminations = DataServiceLoader.getCrossingData().getIlluminations();
        spatialClaritys = DataServiceLoader.getCrossingData().getSpatialClarities();
        traffics = DataServiceLoader.getCrossingData().getTraffics();
        reloadCrossing();
        reloadUsers();
        nodeSort = true;
        ratingAmountSort = true;
        statusSort = true;
        userSort = true;
        trafficSort = true;
        claritySort = true;
        illuminationSort = true;
        commentSort = true;
        imageSort = true;
        lastChangedSort = true;
        creationTimeSort = true;
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

    public List<User> getUsers() {
        return users;
    }

    public List<Crossing> getCrossings() {
        return crossings;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    //<editor-fold desc="Sort methods">
    public void sortByNode() {
        if (nodeSort) {
            crossings = crossings.stream().sorted((o1, o2) -> Long.compare(o1.getOsmNodeId(), o2.getOsmNodeId())).collect(Collectors.toList());
            nodeSort = false;
        } else {
            crossings = crossings.stream().sorted((o1, o2) -> Long.compare(o2.getOsmNodeId(), o1.getOsmNodeId())).collect(Collectors.toList());
            nodeSort = true;
        }
    }

    public void sortByNumberOfRatings() {
        if (ratingAmountSort) {
            crossings = crossings.stream().sorted((o1, o2) -> Long.compare(o1.getRatingAmount(), o2.getRatingAmount())).collect(Collectors.toList());
            ratingAmountSort = false;
        } else {
            crossings = crossings.stream().sorted((o1, o2) -> Long.compare(o2.getRatingAmount(), o1.getRatingAmount())).collect(Collectors.toList());
            ratingAmountSort = true;
        }
    }

    public void sortByStatus() {
        if (statusSort) {
            crossings = crossings.stream().sorted((o1, o2) -> Integer.compare(o1.getStatus(), o2.getStatus())).collect(Collectors.toList());
            statusSort = false;
        } else {
            crossings = crossings.stream().sorted((o1, o2) -> Integer.compare(o2.getStatus(), o1.getStatus())).collect(Collectors.toList());
            statusSort = true;
        }
    }

    public void sortByUser() {
        if (userSort) {
            ratings = ratings.stream().sorted((o1, o2) -> o1.getUserId().getName().compareTo(o2.getUserId().getName())).collect(Collectors.toList());
            userSort = false;
        } else {
            ratings = ratings.stream().sorted((o1, o2) -> o2.getUserId().getName().compareTo(o1.getUserId().getName())).collect(Collectors.toList());
            userSort = true;
        }
    }

    public void sortByTraffic() {
        if (trafficSort) {
            ratings = ratings.stream().sorted((o1, o2) -> o1.getTrafficId().getValue().compareTo(o2.getTrafficId().getValue())).collect(Collectors.toList());
            trafficSort = false;
        } else {
            ratings = ratings.stream().sorted((o1, o2) -> o2.getTrafficId().getValue().compareTo(o1.getTrafficId().getValue())).collect(Collectors.toList());
            trafficSort = true;
        }
    }

    public void sortByClarity() {
        if (claritySort) {
            ratings = ratings.stream().sorted((o1, o2) -> o1.getSpatialClarityId().getValue().compareTo(o2.getSpatialClarityId().getValue())).collect(Collectors.toList());
            claritySort = false;
        } else {
            ratings = ratings.stream().sorted((o1, o2) -> o2.getSpatialClarityId().getValue().compareTo(o1.getSpatialClarityId().getValue())).collect(Collectors.toList());
            claritySort = true;
        }
    }

    public void sortByIllumination() {
        if (illuminationSort) {
            ratings = ratings.stream().sorted((o1, o2) -> o1.getIlluminationId().getValue().compareTo(o2.getIlluminationId().getValue())).collect(Collectors.toList());
            illuminationSort = false;
        } else {
            ratings = ratings.stream().sorted((o1, o2) -> o2.getIlluminationId().getValue().compareTo(o1.getIlluminationId().getValue())).collect(Collectors.toList());
            illuminationSort = true;
        }
    }

    public void sortByComment() {
        if (commentSort) {
            ratings = ratings.stream().sorted(((o1, o2) -> {
                if (o1.getComment() == null) {
                    return 1;
                } else if (o2.getComment() == null) {
                    return -1;
                } else {
                    return o1.getComment().compareTo(o2.getComment());
                }
            })).collect(Collectors.toList());
            commentSort = false;
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
            commentSort = true;
        }
    }

    public void sortByImage() {
        if (imageSort) {
            ratings = ratings.stream().sorted(((o1, o2) -> {
                if (o1.getImageWeblink() == null) {
                    return 1;
                } else if (o2.getImageWeblink() == null) {
                    return -1;
                } else {
                    return o1.getImageWeblink().compareTo(o2.getImageWeblink());
                }
            })).collect(Collectors.toList());
            imageSort = false;
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
            imageSort = true;
        }
    }

    public void sortByLastChanged() {
        if (lastChangedSort) {
            ratings = ratings.stream().sorted((o1, o2) -> o1.getLastChanged().compareTo(o2.getLastChanged())).collect(Collectors.toList());
            lastChangedSort = false;
        } else {
            ratings = ratings.stream().sorted((o1, o2) -> o2.getLastChanged().compareTo(o1.getLastChanged())).collect(Collectors.toList());
            lastChangedSort = true;
        }
    }

    public void sortByCreationTime() {
        if (creationTimeSort) {
            ratings = ratings.stream().sorted(((o1, o2) -> {
                if (o1.getCreationTime() == null) {
                    return 1;
                } else if (o2.getCreationTime() == null) {
                    return -1;
                } else {
                    return o1.getCreationTime().compareTo(o2.getCreationTime());
                }
            })).collect(Collectors.toList());
            creationTimeSort = false;
        } else {
            ratings = ratings.stream().sorted(((o1, o2) -> {
                if (o1.getCreationTime() == null) {
                    return -1;
                } else if (o2.getCreationTime() == null) {
                    return 1;
                } else {
                    return o2.getCreationTime().compareTo(o1.getCreationTime());
                }
            })).collect(Collectors.toList());
            creationTimeSort = true;
        }
    }
    //</editor-fold>
}
