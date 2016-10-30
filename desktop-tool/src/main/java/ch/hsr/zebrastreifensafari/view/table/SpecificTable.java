package ch.hsr.zebrastreifensafari.view.table;

import ch.hsr.zebrastreifensafari.controller.callback.table.ITable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 1.0
 */
public abstract class SpecificTable<T> extends JTable implements ITable<T> {

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

    @Override
    public void changeSelection(int index) {
        changeSelection(index, 0, false, false);
    }

    @Override
    public void remove(int index) {
        getModel().removeRow(index);
    }

    @Override
    public int getSelectedId() {
        return (int) getModel().getValueAt(getSelectedRow(), ID_POSITION);
    }

    @Override
    public boolean hasData() {
        return !getSelectionModel().isSelectionEmpty() && getRowCount() > 0;
    }

    @Override
    public int getSelectedRow() {
        return super.getSelectedRow();
    }

    @Override
    public int getRowCount() {
        return getModel().getRowCount();
    }
}
