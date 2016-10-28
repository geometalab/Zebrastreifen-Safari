package ch.hsr.zebrastreifensafari.view.table;

import ch.hsr.zebrastreifensafari.controller.callback.table.IRatingTable;
import ch.hsr.zebrastreifensafari.jpa.entities.Rating;
import ch.hsr.zebrastreifensafari.service.Properties;

import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.util.List;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 08:57
 * @date : 06.01.2016
 */

public class RatingTable extends SpecificTable<Rating> implements IRatingTable{

    public RatingTable() {
        super(new RatingTableModel(), 8);
    }

    @Override
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

        changeSelection(0);
    }

    @Override
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

    @Override
    public void setUserIdAtSelectedRow(String username) {
        setValueAt(username, getSelectedRow(), getColumn(Properties.get("user")).getModelIndex());
    }

    @Override
    public void setTrafficIdAtSelectedRow(String traffic) {
        setValueAt(traffic, getSelectedRow(), getColumn(Properties.get("traffic")).getModelIndex());
    }

    @Override
    public void setSpatialClarityIdAtSelectedRow(String spatialClarity) {
        setValueAt(spatialClarity, getSelectedRow(), getColumn(Properties.get("spacialClarity")).getModelIndex());
    }

    @Override
    public void setIlluminationIdAtSelectedRow(String illumination) {
        setValueAt(illumination, getSelectedRow(), getColumn(Properties.get("illumination")).getModelIndex());
    }

    @Override
    public void setCommentAtSelectedRow(String comment) {
        setValueAt(comment, getSelectedRow(), getColumn(Properties.get("comment")).getModelIndex());
    }

    @Override
    public void setImageWeblinkAtSelectedRow(String imageWeblink) {
        setValueAt(imageWeblink, getSelectedRow(), getColumn(Properties.get("imageId")).getModelIndex());
    }

    @Override
    public void setLastChangedAtSelectedRow(Date lastChanged) {
        setValueAt(lastChanged, getSelectedRow(), getColumn(Properties.get("lastChange")).getModelIndex());
    }

    @Override
    public void setCreationTimeAtSelectedRow(Date creationTime) {
        setValueAt(creationTime, getSelectedRow(), getColumn(Properties.get("creationDate")).getModelIndex());
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