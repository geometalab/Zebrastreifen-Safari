package ch.hsr.zebrastreifensafari.gui.update;

import java.util.Enumeration;
import javax.swing.*;

import ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.gui.view.View;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

/**
 *
 * @author aeugster
 */
public class UpdateRatingGUI extends CreateUpdateGUI {

    private Rating rating;

    public UpdateRatingGUI(Model model, View view, Rating rating) {
        super(model, view, "Update the rating from " + rating.getUserFk().getName() + " for the zebracrossing " + rating.getZebracrossingFk().getNode());
        this.rating = rating;
        setValues();
        jLabel1.setVisible(false);
        jLabel7.setVisible(false);
        osmNode.setVisible(false);
        imageTF.setVisible(false);
        chooseFile.setVisible(false);
    }

    @Override
    protected void onSendClick() {
        rating.setUserFk(model.getUser((String) usersCB.getSelectedItem()));
        rating.setIlluminationFk(model.getIllumination(getSelectedButtonInt(buttonGroup2)));
        rating.setOverviewFk(model.getOverview(getSelectedButtonInt(buttonGroup1)));
        rating.setTrafficFk(model.getTraffic(getSelectedButtonInt(buttonGroup3)));
        rating.setComment(CommentsTA.getText());
        DataServiceLoader.getZebraData().updateRating(rating);
        observable.notifyObservers(rating.getZebracrossingFk());
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
        usersCB.setSelectedItem(rating.getUserFk().getName());
        setButtonGroupValue(buttonGroup1, rating.getOverviewFk().getOverviewId());
        setButtonGroupValue(buttonGroup2, rating.getIlluminationFk().getIlluminationId());
        setButtonGroupValue(buttonGroup3, rating.getTrafficFk().getTrafficId());
        CommentsTA.setText(rating.getComment() == null ? "": rating.getComment());
    }
}
