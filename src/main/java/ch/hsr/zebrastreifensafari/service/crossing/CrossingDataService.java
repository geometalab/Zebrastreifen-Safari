package ch.hsr.zebrastreifensafari.service.crossing;

import ch.hsr.zebrastreifensafari.jpa.controllers.*;
import ch.hsr.zebrastreifensafari.jpa.controllers.exceptions.NonexistentEntityException;
import ch.hsr.zebrastreifensafari.jpa.entities.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 15:34
 * @date : 08.10.2015
 */

public class CrossingDataService implements ICrossingDataService {

    private EntityManagerFactory emFactory;
    private EntityManager em;

    public CrossingDataService(String entityName) {
        emFactory = Persistence.createEntityManagerFactory(entityName);
        em = emFactory.createEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();
    }

    @Override
    public List<User> getUsers() {
        return new UserJpaController(emFactory).findUserEntities();
    }

    @Override
    public List<Crossing> getCrossings() {
        return new CrossingJpaController(emFactory).findCrossingEntities();
    }

    @Override
    public List<Rating> getRatings(Crossing Crossing) {
        return new RatingJpaController(emFactory).findRatingByCrossing(Crossing);
    }

    @Override
    public List<Illumination> getIlluminations() {
        return new IlluminationJpaController(emFactory).findIlluminationEntities();
    }

    @Override
    public List<SpatialClarity> getSpatialClaritys() {
        return new SpatialClarityJpaController(emFactory).findSpatialClarityEntities();
    }

    @Override
    public List<Traffic> getTraffics() {
        return new TrafficJpaController(emFactory).findTrafficEntities();
    }

    @Override
    public void removeCrossing(int id) {
        try {
            new CrossingJpaController(emFactory).destroy(id);
        } catch (NonexistentEntityException neex) {
            neex.printStackTrace();
        }
    }

    @Override
    public void addCrossing(Crossing zebra) {
        new CrossingJpaController(emFactory).create(zebra);
    }

    @Override
    public void addRating(Rating rating) {
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
    public void updateRating(Rating rating) throws Exception{
        new RatingJpaController(emFactory).edit(rating);
    }

    @Override
    public void updateCrossing(Crossing Crossing) throws Exception{
        new CrossingJpaController(emFactory).edit(Crossing);
    }
}
