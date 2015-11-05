package ch.hsr.zebrastreifensafari.service.crossing;

import ch.hsr.zebrastreifensafari.jpa.controllers.*;
import ch.hsr.zebrastreifensafari.jpa.controllers.exceptions.NonexistentEntityException;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.table.DefaultTableModel;

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
    public List<Rating> getRatings(Crossing crossing) {
        return new RatingJpaController(emFactory).findRatingByCrossing(crossing);
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
    public void addCrossing(Crossing crossing, Model model) {
        new CrossingJpaController(emFactory).create(crossing);
        model.getCrossings().add(crossing);
    }

    @Override
    public void addCrossing(Crossing crossing, Model model, DefaultTableModel tableModel) {
        addCrossing(crossing, model);
        tableModel.addRow(new Object[] {
                crossing.getOsmNodeId(),
                crossing.getRatingAmount(),
                crossing.getStatus(),
                crossing.getId()
        });
    }

    @Override
    public void addRating(Rating rating) {
        new RatingJpaController(emFactory).create(rating);
    }

    @Override
    public void addRating(Rating rating, Model model) {
        addRating(rating);
        model.getRatings().add(rating);

    }

    @Override
    public void addRating(Rating rating, Model model, DefaultTableModel tableModel) {
        addRating(rating, model);
        tableModel.addRow(new Object[] {
                rating.getUserId().getName(),
                rating.getTrafficId().getValue(),
                rating.getSpatialClarityId().getValue(),
                rating.getIlluminationId().getValue(),
                rating.getComment(),
                rating.getImageWeblink(),
                rating.getLastChanged().toString(),
                rating.getId()
        });
    }

    private void removeCrossing(int id) {
        try {
            new CrossingJpaController(emFactory).destroy(id);
        } catch (NonexistentEntityException neex) {
            neex.printStackTrace();
        }
    }

    @Override
    public void removeCrossing(int id, Model model) {
        removeRating(id);
        model.getCrossings().remove(model.getCrossing(id));
    }

    @Override
    public void removeCrossing(int id, Model model, DefaultTableModel tableModel) {
        removeCrossing(id);
        Crossing removeCrossing = model.getCrossing(id);
        tableModel.removeRow(model.getCrossings().indexOf(removeCrossing));
        model.getCrossings().remove(removeCrossing);
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
    public void removeRating(int id, Model model) {
        removeRating(id);
        model.getRatings().remove(model.getRating(id));
    }

    @Override
    public void removeRating(int id, Model model, DefaultTableModel tableModel) {
        removeRating(id);
        Rating removeRating = model.getRating(id);
        tableModel.removeRow(model.getRatings().indexOf(removeRating));
        model.getRatings().remove(model.getRating(id));
    }

    @Override
    public void updateRating(Rating rating) throws Exception{
        new RatingJpaController(emFactory).edit(rating);
    }

    @Override
    public void updateCrossing(Crossing crossing) throws Exception{
        new CrossingJpaController(emFactory).edit(crossing);
    }
}
