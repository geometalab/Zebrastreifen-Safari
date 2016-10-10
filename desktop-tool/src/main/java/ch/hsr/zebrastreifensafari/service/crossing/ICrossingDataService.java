package ch.hsr.zebrastreifensafari.service.crossing;

import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;

import javax.persistence.EntityNotFoundException;
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

    List<SpatialClarity> getSpatialClarities();

    List<Traffic> getTraffics();

    void createCrossing(Crossing crossing) throws EntityNotFoundException;

    void editCrossing(Crossing crossing) throws EntityNotFoundException;

    void removeCrossing(int id) throws EntityNotFoundException;

    void createRating(Rating rating) throws EntityNotFoundException;

    void editRating(Rating rating) throws EntityNotFoundException;

    void removeRating(int id) throws EntityNotFoundException;
}