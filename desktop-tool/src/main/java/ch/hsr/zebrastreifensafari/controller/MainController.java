package ch.hsr.zebrastreifensafari.controller;

import ch.hsr.zebrastreifensafari.controller.callback.IMainCallback;
import ch.hsr.zebrastreifensafari.controller.callback.table.ICrossingTable;
import ch.hsr.zebrastreifensafari.controller.callback.table.IRatingTable;
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

    public void setCallback(IMainCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback can't be null");
        }

        this.callback = callback;
    }

    public void drawCrossings(ICrossingTable crossingTable) {
        crossingTable.drawData(model.getCrossings());
    }

    public void changeTabbedPane(ICrossingTable crossingTable, IRatingTable ratingTable) {
        try {
            if (callback.isRatingMode()) {
                model.loadRating(getCrossingFromTable(crossingTable));
                ratingTable.drawData(model.getRatings());
            }
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            callback.setSelectedTabbedPaneIndex(0);
            callback.errorMessage(Properties.get("changeSelectionError"));
        } catch (PersistenceException pex) {
            callback.setSelectedTabbedPaneIndex(0);
            callback.errorMessage(Properties.get("connectionError"));
        }
    }

    public void updateRatingTabTitle(ICrossingTable crossingTable) {
        if (crossingTable.hasData()) {
            callback.setRatingTabbedPaneTitle(Properties.get("specificRatingTabbedPaneTitle") + crossingTable.getOsmNodeIdAtSelectedRow());
        }
    }

    public void search(ICrossingTable crossingTable, String searchText) {
        if (searchText.isEmpty()) {
            drawCrossings(crossingTable);
        } else {
            crossingTable.drawData(model.getCrossings().stream()
                    .filter(crossing -> String.valueOf(crossing.getOsmNodeId()).startsWith(searchText))
                    .collect(Collectors.toList()));
        }
    }

    public void add(ICrossingTable crossingTable) {
        if (callback.isRatingMode()) {
            callback.showCreateRating(getCrossingFromTable(crossingTable).getOsmNodeId());
        } else {
            callback.showCreateCrossing();
        }
    }

    public void edit(ICrossingTable crossingTable, IRatingTable ratingTable) {
        try {
            if (callback.isRatingMode()) {
                callback.showEditRating(getRatingFromTable(ratingTable));
            } else {
                callback.showEditCrossing(getCrossingFromTable(crossingTable));
            }
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            callback.errorMessage(Properties.get("editSelectionError"));
        }
    }

    public void doubleClickEdit(ICrossingTable crossingTable, IRatingTable ratingTable, int clickCount) {
        if (clickCount >= 2) {
            edit(crossingTable, ratingTable);
        }
    }

    public void openAboutDialog() {
        callback.showAbout(new AboutController());
    }

    public void delete(ICrossingTable crossingTable, IRatingTable ratingTable) {
        try {
            if (callback.isRatingMode()) {
                deleteRating(crossingTable, ratingTable);
            } else {
                deleteCrossing(crossingTable);
            }
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            callback.errorMessage(Properties.get("deleteSelectionError"));
        } catch (DatabaseException dbex) {
            callback.errorMessage(Properties.get("connectionError"));
        }
    }

    public void previousCrossing(ICrossingTable crossingTable, IRatingTable ratingTable) {
        if (crossingTable.getSelectedRow() != 0) {
            crossingTable.changeSelection(crossingTable.getSelectedRow() - 1);
        } else {
            crossingTable.changeSelection(crossingTable.getRowCount() - 1);
        }

        changeTabbedPane(crossingTable, ratingTable);
    }

    public void nextCrossing(ICrossingTable crossingTable, IRatingTable ratingTable) {
        if (crossingTable.getSelectedRow() != crossingTable.getRowCount() - 1) {
            crossingTable.changeSelection(crossingTable.getSelectedRow() + 1);
        } else {
            crossingTable.changeSelection(0);
        }

        changeTabbedPane(crossingTable, ratingTable);
    }

    public void refresh(ICrossingTable crossingTable, IRatingTable ratingTable) {
        try {
            model.loadUsers();

            if (callback.isRatingMode()) {
                model.loadRating(getCrossingFromTable(crossingTable));
                ratingTable.drawData(model.getRatings());
            } else {
                model.loadCrossing();
                drawCrossings(crossingTable);
            }
        } catch (PersistenceException pex) {
            callback.errorMessage(Properties.get("connectionError"));
        }
    }

    public void help() {
        WebsiteService.openWebsite(Properties.get("helpLink"));
    }

    public void sortCrossing(ICrossingTable crossingTable, String columnName) {
        if (columnName.equals(Properties.get("osmNodeId"))) {
            model.sortByNode();
        } else if (columnName.equals(Properties.get("ratingAmount"))) {
            model.sortByNumberOfRatings();
        } else if (columnName.equals(Properties.get("status"))) {
            model.sortByStatus();
        }

        drawCrossings(crossingTable);
    }

    public void sortRating(IRatingTable ratingTable, String columnName) {
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

        ratingTable.drawData(model.getRatings());
    }

    //<editor-fold desc="CRUD Crossing">
    public void createCrossing(ICrossingTable crossingTable, Crossing crossing, String searchText) {
        if (model.contains(crossing)) {
            createExistingCrossing(crossingTable, crossing, searchText);
            return;
        }

        DataServiceLoader.getCrossingData().createCrossing(crossing);
        model.add(crossing);
        crossingTable.add(crossing);

        if (Long.toString(crossing.getOsmNodeId()).startsWith(searchText) || searchText.isEmpty()) {
            crossingTable.changeSelection(crossingTable.getRowCount() - 1);
        } else {
            crossingTable.removeRow(crossingTable.getRowCount() - 1);
        }
    }

    private void createExistingCrossing(ICrossingTable crossingTable, Crossing crossing, String searchText) {
        crossing.increaseRatingAmount();

        if (searchText.isEmpty()) {
            crossingTable.changeSelection(model.indexOf(crossing));
            crossingTable.setRatingAmountAtSelectedRow(crossing.getRatingAmount());
        } else if (Long.toString(crossing.getOsmNodeId()).startsWith(searchText)) {
            for (int i = 0; i < crossingTable.getRowCount(); i++) {
                if (crossingTable.getOsmNodeIdAt(i) == crossing.getOsmNodeId()) {
                    crossingTable.changeSelection(i);
                    crossingTable.setRatingAmountAtSelectedRow(crossing.getRatingAmount());
                    break;
                }
            }
        }
    }

    public void editCrossing(ICrossingTable crossingTable, Crossing crossing, String searchText) throws EntityNotFoundException {
        DataServiceLoader.getCrossingData().editCrossing(crossing);

        if (searchText.isEmpty() || Long.toString(crossing.getOsmNodeId()).startsWith(searchText)) {
            crossingTable.setOsmNodeIdAtSelectedRow(crossing.getOsmNodeId());
            updateRatingTabTitle(crossingTable);
        } else {
            crossingTable.removeRow(crossingTable.getSelectedRow());
        }
    }

    public void deleteCrossing(ICrossingTable crossingTable) {
        try {
            int selectedRow = crossingTable.getSelectedRow();
            Crossing crossing = getCrossingFromTable(crossingTable);
            DataServiceLoader.getCrossingData().removeCrossing(crossing.getId());
            crossingTable.remove(model.indexOf(crossing));
            model.remove(crossing);

            if (crossingTable.getRowCount() == selectedRow) {
                selectedRow--;
            }

            crossingTable.changeSelection(selectedRow);
        } catch (EntityNotFoundException enfex) {
            callback.errorMessage(Properties.get("crossingExistError"));
        }
    }

    //</editor-fold>

    //<editor-fold desc="CRUD Rating">
    public void createRating(ICrossingTable crossingTable, IRatingTable ratingTable, Rating rating) throws EntityNotFoundException {
        DataServiceLoader.getCrossingData().createRating(rating);
        model.add(rating);
        ratingTable.add(rating);
        ratingTable.changeSelection(ratingTable.getRowCount() - 1);
        Crossing crossingOfRating = model.getCrossing(rating.getCrossingId().getId());
        crossingOfRating.increaseRatingAmount();
        crossingTable.setRatingAmountAtSelectedRow(crossingOfRating.getRatingAmount());
    }

    public void editRating(IRatingTable ratingTable, Rating rating) throws EntityNotFoundException {
        DataServiceLoader.getCrossingData().editRating(rating);
        ratingTable.setUserIdAtSelectedRow(rating.getUserId().getName());
        ratingTable.setTrafficIdAtSelectedRow(rating.getTrafficId().getValue());
        ratingTable.setSpatialClarityIdAtSelectedRow(rating.getSpatialClarityId().getValue());
        ratingTable.setIlluminationIdAtSelectedRow(rating.getIlluminationId().getValue());
        ratingTable.setCommentAtSelectedRow(rating.getComment());
        ratingTable.setImageWeblinkAtSelectedRow(rating.getImageWeblink());
        ratingTable.setLastChangedAtSelectedRow(rating.getLastChanged());
        ratingTable.setCreationTimeAtSelectedRow(rating.getCreationTime());
    }

    public void deleteRating(ICrossingTable crossingTable, IRatingTable ratingTable) {
        try {
            int selectedRow = ratingTable.getSelectedRow();
            Rating rating = getRatingFromTable(ratingTable);
            DataServiceLoader.getCrossingData().removeRating(rating.getId());
            ratingTable.remove(model.indexOf(rating));
            model.remove(rating);

            if (model.getRatings().isEmpty()) {
                deleteCrossing(crossingTable);
                callback.setSelectedTabbedPaneIndex(0);
            } else {
                if (ratingTable.getRowCount() == selectedRow) {
                    selectedRow--;
                }

                ratingTable.changeSelection(selectedRow);
                Crossing crossingOfRating = model.getCrossing(rating.getCrossingId().getId());
                crossingOfRating.decreaseRatingAmount();
                crossingTable.setRatingAmountAtSelectedRow(crossingOfRating.getRatingAmount());
            }
        } catch (EntityNotFoundException enfex) {
            callback.errorMessage(Properties.get("ratingCrossingExistError"));
        }
    }
    //</editor-fold>

    private Crossing getCrossingFromTable(ICrossingTable crossingTable) {
        return model.getCrossing(crossingTable.getSelectedId());
    }

    private Rating getRatingFromTable(IRatingTable ratingTable) {
        return model.getRating(ratingTable.getSelectedId());
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
