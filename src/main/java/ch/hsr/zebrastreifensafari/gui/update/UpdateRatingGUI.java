package ch.hsr.zebrastreifensafari.gui.update;

import java.util.Enumeration;
import javax.swing.*;

import ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.gui.view.View;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

/**
 *
 * @author aeugster
 */
public class UpdateRatingGUI extends CreateUpdateGUI {

    private Rating rating;

    public UpdateRatingGUI(Model model, View view, Rating rating) {
        super(model, view, "Update the rating from " + rating.getUserId().getName() + " for the Crossing " + rating.getCrossingId().getOsmNodeId());
        this.rating = rating;
        setValues();
        osmNodeIdLabel.setVisible(false);
        osmNodeIdTextField.setVisible(false);
        pack();
    }

    @Override
    protected void onSendClick() {
        User userBackup = rating.getUserId();
        Illumination illuminationBackup = rating.getIlluminationId();
        SpatialClarity spatialClarityBackup = rating.getSpatialClarityId();
        Traffic trafficBackup = rating.getTrafficId();
        String imageWeblinkBackup = rating.getImageWeblink();
        String commentBackup = rating.getComment();

        try {
            rating.setUserId(model.getUser((String) userComboBox.getSelectedItem()));
            rating.setIlluminationId(model.getIllumination(getSelectedButtonInt(illuminationButtonGroup)));
            rating.setSpatialClarityId(model.getSpatialClarity(getSelectedButtonInt(spatialClarityButtonGroupe)));
            rating.setTrafficId(model.getTraffic(getSelectedButtonInt(trafficButtonGroup)));
            rating.setImageWeblink(imageTextField.getText());
            rating.setComment(commentTextArea.getText());
            DataServiceLoader.getCrossingData().updateRating(rating);
            observable.notifyObservers(rating.getCrossingId());
            this.dispose();
        } catch (Exception e) {
            rating.setUserId(userBackup);
            rating.setIlluminationId(illuminationBackup);
            rating.setSpatialClarityId(spatialClarityBackup);
            rating.setTrafficId(trafficBackup);
            rating.setImageWeblink(imageWeblinkBackup);
            rating.setComment(commentBackup);
            JOptionPane.showMessageDialog(this, "This Photo is already used", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setButtonGroupValue(ButtonGroup bg, int selectedButtonInt) {
        Enumeration<AbstractButton> buttons = bg.getElements();

        for (int counter = 1; buttons.hasMoreElements(); counter++) {
            AbstractButton button = buttons.nextElement();

            if (counter == selectedButtonInt) {
                button.setSelected(true);
                return;
            }
        }
    }

    private void setValues() {
        userComboBox.setSelectedItem(rating.getUserId().getName());
        setButtonGroupValue(spatialClarityButtonGroupe, rating.getSpatialClarityId().getId());
        setButtonGroupValue(illuminationButtonGroup, rating.getIlluminationId().getId());
        setButtonGroupValue(trafficButtonGroup, rating.getTrafficId().getId());
        commentTextArea.setText(rating.getComment() == null ? "": rating.getComment());
        imageTextField.setText(rating.getImageWeblink() == null ? "" : rating.getImageWeblink());
    }
}
