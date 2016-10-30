package ch.hsr.ifs.zebrastreifensafari.controller;

import ch.hsr.ifs.zebrastreifensafari.controller.callback.modify.IEntityManipulation;
import ch.hsr.ifs.zebrastreifensafari.controller.callback.table.ICrossingTable;
import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.*;
import ch.hsr.ifs.zebrastreifensafari.model.sort.crossing.CrossingSorter;
import ch.hsr.ifs.zebrastreifensafari.controller.callback.IMainCallback;
import ch.hsr.ifs.zebrastreifensafari.controller.callback.table.IRatingTable;
import ch.hsr.ifs.zebrastreifensafari.controller.modify.create.CreateCrossingController;
import ch.hsr.ifs.zebrastreifensafari.controller.modify.create.CreateRatingController;
import ch.hsr.ifs.zebrastreifensafari.controller.modify.edit.EditCrossingController;
import ch.hsr.ifs.zebrastreifensafari.controller.modify.edit.EditRatingController;
import ch.hsr.ifs.zebrastreifensafari.model.Model;
import ch.hsr.ifs.zebrastreifensafari.model.sort.rating.RatingSorter;
import ch.hsr.ifs.zebrastreifensafari.service.DataServiceLoader;
import ch.hsr.ifs.zebrastreifensafari.service.Properties;
import ch.hsr.ifs.zebrastreifensafari.service.WebsiteService;
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
public class MainController implements IEntityManipulation {

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

    public void drawCrossings() {
        callback.getCrossingTable().drawData(model.getCrossings());
    }

    public void updateRatingTabTitle() {
        ICrossingTable crossingTable = callback.getCrossingTable();

        if (crossingTable.hasData()) {
            callback.setRatingTabbedPaneTitle(Properties.get("specificRatingTabbedPaneTitle") + crossingTable.getOsmNodeIdAtSelectedRow());
        }
    }

    public void filter() {
        String filter = callback.getFilter();

        if (filter.isEmpty()) {
            drawCrossings();
        } else {
            callback.getCrossingTable().drawData(model.getCrossings().stream()
                    .filter(crossing -> String.valueOf(crossing.getOsmNodeId()).startsWith(filter))
                    .collect(Collectors.toList()));
        }
    }

    public void addCrossingDialog() {
        callback.showCreateCrossing(new CreateCrossingController(this, model));
    }

    public void addRatingDialog() {
        callback.showCreateRating(new CreateRatingController(this, model, getCrossingFromTable(callback.getCrossingTable()).getOsmNodeId()));
    }

    public void editCrossingDialog() throws ArrayIndexOutOfBoundsException {
        callback.showEditCrossing(new EditCrossingController(this, model, getCrossingFromTable(callback.getCrossingTable())));
    }

    public void editRatingDialog() throws ArrayIndexOutOfBoundsException {
        callback.showEditRating(new EditRatingController(this, model, getRatingFromTable(callback.getRatingTable())));
    }

    public void openAboutDialog() {
        callback.showAbout(new AboutController());
    }

    public void previousCrossing() {
        ICrossingTable crossingTable = callback.getCrossingTable();

        if (crossingTable.getSelectedRow() != 0) {
            crossingTable.changeSelection(crossingTable.getSelectedRow() - 1);
        } else {
            crossingTable.changeSelection(crossingTable.getRowCount() - 1);
        }
    }

    public void nextCrossing() {
        ICrossingTable crossingTable = callback.getCrossingTable();

        if (crossingTable.getSelectedRow() != crossingTable.getRowCount() - 1) {
            crossingTable.changeSelection(crossingTable.getSelectedRow() + 1);
        } else {
            crossingTable.changeSelection(0);
        }
    }

    public void loadCrossings() throws ArrayIndexOutOfBoundsException, PersistenceException {
        model.loadCrossing();
        drawCrossings();
    }

    public void loadRatings() throws ArrayIndexOutOfBoundsException, PersistenceException {
        model.loadRating(getCrossingFromTable(callback.getCrossingTable()));
        callback.getRatingTable().drawData(model.getRatings());
    }

    public void loadUsers() throws ArrayIndexOutOfBoundsException, PersistenceException {
        model.loadUsers();
    }

    public void help() {
        WebsiteService.openWebsite(Properties.get("helpLink"));
    }

    public void sortFilteredCrossing(String columnName) {
        sortCrossing(columnName);
        filter();
    }

    public void sortCrossing(String columnName) {
        if (columnName.equals(Properties.get("osmNodeId"))) {
            CrossingSorter.sortByNode(model.getCrossings());
        } else if (columnName.equals(Properties.get("ratingAmount"))) {
            CrossingSorter.sortByNumberOfRatings(model.getCrossings());
        } else if (columnName.equals(Properties.get("status"))) {
            CrossingSorter.sortByStatus(model.getCrossings());
        }

        drawCrossings();
    }

    public void sortRating(String columnName) {
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

        callback.getRatingTable().drawData(model.getRatings());
    }

    //<editor-fold desc="CRUD Crossing">
    @Override
    public void createCrossing(Crossing crossing) {
        ICrossingTable crossingTable = callback.getCrossingTable();
        String filter = callback.getFilter();

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
    public void editCrossing(Crossing crossing) throws EntityNotFoundException {
        ICrossingTable crossingTable = callback.getCrossingTable();
        String filter = callback.getFilter();
        DataServiceLoader.getCrossingData().editCrossing(crossing);

        if (filter.isEmpty() || Long.toString(crossing.getOsmNodeId()).startsWith(filter)) {
            crossingTable.setOsmNodeIdAtSelectedRow(crossing.getOsmNodeId());
            updateRatingTabTitle();
        } else {
            crossingTable.removeRow(crossingTable.getSelectedRow());
        }
    }

    @Override
    public void deleteCrossing() throws ArrayIndexOutOfBoundsException, DatabaseException, EntityNotFoundException {
        ICrossingTable crossingTable = callback.getCrossingTable();
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
    public void createRating(Rating rating) throws EntityNotFoundException {
        ICrossingTable crossingTable = callback.getCrossingTable();
        IRatingTable ratingTable = callback.getRatingTable();
        DataServiceLoader.getCrossingData().createRating(rating);
        model.add(rating);
        ratingTable.add(rating);
        ratingTable.changeSelection(ratingTable.getRowCount() - 1);
        Crossing crossingOfRating = model.getCrossing(rating.getCrossingId().getId());
        crossingOfRating.increaseRatingAmount();
        crossingTable.setRatingAmountAtSelectedRow(crossingOfRating.getRatingAmount());
    }

    @Override
    public void editRating(Rating rating) throws EntityNotFoundException {
        IRatingTable ratingTable = callback.getRatingTable();
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

    public void deleteRating() throws ArrayIndexOutOfBoundsException, DatabaseException, EntityNotFoundException {
        IRatingTable ratingTable = callback.getRatingTable();
        Rating rating = getRatingFromTable(ratingTable);
        DataServiceLoader.getCrossingData().removeRating(rating.getId());
        ratingTable.remove(model.indexOf(rating));
        model.remove(rating);
    }

    public void updateRatingAmount(int selectedRow) throws EntityNotFoundException {
        ICrossingTable crossingTable = callback.getCrossingTable();
        IRatingTable ratingTable = callback.getRatingTable();

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
