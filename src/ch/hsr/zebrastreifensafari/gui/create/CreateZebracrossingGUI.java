package ch.hsr.zebrastreifensafari.gui.create;

import ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import ch.hsr.zebrastreifensafari.gui.view.View;

import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

/**
 *
 * @author aeugster
 */
public class CreateZebracrossingGUI extends CreateUpdateGUI {

    public CreateZebracrossingGUI(Model model, View view) {
        super(model, view);
    }

    @Override
    protected void onSendClick() {
        Zebracrossing z = new Zebracrossing(null, Long.parseLong(osmNode.getText()), file == null ? null : file.getName(), null);

        if (DataServiceLoader.getZebraData().getZebracrossings().stream().filter(zebracrossing -> zebracrossing.getNode() == z.getNode()).count() == 0) {
            DataServiceLoader.getZebraData().addZebracrossing(z);
            observable.notifyObservers(z);
        }

        Rating r = new Rating(null,
                CommentsTA.getText(),
                DataServiceLoader.getZebraData().getIlluminationValue(getSelectedButtonInt(buttonGroup2)),
                DataServiceLoader.getZebraData().getOverviewValue(getSelectedButtonInt(buttonGroup1)),
                DataServiceLoader.getZebraData().getTrafficValue(getSelectedButtonInt(buttonGroup3)),
                model.getUser((String) usersCB.getSelectedItem()),
                model.getZebracrossing(z.getNode()));

        model.getZebracrossing(z.getNode()).getRatingList().add(r);
        DataServiceLoader.getZebraData().addRating(r);
        observable.notifyObservers(r);
        this.dispose();
    }
}
