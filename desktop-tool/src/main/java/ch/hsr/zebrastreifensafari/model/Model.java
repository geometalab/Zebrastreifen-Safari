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


    public Model() {
        ratings = new ArrayList<>();
        illuminations = DataServiceLoader.getCrossingData().getIlluminations();
        spatialClaritys = DataServiceLoader.getCrossingData().getSpatialClarities();
        traffics = DataServiceLoader.getCrossingData().getTraffics();
        loadCrossing();
        loadUsers();
    }

    public void loadCrossing() {
        crossings = DataServiceLoader.getCrossingData().getCrossings();
    }

    public void loadRating(Crossing crossing) {
        ratings = DataServiceLoader.getCrossingData().getRatings(crossing);
    }

    public void loadUsers() {
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

    public boolean add(Crossing crossing) {
        return crossings.add(crossing);
    }

    public boolean remove(Crossing crossing) {
        return crossings.remove(crossing);
    }

    public boolean contains(Crossing crossing) {
        return crossings.contains(crossing);
    }

    public int indexOf(Crossing crossing) {
        return crossings.indexOf(crossing);
    }

    public boolean add(Rating rating) {
        return ratings.add(rating);
    }

    public boolean remove(Rating rating) {
        return ratings.remove(rating);
    }

    public int indexOf(Rating rating) {
        return ratings.indexOf(rating);
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
}
