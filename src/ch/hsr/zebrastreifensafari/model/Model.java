/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.zebrastreifensafari.model;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.zebrastreifensafari.jpa.controllers.RatingJpaController;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author aeugster
 */
public class Model{
    
    private boolean ratingMode;
    private List<Zebracrossing> zebras;
    private List<Rating> ratings;
    private List<User> users;

    public Model() {
        ratingMode = false;
        users = DataServiceLoader.getZebraData().getUsers();
        updateData();
    }

    public void updateData() {
        zebras = DataServiceLoader.getZebraData().getZebracrossings();
    }

    public User getUser(int id){
        for(User user : users){
            if(user.getUserId() == id){
                return user;
            }
        }

        return null;
    }

    public User getUser(String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }

        return null;
    }

    public Zebracrossing getZebracrossing(int id){
        System.out.println("id");
        for(Zebracrossing z : zebras){
            if(z.getZebracrossingId() == id){
                return z;
            }
        }

        return null;
    }

    public Zebracrossing getZebracrossing(long node) {
        System.out.println("node");
        for(Zebracrossing z: zebras){
            if(z.getNode() == node){
                return z;
            }
        }

        return null;
    }

    public Rating getRating(int id){
        for(Rating r : ratings){
            if(r.getRatingId() == id){
                return r;
            }
        }

        return null;
    }

    public void setRatings(Zebracrossing zebracrossing) {
        ratings = DataServiceLoader.getZebraData().getRatingsByZebracrossing(zebracrossing);
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
    public List<Zebracrossing> getZebras() {
        return zebras;
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
