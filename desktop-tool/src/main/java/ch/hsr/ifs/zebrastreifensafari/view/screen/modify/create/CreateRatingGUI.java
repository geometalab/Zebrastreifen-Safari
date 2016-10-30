package ch.hsr.ifs.zebrastreifensafari.view.screen.modify.create;

import ch.hsr.ifs.zebrastreifensafari.exception.InvalidTimeException;
import ch.hsr.ifs.zebrastreifensafari.service.Properties;
import ch.hsr.ifs.zebrastreifensafari.controller.modify.create.CreateRatingController;
import ch.hsr.ifs.zebrastreifensafari.view.screen.MainGUI;
import ch.hsr.ifs.zebrastreifensafari.view.screen.modify.ModifyGUI;
import org.eclipse.persistence.exceptions.DatabaseException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.RollbackException;
import java.util.Date;

/**
 * @author aeugster
 */
public class CreateRatingGUI extends ModifyGUI {

    private final CreateRatingController controller;

    public CreateRatingGUI(CreateRatingController controller, MainGUI mainGUI) {
        super(controller, mainGUI, Properties.get("createRatingGuiTitle") + controller.getNode());
        this.controller = controller;
        hideGuiElements();
    }

    @Override
    protected void onSendClick() {
        try {
            String commentText = commentTextArea.getText();
            int selectedIllumination = getSelectedButton(illuminationButtonGroup);
            int selectedSpatialClarity = getSelectedButton(spatialClarityButtonGroup);
            int selectedTraffic = getSelectedButton(trafficButtonGroup);
            String selectedUser = userComboBox.getSelectedItem().toString();
            String imageWeblinkText = imageTextField.getText();
            Date creationTime = getCreationTime();
            controller.send(commentText, selectedIllumination, selectedSpatialClarity, selectedTraffic, selectedUser, imageWeblinkText, creationTime);
            dispose();
        } catch (EntityNotFoundException enfex) {
            errorMessage(Properties.get("crossingExistError"));
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
    }

    private void hideGuiElements() {
        osmNodeIdLabel.setVisible(false);
        osmNodeIdTextField.setVisible(false);
    }
}
