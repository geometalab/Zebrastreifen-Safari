package ch.hsr.zebrastreifensafari.gui.create;

import ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import ch.hsr.zebrastreifensafari.gui.view.View;
import java.io.File;
import java.util.List;

import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

/**
 *
 * @author aeugster
 */
public class CreateRatingGUI extends CreateUpdateGUI {

    private long node;

    public CreateRatingGUI(Model model, long node, View view) {
        super(model, view);
        this.node = node;
        osmNode.setText(Long.toString(node));
        osmNode.setEnabled(false);
    }

    @Override
    protected void onSendClick() {
        Zebracrossing z = model.getZebracrossing(node);

        Rating r = new Rating(null,
                CommentsTA.getText(),
                model.getIllumination(getSelectedButtonInt(buttonGroup2)),
                model.getOverview(getSelectedButtonInt(buttonGroup1)),
                model.getTraffic(getSelectedButtonInt(buttonGroup3)),
                model.getUser((String) usersCB.getSelectedItem()),
                model.getZebracrossing(z.getNode()));

        model.getZebracrossing(z.getNode()).getRatingList().add(r);
        DataServiceLoader.getZebraData().addRating(r);
        observable.notifyObservers(r);
        this.dispose();
    }
}