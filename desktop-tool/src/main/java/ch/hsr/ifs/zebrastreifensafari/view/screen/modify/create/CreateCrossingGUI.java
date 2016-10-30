package ch.hsr.ifs.zebrastreifensafari.view.screen.modify.create;

import ch.hsr.ifs.zebrastreifensafari.controller.modify.create.CreateCrossingController;
import ch.hsr.ifs.zebrastreifensafari.exception.InvalidTimeException;
import ch.hsr.ifs.zebrastreifensafari.service.Properties;
import ch.hsr.ifs.zebrastreifensafari.view.screen.MainGUI;
import ch.hsr.ifs.zebrastreifensafari.view.screen.modify.ModifyGUI;

import javax.persistence.RollbackException;
import java.util.Date;

/**
 * @author aeugster
 */
public class CreateCrossingGUI extends ModifyGUI {

    private CreateCrossingController controller;

    public CreateCrossingGUI(CreateCrossingController controller, MainGUI mainGUI) {
        super(controller, mainGUI, Properties.get("createCrossingGuiTitle"));
        this.controller = controller;
    }

    @Override
    protected void onSendClick() {
        //todo: überarbeiten, wenn ein bild wiederholt verwendet wird, wird trozdem ein crossing erstellt (momentane lösung funktioniert ist aber nicht schön)
        try {
            controller.createCrossing(osmNodeIdTextField.getText());

            if (createRating()) {
                dispose();
            } else {
                controller.deleteCrossing();
            }
        } catch (NumberFormatException nfex) {
            errorMessage(Properties.get("osmNodeIdNumericError"));
        } catch (RollbackException rex) {
            errorMessage(Properties.get("connectionError"));
        } catch (Exception ex) {
            ex.printStackTrace();
            errorMessage(Properties.get("unexpectedError"));
        }
    }

    private boolean createRating() {
        try {
            String commentText = commentTextArea.getText();
            int selectedIllumination = getSelectedButton(illuminationButtonGroup);
            int selectedSpatialClarity = getSelectedButton(spatialClarityButtonGroup);
            int selectedTraffic = getSelectedButton(trafficButtonGroup);
            String selectedUser = userComboBox.getSelectedItem().toString();
            String imageWeblinkText = imageTextField.getText();
            Date creationTime = getCreationTime();
            controller.createRating(commentText, selectedIllumination, selectedSpatialClarity, selectedTraffic, selectedUser, imageWeblinkText, creationTime);
            return true;
        } catch (RollbackException rex) {
            errorMessage(Properties.get("duplicatedPhotoError"));
        } catch (NumberFormatException nfex) {
            errorMessage(Properties.get("invalidTimeFormatError"));
        } catch (InvalidTimeException itex) {
            errorMessage(Properties.get("invalidTimeError"));
        }

        return false;
    }
}
