package ch.hsr.zebrastreifensafari.view.screen.modify.edit;

import ch.hsr.zebrastreifensafari.controller.callback.modify.IEditRatingCallback;
import ch.hsr.zebrastreifensafari.controller.modify.edit.EditRatingController;
import ch.hsr.zebrastreifensafari.service.Properties;
import ch.hsr.zebrastreifensafari.view.screen.MainGUI;
import ch.hsr.zebrastreifensafari.view.screen.modify.ModifyGUI;
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
public class EditRatingGUI extends ModifyGUI implements IEditRatingCallback {

    private final EditRatingController controller;

    public EditRatingGUI(EditRatingController controller, MainGUI mainGUI) {
        super(controller, mainGUI, Properties.get("editRatingGuiTitleOne") + controller.getRating().getUserId().getName() + Properties.get("editRatingGuiTitleTwo") + controller.getRating().getCrossingId().getOsmNodeId());
        this.controller = controller;
        controller.setCallback(this);
        setInitialValues();
        hideGuiElements();
    }

    @Override
    protected void onSendClick() {
        controller.send();
    }

    @Override
    public boolean editRating() {
        try {
            String selectedUser = userComboBox.getSelectedItem().toString();
            int illumination = getSelectedButton(illuminationButtonGroup);
            int spatialClarity = getSelectedButton(spatialClarityButtonGroup);
            int traffic = getSelectedButton(trafficButtonGroup);
            String imageText = imageTextField.getText();
            String commentText = commentTextArea.getText();
            Date creationTime = getCreationTime();
            controller.editRating(selectedUser, illumination, spatialClarity, traffic, imageText, commentText, creationTime);
            dispose();
            return true;
        } catch (EntityNotFoundException enfex) {
            errorMessage(Properties.get("ratingCrossingExistError"));
        } catch (RollbackException rex) {
            errorMessage(Properties.get("duplicatedPhotoError"));
        } catch (DatabaseException dbex) {
            errorMessage(Properties.get("connectionError"));
        } catch (NumberFormatException nfex) {
            errorMessage(Properties.get("invalideTimeFormatError"));
        } catch (IllegalArgumentException iaex) {
            errorMessage(Properties.get("invalideTimeError"));
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
