package ch.hsr.zebrastreifensafari.controller.callback.table;

import ch.hsr.zebrastreifensafari.jpa.entities.Rating;

import java.util.Date;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public interface IRatingTable extends ITable<Rating> {

    void setUserIdAtSelectedRow(String username);

    void setTrafficIdAtSelectedRow(String traffic);

    void setSpatialClarityIdAtSelectedRow(String spatialClarity);

    void setIlluminationIdAtSelectedRow(String illumination);

    void setCommentAtSelectedRow(String comment);

    void setImageWeblinkAtSelectedRow(String imageWeblink);

    void setLastChangedAtSelectedRow(Date lastChanged);

    void setCreationTimeAtSelectedRow(Date creationTime);
}
