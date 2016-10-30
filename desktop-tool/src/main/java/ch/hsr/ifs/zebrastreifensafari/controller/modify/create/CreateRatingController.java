package ch.hsr.ifs.zebrastreifensafari.controller.modify.create;

import ch.hsr.ifs.zebrastreifensafari.controller.callback.IEntityManipulation;
import ch.hsr.ifs.zebrastreifensafari.controller.modify.ModifyController;
import ch.hsr.ifs.zebrastreifensafari.model.Model;
import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.*;

import java.util.Date;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public class CreateRatingController extends ModifyController {

    private final long node;

    public CreateRatingController(IEntityManipulation entityManipulation, Model model, long node) {
        super(entityManipulation, model);
        this.node = node;
    }

    public long getNode() {
        return node;
    }

    public void createRating(String commentText, int selectedIllumination, int selectedSpatialClarity, int selectedTraffic, String selectedUser, String imageWeblinkText, Date creationTime) {
        String comment = commentText.isEmpty() ? null : commentText;
        Illumination illumination = model.getIllumination(selectedIllumination);
        SpatialClarity spatialClarity = model.getSpatialClarity(selectedSpatialClarity);
        Traffic traffic = model.getTraffic(selectedTraffic);
        User user = model.getUser(selectedUser);
        Crossing crossing = model.getCrossing(node);
        String imageWeblink = imageWeblinkText.isEmpty() ? null : imageWeblinkText;
        Rating rating = new Rating(null, comment, illumination, spatialClarity, traffic, user, crossing, imageWeblink, new Date(), creationTime);
        entityManipulation.createRating(rating);
    }
}
