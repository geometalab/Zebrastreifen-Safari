package ch.hsr.zebrastreifensafari.gui.update;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.*;

import ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.gui.view.View;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

/**
 *
 * @author aeugster
 */
public class UpdateGUI extends CreateUpdateGUI {

    private Rating rating;

    /**
     * Creates new form GUI s
     *
     * @param users the users which are listed in the JCombobox
     * @param rating
     */
    public UpdateGUI(List<User> users, Rating rating, View view) {
        super(users, view);
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
        rating.setZebracrossingFk(DataServiceLoader.getZebraData().getZebracrossingByNode(z.getNode()));
        rating.setUserFk(DataServiceLoader.getZebraData().getUserByName((String) usersCB.getSelectedItem()));
        DataServiceLoader.getZebraData().updateRating(rating);

        observable.notifyObservers(null);
        //view.addDataToTable();

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
        osmNode.setText(Long.toString(DataServiceLoader.getZebraData().getZebracrossingById(rating.getRatingId()).getNode()));
        imageTF.setText(DataServiceLoader.getZebraData().getZebracrossingById(rating.getRatingId()).getImage() == null ? "" : DataServiceLoader.getZebraData().getZebracrossingById(rating.getRatingId()).getImage());
        setButtonGroupValue(buttonGroup1, rating.getOverviewFk().getOverviewId());
        setButtonGroupValue(buttonGroup2, rating.getIlluminationFk().getIlluminationId());
        setButtonGroupValue(buttonGroup3, rating.getTrafficFk().getTrafficId());
        usersCB.setSelectedItem(rating.getUserFk().getName());
    }
}
