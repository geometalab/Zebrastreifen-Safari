package ch.hsr.zebrastreifensafari.controller;

import ch.hsr.zebrastreifensafari.controller.callback.IMainCallback;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.Properties;
import ch.hsr.zebrastreifensafari.service.WebsiteService;
import org.eclipse.persistence.exceptions.DatabaseException;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public class MainController {

    private final Model model;
    private IMainCallback callback;

    public MainController(Model model) {
        this.model = model;
    }

    public void subscribe(IMainCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback can't be null");
        }

        this.callback = callback;
    }

    public void onTabbedPaneChange() {
        try {
            if (callback.isRatingMode()) {
                model.reloadRating(model.getCrossing(callback.getCrossingTable().getSelectedId()));
                callback.getRatingTable().drawData(model.getRatings());
            }
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            callback.setSelectedTabbedPaneIndex(0);
            callback.errorMessage(Properties.get("changeSelectionError"));
        } catch (PersistenceException pex) {
            callback.setSelectedTabbedPaneIndex(0);
            callback.errorMessage(Properties.get("connectionError"));
        }
    }

    //todo refactor
    public void onCrossingSelection(boolean hasData) {
        if (hasData) {
            callback.setRatingTabbedPaneTitle(Properties.get("specificRatingTabbedPaneTitle") + callback.getCrossingTable().getOsmNodeIdAtSelectedRow());
        }
    }

    public void onSearch(String searchText) {
        if (searchText.isEmpty()) {
            callback.getCrossingTable().drawData(model.getCrossings());
        } else {
            callback.getCrossingTable().drawData(model.getCrossings().stream()
                    .filter(crossing -> String.valueOf(crossing.getOsmNodeId()).startsWith(searchText))
                    .collect(Collectors.toList()));
        }
    }

    public void onAddClick() {
        if (callback.isRatingMode()) {
            callback.createRating();
        } else {
            callback.createCrossing();
        }
    }

    public void onEditClick() {
        try {
            if (callback.isRatingMode()) {
                callback.editRating();
            } else {
                callback.editCrossing();
            }
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            callback.errorMessage(Properties.get("editSelectionError"));
        }
    }

    public void onDelete() {
        try {
            if (callback.isRatingMode()) {
                callback.removeRating();
            } else {
                callback.removeCrossing();
            }
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            callback.errorMessage(Properties.get("deleteSelectionError"));
        } catch (DatabaseException dbex) {
            callback.errorMessage(Properties.get("connectionError"));
        }
    }

    public void onPreviousCrossingClick() {
        if (callback.getCrossingTable().getSelectedRow() != 0) {
            callback.getCrossingTable().changeSelection(callback.getCrossingTable().getSelectedRow() - 1);
        } else {
            callback.getCrossingTable().changeSelection(callback.getCrossingTable().getRowCount() - 1);
        }

        onTabbedPaneChange();
    }

    public void onNextCrossingClick() {
        if (callback.getCrossingTable().getSelectedRow() != callback.getCrossingTable().getRowCount() - 1) {
            callback.getCrossingTable().changeSelection(callback.getCrossingTable().getSelectedRow() + 1);
        } else {
            callback.getCrossingTable().changeSelection(0);
        }

        onTabbedPaneChange();
    }

    public void onRefreshClick() {
        try {
            model.reloadUsers();

            if (callback.isRatingMode()) {
                model.reloadRating(model.getCrossing(callback.getCrossingTable().getSelectedId()));
                callback.getRatingTable().drawData(model.getRatings());
            } else {
                model.reloadCrossing();
                callback.getCrossingTable().drawData(model.getCrossings());
            }
        } catch (PersistenceException pex) {
            callback.errorMessage(Properties.get("connectionError"));
        }
    }

    public void onHelpClick() {
        WebsiteService.openWebsite(Properties.get("helpLink"));
    }

    public void onCrossingSort(String columnName) {
        if (columnName.equals(Properties.get("osmNodeId"))) {
            model.sortByNode();
        } else if (columnName.equals(Properties.get("ratingAmount"))) {
            model.sortByNumberOfRatings();
        } else if (columnName.equals(Properties.get("status"))) {
            model.sortByStatus();
        }

        callback.getCrossingTable().drawData(model.getCrossings());
    }

    public void onRatingSort(String columnName) {
        if (columnName.equals(Properties.get("user"))) {
            model.sortByUser();
        } else if (columnName.equals(Properties.get("traffic"))) {
            model.sortByTraffic();
        } else if (columnName.equals(Properties.get("spacialClarity"))) {
            model.sortByClarity();
        } else if (columnName.equals(Properties.get("illumination"))) {
            model.sortByIllumination();
        } else if (columnName.equals(Properties.get("comment"))) {
            model.sortByComment();
        } else if (columnName.equals(Properties.get("imageId"))) {
            model.sortByImage();
        } else if (columnName.equals(Properties.get("lastChange"))) {
            model.sortByLastChanged();
        } else if (columnName.equals(Properties.get("creationDate"))) {
            model.sortByCreationTime();
        }

        callback.getRatingTable().drawData(model.getRatings());
    }

    public void onTableDoubleClick(int clickCount) {
        if (clickCount >= 2) {
            onEditClick();
        }
    }

    public User getUser(String name) {
        return model.getUser(name);
    }

    public Crossing getCrossing(long node) {
        return model.getCrossing(node);
    }

    public Illumination getIllumination(int id) {
        return model.getIllumination(id);
    }

    public SpatialClarity getSpatialClarity(int id) {
        return model.getSpatialClarity(id);
    }

    public Traffic getTraffic(int id) {
        return model.getTraffic(id);
    }

    public List<User> getUsers() {
        return model.getUsers();
    }
}
