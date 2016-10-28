package ch.hsr.zebrastreifensafari.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 1.0
 */
public abstract class SpecificTable<T> extends JTable {

    protected final int ID_POSITION;

    public SpecificTable(DefaultTableModel tableModel, int idPosition) {
        super(tableModel);
        ID_POSITION = idPosition;
        removeColumn(getColumnModel().getColumn(ID_POSITION));
    }

    public abstract void drawData(List<T> list);

    public abstract void add(T type);

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
