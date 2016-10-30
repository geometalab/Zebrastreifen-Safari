package ch.hsr.ifs.zebrastreifensafari.view.screen.modify.edit;

import ch.hsr.ifs.zebrastreifensafari.controller.callback.modify.edit.IEditCrossingCallback;
import ch.hsr.ifs.zebrastreifensafari.controller.modify.edit.EditCrossingController;
import ch.hsr.ifs.zebrastreifensafari.service.Properties;
import ch.hsr.ifs.zebrastreifensafari.view.screen.MainGUI;
import ch.hsr.ifs.zebrastreifensafari.view.screen.modify.ModifyGUI;
import org.eclipse.persistence.exceptions.DatabaseException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.RollbackException;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 1.0
 */
public class EditCrossingGUI extends ModifyGUI implements IEditCrossingCallback {

    private final EditCrossingController controller;

    public EditCrossingGUI(EditCrossingController controller, MainGUI mainGUI) {
        super(controller, mainGUI, Properties.get("editCrossingGuiTitle") + controller.getCrossing().getOsmNodeId());
        this.controller = controller;
        controller.setCallback(this);
        setInitialValues();
        hideGuiElements();
        setSize(getWidth(), 110);
    }

    @Override
    protected void onSendClick() {
        controller.send();
    }

    @Override
    public boolean editCrossing() {
        try {
            controller.editCrossing(osmNodeIdTextField.getText());
            dispose();
            return true;
        } catch (NumberFormatException nfex) {
            errorMessage(Properties.get("osmNodeIdNumericError"));
        } catch (EntityNotFoundException enfex) {
            errorMessage(Properties.get("crossingExistError"));
        } catch (RollbackException rex) {
            errorMessage(Properties.get("duplicatedOsmNodeIdError"));
        } catch (DatabaseException dbex) {
            errorMessage(Properties.get("connectionError"));
        } catch (Exception ex) {
            ex.printStackTrace();
            errorMessage(Properties.get("unexpectedError"));
        }

        return false;
    }

    @Override
    protected boolean checkValues() {
        if (osmNodeIdTextField.getText() == null) {
            errorMessage(Properties.get("missingInputError"));
            return false;
        }

        return true;
    }

    private void setInitialValues() {
        sendButton.setText(Properties.get("change"));
        osmNodeIdTextField.setText(Long.toString(controller.getCrossing().getOsmNodeId()));
    }

    private void hideGuiElements() {
        userLabel.setVisible(false);
        userComboBox.setVisible(false);
        spatialClarityLabel.setVisible(false);
        spatialClarityGoodRadioButton.setVisible(false);
        spatialClarityOkRadioButton.setVisible(false);
        spatialClarityBadRadioButton.setVisible(false);
        illuminationLabel.setVisible(false);
        illuminationGoodRadioButton.setVisible(false);
        illuminationOkRadioButton.setVisible(false);
        illuminationBadRadioButton.setVisible(false);
        illuminationNoneRadioButton.setVisible(false);
        trafficLabel.setVisible(false);
        trafficFewRadioButton.setVisible(false);
        trafficLittleRadioButton.setVisible(false);
        trafficALotRadioButton.setVisible(false);
        commentLabel.setVisible(false);
        commentScrollPane.setVisible(false);
        imageLabel.setVisible(false);
        imageTextField.setVisible(false);
        imageField.setVisible(false);
        creationDateLabel.setVisible(false);
        creationDate.setVisible(false);
        creationTimeLabel.setVisible(false);
        creationTime.setVisible(false);
    }
}
