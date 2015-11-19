package ch.hsr.zebrastreifensafari.gui.create;

import ch.hsr.zebrastreifensafari.gui.CreateEditGUI;
import ch.hsr.zebrastreifensafari.gui.view.MainGUI;

import ch.hsr.zebrastreifensafari.jpa.entities.Rating;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

import javax.swing.*;
import java.util.Date;

/**
 *
 * @author aeugster
 */
public class CreateRatingGUI extends CreateEditGUI {

    private final long node;

    public CreateRatingGUI(MainGUI mainGUI, long node) {
        super(mainGUI, DataServiceLoader.getBundleString("createRatingGuiTitle") + node);
        this.node = node;
        osmNodeIdLabel.setVisible(false);
        osmNodeIdTextField.setVisible(false);
    }

    @Override
    protected void onSendClick() {
        try {
            Rating rating = new Rating(
                    null,
                    commentTextArea.getText().isEmpty() ? null : commentTextArea.getText(),
                    mainGUI.getIllumination(getSelectedButtonInt(illuminationButtonGroup)),
                    mainGUI.getSpatialClarity(getSelectedButtonInt(spatialClarityButtonGroup)),
                    mainGUI.getTraffic(getSelectedButtonInt(trafficButtonGroup)),
                    mainGUI.getUser((String) userComboBox.getSelectedItem()),
                    mainGUI.getCrossing(node),
                    imageTextField.getText().isEmpty() ? null : imageTextField.getText(),
                    new Date()
            );

            observable.notifyObservers(rating);
            this.dispose();
        } catch (Exception e) {
            errorMessage(DataServiceLoader.getBundleString("duplicatedPhotoError"));
        }
    }
}
