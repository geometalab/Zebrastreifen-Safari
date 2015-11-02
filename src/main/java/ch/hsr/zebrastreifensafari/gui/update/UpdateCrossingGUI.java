package ch.hsr.zebrastreifensafari.gui.update;

import ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import ch.hsr.zebrastreifensafari.gui.view.MainGUI;
import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

import javax.swing.*;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 14:11
 * @date : 12.10.2015
 */

public class UpdateCrossingGUI extends CreateUpdateGUI {

    private Crossing crossing;

    public UpdateCrossingGUI(MainGUI mainGUI, Crossing crossing) {
        super(mainGUI, "Update the crossing " + crossing.getOsmNodeId());
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
        trafficLabel.setVisible(false);
        trafficNoneRadioButton.setVisible(false);
        trafficLittleRadioButton.setVisible(false);
        trafficALotRadioButton.setVisible(false);
        commentLabel.setVisible(false);
        commentTextArea.setVisible(false);
        imageLabel.setVisible(false);
        imageTextField.setVisible(false);
        pack();
    }

    @Override
    protected void onSendClick() {
        long osmNodeIdBackup = crossing.getOsmNodeId();

        try{
            crossing.setOsmNodeId(Long.parseLong(osmNodeIdTextField.getText()));
            DataServiceLoader.getCrossingData().updateCrossing(crossing);
            observable.notifyObservers();
            this.dispose();
        } catch (NumberFormatException nfex) {
            JOptionPane.showMessageDialog(this, "The Node needs to be a number", "Error", JOptionPane.ERROR_MESSAGE);
        }  catch (Exception e) {
            crossing.setOsmNodeId(osmNodeIdBackup);
            JOptionPane.showMessageDialog(this, "This Node is already used", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected boolean checkValues() {
        if (osmNodeIdTextField.getText() == null) {
            JOptionPane.showMessageDialog(this, "There is an Input missing", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void setValues() {
        osmNodeIdTextField.setText(Long.toString(crossing.getOsmNodeId()));
    }
}
