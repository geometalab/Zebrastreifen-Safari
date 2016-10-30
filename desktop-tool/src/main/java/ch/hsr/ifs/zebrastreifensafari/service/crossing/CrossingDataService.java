package ch.hsr.ifs.zebrastreifensafari.service.crossing;

import ch.hsr.ifs.zebrastreifensafari.model.jpa.controllers.*;
import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 1.0
 */
public class CrossingDataService implements ICrossingDataService {

    private final EntityManagerFactory emFactory;

    public CrossingDataService(String entityName) {
        emFactory = Persistence.createEntityManagerFactory(entityName);
        EntityTransaction trans = emFactory.createEntityManager().getTransaction();
        trans.begin();
    }

    @Override
    public List<User> getUsers() {
        return new UserJpaController(emFactory).findEntities();
    }

    @Override
    public List<Crossing> getCrossings() {
        return new CrossingJpaController(emFactory).findEntities();
    }

    @Override
    public List<Rating> getRatings(Crossing crossing) {
        return new RatingJpaController(emFactory).findEntities(crossing);
    }

    @Override
    public List<Illumination> getIlluminations() {
        return new IlluminationJpaController(emFactory).findEntities();
    }

    @Override
    public List<SpatialClarity> getSpatialClarities() {
        return new SpatialClarityJpaController(emFactory).findEntities();
    }

    @Override
    public List<Traffic> getTraffics() {
        return new TrafficJpaController(emFactory).findEntities();
    }

    @Override
    public void createCrossing(Crossing crossing) {
        new CrossingJpaController(emFactory).create(crossing);
    }

    @Override
    public void editCrossing(Crossing crossing) throws EntityNotFoundException {
        new CrossingJpaController(emFactory).edit(crossing);
    }

    @Override
    public void removeCrossing(int id) throws EntityNotFoundException {
        new CrossingJpaController(emFactory).destroy(id);
    }

    @Override
    public void createRating(Rating rating) throws EntityNotFoundException {
        new RatingJpaController(emFactory).create(rating);
    }

    @Override
    public void editRating(Rating rating) throws EntityNotFoundException {
        new RatingJpaController(emFactory).edit(rating);
    }

    @Override
    public void removeRating(int id) throws EntityNotFoundException {
        new RatingJpaController(emFactory).destroy(id);
    }
}
