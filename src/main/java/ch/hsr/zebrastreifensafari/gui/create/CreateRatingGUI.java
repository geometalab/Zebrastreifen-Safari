package main.java.ch.hsr.zebrastreifensafari.gui.create;

import ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import ch.hsr.zebrastreifensafari.gui.view.View;

import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

/**
 *
 * @author aeugster
 */
public class CreateRatingGUI extends CreateUpdateGUI {

    private long node;

    public CreateRatingGUI(Model model, View view, long node) {
        super(model, view, "Create a new rating for the zebracrossing " + node);
        this.node = node;
        jLabel1.setVisible(false);
        jLabel7.setVisible(false);
        osmNode.setVisible(false);
        imageTF.setVisible(false);
        chooseFile.setVisible(false);
    }

    @Override
    protected void onSendClick() {
        DataServiceLoader.getZebraData().addRating(
            new Rating(null,
                CommentsTA.getText(),
                model.getIllumination(getSelectedButtonInt(buttonGroup2)),
                model.getOverview(getSelectedButtonInt(buttonGroup1)),
                model.getTraffic(getSelectedButtonInt(buttonGroup3)),
                model.getUser((String) usersCB.getSelectedItem()),
                model.getZebracrossing(node)));
        observable.notifyObservers(model.getZebracrossing(node));
        this.dispose();
    }
}
