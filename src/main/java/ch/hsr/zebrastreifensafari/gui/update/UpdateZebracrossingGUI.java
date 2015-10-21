package main.java.ch.hsr.zebrastreifensafari.gui.update;

import main.java.ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import main.java.ch.hsr.zebrastreifensafari.gui.view.View;
import main.java.ch.hsr.zebrastreifensafari.jpa.entities.Zebracrossing;
import main.java.ch.hsr.zebrastreifensafari.model.Model;
import main.java.ch.hsr.zebrastreifensafari.service.DataServiceLoader;

import javax.swing.*;
import java.awt.*;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 14:11
 * @date : 12.10.2015
 */

public class UpdateZebracrossingGUI extends CreateUpdateGUI {

    private Zebracrossing zebracrossing;

    public UpdateZebracrossingGUI(Model model, View view, Zebracrossing zebracrossing) {
        super(model, view, "Update the zebracrossing " + zebracrossing.getNode());
        this.zebracrossing = zebracrossing;
        setValues();
        imageTF.setPreferredSize(new Dimension(150, imageTF.getHeight()));
        jLabel2.setVisible(false);
        jLabel3.setVisible(false);
        jLabel4.setVisible(false);
        jLabel5.setVisible(false);
        jLabel6.setVisible(false);
        usersCB.setVisible(false);
        jRadioButton1.setVisible(false);
        jRadioButton2.setVisible(false);
        jRadioButton3.setVisible(false);
        jRadioButton4.setVisible(false);
        jRadioButton5.setVisible(false);
        jRadioButton6.setVisible(false);
        jRadioButton7.setVisible(false);
        jRadioButton8.setVisible(false);
        jRadioButton9.setVisible(false);
        jScrollPane1.setVisible(false);
    }

    @Override
    protected void onSendClick() {
        try{
            zebracrossing.setNode(Long.parseLong(osmNode.getText()));
            zebracrossing.setImage(imageTF.getText());
            DataServiceLoader.getZebraData().updateZebracrossing(zebracrossing);
            observable.notifyObservers();
            this.dispose();
        } catch (NumberFormatException nfex) {
            JOptionPane.showMessageDialog(this, "The Node needs to be a number", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setValues() {
        osmNode.setText(Long.toString(zebracrossing.getNode()));
        imageTF.setText(zebracrossing.getImage() == null ? "" : zebracrossing.getImage());
        jRadioButton1.setSelected(true);
        jRadioButton4.setSelected(true);
        jRadioButton7.setSelected(true);
    }
}
