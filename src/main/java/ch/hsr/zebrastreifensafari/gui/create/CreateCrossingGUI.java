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
        pack();
    }

    @Override
    protected void onSendClick() {
        try {
            long osmNode = Long.parseLong(osmNodeIdTextField.getText());

            if (model.getCrossing(osmNode) == null) {
                DataServiceLoader.getCrossingData().addCrossing(new Crossing(null, osmNode, 1));
                observable.notifyObservers(null);
            }

            DataServiceLoader.getCrossingData().addRating(
                    new Rating(null,
                            commentTextArea.getText(),
                            model.getIllumination(getSelectedButtonInt(illuminationButtonGroup)),
                            model.getSpatialClarity(getSelectedButtonInt(spatialClarityButtonGroupe)),
                            model.getTraffic(getSelectedButtonInt(trafficButtonGroup)),
                            model.getUser((String) userComboBox.getSelectedItem()),
                            model.getCrossing(osmNode),
                            imageTextField.getText(),
                            new Date()
                    )
            );

            this.dispose();
        } catch (NumberFormatException nfex) {
            JOptionPane.showMessageDialog(this, "The Node needs to be a number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
