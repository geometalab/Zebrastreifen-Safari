package ch.hsr.zebrastreifensafari.controller;

import ch.hsr.zebrastreifensafari.controller.callback.IMainCallback;
import ch.hsr.zebrastreifensafari.controller.callback.modify.IMainController;
import ch.hsr.zebrastreifensafari.controller.callback.table.ICrossingTable;
import ch.hsr.zebrastreifensafari.controller.callback.table.IRatingTable;
import ch.hsr.zebrastreifensafari.controller.modify.create.CreateCrossingController;
import ch.hsr.zebrastreifensafari.controller.modify.create.CreateRatingController;
import ch.hsr.zebrastreifensafari.controller.modify.edit.EditCrossingController;
import ch.hsr.zebrastreifensafari.controller.modify.edit.EditRatingController;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.model.sort.crossing.CrossingSorter;
import ch.hsr.zebrastreifensafari.model.sort.rating.RatingSorter;
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
public class MainController implements IMainController {

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

    public void updateRatingTabTitle(ICrossingTable crossingTable) {
        if (crossingTable.hasData()) {
            callback.setRatingTabbedPaneTitle(Properties.get("specificRatingTabbedPaneTitle") + crossingTable.getOsmNodeIdAtSelectedRow());
        }
    }

    public void filter(ICrossingTable crossingTable, String filter) {
        if (filter.isEmpty()) {
            drawCrossings(crossingTable);
        } else {
            crossingTable.drawData(model.getCrossings().stream()
                    .filter(crossing -> String.valueOf(crossing.getOsmNodeId()).startsWith(filter))
                    .collect(Collectors.toList()));
        }
    }

    public void addCrossingDialog() {
        callback.showCreateCrossing(new CreateCrossingController(this, model));
    }

    public void addRatingDialog(ICrossingTable crossingTable) {
        callback.showCreateRating(new CreateRatingController(this, model, getCrossingFromTable(crossingTable).getOsmNodeId()));
    }

    public void editCrossingDialog(ICrossingTable crossingTable) throws ArrayIndexOutOfBoundsException {
        callback.showEditCrossing(new EditCrossingController(this, model, getCrossingFromTable(crossingTable)));
    }

    public void editRatingDialog(IRatingTable ratingTable) throws ArrayIndexOutOfBoundsException {
        callback.showEditRating(new EditRatingController(this, model, getRatingFromTable(ratingTable)));
    }

    public void openAboutDialog() {
        callback.showAbout(new AboutController());
    }

    public void previousCrossing(ICrossingTable crossingTable) {
        if (crossingTable.getSelectedRow() != 0) {
            crossingTable.changeSelection(crossingTable.getSelectedRow() - 1);
        } else {
            crossingTable.changeSelection(crossingTable.getRowCount() - 1);
        }
    }

    public void nextCrossing(ICrossingTable crossingTable) {
        if (crossingTable.getSelectedRow() != crossingTable.getRowCount() - 1) {
            crossingTable.changeSelection(crossingTable.getSelectedRow() + 1);
        } else {
            crossingTable.changeSelection(0);
        }
    }

    public void loadCrossings(ICrossingTable crossingTable) throws ArrayIndexOutOfBoundsException, PersistenceException {
        model.loadCrossing();
        drawCrossings(crossingTable);
    }

    public void loadRatings(ICrossingTable crossingTable, IRatingTable ratingTable) throws ArrayIndexOutOfBoundsException, PersistenceException {
        model.loadRating(getCrossingFromTable(crossingTable));
        ratingTable.drawData(model.getRatings());
    }

    public void loadUsers() throws ArrayIndexOutOfBoundsException, PersistenceException {
        model.loadUsers();
    }

    public void help() {
        WebsiteService.openWebsite(Properties.get("helpLink"));
    }

    public void sortFilteredCrossing(ICrossingTable crossingTable, String columnName, String filter) {
        sortCrossing(crossingTable, columnName);
        filter(crossingTable, filter);
    }

    public void sortCrossing(ICrossingTable crossingTable, String columnName) {
        if (columnName.equals(Properties.get("osmNodeId"))) {
            CrossingSorter.sortByNode(model.getCrossings());
        } else if (columnName.equals(Properties.get("ratingAmount"))) {
            CrossingSorter.sortByNumberOfRatings(model.getCrossings());
        } else if (columnName.equals(Properties.get("status"))) {
            CrossingSorter.sortByStatus(model.getCrossings());
        }

        drawCrossings(crossingTable);
    }

    public void sortRating(IRatingTable ratingTable, String columnName) {
        if (columnName.equals(Properties.get("user"))) {
            RatingSorter.sortByUser(model.getRatings());
        } else if (columnName.equals(Properties.get("traffic"))) {
            RatingSorter.sortByTraffic(model.getRatings());
        } else if (columnName.equals(Properties.get("spacialClarity"))) {
            RatingSorter.sortByClarity(model.getRatings());
        } else if (columnName.equals(Properties.get("illumination"))) {
            RatingSorter.sortByIllumination(model.getRatings());
        } else if (columnName.equals(Properties.get("comment"))) {
            RatingSorter.sortByComment(model.getRatings());
        } else if (columnName.equals(Properties.get("imageId"))) {
            RatingSorter.sortByImage(model.getRatings());
        } else if (columnName.equals(Properties.get("lastChange"))) {
            RatingSorter.sortByLastChanged(model.getRatings());
        } else if (columnName.equals(Properties.get("creationDate"))) {
            RatingSorter.sortByCreationTime(model.getRatings());
        }

        ratingTable.drawData(model.getRatings());
    }

    //<editor-fold desc="CRUD Crossing">
    @Override
    public void createCrossing(ICrossingTable crossingTable, Crossing crossing, String filter) {
        if (model.contains(crossing)) {
            createExistingCrossing(crossingTable, crossing, filter);
            return;
        }

        DataServiceLoader.getCrossingData().createCrossing(crossing);
        model.add(crossing);
        crossingTable.add(crossing);

        if (Long.toString(crossing.getOsmNodeId()).startsWith(filter) || filter.isEmpty()) {
            crossingTable.changeSelection(crossingTable.getRowCount() - 1);
        } else {
            crossingTable.removeRow(crossingTable.getRowCount() - 1);
        }
    }

    private void createExistingCrossing(ICrossingTable crossingTable, Crossing crossing, String filter) {
        crossing.increaseRatingAmount();

        if (filter.isEmpty()) {
            crossingTable.changeSelection(model.indexOf(crossing));
            crossingTable.setRatingAmountAtSelectedRow(crossing.getRatingAmount());
        } else if (Long.toString(crossing.getOsmNodeId()).startsWith(filter)) {
            for (int i = 0; i < crossingTable.getRowCount(); i++) {
                if (crossingTable.getOsmNodeIdAt(i) == crossing.getOsmNodeId()) {
                    crossingTable.changeSelection(i);
                    crossingTable.setRatingAmountAtSelectedRow(crossing.getRatingAmount());
                    break;
                }
            }
        }
    }

    @Override
    public void editCrossing(ICrossingTable crossingTable, Crossing crossing, String filter) throws EntityNotFoundException {
        DataServiceLoader.getCrossingData().editCrossing(crossing);

        if (filter.isEmpty() || Long.toString(crossing.getOsmNodeId()).startsWith(filter)) {
            crossingTable.setOsmNodeIdAtSelectedRow(crossing.getOsmNodeId());
            updateRatingTabTitle(crossingTable);
        } else {
            crossingTable.removeRow(crossingTable.getSelectedRow());
        }
    }

    @Override
    public void deleteCrossing(ICrossingTable crossingTable) throws ArrayIndexOutOfBoundsException, DatabaseException, EntityNotFoundException {
        int selectedRow = crossingTable.getSelectedRow();
        Crossing crossing = getCrossingFromTable(crossingTable);
        DataServiceLoader.getCrossingData().removeCrossing(crossing.getId());
        crossingTable.remove(model.indexOf(crossing));
        model.remove(crossing);

        if (crossingTable.getRowCount() == selectedRow) {
            selectedRow--;
        }

        crossingTable.changeSelection(selectedRow);
    }
    //</editor-fold>

    //<editor-fold desc="CRUD Rating">
    @Override
    public void createRating(ICrossingTable crossingTable, IRatingTable ratingTable, Rating rating) throws EntityNotFoundException {
        DataServiceLoader.getCrossingData().createRating(rating);
        model.add(rating);
        ratingTable.add(rating);
        ratingTable.changeSelection(ratingTable.getRowCount() - 1);
        Crossing crossingOfRating = model.getCrossing(rating.getCrossingId().getId());
        crossingOfRating.increaseRatingAmount();
        crossingTable.setRatingAmountAtSelectedRow(crossingOfRating.getRatingAmount());
    }

    @Override
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

    public void deleteRating(IRatingTable ratingTable) throws ArrayIndexOutOfBoundsException, DatabaseException, EntityNotFoundException {
        Rating rating = getRatingFromTable(ratingTable);
        DataServiceLoader.getCrossingData().removeRating(rating.getId());
        ratingTable.remove(model.indexOf(rating));
        model.remove(rating);
    }

    public void updateRatingAmount(ICrossingTable crossingTable, IRatingTable ratingTable, int selectedRow) throws EntityNotFoundException {
        if (ratingTable.getRowCount() == selectedRow) {
            selectedRow--;
        }

        ratingTable.changeSelection(selectedRow);
        Crossing crossing = getCrossingFromTable(crossingTable);
        crossing.decreaseRatingAmount();
        crossingTable.setRatingAmountAtSelectedRow(crossing.getRatingAmount());
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
