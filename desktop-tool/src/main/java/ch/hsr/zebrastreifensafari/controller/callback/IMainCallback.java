package ch.hsr.zebrastreifensafari.controller.callback;

import ch.hsr.zebrastreifensafari.controller.AboutController;
import ch.hsr.zebrastreifensafari.controller.callback.table.ICrossingTable;
import ch.hsr.zebrastreifensafari.controller.callback.table.IRatingTable;
import ch.hsr.zebrastreifensafari.controller.modify.create.CreateCrossingController;
import ch.hsr.zebrastreifensafari.controller.modify.create.CreateRatingController;
import ch.hsr.zebrastreifensafari.controller.modify.edit.EditCrossingController;
import ch.hsr.zebrastreifensafari.controller.modify.edit.EditRatingController;
import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;
import ch.hsr.zebrastreifensafari.jpa.entities.Rating;

import java.util.List;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public interface IMainCallback {

    void showCreateCrossing(CreateCrossingController controller);

    void showCreateRating(CreateRatingController controller);

    void showEditCrossing(EditCrossingController controller);

    void showEditRating(EditRatingController controller);

    void showAbout(AboutController controller);

    void setSelectedTabbedPaneIndex(int index);

    void setRatingTabbedPaneTitle(String title);
}
