package ch.hsr.zebrastreifensafari.gui;

import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;
import ch.hsr.zebrastreifensafari.service.Properties;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 08:51
 * @date : 06.01.2016
 */

public class CrossingTable extends JTable {

    public CrossingTable() {
        super(new CrossingTableModel());
        removeColumn(getColumnModel().getColumn(3));
    }

    @Override
    public DefaultTableModel getModel() {
        return (DefaultTableModel) super.getModel();
    }

    public void changeTableSelection(int index) {
        changeSelection(index, 0, false, false);
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

        changeTableSelection(0);
    }

    public void add(Crossing crossing) {
        getModel().addRow(new Object[]{
                crossing.getOsmNodeId(),
                crossing.getRatingAmount(),
                crossing.getStatus(),
                crossing.getId()
        });
    }

    public void remove(int index) {
        getModel().removeRow(index);
    }

    public int getSelectedId() {
        return (int) getModel().getValueAt(getSelectedRow(), 3);
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
