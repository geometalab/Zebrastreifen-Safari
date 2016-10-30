package ch.hsr.ifs.zebrastreifensafari.controller.modify.edit;

import ch.hsr.ifs.zebrastreifensafari.controller.callback.modify.IMainController;
import ch.hsr.ifs.zebrastreifensafari.controller.modify.ModifyController;
import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.Crossing;
import ch.hsr.ifs.zebrastreifensafari.model.Model;
import ch.hsr.ifs.zebrastreifensafari.controller.callback.modify.edit.IEditCrossingCallback;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public class EditCrossingController extends ModifyController {

    private final IMainController parent;
    private final Crossing crossing;
    private IEditCrossingCallback callback;

    public EditCrossingController(IMainController parent, Model model, Crossing crossing) {
        super(model);
        this.parent = parent;
        this.crossing = crossing;
    }

    public Crossing getCrossing() {
        return crossing;
    }

    public void setCallback(IEditCrossingCallback callback) {
        this.callback = callback;
    }

    public void send() {
        long osmNodeId = crossing.getOsmNodeId();

        if (!callback.editCrossing()) {
            setCrossingData(osmNodeId);
        }
    }

    public void editCrossing(String osmNodeIdText) {
        setCrossingData(Long.parseLong(osmNodeIdText));
        parent.editCrossing(crossing);
    }

    private void setCrossingData(long osmNodeId) {
        crossing.setOsmNodeId(osmNodeId);
    }
}
