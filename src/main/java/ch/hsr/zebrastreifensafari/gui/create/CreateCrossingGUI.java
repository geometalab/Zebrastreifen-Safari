package ch.hsr.zebrastreifensafari.gui.create;

import ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import ch.hsr.zebrastreifensafari.gui.view.View;

import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

import javax.swing.*;
import java.util.Date;

/**
 *
 * @author aeugster
 */
public class CreateCrossingGUI extends CreateUpdateGUI {

    public CreateCrossingGUI(Model model, View view) {
        super(model, view, "Create a new Crossing");
    }

    @Override
    protected void onSendClick() {
        try {
            long osmNode = Long.parseLong(this.osmNode.getText());

            if (model.getCrossing(osmNode) == null) {
                DataServiceLoader.getCrossingData().addCrossing(new Crossing(null, osmNode, 1));
                observable.notifyObservers(null);
            }

            DataServiceLoader.getCrossingData().addRating(
                    new Rating(null,
                            CommentsTA.getText(),
                            model.getIllumination(getSelectedButtonInt(buttonGroup2)),
                            model.getSpatialClarity(getSelectedButtonInt(buttonGroup1)),
                            model.getTraffic(getSelectedButtonInt(buttonGroup3)),
                            model.getUser((String) usersCB.getSelectedItem()),
                            model.getCrossing(osmNode),
                            imageTF.getText(),
                            new Date()
                    )
            );

            this.dispose();
        } catch (NumberFormatException nfex) {
            JOptionPane.showMessageDialog(this, "The Node needs to be a number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
