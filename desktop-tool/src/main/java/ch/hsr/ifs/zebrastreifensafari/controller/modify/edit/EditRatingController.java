package ch.hsr.ifs.zebrastreifensafari.controller.modify.edit;

import ch.hsr.ifs.zebrastreifensafari.controller.callback.modify.IEntityManipulation;
import ch.hsr.ifs.zebrastreifensafari.controller.modify.ModifyController;
import ch.hsr.ifs.zebrastreifensafari.model.Model;
import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.*;

import java.util.Date;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public class EditRatingController extends ModifyController {

    private final Rating rating;

    public EditRatingController(IEntityManipulation entityManipulation, Model model, Rating rating) {
        super(entityManipulation, model);
        this.rating = rating;
    }

    public Rating getRating() {
        return rating;
    }

    public void editRating(String selectedUser, int selectedIllumination, int selectedSpatialClarity, int selectedTraffic, String imageWeblinkText, String commentText, Date creationTime) {
        User user = model.getUser(selectedUser);
        Illumination illumination = model.getIllumination(selectedIllumination);
        SpatialClarity spatialClarity = model.getSpatialClarity(selectedSpatialClarity);
        Traffic traffic = model.getTraffic(selectedTraffic);
        String imageWeblink = imageWeblinkText.isEmpty() ? null : imageWeblinkText;
        String comment = commentText.isEmpty() ? null : commentText;
        setRatingData(user, illumination, spatialClarity, traffic, imageWeblink, comment, new Date(), creationTime);
        entityManipulation.editRating(rating);
    }

    public void setRatingData(User user, Illumination illumination, SpatialClarity spatialClarity, Traffic traffic, String imageWeblink, String comment, Date lastChanged, Date creationTime) {
        rating.setUserId(user);
        rating.setIlluminationId(illumination);
        rating.setSpatialClarityId(spatialClarity);
        rating.setTrafficId(traffic);
        rating.setImageWeblink(imageWeblink);
        rating.setComment(comment);
        rating.setLastChanged(lastChanged);
        rating.setCreationTime(creationTime);
    }
}
