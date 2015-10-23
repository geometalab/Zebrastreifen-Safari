package main.java.ch.hsr.zebrastreifensafari.gui.update;

import java.util.Enumeration;
import javax.swing.*;

import main.java.ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import main.java.ch.hsr.zebrastreifensafari.jpa.entities.*;
import main.java.ch.hsr.zebrastreifensafari.gui.view.View;
import main.java.ch.hsr.zebrastreifensafari.model.Model;
import main.java.ch.hsr.zebrastreifensafari.service.DataServiceLoader;

/**
 *
 * @author aeugster
 */
public class UpdateRatingGUI extends CreateUpdateGUI {

    private Rating rating;

    public UpdateRatingGUI(Model model, View view, Rating rating) {
        super(model, view, "Update the rating from " + rating.getUserId().getName() + " for the Crossing " + rating.getCrossingId().getOsmNodeId());
        this.rating = rating;
        setValues();
        jLabel1.setVisible(false);
        jLabel7.setVisible(false);
        osmNode.setVisible(false);
    }

    @Override
    protected void onSendClick() {
        rating.setUserId(model.getUser((String) usersCB.getSelectedItem()));
        rating.setIlluminationId(model.getIllumination(getSelectedButtonInt(buttonGroup2)));
        rating.setSpatialClarityId(model.getSpatialClarity(getSelectedButtonInt(buttonGroup1)));
        rating.setTrafficId(model.getTraffic(getSelectedButtonInt(buttonGroup3)));
        rating.setImageWeblink(imageTF.getText());
        rating.setComment(CommentsTA.getText());
        DataServiceLoader.getCrossingData().updateRating(rating);
        observable.notifyObservers(rating.getCrossingId());
        this.dispose();
    }

    public void setButtonGroupValue(ButtonGroup bg, int selectedButtonInt) {
        Enumeration<AbstractButton> buttons = bg.getElements();

        for (int counter = 1; buttons.hasMoreElements(); counter++) {
            AbstractButton button = buttons.nextElement();

            if (counter == selectedButtonInt) {
                button.setSelected(true);
                return;
            }
        }
    }

    private void setValues() {
        usersCB.setSelectedItem(rating.getUserId().getName());
        setButtonGroupValue(buttonGroup1, rating.getSpatialClarityId().getId());
        setButtonGroupValue(buttonGroup2, rating.getIlluminationId().getId());
        setButtonGroupValue(buttonGroup3, rating.getTrafficId().getId());
        CommentsTA.setText(rating.getComment() == null ? "": rating.getComment());
        imageTF.setText(rating.getImageWeblink() == null ? "" : rating.getImageWeblink());
    }
}
