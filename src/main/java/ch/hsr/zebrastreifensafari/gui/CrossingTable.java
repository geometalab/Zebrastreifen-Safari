package ch.hsr.zebrastreifensafari.gui;

import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;
import ch.hsr.zebrastreifensafari.service.Properties;

import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 08:51
 * @date : 06.01.2016
 */

public class CrossingTable extends SpecificTable {

    public CrossingTable() {
        super(new CrossingTableModel(), 3);
    }

    public void drawData(List<Crossing> list) {
        getModel().setRowCount(0);

        for (Crossing crossing : list) {
            getModel().addRow(new Object[]{
                    crossing.getOsmNodeId(),
                    crossing.getRatingAmount(),
                    crossing.getStatus(),
                    crossing.getId()
            });
        }

        changeSelection(0);
    }

    public void add(Crossing crossing) {
        getModel().addRow(new Object[]{
                crossing.getOsmNodeId(),
                crossing.getRatingAmount(),
                crossing.getStatus(),
                crossing.getId()
        });
    }

    public long getOsmNodeIdAt(int row) {
        return (long) getValueAt(row, getColumn(Properties.get("osmNodeId")).getModelIndex());
    }

    public long getOsmNodeIdAtSelectedRow() {
        return (long) getValueAt(getSelectedRow(), getColumn(Properties.get("osmNodeId")).getModelIndex());
    }

    public void setOsmNodeIdAtSelectedRow(Crossing crossing) {
        setValueAt(crossing.getOsmNodeId(), getSelectedRow(), getColumn(Properties.get("osmNodeId")).getModelIndex());
    }

    public void setRatingAmountAtSelectedRow(Crossing crossing) {
        setValueAt(crossing.getRatingAmount(), getSelectedRow(), getColumn(Properties.get("ratingAmount")).getModelIndex());
    }
}

class CrossingTableModel extends DefaultTableModel {

    public CrossingTableModel() {
        super(new String[]{Properties.get("osmNodeId"), Properties.get("ratingAmount"), Properties.get("status"), Properties.get("id")}, 0);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if (column == 0) {
            return Long.class;
        } else if (column == 1 || column == 2) {
            return Integer.class;
        }

        return super.getColumnClass(column);
    }
}
