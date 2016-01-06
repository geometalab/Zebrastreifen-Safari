package ch.hsr.zebrastreifensafari.gui;

import ch.hsr.zebrastreifensafari.jpa.entities.Rating;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 08:57
 * @date : 06.01.2016
 */

public class RatingTable extends JTable {

    public RatingTable() {
        super(new RatingTableModel());
        removeColumn(getColumnModel().getColumn(8));
    }

    @Override
    public DefaultTableModel getModel() {
        return (DefaultTableModel) super.getModel();
    }

    public void addRatingDataToTable(List<Rating> list) {
        getModel().setRowCount(0);

        for (Rating rating : list) {
            getModel().addRow(new Object[]{
                    rating.getUserId().getName(),
                    rating.getSpatialClarityId().getValue(),
                    rating.getIlluminationId().getValue(),
                    rating.getTrafficId().getValue(),
                    rating.getComment(),
                    rating.getImageWeblink(),
                    rating.getLastChanged(),
                    rating.getCreationTime(),
                    rating.getId()
            });
        }

        changeTableSelection(0);
    }

    void changeTableSelection(int index) {
        changeSelection(index, 0, false, false);
    }
}
