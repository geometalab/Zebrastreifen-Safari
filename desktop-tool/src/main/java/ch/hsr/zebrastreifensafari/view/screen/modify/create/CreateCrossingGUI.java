package ch.hsr.zebrastreifensafari.view.screen.modify.create;

import ch.hsr.zebrastreifensafari.controller.callback.modify.ICreateCrossingCallback;
import ch.hsr.zebrastreifensafari.controller.modify.create.CreateCrossingController;
import ch.hsr.zebrastreifensafari.service.Properties;
import ch.hsr.zebrastreifensafari.view.screen.MainGUI;
import ch.hsr.zebrastreifensafari.view.screen.modify.ModifyGUI;

import javax.persistence.RollbackException;
import java.util.Date;

/**
 * @author aeugster
 */
public class CreateCrossingGUI extends ModifyGUI implements ICreateCrossingCallback {

    private CreateCrossingController controller;

    public CreateCrossingGUI(CreateCrossingController controller, MainGUI mainGUI) {
        super(controller, mainGUI, Properties.get("createCrossingGuiTitle"));
        this.controller = controller;
        controller.setCallback(this);
    }

    @Override
    protected void onSendClick() {
        //todo: überarbeiten, wenn ein bild wiederholt verwendet wird, wird trozdem ein crossing erstellt (momentane lösung funktioniert ist aber nicht schön)
        try {
            controller.send(osmNodeIdTextField.getText());
        } catch (NumberFormatException nfex) {
            errorMessage(Properties.get("osmNodeIdNumericError"));
        } catch (RollbackException rex) {
            errorMessage(Properties.get("connectionError"));
        } catch (Exception ex) {
            ex.printStackTrace();
            errorMessage(Properties.get("unexpectedError"));
        }
    }

    @Override
    public boolean createRating() {
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
            errorMessage(Properties.get("invalideTimeFormatError"));
        } catch (IllegalArgumentException iaex) {
            errorMessage(Properties.get("invalideTimeError"));
        }

        controller.deleteCrossing();
        return false;
    }
}
