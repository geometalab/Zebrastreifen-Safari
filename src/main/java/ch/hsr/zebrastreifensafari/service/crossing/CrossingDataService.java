package ch.hsr.zebrastreifensafari.service.crossing;

import ch.hsr.zebrastreifensafari.jpa.controllers.*;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 15:34
 * @date : 08.10.2015
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
    public void createCrossing(Crossing crossing, Model model) {
        new CrossingJpaController(emFactory).create(crossing);
        model.getCrossings().add(crossing);
    }

    @Override
    public void createCrossing(Crossing crossing, Model model, DefaultTableModel tableModel) {
        createCrossing(crossing, model);
        tableModel.addRow(new Object[]{
                crossing.getOsmNodeId(),
                crossing.getRatingAmount(),
                crossing.getStatus(),
                crossing.getId()
        });
    }

    @Override
    public void createRating(Rating rating) throws EntityNotFoundException {
        new RatingJpaController(emFactory).create(rating);
    }

    @Override
    public void createRating(Rating rating, Model model) throws EntityNotFoundException {
        createRating(rating);
        model.getRatings().add(rating);

    }

    @Override
    public void createRating(Rating rating, Model model, DefaultTableModel tableModel) throws EntityNotFoundException {
        createRating(rating, model);
        tableModel.addRow(new Object[]{
                rating.getUserId().getName(),
                rating.getTrafficId().getValue(),
                rating.getSpatialClarityId().getValue(),
                rating.getIlluminationId().getValue(),
                rating.getComment(),
                rating.getImageWeblink(),
                rating.getLastChanged(),
                rating.getCreationTime(),
                rating.getId()
        });
    }

    @Override
    public void editRating(Rating rating) throws EntityNotFoundException {
        new RatingJpaController(emFactory).edit(rating);
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
    public void removeCrossing(int id, Model model, DefaultTableModel tableModel) throws EntityNotFoundException {
        removeCrossing(id);
        Crossing removeCrossing = model.getCrossing(id);
        tableModel.removeRow(model.getCrossings().indexOf(removeCrossing));
        model.getCrossings().remove(removeCrossing);
    }

    @Override
    public void removeRating(int id) throws EntityNotFoundException {
        new RatingJpaController(emFactory).destroy(id);
    }

    @Override
    public void removeRating(int id, Model model, DefaultTableModel tableModel) throws EntityNotFoundException {
        removeRating(id);
        Rating removeRating = model.getRating(id);
        tableModel.removeRow(model.getRatings().indexOf(removeRating));
        model.getRatings().remove(model.getRating(id));
    }
}
