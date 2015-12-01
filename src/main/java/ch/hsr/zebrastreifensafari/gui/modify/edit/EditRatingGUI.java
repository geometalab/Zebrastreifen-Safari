package ch.hsr.zebrastreifensafari.gui.modify.edit;

import ch.hsr.zebrastreifensafari.gui.main.MainGUI;
import ch.hsr.zebrastreifensafari.gui.modify.ModifyGUI;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.service.Properties;
import org.eclipse.persistence.exceptions.DatabaseException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.RollbackException;
import javax.swing.*;
import java.util.Date;
import java.util.Enumeration;

/**
 * @author aeugster
 */
public class EditRatingGUI extends ModifyGUI {

    private final Rating rating;

    public EditRatingGUI(MainGUI mainGUI, Rating rating) {
        super(mainGUI, Properties.get("editRatingGuiTitleOne") + rating.getUserId().getName() + Properties.get("editRatingGuiTitleTwo") + rating.getCrossingId().getOsmNodeId());
        this.rating = rating;
        setValues();
        setInvisible();
        sendButton.setText(Properties.get("change"));
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
            mainGUI.editRating(rating);
            dispose();
        } catch (EntityNotFoundException enfex) {
            rollback(userBackup, illuminationBackup, spatialClarityBackup, trafficBackup, imageWeblinkBackup, commentBackup, lastChangedBackup);
            errorMessage(Properties.get("ratingCrossingExistError"));
        } catch (RollbackException rex) {
            rollback(userBackup, illuminationBackup, spatialClarityBackup, trafficBackup, imageWeblinkBackup, commentBackup, lastChangedBackup);
            errorMessage(Properties.get("duplicatedPhotoError"));
        } catch (DatabaseException dbex) {
            rollback(userBackup, illuminationBackup, spatialClarityBackup, trafficBackup, imageWeblinkBackup, commentBackup, lastChangedBackup);
            errorMessage(Properties.get("connectionError"));
        } catch (Exception ex) {
            rollback(userBackup, illuminationBackup, spatialClarityBackup, trafficBackup, imageWeblinkBackup, commentBackup, lastChangedBackup);
            ex.printStackTrace();
            errorMessage(Properties.get("unexpectedError"));
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
        setImage(imageTextField.getText());
    }

    private void rollback(User user, Illumination illumination, SpatialClarity spatialClarity, Traffic traffic, String imageWeblink, String comment, Date lastChanged) {
        rating.setUserId(user);
        rating.setIlluminationId(illumination);
        rating.setSpatialClarityId(spatialClarity);
        rating.setTrafficId(traffic);
        rating.setImageWeblink(imageWeblink);
        rating.setComment(comment);
        rating.setLastChanged(lastChanged);
    }

    private void setInvisible() {
        osmNodeIdLabel.setVisible(false);
        osmNodeIdTextField.setVisible(false);
    }
}
