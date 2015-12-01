package ch.hsr.zebrastreifensafari.gui.modify.create;

import ch.hsr.zebrastreifensafari.gui.modify.ModifyGUI;
import ch.hsr.zebrastreifensafari.gui.main.MainGUI;

import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import ch.hsr.zebrastreifensafari.service.Properties;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.postgresql.util.PSQLException;

import javax.persistence.RollbackException;
import java.net.SocketException;
import java.util.Date;

/**
 *
 * @author aeugster
 */
public class CreateCrossingGUI extends ModifyGUI {

    public CreateCrossingGUI(MainGUI mainGUI) {
        super(mainGUI, Properties.get("createCrossingGuiTitle"));
    }

    @Override
    protected void onSendClick() {
        try {
            //todo: überarbeiten, wenn ein bild wiederholt verwendet wird, wird trozdem ein crossing erstellt (momentane lösung funktioniert ist aber nicht schön)
            long osmNode = Long.parseLong(osmNodeIdTextField.getText());
            Crossing crossing = mainGUI.getCrossing(osmNode);

            if (crossing == null) {
                crossing = new Crossing(null, osmNode, 1, 1);
            }

            mainGUI.createCrossing(crossing);

            try {
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
            } catch (RollbackException rex) {
                rex.printStackTrace();
                mainGUI.removeCrossing();
                errorMessage(Properties.get("duplicatedPhotoError"));
            }
        } catch (NumberFormatException nfex) {
            errorMessage(Properties.get("osmNodeIdNumericError"));
        } catch (RollbackException rex) {
            //todo: exceptionhandling
            rex.printStackTrace();
        } catch (DatabaseException dbex) {
            errorMessage(Properties.get("connectionError"));
        } catch (Exception ex) {
            ex.printStackTrace();
            errorMessage(Properties.get("unexpectedError"));
        }
    }
}
