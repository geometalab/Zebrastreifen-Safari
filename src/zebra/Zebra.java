/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zebra;

import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import jpa.controllers.*;
import jpa.entities.*;
import zebra.create.CreateGUI;

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
    
    public static void main(String[] args) {
        
        //getZebracrossings();             
        
        CreateGUI gui = new CreateGUI(getUsers());
        gui.setVisible(true);
        
    }
}
