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
public class UpdateGUI extends CreateUpdateGUI {

    private Rating rating;

    public UpdateGUI(Model model, Rating rating, View view) {
        super(model, view);
        this.rating = rating;
        updateComponents();
    }

    @Override
    protected void onSendClick() {
        Zebracrossing z = new Zebracrossing(null, Long.parseLong(osmNode.getText()), file == null ? null : file.getName(), null);

        rating.setComment(CommentsTA.getText());
        rating.setIlluminationFk(DataServiceLoader.getZebraData().getIlluminationValue(getSelectedButtonInt(buttonGroup2)));
        rating.setOverviewFk(DataServiceLoader.getZebraData().getOverviewValue(getSelectedButtonInt(buttonGroup1)));
        rating.setTrafficFk(DataServiceLoader.getZebraData().getTrafficValue(getSelectedButtonInt(buttonGroup3)));
        rating.setZebracrossingFk(model.getZebracrossing(z.getNode()));
        rating.setUserFk(model.getUser((String) usersCB.getSelectedItem()));
        DataServiceLoader.getZebraData().updateRating(rating);

        observable.notifyObservers(null);

        this.dispose();
    }

    public void setButtonGroupValue(ButtonGroup bg, int selectedButtonInt) {
        int counter = 1;
        for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (counter == selectedButtonInt) {
                button.setSelected(true);
                return;
            }

            counter++;

        }

    }

    private void updateComponents() {
        CommentsTA.setText(rating.getComment() == null ? "": rating.getComment());
        osmNode.setText(Long.toString(model.getZebracrossing(rating.getZebracrossingFk().getZebracrossingId()).getNode()));
        imageTF.setText(model.getZebracrossing(rating.getZebracrossingFk().getZebracrossingId()).getImage() == null ? "" : model.getZebracrossing(rating.getZebracrossingFk().getZebracrossingId()).getImage());
        setButtonGroupValue(buttonGroup1, rating.getOverviewFk().getOverviewId());
        setButtonGroupValue(buttonGroup2, rating.getIlluminationFk().getIlluminationId());
        setButtonGroupValue(buttonGroup3, rating.getTrafficFk().getTrafficId());
        usersCB.setSelectedItem(rating.getUserFk().getName());
    }
}
