package ch.hsr.zebrastreifensafari.controller.callback;

import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;
import ch.hsr.zebrastreifensafari.jpa.entities.Rating;

import java.util.List;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public interface IMainCallback {

    void createCrossing();

    void createRating();

    void editCrossing();

    void editRating();

    void removeCrossing();

    void removeRating();

    void removeCrossing(int index);

    void removeRating(int index);

    void drawDataCrossing(List<Crossing> crossings);

    void drawDataRating(List<Rating> ratings);

    void changeSelectionCrossing(int index);

    void changeSelectionRating(int index);

    void setSelectedTabbedPaneIndex(int index);

    void setRatingTabbedPaneTitle(String title);

    void errorMessage(String changeSelectionError);

    int getSelectedCrossingId();

    int getSelectedCrossingRow();

    int getCrossingRowCount();

    boolean isRatingMode();

    long getOsmNodeIdAtSelectedRow();
}
