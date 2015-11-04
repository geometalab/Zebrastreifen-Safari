/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.zebrastreifensafari.model;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

/**
 *
 * @author aeugster
 */
public class Model {

    private boolean ratingMode;
    private List<User> users;
    private List<Crossing> crossings;
    private List<Rating> ratings;
    private List<Illumination> illuminations;
    private List<SpatialClarity> spatialClaritys;
    private List<Traffic> traffics;
    private boolean node = true, ratingAmount = true;

    public Model() {
        ratingMode = false;
        ratings = new ArrayList<Rating>();
        illuminations = DataServiceLoader.getCrossingData().getIlluminations();
        spatialClaritys = DataServiceLoader.getCrossingData().getSpatialClaritys();
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

    /**
     * @return the ratingMode
     */
    public boolean isRatingMode() {
        return ratingMode;
    }

    /**
     * @param ratingMode the zebraB to set
     */
    public void setRatingMode(boolean ratingMode) {
        this.ratingMode = ratingMode;
    }
    
    public void sortByNode() {
    	if (node) {
            crossings = crossings.stream().sorted((o1, o2) ->  Long.compare(o1.getOsmNodeId(), o2.getOsmNodeId())).collect(Collectors.toList());
    		node = false;
    	}
    	else {
            crossings = crossings.stream().sorted((o1, o2) ->  Long.compare(o2.getOsmNodeId(), o1.getOsmNodeId())).collect(Collectors.toList());
    		node = true;
    	}
    }
    
    public void sortByNumberOfRatings() {
    	if (ratingAmount) {
    		crossings = crossings.stream().sorted((o1, o2) ->  Long.compare(o1.getRatingAmount(), o2.getRatingAmount())).collect(Collectors.toList());
    		ratingAmount = false;
    	}
    	else {
            crossings = crossings.stream().sorted((o1, o2) ->  Long.compare(o2.getRatingAmount(), o1.getRatingAmount())).collect(Collectors.toList());
    		ratingAmount = true;
    	}
    }

    public void sortByUser() {
        ratings = ratings.stream().sorted(((o1, o2) -> o1.getUserId().getName().compareTo(o2.getUserId().getName()))).collect(Collectors.toList());
    }
}
