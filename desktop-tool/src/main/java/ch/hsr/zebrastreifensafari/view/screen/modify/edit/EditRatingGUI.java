package ch.hsr.zebrastreifensafari.view.screen.modify.edit;

import ch.hsr.zebrastreifensafari.view.screen.MainGUI;
import ch.hsr.zebrastreifensafari.view.screen.modify.ModifyGUI;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.service.Properties;
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

    private final Rating rating;

    public EditRatingGUI(MainGUI mainGUI, Rating rating) {
        super(mainGUI, Properties.get("editRatingGuiTitleOne") + rating.getUserId().getName() + Properties.get("editRatingGuiTitleTwo") + rating.getCrossingId().getOsmNodeId());
        this.rating = rating;
        setInitialValues();
        hideGuiElements();
    }

    @Override
    protected void onSendClick() {
        Rating backupRating = new Rating(rating.getId(), rating.getComment(), rating.getIlluminationId(), rating.getSpatialClarityId(), rating.getTrafficId(),
                rating.getUserId(), rating.getCrossingId(), rating.getImageWeblink(), rating.getLastChanged(), rating.getCreationTime());

        if (editRating()) return;

        setRatingData(
                backupRating.getUserId(),
                backupRating.getIlluminationId(),
                backupRating.getSpatialClarityId(),
                backupRating.getTrafficId(),
                backupRating.getImageWeblink(),
                backupRating.getComment(),
                backupRating.getLastChanged(),
                backupRating.getCreationTime()
        );
    }

    private boolean editRating() {
        try {
            setRatingData(
                    mainGUI.getUser((String) userComboBox.getSelectedItem()),
                    mainGUI.getIllumination(getSelectedButtonInt(illuminationButtonGroup)),
                    mainGUI.getSpatialClarity(getSelectedButtonInt(spatialClarityButtonGroup)),
                    mainGUI.getTraffic(getSelectedButtonInt(trafficButtonGroup)),
                    imageTextField.getText().isEmpty() ? null : imageTextField.getText(),
                    commentTextArea.getText().isEmpty() ? null : commentTextArea.getText(),
                    new Date(),
                    getCreationTime()
            );

            mainGUI.editRating(rating);
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

    private void setRatingData(User user, Illumination illumination, SpatialClarity spatialClarity, Traffic traffic, String imageWeblink, String comment, Date lastChanged, Date creationTime) {
        rating.setUserId(user);
        rating.setIlluminationId(illumination);
        rating.setSpatialClarityId(spatialClarity);
        rating.setTrafficId(traffic);
        rating.setImageWeblink(imageWeblink);
        rating.setComment(comment);
        rating.setLastChanged(lastChanged);
        rating.setCreationTime(creationTime);
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

    private void setInitialValues() {
        sendButton.setText(Properties.get("change"));
        userComboBox.setSelectedItem(rating.getUserId().getName());
        setButtonGroupValue(spatialClarityButtonGroup, rating.getSpatialClarityId().getId());
        setButtonGroupValue(illuminationButtonGroup, rating.getIlluminationId().getId());
        setButtonGroupValue(trafficButtonGroup, rating.getTrafficId().getId());
        commentTextArea.setText(rating.getComment());
        imageTextField.setText(rating.getImageWeblink());
        setImage(imageTextField.getText());

        if (rating.getCreationTime() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rating.getCreationTime());
            creationDate.setDate(calendar.getTime());
            creationTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + (calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE)));
        }
    }

    private void hideGuiElements() {
        osmNodeIdLabel.setVisible(false);
        osmNodeIdTextField.setVisible(false);
    }
}
