package ch.hsr.zebrastreifensafari.service.zebracrossing;

import ch.hsr.zebrastreifensafari.jpa.entities.*;

import java.util.List;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 15:34
 * @date : 08.10.2015
 */

public interface IZebracrossingDataService {

    List<User> getUsers();

    List<Zebracrossing> getZebracrossings();

    List<Rating> getRatingsByZebracrossing(Zebracrossing zebracrossing);

    void addZebracrossing(Zebracrossing zebra);

    void addRating(Rating rating);

    void removeZebracrossing(int id);

    void removeRating(int id);

    Illumination getIlluminationValue(int value);
    
    Overview getOverviewValue(int value);

    Traffic getTrafficValue(int value);

    void updateRating(Rating rating);
}
