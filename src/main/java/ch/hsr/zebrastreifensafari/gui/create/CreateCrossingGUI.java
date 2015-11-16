package ch.hsr.zebrastreifensafari.gui.create;

import ch.hsr.zebrastreifensafari.gui.CreateEditGUI;
import ch.hsr.zebrastreifensafari.gui.view.MainGUI;

import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

import javax.swing.*;
import java.util.Date;

/**
 *
 * @author aeugster
 */
public class CreateCrossingGUI extends CreateEditGUI {

    public CreateCrossingGUI(MainGUI mainGUI) {
        super(mainGUI, DataServiceLoader.getBundleString("createCrossingGuiTitle"));
        pack();
    }

    @Override
    protected void onSendClick() {
        try {
            //todo: überarbeiten, wenn ein bild wiederholt verwendet wird, wird trozdem ein crossing erstellt
            long osmNode = Long.parseLong(osmNodeIdTextField.getText());
            Crossing crossing = mainGUI.getCrossing(osmNode);

            if (crossing == null) {
                crossing = new Crossing(null, osmNode, 1, 1);
            }

            observable.notifyObservers(crossing);
            DataServiceLoader.getCrossingData().addRating(
                    new Rating(
                            null,
                            commentTextArea.getText().isEmpty() ? null : commentTextArea.getText(),
                            mainGUI.getIllumination(getSelectedButtonInt(illuminationButtonGroup)),
                            mainGUI.getSpatialClarity(getSelectedButtonInt(spatialClarityButtonGroup)),
                            mainGUI.getTraffic(getSelectedButtonInt(trafficButtonGroup)),
                            mainGUI.getUser(userComboBox.getSelectedItem().toString()),
                            crossing,
                            imageTextField.getText().isEmpty() ? null : imageTextField.getText(),
                            new Date()
                    )
            );

            this.dispose();
        } catch (NumberFormatException nfex) {
            errorMessage(DataServiceLoader.getBundleString("osmNodeIdNumericError"));
        } catch (Exception e) {
            errorMessage(DataServiceLoader.getBundleString("duplicatedPhotoError"));
        }
    }
}
