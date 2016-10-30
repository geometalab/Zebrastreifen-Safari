package ch.hsr.ifs.zebrastreifensafari.controller.callback.table;

import java.util.List;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public interface ITable<T> {

    void drawData(List<T> data);

    void add(T element);

    void changeSelection(int index);

    void remove(int index);

    int getSelectedId();

    boolean hasData();

    int getSelectedRow();

    int getRowCount();
}
