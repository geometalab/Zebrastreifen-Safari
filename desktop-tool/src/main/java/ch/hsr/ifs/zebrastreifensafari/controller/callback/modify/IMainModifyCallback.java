package ch.hsr.ifs.zebrastreifensafari.controller.callback.modify;

import ch.hsr.ifs.zebrastreifensafari.controller.callback.table.ICrossingTable;
import ch.hsr.ifs.zebrastreifensafari.controller.callback.table.IRatingTable;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public interface IMainModifyCallback {

    ICrossingTable getCrossingTable();

    IRatingTable getRatingTable();

    String getFilter();
}
