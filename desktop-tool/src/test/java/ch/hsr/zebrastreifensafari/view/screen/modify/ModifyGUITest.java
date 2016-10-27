package ch.hsr.zebrastreifensafari.view.screen.modify;

import ch.hsr.zebrastreifensafari.TestJDBC;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.view.screen.MainGUI;
import org.junit.After;
import org.junit.Before;

import javax.swing.*;

import static org.junit.Assert.assertEquals;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 13:33
 * @date : 21.10.2015
 */
public class ModifyGUITest {

    private TestJDBC db;
    private ModifyGUI cug;

    @Before
    public void setUp() throws Exception {
        db = new TestJDBC();
        cug = new ModifyGUI(new MainGUI(new Model()), "changeSelectionError") {

            @Override
            protected void onSendClick() {
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        db.getConnection().close();
    }

    @org.junit.Test
    public void testGetSelectedButtonInt() throws Exception {
        ButtonGroup bg = new ButtonGroup();
        JButton b1 = new JButton("Button 1");
        JButton b2 = new JButton("Button 2");
        JButton b3 = new JButton("Button 3");
        bg.add(b1);
        bg.add(b2);
        bg.add(b3);
        b2.setSelected(true);
        assertEquals(2, cug.getSelectedButtonInt(bg));
    }
}
