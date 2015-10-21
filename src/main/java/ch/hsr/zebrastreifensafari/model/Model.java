/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.ch.hsr.zebrastreifensafari.model;

import java.util.List;

import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import java.util.ArrayList;

/**
 *
 * @author aeugster
 */
public class Model {

    private boolean ratingMode;
    private List<User> users;
    private List<Zebracrossing> zebracrossings;
    private List<Rating> ratings;
    private List<Illumination> illuminations;
    private List<Overview> overviews;
    private List<Traffic> traffics;

    public Model() {
        ratingMode = false;
        ratings = new ArrayList<Rating>();
        illuminations = DataServiceLoader.getZebraData().getIlluminations();
        overviews = DataServiceLoader.getZebraData().getOverviews();
        traffics = DataServiceLoader.getZebraData().getTraffics();
        reloadZebracrossing();
        reloadUsers();
    }

    public void reloadZebracrossing() {
        zebracrossings = DataServiceLoader.getZebraData().getZebracrossings();
    }

    public void reloadRating(Zebracrossing zebracrossing) {
        ratings = DataServiceLoader.getZebraData().getRatings(zebracrossing);
    }

    public void reloadUsers() {
        users = DataServiceLoader.getZebraData().getUsers();
    }

    public User getUser(String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }

        return null;
    }

    public Zebracrossing getZebracrossing(int id) {
        for (Zebracrossing zebracrossing : zebracrossings) {
            if (zebracrossing.getZebracrossingId() == id) {
                return zebracrossing;
            }
        }

        return null;
    }

    public Zebracrossing getZebracrossing(long node) {
        for (Zebracrossing zebracrossing : zebracrossings) {
            if (zebracrossing.getNode() == node) {
                return zebracrossing;
            }
        }

        return null;
    }

    public Rating getRating(int id) {
        for (Rating rating : ratings) {
            if (rating.getRatingId() == id) {
                return rating;
            }
        }

        return null;
    }

    public Illumination getIllumination(int id) {
        for (Illumination illumination : illuminations) {
            if (illumination.getIlluminationId() == id) {
                return illumination;
            }
        }

        return null;
    }

    public Overview getOverview(int id) {
        for (Overview overview : overviews) {
            if (overview.getOverviewId() == id) {
                return overview;
            }
        }

        return null;
    }

    public Traffic getTraffic(int id) {
        for (Traffic traffic : traffics) {
            if (traffic.getTrafficId() == id) {
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
    public List<Zebracrossing> getZebracrossings() {
        return zebracrossings;
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
}
