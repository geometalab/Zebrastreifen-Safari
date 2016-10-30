package ch.hsr.ifs.zebrastreifensafari.controller.modify.edit;

import ch.hsr.ifs.zebrastreifensafari.controller.callback.modify.IEntityManipulation;
import ch.hsr.ifs.zebrastreifensafari.controller.modify.ModifyController;
import ch.hsr.ifs.zebrastreifensafari.model.Model;
import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.Crossing;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public class EditCrossingController extends ModifyController {

    private final Crossing crossing;

    public EditCrossingController(IEntityManipulation entityManipulation, Model model, Crossing crossing) {
        super(entityManipulation, model);
        this.crossing = crossing;
    }

    public Crossing getCrossing() {
        return crossing;
    }

    public void editCrossing(String osmNodeIdText) {
        setCrossingData(Long.parseLong(osmNodeIdText));
        entityManipulation.editCrossing(crossing);
    }

    public void setCrossingData(long osmNodeId) {
        crossing.setOsmNodeId(osmNodeId);
    }
}
