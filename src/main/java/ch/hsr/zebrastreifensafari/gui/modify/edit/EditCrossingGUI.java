package ch.hsr.zebrastreifensafari.gui.modify.edit;

import ch.hsr.zebrastreifensafari.gui.main.MainGUI;
import ch.hsr.zebrastreifensafari.gui.modify.ModifyGUI;
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
        setValues();
        setInvisible();
        sendButton.setText(Properties.get("change"));
        setSize(getWidth(), 110);
    }

    @Override
    protected void onSendClick() {
        long osmNodeIdBackup = crossing.getOsmNodeId();

        try {
            crossing.setOsmNodeId(Long.parseLong(osmNodeIdTextField.getText()));
            mainGUI.editCrossing(crossing);
            dispose();
        } catch (NumberFormatException nfex) {
            errorMessage(Properties.get("osmNodeIdNumericError"));
        } catch (EntityNotFoundException enfex) {
            rollback(osmNodeIdBackup);
            errorMessage(Properties.get("crossingExistError"));
        } catch (RollbackException rex) {
            rollback(osmNodeIdBackup);
            errorMessage(Properties.get("duplicatedOsmNodeIdError"));
        } catch (DatabaseException dbex) {
            rollback(osmNodeIdBackup);
            errorMessage(Properties.get("connectionError"));
        } catch (Exception ex) {
            rollback(osmNodeIdBackup);
            ex.printStackTrace();
            errorMessage(Properties.get("unexpectedError"));
        }
    }

    @Override
    protected boolean checkValues() {
        if (osmNodeIdTextField.getText() == null) {
            errorMessage(Properties.get("missingInputError"));
            return false;
        }

        return true;
    }

    private void setValues() {
        osmNodeIdTextField.setText(Long.toString(crossing.getOsmNodeId()));
    }

    private void rollback(long osmNodeId) {
        crossing.setOsmNodeId(osmNodeId);
    }

    private void setInvisible() {
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
    }
}
