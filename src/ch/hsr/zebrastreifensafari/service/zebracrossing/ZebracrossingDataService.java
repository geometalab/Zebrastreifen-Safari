package ch.hsr.zebrastreifensafari.service.zebracrossing;

import ch.hsr.zebrastreifensafari.jpa.controllers.*;
import ch.hsr.zebrastreifensafari.jpa.controllers.exceptions.NonexistentEntityException;
import ch.hsr.zebrastreifensafari.jpa.entities.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
        return new UserJpaController(emFactory).findUserEntities();
    }

    @Override
    public List<Zebracrossing> getZebracrossings(){
        return new ZebracrossingJpaController(emFactory).findZebracrossingEntities();
    }

    @Override
    public List<Rating> getRatingsByZebracrossing(Zebracrossing zebracrossing){
        return new RatingJpaController(emFactory).findRatingByZebracrossing(zebracrossing);
    }

    @Override
    public void removeZebracrossing(int id){
        try {
            new ZebracrossingJpaController(emFactory).destroy(id);
        } catch (NonexistentEntityException neex) {
            neex.printStackTrace();
        }
    }

    @Override
    public void addZebracrossing(Zebracrossing zebra){
        new ZebracrossingJpaController(emFactory).create(zebra);
    }

    @Override
    public void addRating(Rating rating){
        new RatingJpaController(emFactory).create(rating);
    }

    @Override
    public void removeRating(int id) {
        try {
            new RatingJpaController(emFactory).destroy(id);
        } catch (NonexistentEntityException neex) {
            neex.printStackTrace();
        }
    }

    @Override
    public Illumination getIlluminationValue(int value){
        return new IlluminationJpaController(emFactory).findIllumination(value);
    }

    @Override
    public Overview getOverviewValue(int value){
        return new OverviewJpaController(emFactory).findOverview(value);
    }

    @Override
    public Traffic getTrafficValue(int value){
        return new TrafficJpaController(emFactory).findTraffic(value);
    }

    @Override
    public void updateRating(Rating rating) {
        try {
            new RatingJpaController(emFactory).edit(rating);
        } catch (Exception ex) {
            Logger.getLogger(ZebracrossingDataService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
