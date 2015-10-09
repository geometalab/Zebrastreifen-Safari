package ch.hsr.zebrastreifensafari.gui.create;

import ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import ch.hsr.zebrastreifensafari.gui.view.View;
import java.io.File;
import java.util.List;

import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

/**
 *
 * @author aeugster
 */
public class CreateRatingGUI extends CreateUpdateGUI {

    private File f;
    private View view;
    private Model model;

    /**
     * Creates new form GUI
     *
     * @param users the users which are listed in the JCombobox
     */

    public CreateRatingGUI(List<User> users, long node, View view) {
        super(users, view);

            osmNode.setText(Long.toString(node));
            osmNode.setEnabled(false);
    }

    @Override
    protected void onSendClick() {
        //TODO: node redundency
        Zebracrossing z = new Zebracrossing(null, Long.parseLong(osmNode.getText()), f == null ? null : f.getName(), null);

        boolean unique = true;
        for (Zebracrossing zebra : DataServiceLoader.getZebraData().getZebracrossings()) {
            if (zebra.getNode() == z.getNode()) {
                unique = false;
            }
        }

        if (unique) {
            DataServiceLoader.getZebraData().addZebracrossing(z);

        }

        Rating r = new Rating(null, CommentsTA.getText(),
                DataServiceLoader.getZebraData().getIlluminationValue(getSelectedButtonInt(buttonGroup2)),
                DataServiceLoader.getZebraData().getOverviewValue(getSelectedButtonInt(buttonGroup1)), DataServiceLoader.getZebraData().getTrafficValue(getSelectedButtonInt(buttonGroup3)),
                DataServiceLoader.getZebraData().getUserByName((String) usersCB.getSelectedItem()), DataServiceLoader.getZebraData().getZebracrossingByNode(z.getNode()));

        DataServiceLoader.getZebraData().getZebracrossingByNode(z.getNode()).getRatingList().add(r);
        DataServiceLoader.getZebraData().addRating(r);

        observable.notifyObservers(r);
        //model.getRatings().add(r);
        //view.addDataToTable();

        this.dispose();
    }
}