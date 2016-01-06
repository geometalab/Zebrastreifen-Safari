package ch.hsr.zebrastreifensafari.gui;

import ch.hsr.zebrastreifensafari.service.Properties;

import javax.swing.table.DefaultTableModel;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 08:36
 * @date : 06.01.2016
 */

public class CrossingTableModel extends DefaultTableModel {

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
