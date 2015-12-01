package ch.hsr.zebrastreifensafari.gui.modify.create;

import ch.hsr.zebrastreifensafari.gui.modify.ModifyGUI;
import ch.hsr.zebrastreifensafari.gui.main.MainGUI;

import ch.hsr.zebrastreifensafari.jpa.entities.Rating;
import ch.hsr.zebrastreifensafari.service.Properties;
import org.eclipse.persistence.exceptions.DatabaseException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.RollbackException;
import java.util.Date;

/**
 *
 * @author aeugster
 */
public class CreateRatingGUI extends ModifyGUI {

    private final long node;

    public CreateRatingGUI(MainGUI mainGUI, long node) {
        super(mainGUI, Properties.get("createRatingGuiTitle") + node);
        this.node = node;
        osmNodeIdLabel.setVisible(false);
        osmNodeIdTextField.setVisible(false);
    }

    @Override
    protected void onSendClick() {
        try {
            Rating rating = new Rating(
                    null,
                    commentTextArea.getText().isEmpty() ? null : commentTextArea.getText(),
                    mainGUI.getIllumination(getSelectedButtonInt(illuminationButtonGroup)),
                    mainGUI.getSpatialClarity(getSelectedButtonInt(spatialClarityButtonGroup)),
                    mainGUI.getTraffic(getSelectedButtonInt(trafficButtonGroup)),
                    mainGUI.getUser((String) userComboBox.getSelectedItem()),
                    mainGUI.getCrossing(node),
                    imageTextField.getText().isEmpty() ? null : imageTextField.getText(),
                    new Date()
            );

            mainGUI.createRating(rating);
            this.dispose();
        } catch (EntityNotFoundException enfex) {
            errorMessage(Properties.get("crossingExistError"));
        } catch (RollbackException rex) {
            errorMessage(Properties.get("duplicatedPhotoError"));
        } catch (DatabaseException dbex) {
            errorMessage(Properties.get("connectionError"));
        } catch (Exception ex) {
            ex.printStackTrace();
            errorMessage(Properties.get("unexpectedError"));
        }
    }
}
