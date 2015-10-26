package ch.hsr.zebrastreifensafari.gui2;

import javax.swing.*;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 10:46
 * @date : 26.10.2015
 */

public abstract class CreateUpdateGUI extends JFrame {

    private JPanel mainPanel;
    private JButton sendButton;
    private JButton cancelButton;
    private JTextField osmNodeIdTextField;
    private JComboBox userComboBox;
    private JTextArea commentTextArea;
    private JTextField imageTextField;
    private JRadioButton spatialClarityGoodRadioButton;
    private JRadioButton spatialClarityOkRadioButton;
    private JRadioButton spatialClarityBadRadioButton;
    private JRadioButton trafficNoneRadioButton;
    private JRadioButton trafficLittleRadioButton;
    private JRadioButton trafficALotRadioButton;
    private JRadioButton illuminationGoodRadioButton;
    private JRadioButton illuminationBadRadioButton;
    private JRadioButton illuminationOkRadioButton;

    public CreateUpdateGUI() {
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
