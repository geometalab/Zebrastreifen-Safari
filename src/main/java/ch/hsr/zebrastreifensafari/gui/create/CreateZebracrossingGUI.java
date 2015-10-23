package main.java.ch.hsr.zebrastreifensafari.gui.create;

import main.java.ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import main.java.ch.hsr.zebrastreifensafari.gui.view.View;

import main.java.ch.hsr.zebrastreifensafari.jpaold.entities.*;
import main.java.ch.hsr.zebrastreifensafari.model.Model;
import main.java.ch.hsr.zebrastreifensafari.service.DataServiceLoader;

import javax.swing.*;

/**
 *
 * @author aeugster
 */
public class CreateZebracrossingGUI extends CreateUpdateGUI {

    public CreateZebracrossingGUI(Model model, View view) {
        super(model, view, "Create a new zebracrossing");
    }

    @Override
    protected void onSendClick() {
        try {
            long osmNode = Long.parseLong(this.osmNode.getText());

            if (model.getZebracrossing(osmNode) == null) {
                DataServiceLoader.getZebraData().addZebracrossing(new Zebracrossing(osmNode, file == null ? null : file.getName()));
                observable.notifyObservers(null);
            }

            DataServiceLoader.getZebraData().addRating(
                    new Rating(null,
                            CommentsTA.getText(),
                            model.getIllumination(getSelectedButtonInt(buttonGroup2)),
                            model.getOverview(getSelectedButtonInt(buttonGroup1)),
                            model.getTraffic(getSelectedButtonInt(buttonGroup3)),
                            model.getUser((String) usersCB.getSelectedItem()),
                            model.getZebracrossing(osmNode)));
            this.dispose();
        } catch (NumberFormatException nfex) {
            JOptionPane.showMessageDialog(this, "The Node needs to be a number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
