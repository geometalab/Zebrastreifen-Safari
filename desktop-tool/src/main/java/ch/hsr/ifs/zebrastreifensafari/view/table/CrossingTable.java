package ch.hsr.ifs.zebrastreifensafari.view.table;

import ch.hsr.ifs.zebrastreifensafari.controller.callback.table.ICrossingTable;
import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.Crossing;
import ch.hsr.ifs.zebrastreifensafari.service.Properties;

import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 1.0
 */
public class CrossingTable extends SpecificTable<Crossing> implements ICrossingTable {

    public CrossingTable() {
        super(new CrossingTableModel(), 3);
    }

    @Override
    public void drawData(List<Crossing> crossings) {
        getModel().setRowCount(0);

        for (Crossing crossing : crossings) {
            getModel().addRow(new Object[] {crossing.getOsmNodeId(), crossing.getRatingAmount(), crossing.getStatus(), crossing.getId()});
        }

        changeSelection(0);
    }

    @Override
    public void add(Crossing crossing) {
        getModel().addRow(new Object[] {crossing.getOsmNodeId(), crossing.getRatingAmount(), crossing.getStatus(), crossing.getId()});
    }

    @Override
    public long getOsmNodeIdAt(int row) {
        return (long) getValueAt(row, getColumn(Properties.get("osmNodeId")).getModelIndex());
    }

    @Override
    public long getOsmNodeIdAtSelectedRow() {
        return (long) getValueAt(getSelectedRow(), getColumn(Properties.get("osmNodeId")).getModelIndex());
    }

    @Override
    public void setOsmNodeIdAtSelectedRow(long osmNodeId) {
        setValueAt(osmNodeId, getSelectedRow(), getColumn(Properties.get("osmNodeId")).getModelIndex());
    }

    @Override
    public void setRatingAmountAtSelectedRow(long ratingAmount) {
        setValueAt(ratingAmount, getSelectedRow(), getColumn(Properties.get("ratingAmount")).getModelIndex());
    }

    @Override
    public void removeRow(int row) {
        getModel().removeRow(row);
    }
}

class CrossingTableModel extends DefaultTableModel {

    public CrossingTableModel() {
        super(new String[] {Properties.get("osmNodeId"), Properties.get("ratingAmount"), Properties.get("status"), Properties.get("id")}, 0);
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
