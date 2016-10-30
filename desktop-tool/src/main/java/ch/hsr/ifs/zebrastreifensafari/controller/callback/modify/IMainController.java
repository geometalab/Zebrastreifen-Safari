package ch.hsr.ifs.zebrastreifensafari.controller.callback.modify;

import ch.hsr.ifs.zebrastreifensafari.controller.callback.table.ICrossingTable;
import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.Crossing;
import ch.hsr.ifs.zebrastreifensafari.controller.callback.table.IRatingTable;
import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.Rating;

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
