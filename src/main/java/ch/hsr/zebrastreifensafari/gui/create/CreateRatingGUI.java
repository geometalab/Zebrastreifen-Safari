package ch.hsr.zebrastreifensafari.gui.create;

import ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import ch.hsr.zebrastreifensafari.gui.view.View;

import ch.hsr.zebrastreifensafari.jpa.entities.Rating;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

import java.util.Date;

/**
 *
 * @author aeugster
 */
public class CreateRatingGUI extends CreateUpdateGUI {

    private long node;

    public CreateRatingGUI(Model model, View view, long node) {
        super(model, view, "Create a new rating for the Crossing " + node);
        this.node = node;
        jLabel1.setVisible(false);
        jLabel7.setVisible(false);
        osmNode.setVisible(false);
    }

    @Override
    protected void onSendClick() {
        DataServiceLoader.getCrossingData().addRating(
            new Rating(null,
                CommentsTA.getText(),
                model.getIllumination(getSelectedButtonInt(buttonGroup2)),
                model.getSpatialClarity(getSelectedButtonInt(buttonGroup1)),
                model.getTraffic(getSelectedButtonInt(buttonGroup3)),
                model.getUser((String) usersCB.getSelectedItem()),
                model.getCrossing(node),
                imageTF.getText(),
                new Date()
            )
        );

        observable.notifyObservers(model.getCrossing(node));
        this.dispose();
    }
}
