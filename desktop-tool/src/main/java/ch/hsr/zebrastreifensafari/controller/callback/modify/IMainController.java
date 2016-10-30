package ch.hsr.zebrastreifensafari.controller.callback.modify;

import ch.hsr.zebrastreifensafari.controller.callback.table.ICrossingTable;
import ch.hsr.zebrastreifensafari.controller.callback.table.IRatingTable;
import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;
import ch.hsr.zebrastreifensafari.jpa.entities.Rating;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public interface IMainController {

    void createCrossing(ICrossingTable crossingTable, Crossing crossing, String filter);

    void createRating(ICrossingTable crossingTable, IRatingTable ratingTable, Rating rating);

    void editCrossing(ICrossingTable crossingTable, Crossing crossing, String filter);

    void editRating(IRatingTable ratingTable, Rating rating);

    void deleteCrossing(ICrossingTable crossingTable);
}
