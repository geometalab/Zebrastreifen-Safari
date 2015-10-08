/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zebra;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import jpa.controllers.*;
import jpa.controllers.exceptions.NonexistentEntityException;
import jpa.entities.*;
import zebra.create.CreateGUI;
import zebra.view.View;

/**
 *
 * @author aeugster
 */
public class Zebra {
    
    private static EntityManagerFactory emFactory;
    
    
    
    static{
        emFactory = Persistence.createEntityManagerFactory("ZebraPU");
    }
    
    public static ArrayList<User> getUsers(){
        ArrayList<User> users = new ArrayList<>();
        UserJpaController ujc = new UserJpaController(emFactory);
        for(User u : ujc.findUserEntities()){
            users.add(u);
        }
        return users;
    }
    
    public static ArrayList<Zebracrossing> getZebracrossings(){
        ArrayList<Zebracrossing> zebras = new ArrayList<>();
        ZebracrossingJpaController zjc = new ZebracrossingJpaController(emFactory);
        for(Zebracrossing z : zjc.findZebracrossingEntities()){
            zebras.add(z);
        }
        return zebras;
    } 
    
    public static ArrayList<Rating> getRatingsOfZebra(Zebracrossing zebra){
        ArrayList<Rating> ratings = new ArrayList<>();
        RatingJpaController rjc = new RatingJpaController(emFactory);
        for(Rating r : rjc.findRatingEntities()){
            if(r.getZebracrossingFk().equals(zebra)){
                ratings.add(r);
            }
        }
        return ratings;
    }
    
    public static void removeZebracrossing(int id){
        ZebracrossingJpaController zjc = new ZebracrossingJpaController(emFactory);

        try {
            zjc.destroy(id);
        } catch (NonexistentEntityException neex) {
            neex.printStackTrace();
        }
    }
    
    public static void addZebracrossing(Zebracrossing zebra){
        ZebracrossingJpaController zjc = new ZebracrossingJpaController(emFactory);
        zjc.create(zebra);
    }
    
    public static void addRating(Rating rating){
        RatingJpaController rjc = new RatingJpaController(emFactory);
        rjc.create(rating);
    }

    public static void removeRating(int id) {
        RatingJpaController rjc = new RatingJpaController(emFactory);

        try {
            rjc.destroy(id);
        } catch (NonexistentEntityException neex) {
            neex.printStackTrace();
        }
    }
    
    public static Illumination getIlluminationValue(int value){
        IlluminationJpaController ijc = new IlluminationJpaController(emFactory);
        Illumination i = ijc.findIllumination(value);
        return i;
    }
    
    public static Overview getOverviewValue(int value){
        OverviewJpaController ojc = new OverviewJpaController(emFactory);
        Overview o = ojc.findOverview(value);
        return o;
    }
    
    public static Traffic getTrafficValue(int value){
        TrafficJpaController tjc = new TrafficJpaController(emFactory);
        Traffic t = tjc.findTraffic(value);
        return t;
    }
    
    public static User getUserByName(String name){
        UserJpaController ujc = new UserJpaController(emFactory);
        for(User u: ujc.findUserEntities()){
            if(u.getName().equals(name)){
                return u;
            }
        }
        return null;
    }
    
    public static User getUserByID(int id){
        UserJpaController ujc = new UserJpaController(emFactory);
        for(User u: ujc.findUserEntities()){
            if(u.getUserId() == id){
                return u;
            }
        }
        return null;
    }
    
    public static Zebracrossing getZebracrossingByNode(long node){
        ZebracrossingJpaController zjc = new ZebracrossingJpaController(emFactory);
        for(Zebracrossing zebra: zjc.findZebracrossingEntities()){
            if(node == zebra.getNode()){
                return zebra;
            }         
        }
         return null;
    }

    public static Zebracrossing getZebracrossingById(int id){
        ZebracrossingJpaController zjc = new ZebracrossingJpaController(emFactory);
        for(Zebracrossing zebra: zjc.findZebracrossingEntities()){
            if(id == zebra.getZebracrossingId()){
                return zebra;
            }         
        }
         return null;
    }
    
    public static void updateRating(Rating rating){
        RatingJpaController rjc = new RatingJpaController(emFactory);
        try {
            rjc.edit(rating);
        } catch (Exception ex) {
            Logger.getLogger(Zebra.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
            

    
    public static void main(String[] args) {

        //getZebracrossings();
        
        View view = new View();
        view.setVisible(true);
        
    }
}
