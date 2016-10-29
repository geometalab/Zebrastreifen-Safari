package ch.hsr.zebrastreifensafari.controller.callback;

import ch.hsr.zebrastreifensafari.controller.AboutController;
import ch.hsr.zebrastreifensafari.controller.callback.table.ICrossingTable;
import ch.hsr.zebrastreifensafari.controller.callback.table.IRatingTable;
import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;
import ch.hsr.zebrastreifensafari.jpa.entities.Rating;

import java.util.List;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public interface IMainCallback {

    void showCreateCrossing();

    void showCreateRating(long osmNodeId);

    void showEditCrossing(Crossing crossing);

    void showEditRating(Rating rating);

    void showAbout(AboutController controller);

    void setSelectedTabbedPaneIndex(int index);

    void setRatingTabbedPaneTitle(String title);

    void errorMessage(String changeSelectionError);

    boolean isRatingMode();
}
