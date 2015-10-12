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

    List<Rating> getRatings(Zebracrossing zebracrossing);

    List<Illumination> getIlluminations();

    List<Overview> getOverviews();

    List<Traffic> getTraffics();

    void addZebracrossing(Zebracrossing zebra);

    void addRating(Rating rating);

    void removeZebracrossing(int id);

    void removeRating(int id);

    void updateRating(Rating rating);

}
