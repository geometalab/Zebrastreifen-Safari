package ch.hsr.ifs.zebrastreifensafari.controller.callback.modify;

import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.Crossing;
import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.Rating;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public interface IEntityManipulation {

    void createCrossing(Crossing crossing);

    void createRating(Rating rating);

    void editCrossing(Crossing crossing);

    void editRating(Rating rating);

    void deleteCrossing();
}
