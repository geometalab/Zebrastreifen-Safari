package ch.hsr.zebrastreifensafari.gui.edit;

import ch.hsr.zebrastreifensafari.gui.MainGUI;
import ch.hsr.zebrastreifensafari.gui.ModifyGUI;
import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;
import ch.hsr.zebrastreifensafari.service.Properties;
import org.eclipse.persistence.exceptions.DatabaseException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.RollbackException;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 14:11
 * @date : 12.10.2015
 */

public class EditCrossingGUI extends ModifyGUI {

    private final Crossing crossing;

    public EditCrossingGUI(MainGUI mainGUI, Crossing crossing) {
        super(mainGUI, Properties.get("editCrossingGuiTitle") + crossing.getOsmNodeId());
        this.crossing = crossing;
        setInitialValues();
        hideGuiElements();
        setSize(getWidth(), 110);
    }

    @Override
    protected void onSendClick() {
        Crossing backupCrossing = new Crossing(crossing.getId(), crossing.getOsmNodeId(), crossing.getRatingAmount(), crossing.getStatus());

        if (editCrossing()) return;

        setCrossingData(backupCrossing.getOsmNodeId());
    }

    private boolean editCrossing() {
        try {
            setCrossingData(Long.parseLong(osmNodeIdTextField.getText()));
            mainGUI.editCrossing(crossing);
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

    private void setCrossingData(long osmNodeId) {
        crossing.setOsmNodeId(osmNodeId);
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
        osmNodeIdTextField.setText(Long.toString(crossing.getOsmNodeId()));
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
