package ch.hsr.zebrastreifensafari.gui.sub;

import javax.swing.*;
import java.awt.*;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 16:10
 * @date : 17.12.2015
 */

public abstract class CenterJDialog extends JDialog {

    public CenterJDialog(JFrame parent, String title, boolean modal, int width, int height) {
        super(parent, title, modal);
        setPosition(width, height);
    }

    private void setPosition(int width, int height) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((int) ((dimension.getWidth() - width) / 2), (int) ((dimension.getHeight() - height) / 2), width, height);
    }
}
