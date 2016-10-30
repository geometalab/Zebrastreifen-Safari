package ch.hsr.zebrastreifensafari.controller.modify.edit;

import ch.hsr.zebrastreifensafari.controller.MainController;
import ch.hsr.zebrastreifensafari.controller.callback.modify.IEditRatingCallback;
import ch.hsr.zebrastreifensafari.controller.callback.modify.IMainController;
import ch.hsr.zebrastreifensafari.controller.modify.ModifyController;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;

import java.util.Date;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public class EditRatingController extends ModifyController {

    private final IMainController parent;
    private final Rating rating;
    private IEditRatingCallback callback;

    public EditRatingController(IMainController parent, Model model, Rating rating) {
        super(model);
        this.parent = parent;
        this.rating = rating;
    }

    public Rating getRating() {
        return rating;
    }

    public void setCallback(IEditRatingCallback callback) {
        this.callback = callback;
    }

    public void send() {
        String comment = rating.getComment();
        Illumination illumination = rating.getIlluminationId();
        SpatialClarity spatialClarity = rating.getSpatialClarityId();
        Traffic traffic = rating.getTrafficId();
        User user = rating.getUserId();
        String imageWeblink = rating.getImageWeblink();
        Date lastChanged = rating.getLastChanged();
        Date creationTime = rating.getCreationTime();

        if (!callback.editRating()) {
            setRatingData(user, illumination, spatialClarity, traffic, imageWeblink, comment, lastChanged, creationTime);
        }
    }

    public void editRating(String selectedUser, int selectedIllumination, int selectedSpatialClarity, int selectedTraffic, String imageWeblinkText, String commentText, Date creationTime) {
        User user = model.getUser(selectedUser);
        Illumination illumination = model.getIllumination(selectedIllumination);
        SpatialClarity spatialClarity = model.getSpatialClarity(selectedSpatialClarity);
        Traffic traffic = model.getTraffic(selectedTraffic);
        String imageWeblink = imageWeblinkText.isEmpty() ? null : imageWeblinkText;
        String comment = commentText.isEmpty() ? null : commentText;
        setRatingData(user, illumination, spatialClarity, traffic, imageWeblink, comment, new Date(), creationTime);
        parent.editRating(callback.getCallback().getRatingTable(), rating);
    }

    private void setRatingData(User user, Illumination illumination, SpatialClarity spatialClarity, Traffic traffic, String imageWeblink, String comment, Date lastChanged, Date creationTime) {
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
