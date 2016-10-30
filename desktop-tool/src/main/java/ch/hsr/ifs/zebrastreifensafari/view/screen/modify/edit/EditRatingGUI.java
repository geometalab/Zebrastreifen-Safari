package ch.hsr.ifs.zebrastreifensafari.view.screen.modify.edit;

import ch.hsr.ifs.zebrastreifensafari.controller.modify.edit.EditRatingController;
import ch.hsr.ifs.zebrastreifensafari.exception.InvalidTimeException;
import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.Illumination;
import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.SpatialClarity;
import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.Traffic;
import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.User;
import ch.hsr.ifs.zebrastreifensafari.service.Properties;
import ch.hsr.ifs.zebrastreifensafari.view.screen.MainGUI;
import ch.hsr.ifs.zebrastreifensafari.view.screen.modify.ModifyGUI;
import org.eclipse.persistence.exceptions.DatabaseException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.RollbackException;
import javax.swing.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

/**
 * @author aeugster
 */
public class EditRatingGUI extends ModifyGUI {

    private final EditRatingController controller;

    public EditRatingGUI(EditRatingController controller, MainGUI mainGUI) {
        super(controller, mainGUI, Properties.get("editRatingGuiTitleOne") + controller.getRating().getUserId().getName() + Properties.get("editRatingGuiTitleTwo") + controller.getRating().getCrossingId().getOsmNodeId());
        this.controller = controller;
        setInitialValues();
        hideGuiElements();
    }

    @Override
    protected void onSendClick() {
        String comment = controller.getRating().getComment();
        Illumination illumination = controller.getRating().getIlluminationId();
        SpatialClarity spatialClarity = controller.getRating().getSpatialClarityId();
        Traffic traffic = controller.getRating().getTrafficId();
        User user = controller.getRating().getUserId();
        String imageWeblink = controller.getRating().getImageWeblink();
        Date lastChanged = controller.getRating().getLastChanged();
        Date creationTime = controller.getRating().getCreationTime();

        if (editRating()) {
            dispose();
        } else {
            controller.setRatingData(user, illumination, spatialClarity, traffic, imageWeblink, comment, lastChanged, creationTime);
        }
    }

    private boolean editRating() {
        try {
            String selectedUser = userComboBox.getSelectedItem().toString();
            int illumination = getSelectedButton(illuminationButtonGroup);
            int spatialClarity = getSelectedButton(spatialClarityButtonGroup);
            int traffic = getSelectedButton(trafficButtonGroup);
            String imageText = imageTextField.getText();
            String commentText = commentTextArea.getText();
            Date creationTime = getCreationTime();
            controller.editRating(selectedUser, illumination, spatialClarity, traffic, imageText, commentText, creationTime);
            return true;
        } catch (EntityNotFoundException enfex) {
            errorMessage(Properties.get("ratingCrossingExistError"));
        } catch (RollbackException rex) {
            errorMessage(Properties.get("duplicatedPhotoError"));
        } catch (DatabaseException dbex) {
            errorMessage(Properties.get("connectionError"));
        } catch (NumberFormatException nfex) {
            errorMessage(Properties.get("invalidTimeFormatError"));
        } catch (InvalidTimeException itex) {
            errorMessage(Properties.get("invalidTimeError"));
        } catch (Exception ex) {
            ex.printStackTrace();
            errorMessage(Properties.get("unexpectedError"));
        }

        return false;
    }

    private void setInitialValues() {
        sendButton.setText(Properties.get("change"));
        userComboBox.setSelectedItem(controller.getRating().getUserId().getName());
        setButtonGroupValue(spatialClarityButtonGroup, controller.getRating().getSpatialClarityId().getId());
        setButtonGroupValue(illuminationButtonGroup, controller.getRating().getIlluminationId().getId());
        setButtonGroupValue(trafficButtonGroup, controller.getRating().getTrafficId().getId());
        commentTextArea.setText(controller.getRating().getComment());
        imageTextField.setText(controller.getRating().getImageWeblink());
        setImage(imageTextField.getText());

        if (controller.getRating().getCreationTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(controller.getRating().getCreationTime());
            creationDate.setDate(calendar.getTime());
            creationTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + (calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE)));
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

    private void hideGuiElements() {
        osmNodeIdLabel.setVisible(false);
        osmNodeIdTextField.setVisible(false);
    }

}
