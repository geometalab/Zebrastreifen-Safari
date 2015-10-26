package ch.hsr.zebrastreifensafari.service.crossing;

import ch.hsr.zebrastreifensafari.jpa.entities.*;

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

    List<Rating> getRatings(Crossing Crossing);

    List<Illumination> getIlluminations();

    List<SpatialClarity> getSpatialClaritys();

    List<Traffic> getTraffics();

    void addCrossing(Crossing zebra);

    void addRating(Rating rating);

    void removeCrossing(int id);

    void removeRating(int id);

    void updateRating(Rating rating);

    void updateCrossing(Crossing Crossing);
}
