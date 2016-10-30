package ch.hsr.ifs.zebrastreifensafari.controller.callback.table;

import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.Crossing;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public interface ICrossingTable extends ITable<Crossing> {

    long getOsmNodeIdAt(int row);

    long getOsmNodeIdAtSelectedRow();

    void setOsmNodeIdAtSelectedRow(long osmNodeId);

    void setRatingAmountAtSelectedRow(long ratingAmount);

    void removeRow(int row);
}
