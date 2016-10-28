package ch.hsr.zebrastreifensafari.controller;

import ch.hsr.zebrastreifensafari.controller.callback.IMainCallback;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import ch.hsr.zebrastreifensafari.service.Properties;
import ch.hsr.zebrastreifensafari.service.WebsiteService;
import org.eclipse.persistence.exceptions.DatabaseException;

import javax.persistence.EntityNotFoundException;
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

    public void onCrossingSelection() {
        if (callback.getCrossingTable().hasData()) {
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

    //<editor-fold desc="CRUD Crossing">
    public void createCrossing(Crossing crossing, String searchText) {
        if (model.contains(crossing)) {
            createExistingCrossing(crossing, searchText);
        } else {
            createNewCrossing(crossing, searchText);
        }
    }

    private void createExistingCrossing(Crossing crossing, String searchText) {
        crossing.increaseRatingAmount();

        if (searchText.isEmpty()) {
            callback.getCrossingTable().changeSelection(model.indexOf(crossing));
            callback.getCrossingTable().setRatingAmountAtSelectedRow(crossing.getRatingAmount());
        } else if (Long.toString(crossing.getOsmNodeId()).startsWith(searchText)) {
            for (int i = 0; i < callback.getCrossingTable().getRowCount(); i++) {
                if (callback.getCrossingTable().getOsmNodeIdAt(i) == crossing.getOsmNodeId()) {
                    callback.getCrossingTable().changeSelection(i);
                    callback.getCrossingTable().setRatingAmountAtSelectedRow(crossing.getRatingAmount());
                    break;
                }
            }
        }
    }

    private void createNewCrossing(Crossing crossing, String searchText) {
        DataServiceLoader.getCrossingData().createCrossing(crossing);
        model.add(crossing);
        callback.getCrossingTable().add(crossing);

        if (Long.toString(crossing.getOsmNodeId()).startsWith(searchText) || searchText.isEmpty()) {
            callback.getCrossingTable().changeSelection(callback.getCrossingTable().getRowCount() - 1);
        } else {
            callback.getCrossingTable().removeRow(callback.getCrossingTable().getRowCount() - 1);
        }
    }

    public void editCrossing(Crossing crossing, String searchText) throws EntityNotFoundException {
        DataServiceLoader.getCrossingData().editCrossing(crossing);

        if (searchText.isEmpty() || Long.toString(crossing.getOsmNodeId()).startsWith(searchText)) {
            callback.getCrossingTable().setOsmNodeIdAtSelectedRow(crossing.getOsmNodeId());
            onCrossingSelection();
        } else {
            callback.getCrossingTable().removeRow(callback.getCrossingTable().getSelectedRow());
        }
    }

    public void removeCrossing() {
        try {
            int selectedRow = callback.getCrossingTable().getSelectedRow();
            Crossing crossing = getCrossingFromTable();
            DataServiceLoader.getCrossingData().removeCrossing(crossing.getId());
            callback.getCrossingTable().remove(model.indexOf(crossing));
            model.remove(crossing);

            if (callback.getCrossingTable().getRowCount() == selectedRow) {
                selectedRow--;
            }

            callback.getCrossingTable().changeSelection(selectedRow);
        } catch (EntityNotFoundException enfex) {
            callback.errorMessage(Properties.get("crossingExistError"));
        }
    }
    //</editor-fold>

    //<editor-fold desc="CRUD Rating">
    public void createRating(Rating rating) throws EntityNotFoundException {
        DataServiceLoader.getCrossingData().createRating(rating);
        model.add(rating);
        callback.getRatingTable().add(rating);
        callback.getRatingTable().changeSelection(callback.getRatingTable().getRowCount() - 1);
        Crossing crossingOfRating = model.getCrossing(rating.getCrossingId().getId());
        crossingOfRating.increaseRatingAmount();
        callback.getCrossingTable().setRatingAmountAtSelectedRow(crossingOfRating.getRatingAmount());
    }

    public void editRating(Rating rating) throws EntityNotFoundException {
        DataServiceLoader.getCrossingData().editRating(rating);
        callback.getRatingTable().setUserIdAtSelectedRow(rating.getUserId().getName());
        callback.getRatingTable().setTrafficIdAtSelectedRow(rating.getTrafficId().getValue());
        callback.getRatingTable().setSpatialClarityIdAtSelectedRow(rating.getSpatialClarityId().getValue());
        callback.getRatingTable().setIlluminationIdAtSelectedRow(rating.getIlluminationId().getValue());
        callback.getRatingTable().setCommentAtSelectedRow(rating.getComment());
        callback.getRatingTable().setImageWeblinkAtSelectedRow(rating.getImageWeblink());
        callback.getRatingTable().setLastChangedAtSelectedRow(rating.getLastChanged());
        callback.getRatingTable().setCreationTimeAtSelectedRow(rating.getCreationTime());
    }

    public void removeRating() {
        try {
            int selectedRow = callback.getRatingTable().getSelectedRow();
            Rating rating = getRatingFromTable();
            DataServiceLoader.getCrossingData().removeRating(rating.getId());
            callback.getRatingTable().remove(model.indexOf(rating));
            model.remove(rating);

            if (model.getRatings().isEmpty()) {
                removeCrossing();
                callback.setSelectedTabbedPaneIndex(0);
            } else {
                if (callback.getRatingTable().getRowCount() == selectedRow) {
                    selectedRow--;
                }

                callback.getRatingTable().changeSelection(selectedRow);
                Crossing crossingOfRating = model.getCrossing(rating.getCrossingId().getId());
                crossingOfRating.decreaseRatingAmount();
                callback.getCrossingTable().setRatingAmountAtSelectedRow(crossingOfRating.getRatingAmount());
            }
        } catch (EntityNotFoundException enfex) {
            callback.errorMessage(Properties.get("ratingCrossingExistError"));
        }
    }
    //</editor-fold>

    private Crossing getCrossingFromTable() {
        return model.getCrossing(callback.getCrossingTable().getSelectedId());
    }

    private Rating getRatingFromTable() {
        return model.getRating(callback.getRatingTable().getSelectedId());
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
