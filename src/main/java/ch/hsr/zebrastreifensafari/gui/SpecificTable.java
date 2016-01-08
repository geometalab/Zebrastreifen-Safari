package ch.hsr.zebrastreifensafari.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 15:14
 * @date : 06.01.2016
 */

public abstract class SpecificTable extends JTable {

    protected final int ID_POSITION;

    public SpecificTable(DefaultTableModel tableModel, int idPosition) {
        super(tableModel);
        ID_POSITION = idPosition;
        removeColumn(getColumnModel().getColumn(ID_POSITION));
    }

    @Override
    public DefaultTableModel getModel() {
        return (DefaultTableModel) super.getModel();
    }

    public void changeSelection(int index) {
        changeSelection(index, 0, false, false);
    }

    public void remove(int index) {
        getModel().removeRow(index);
    }

    public int getSelectedId() {
        return (int) getModel().getValueAt(getSelectedRow(), ID_POSITION);
    }

    public boolean hasData() {
        return !getSelectionModel().isSelectionEmpty() && getRowCount() > 0;
    }
}
