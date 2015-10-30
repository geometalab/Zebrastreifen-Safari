package ch.hsr.zebrastreifensafari.gui.create;

import ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import ch.hsr.zebrastreifensafari.gui.view.MainGUI;

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

    public CreateRatingGUI(Model model, MainGUI mainGUI, long node) {
        super(model, mainGUI, "Create a new rating for the Crossing " + node);
        this.node = node;
        osmNodeIdLabel.setVisible(false);
        osmNodeIdTextField.setVisible(false);
        pack();
    }

    @Override
    protected void onSendClick() {
        DataServiceLoader.getCrossingData().addRating(
            new Rating(null,
                commentTextArea.getText(),
                model.getIllumination(getSelectedButtonInt(illuminationButtonGroup)),
                model.getSpatialClarity(getSelectedButtonInt(spatialClarityButtonGroupe)),
                model.getTraffic(getSelectedButtonInt(trafficButtonGroup)),
                model.getUser((String) userComboBox.getSelectedItem()),
                model.getCrossing(node),
                imageTextField.getText(),
                new Date()
            )
        );

        observable.notifyObservers(model.getCrossing(node));
        this.dispose();
    }
}
