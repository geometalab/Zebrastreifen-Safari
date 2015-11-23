package ch.hsr.zebrastreifensafari.service.crossing;

import ch.hsr.zebrastreifensafari.jpa.controllers.exceptions.NonexistentEntityException;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;

import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 15:34
 * @date : 08.10.2015
 */

public interface ICrossingDataService {

    List<User> getUsers();

    List<Crossing> getCrossings();

    List<Rating> getRatings(Crossing crossing);

    List<Illumination> getIlluminations();

    List<SpatialClarity> getSpatialClaritys();

    List<Traffic> getTraffics();

    void addCrossing(Crossing crossing, Model model);

    void addCrossing(Crossing crossing, Model model, DefaultTableModel tableModel);

    void addRating(Rating rating);

    void addRating(Rating rating, Model model);

    void addRating(Rating rating, Model model, DefaultTableModel tableModel);

    void removeCrossing(int id) throws NonexistentEntityException;

    void removeCrossing(int id, Model model, DefaultTableModel tableModel) throws NonexistentEntityException;

    void removeRating(int id) throws NonexistentEntityException;

    void removeRating(int id, Model model, DefaultTableModel tableModel) throws NonexistentEntityException;

    void editRating(Rating rating) throws Exception;

    void editCrossing(Crossing crossing) throws Exception;
}
