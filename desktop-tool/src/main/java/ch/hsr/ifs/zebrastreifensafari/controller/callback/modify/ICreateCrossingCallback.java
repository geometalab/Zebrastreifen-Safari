package ch.hsr.ifs.zebrastreifensafari.controller.callback.modify;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public interface ICreateCrossingCallback extends IModifyCallback {

    boolean createRating();

    void dispose();
}
