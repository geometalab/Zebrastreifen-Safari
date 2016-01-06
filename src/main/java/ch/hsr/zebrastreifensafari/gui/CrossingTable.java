package ch.hsr.zebrastreifensafari.gui;

import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;

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

    public void addCrossingDataToTable(List<Crossing> list) {
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
}
