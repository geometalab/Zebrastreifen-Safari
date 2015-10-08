package ch.hsr.zebrastreifensafari.service.zebracrossing;

import ch.hsr.zebrastreifensafari.jpa.controllers.*;
import ch.hsr.zebrastreifensafari.jpa.controllers.exceptions.NonexistentEntityException;
import ch.hsr.zebrastreifensafari.jpa.entities.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 15:34
 * @date : 08.10.2015
 */

public class ZebracrossingDataService implements IZebracrossingDataService {

    private EntityManagerFactory emFactory;

    public ZebracrossingDataService() {
        emFactory = Persistence.createEntityManagerFactory("ZebraPU");
    }

    @Override
    public List<User> getUsers(){
        List<User> users = new ArrayList<User>();
        UserJpaController ujc = new UserJpaController(emFactory);

        for(User u : ujc.findUserEntities()){
            users.add(u);
        }

        return users;
    }

    @Override
    public List<Zebracrossing> getZebracrossings(){
        List<Zebracrossing> zebras = new ArrayList<Zebracrossing>();
        ZebracrossingJpaController zjc = new ZebracrossingJpaController(emFactory);

        for(Zebracrossing z : zjc.findZebracrossingEntities()){
            zebras.add(z);
        }

        return zebras;
    }

    @Override
    public List<Rating> getRatingsOfZebra(Zebracrossing zebra){
        List<Rating> ratings = new ArrayList<Rating>();
        RatingJpaController rjc = new RatingJpaController(emFactory);

        for(Rating r : rjc.findRatingEntities()){
            if(r.getZebracrossingFk().equals(zebra)){
                ratings.add(r);
            }
        }

        return ratings;
    }

    @Override
    public void removeZebracrossing(int id){
        ZebracrossingJpaController zjc = new ZebracrossingJpaController(emFactory);

        try {
            zjc.destroy(id);
        } catch (NonexistentEntityException neex) {
            neex.printStackTrace();
        }
    }

    @Override
    public void addZebracrossing(Zebracrossing zebra){
        ZebracrossingJpaController zjc = new ZebracrossingJpaController(emFactory);
        zjc.create(zebra);
    }

    @Override
    public void addRating(Rating rating){
        RatingJpaController rjc = new RatingJpaController(emFactory);
        rjc.create(rating);
    }

    @Override
    public void removeRating(int id) {
        RatingJpaController rjc = new RatingJpaController(emFactory);

        try {
            rjc.destroy(id);
        } catch (NonexistentEntityException neex) {
            neex.printStackTrace();
        }
    }

    @Override
    public Illumination getIlluminationValue(int value){
        IlluminationJpaController ijc = new IlluminationJpaController(emFactory);
        return ijc.findIllumination(value);
    }

    @Override
    public Overview getOverviewValue(int value){
        OverviewJpaController ojc = new OverviewJpaController(emFactory);
        return ojc.findOverview(value);
    }

    @Override
    public Traffic getTrafficValue(int value){
        TrafficJpaController tjc = new TrafficJpaController(emFactory);
        return tjc.findTraffic(value);
    }

    @Override
    public User getUserByName(String name){
        UserJpaController ujc = new UserJpaController(emFactory);

        for(User u: ujc.findUserEntities()){
            if(u.getName().equals(name)){
                return u;
            }
        }

        return null;
    }

    public User getUserByID(int id){
        UserJpaController ujc = new UserJpaController(emFactory);

        for(User u: ujc.findUserEntities()){
            if(u.getUserId() == id){
                return u;
            }
        }

        return null;
    }

    @Override
    public Zebracrossing getZebracrossingByNode(long node) {
        ZebracrossingJpaController zjc = new ZebracrossingJpaController(emFactory);

        for (Zebracrossing zebra: zjc.findZebracrossingEntities()) {
            if (node == zebra.getNode()) {
                return zebra;
            }
        }

        return null;
    }

    public Zebracrossing getZebracrossingById(int id){
        ZebracrossingJpaController zjc = new ZebracrossingJpaController(emFactory);

        for(Zebracrossing zebra: zjc.findZebracrossingEntities()){
            if(id == zebra.getZebracrossingId()){
                return zebra;
            }
        }

        return null;
    }

    public void updateRating(Rating rating) {
        RatingJpaController rjc = new RatingJpaController(emFactory);

        try {
            rjc.edit(rating);
        } catch (Exception ex) {
            Logger.getLogger(ZebracrossingDataService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
