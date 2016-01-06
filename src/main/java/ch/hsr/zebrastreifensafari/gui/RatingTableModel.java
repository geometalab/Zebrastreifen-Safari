package ch.hsr.zebrastreifensafari.gui;

import ch.hsr.zebrastreifensafari.service.Properties;

import javax.swing.table.DefaultTableModel;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 08:38
 * @date : 06.01.2016
 */

public class RatingTableModel extends DefaultTableModel {

    public RatingTableModel() {
        super(new String[]{Properties.get("user"), Properties.get("spacialClarity"), Properties.get("illumination"), Properties.get("traffic"),
                Properties.get("comment"), Properties.get("imageId"), Properties.get("lastChange"), Properties.get("creationDate"), Properties.get("id")}, 0);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
