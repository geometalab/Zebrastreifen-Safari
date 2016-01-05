package ch.hsr.zebrastreifensafari.gui.create;

import ch.hsr.zebrastreifensafari.gui.MainGUI;
import ch.hsr.zebrastreifensafari.gui.ModifyGUI;
import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;
import ch.hsr.zebrastreifensafari.jpa.entities.Rating;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import ch.hsr.zebrastreifensafari.service.Properties;

import javax.persistence.RollbackException;
import java.util.Date;

/**
 * @author aeugster
 */
public class CreateCrossingGUI extends ModifyGUI {

    public CreateCrossingGUI(MainGUI mainGUI) {
        super(mainGUI, Properties.get("createCrossingGuiTitle"));
    }

    @Override
    protected void onSendClick() {
        //todo: überarbeiten, wenn ein bild wiederholt verwendet wird, wird trozdem ein crossing erstellt (momentane lösung funktioniert ist aber nicht schön)
        try {
            if (createRating(createCrossing())) {
                dispose();
            }
        } catch (NumberFormatException nfex) {
            errorMessage(Properties.get("osmNodeIdNumericError"));
        } catch (RollbackException rex) {
            errorMessage(Properties.get("connectionError"));
        } catch (Exception ex) {
            ex.printStackTrace();
            errorMessage(Properties.get("unexpectedError"));
        }
    }

    private Crossing createCrossing() {
        long osmNode = Long.parseLong(osmNodeIdTextField.getText());
        Crossing crossing = mainGUI.getCrossing(osmNode);

        if (crossing == null) {
            crossing = new Crossing(null, osmNode, 1, 1);
        }

        mainGUI.createCrossing(crossing);
        return crossing;
    }

    private boolean createRating(Crossing crossing) {
        try {
            DataServiceLoader.getCrossingData().createRating(
                    new Rating(
                            null,
                            commentTextArea.getText().isEmpty() ? null : commentTextArea.getText(),
                            mainGUI.getIllumination(getSelectedButtonInt(illuminationButtonGroup)),
                            mainGUI.getSpatialClarity(getSelectedButtonInt(spatialClarityButtonGroup)),
                            mainGUI.getTraffic(getSelectedButtonInt(trafficButtonGroup)),
                            mainGUI.getUser(userComboBox.getSelectedItem().toString()),
                            crossing,
                            imageTextField.getText().isEmpty() ? null : imageTextField.getText(),
                            new Date(),
                            getCreationTime()
                    )
            );

            return true;
        } catch (RollbackException rex) {
            errorMessage(Properties.get("duplicatedPhotoError"));
        } catch (NumberFormatException nfex) {
            errorMessage(Properties.get("invalideTimeFormatError"));
        } catch (IllegalArgumentException iaex) {
            errorMessage(Properties.get("invalideTimeError"));
        }

        mainGUI.removeCrossing();
        return false;
    }
}
