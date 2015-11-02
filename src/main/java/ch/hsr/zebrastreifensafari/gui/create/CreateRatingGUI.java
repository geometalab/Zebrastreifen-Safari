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

    public CreateRatingGUI(MainGUI mainGUI, long node) {
        super(mainGUI, "Create a new rating for the Crossing " + node);
        this.node = node;
        osmNodeIdLabel.setVisible(false);
        osmNodeIdTextField.setVisible(false);
        pack();
    }

    @Override
    protected void onSendClick() {
        Rating rating =new Rating(null,
                commentTextArea.getText(),
                mainGUI.getIllumination(getSelectedButtonInt(illuminationButtonGroup)),
                mainGUI.getSpatialClarity(getSelectedButtonInt(spatialClarityButtonGroupe)),
                mainGUI.getTraffic(getSelectedButtonInt(trafficButtonGroup)),
                mainGUI.getUser((String) userComboBox.getSelectedItem()),
                mainGUI.getCrossing(node),
                imageTextField.getText(),
                new Date()
        );

        DataServiceLoader.getCrossingData().addRating(rating);
        observable.notifyObservers(rating);
        this.dispose();
    }
}
