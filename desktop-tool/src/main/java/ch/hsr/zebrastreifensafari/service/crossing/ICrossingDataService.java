package ch.hsr.zebrastreifensafari.service.crossing;

import ch.hsr.zebrastreifensafari.jpa.entities.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 1.0
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
