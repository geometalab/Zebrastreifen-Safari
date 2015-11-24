package ch.hsr.zebrastreifensafari.gui.edit;

import ch.hsr.zebrastreifensafari.gui.CreateEditGUI;
import ch.hsr.zebrastreifensafari.gui.view.MainGUI;
import ch.hsr.zebrastreifensafari.jpa.controllers.exceptions.NonexistentEntityException;
import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import ch.hsr.zebrastreifensafari.service.Properties;

import javax.swing.*;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 14:11
 * @date : 12.10.2015
 */

public class EditCrossingGUI extends CreateEditGUI {

    private final Crossing crossing;

    public EditCrossingGUI(MainGUI mainGUI, Crossing crossing) {
        super(mainGUI, Properties.get("editCrossingGuiTitle") + crossing.getOsmNodeId());
        this.crossing = crossing;
        setValues();
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
        sendButton.setText(Properties.get("change"));
        setSize(getWidth(), 110);
    }

    @Override
    protected void onSendClick() {
        long osmNodeIdBackup = crossing.getOsmNodeId();

        try{
            crossing.setOsmNodeId(Long.parseLong(osmNodeIdTextField.getText()));
            DataServiceLoader.getCrossingData().editCrossing(crossing);
            observable.notifyObservers(crossing);
            this.dispose();
        } catch (NumberFormatException nfex) {
            errorMessage(Properties.get("osmNodeIdNumericError"));
        } catch (NonexistentEntityException neeex) {
            crossing.setOsmNodeId(osmNodeIdBackup);
            errorMessage(Properties.get("crossingExistError"));
        } catch (Exception e) {
            crossing.setOsmNodeId(osmNodeIdBackup);
            errorMessage(Properties.get("duplicatedOsmNodeIdError"));
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
}
