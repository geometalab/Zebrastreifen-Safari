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
public class CreateZebracrossingGUI extends CreateUpdateGUI {

    private View view;
    private Model model;

    /**
     * Creates new form GUI
     *
     * @param users the users which are listed in the JCombobox
     */

    public CreateZebracrossingGUI(List<User> users, View view, Model model) {
        super(users);
        this.model = model;
        this.view = view;
    }

    @Override
    protected void onSendClick() {
        Zebracrossing z = new Zebracrossing(null, Long.parseLong(osmNode.getText()), file == null ? null : file.getName(), null);

        if (DataServiceLoader.getZebraData().getZebracrossings().stream().filter(zebracrossing -> zebracrossing.getNode() == z.getNode()).count() == 0) {
            DataServiceLoader.getZebraData().addZebracrossing(z);
            model.getZebras().add(z);
        }

        Rating r = new Rating(null, CommentsTA.getText(),
                DataServiceLoader.getZebraData().getIlluminationValue(getSelectedButtonInt(buttonGroup2)),
                DataServiceLoader.getZebraData().getOverviewValue(getSelectedButtonInt(buttonGroup1)), DataServiceLoader.getZebraData().getTrafficValue(getSelectedButtonInt(buttonGroup3)),
                DataServiceLoader.getZebraData().getUserByName((String) usersCB.getSelectedItem()), DataServiceLoader.getZebraData().getZebracrossingByNode(z.getNode()));

        DataServiceLoader.getZebraData().getZebracrossingByNode(z.getNode()).getRatingList().add(r);
        DataServiceLoader.getZebraData().addRating(r);
        model.getRatings().add(r);
        view.addDataToTable();
        this.dispose();
    }
}
