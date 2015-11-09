package ch.hsr.zebrastreifensafari.gui.create;

import ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import ch.hsr.zebrastreifensafari.gui.view.MainGUI;

import ch.hsr.zebrastreifensafari.jpa.entities.Rating;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

import javax.swing.*;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author aeugster
 */
public class CreateRatingGUI extends CreateUpdateGUI {

    private final long node;

    public CreateRatingGUI(MainGUI mainGUI, long node) {
        super(mainGUI, "Erstellen einer neuen Bewertung für den Zebrastreifen mit dem OSM Node " + node);
        this.node = node;
        osmNodeIdLabel.setVisible(false);
        osmNodeIdTextField.setVisible(false);
        pack();
    }

    @Override
    protected void onSendClick() {
        try {
            Rating rating = new Rating(
                    null,
                    commentTextArea.getText().isEmpty() ? null : commentTextArea.getText(),
                    mainGUI.getIllumination(getSelectedButtonInt(illuminationButtonGroup)),
                    mainGUI.getSpatialClarity(getSelectedButtonInt(spatialClarityButtonGroupe)),
                    mainGUI.getTraffic(getSelectedButtonInt(trafficButtonGroup)),
                    mainGUI.getUser((String) userComboBox.getSelectedItem()),
                    mainGUI.getCrossing(node),
                    imageTextField.getText().isEmpty() ? null : imageTextField.getText(),
                    new Date()
            );

            observable.notifyObservers(rating);
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "This Photo is already used", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
