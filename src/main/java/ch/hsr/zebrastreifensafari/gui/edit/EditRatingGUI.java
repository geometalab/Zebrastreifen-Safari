package ch.hsr.zebrastreifensafari.gui.edit;

import java.util.Date;
import java.util.Enumeration;
import javax.swing.*;

import ch.hsr.zebrastreifensafari.gui.CreateEditGUI;
import ch.hsr.zebrastreifensafari.gui.view.MainGUI;
import ch.hsr.zebrastreifensafari.jpa.controllers.exceptions.NonexistentEntityException;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

/**
 *
 * @author aeugster
 */
public class EditRatingGUI extends CreateEditGUI {

    private final Rating rating;

    public EditRatingGUI(MainGUI mainGUI, Rating rating) {
        super(mainGUI, DataServiceLoader.getBundleString("editRatingGuiTitleOne") + rating.getUserId().getName() + DataServiceLoader.getBundleString("editRatingGuiTitleTwo") + rating.getCrossingId().getOsmNodeId());
        this.rating = rating;
        setValues();
        setImage(imageTextField.getText());
        osmNodeIdLabel.setVisible(false);
        osmNodeIdTextField.setVisible(false);
        sendButton.setText(DataServiceLoader.getBundleString("change"));
    }

    @Override
    protected void onSendClick() {
        User userBackup = rating.getUserId();
        Illumination illuminationBackup = rating.getIlluminationId();
        SpatialClarity spatialClarityBackup = rating.getSpatialClarityId();
        Traffic trafficBackup = rating.getTrafficId();
        String imageWeblinkBackup = rating.getImageWeblink();
        String commentBackup = rating.getComment();
        Date lastChangedBackup = rating.getLastChanged();

        try {
            rating.setUserId(mainGUI.getUser((String) userComboBox.getSelectedItem()));
            rating.setIlluminationId(mainGUI.getIllumination(getSelectedButtonInt(illuminationButtonGroup)));
            rating.setSpatialClarityId(mainGUI.getSpatialClarity(getSelectedButtonInt(spatialClarityButtonGroup)));
            rating.setTrafficId(mainGUI.getTraffic(getSelectedButtonInt(trafficButtonGroup)));
            rating.setImageWeblink(imageTextField.getText().isEmpty() ? null : imageTextField.getText());
            rating.setComment(commentTextArea.getText().isEmpty() ? null : commentTextArea.getText());
            rating.setLastChanged(new Date());
            DataServiceLoader.getCrossingData().editRating(rating);
            observable.notifyObservers(rating);
            this.dispose();
        } catch (NonexistentEntityException neeex) {
            rating.setUserId(userBackup);
            rating.setIlluminationId(illuminationBackup);
            rating.setSpatialClarityId(spatialClarityBackup);
            rating.setTrafficId(trafficBackup);
            rating.setImageWeblink(imageWeblinkBackup);
            rating.setComment(commentBackup);
            rating.setLastChanged(lastChangedBackup);
            neeex.printStackTrace();
            //todo das Rating oder der Zebrastreifen existiert nicht mehr
        } catch (Exception e) {
            rating.setUserId(userBackup);
            rating.setIlluminationId(illuminationBackup);
            rating.setSpatialClarityId(spatialClarityBackup);
            rating.setTrafficId(trafficBackup);
            rating.setImageWeblink(imageWeblinkBackup);
            rating.setComment(commentBackup);
            rating.setLastChanged(lastChangedBackup);
            e.printStackTrace();
            errorMessage(DataServiceLoader.getBundleString("duplicatedPhotoError"));
        }
    }

    private void setButtonGroupValue(ButtonGroup bg, int selectedButtonInt) {
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
        setButtonGroupValue(spatialClarityButtonGroup, rating.getSpatialClarityId().getId());
        setButtonGroupValue(illuminationButtonGroup, rating.getIlluminationId().getId());
        setButtonGroupValue(trafficButtonGroup, rating.getTrafficId().getId());
        commentTextArea.setText(rating.getComment());
        imageTextField.setText(rating.getImageWeblink());
    }
}
