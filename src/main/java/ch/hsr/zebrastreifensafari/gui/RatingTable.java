package ch.hsr.zebrastreifensafari.gui;

import ch.hsr.zebrastreifensafari.jpa.entities.Rating;
import ch.hsr.zebrastreifensafari.service.Properties;

import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 08:57
 * @date : 06.01.2016
 */

public class RatingTable extends SpecificTable {

    public RatingTable() {
        super(new RatingTableModel(), 8);
    }

    public void drawData(List<Rating> list) {
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

    public void add(Rating rating) {
        getModel().addRow(new Object[]{
                rating.getUserId().getName(),
                rating.getTrafficId().getValue(),
                rating.getSpatialClarityId().getValue(),
                rating.getIlluminationId().getValue(),
                rating.getComment(),
                rating.getImageWeblink(),
                rating.getLastChanged(),
                rating.getCreationTime(),
                rating.getId()
        });
    }

    public void setUserIdAtSelectedRow(Rating rating) {
        setValueAt(rating.getUserId().getName(), getSelectedRow(), getColumn(Properties.get("user")).getModelIndex());
    }

    public void setTrafficIdAtSelectedRow(Rating rating) {
        setValueAt(rating.getTrafficId().getValue(), getSelectedRow(), getColumn(Properties.get("traffic")).getModelIndex());
    }

    public void setSpatialClarityIdAtSelected(Rating rating) {
        setValueAt(rating.getSpatialClarityId().getValue(), getSelectedRow(), getColumn(Properties.get("spacialClarity")).getModelIndex());
    }

    public void setIlluminationIdAtSelectedRow(Rating rating) {
        setValueAt(rating.getIlluminationId().getValue(), getSelectedRow(), getColumn(Properties.get("illumination")).getModelIndex());
    }

    public void setCommentAtSelectedRow(Rating rating) {
        setValueAt(rating.getComment(), getSelectedRow(), getColumn(Properties.get("comment")).getModelIndex());
    }

    public void setImageWeblinkAtSelectedRow(Rating rating) {
        setValueAt(rating.getImageWeblink(), getSelectedRow(), getColumn(Properties.get("imageId")).getModelIndex());
    }

    public void setLastChangedAtSelectedRow(Rating rating) {
        setValueAt(rating.getLastChanged(), getSelectedRow(), getColumn(Properties.get("lastChange")).getModelIndex());
    }

    public void setValueAtSelectedRowCreationTime(Rating rating) {
        setValueAt(rating.getCreationTime(), getSelectedRow(), getColumn(Properties.get("creationDate")).getModelIndex());
    }
}


class RatingTableModel extends DefaultTableModel {

    public RatingTableModel() {
        super(new String[]{Properties.get("user"), Properties.get("spacialClarity"), Properties.get("illumination"), Properties.get("traffic"),
                Properties.get("comment"), Properties.get("imageId"), Properties.get("lastChange"), Properties.get("creationDate"), Properties.get("id")}, 0);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}