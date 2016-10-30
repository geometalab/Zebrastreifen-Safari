package ch.hsr.ifs.zebrastreifensafari.controller.modify.create;

import ch.hsr.ifs.zebrastreifensafari.controller.callback.modify.ICreateCrossingCallback;
import ch.hsr.ifs.zebrastreifensafari.controller.callback.modify.IMainController;
import ch.hsr.ifs.zebrastreifensafari.controller.modify.ModifyController;
import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.*;
import ch.hsr.ifs.zebrastreifensafari.model.Model;
import ch.hsr.ifs.zebrastreifensafari.service.DataServiceLoader;

import java.util.Date;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public class CreateCrossingController extends ModifyController {

    private final IMainController parent;
    private ICreateCrossingCallback callback;
    private Crossing crossing;

    public CreateCrossingController(IMainController parent, Model model) {
        super(model);
        this.parent = parent;
    }

    public void setCallback(ICreateCrossingCallback callback) {
        this.callback = callback;
    }

    public void send(String osmNodeIdText) {
        createCrossing(osmNodeIdText);

        if (callback.createRating()) {
            callback.dispose();
        }
    }

    private void createCrossing(String osmNodeIdText) {
        long osmNodeId = Long.parseLong(osmNodeIdText);
        crossing = model.getCrossing(osmNodeId);

        if (crossing == null) {
            crossing = new Crossing(null, osmNodeId, 1, 1);
        }

        parent.createCrossing(callback.getCallback().getCrossingTable(), crossing, callback.getCallback().getFilter());
    }

    public void createRating(String commentText, int selectedIllumination, int selectedSpatialClarity, int selectedTraffic, String selectedUser, String imageWeblinkText, Date creationTime) {
        String comment = commentText.isEmpty() ? null : commentText;
        Illumination illumination = model.getIllumination(selectedIllumination);
        SpatialClarity spatialClarity = model.getSpatialClarity(selectedSpatialClarity);
        Traffic traffic = model.getTraffic(selectedTraffic);
        User user = model.getUser(selectedUser);
        String imageWeblink = imageWeblinkText.isEmpty() ? null : imageWeblinkText;
        Rating rating = new Rating(null, comment, illumination, spatialClarity, traffic, user, crossing, imageWeblink, new Date(), creationTime);
        DataServiceLoader.getCrossingData().createRating(rating);
    }

    public void deleteCrossing() {
        parent.deleteCrossing(callback.getCallback().getCrossingTable());
    }
}
