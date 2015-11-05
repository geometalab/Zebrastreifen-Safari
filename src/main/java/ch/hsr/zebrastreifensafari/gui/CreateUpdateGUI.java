package ch.hsr.zebrastreifensafari.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Enumeration;
import java.util.Observer;

import ch.hsr.zebrastreifensafari.gui.view.ObservableHelper;
import ch.hsr.zebrastreifensafari.gui.view.MainGUI;
import ch.hsr.zebrastreifensafari.jpa.entities.User;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 10:46
 * @date : 26.10.2015
 */

public abstract class CreateUpdateGUI extends JDialog {

    protected JPanel mainPanel;
    protected JButton sendButton;
    protected JButton cancelButton;
    protected JTextField osmNodeIdTextField;
    protected JTextArea commentTextArea;
    protected JTextField imageTextField;
    protected JRadioButton spatialClarityGoodRadioButton;
    protected JRadioButton spatialClarityOkRadioButton;
    protected JRadioButton spatialClarityBadRadioButton;
    protected JRadioButton trafficFewRadioButton;
    protected JRadioButton trafficLittleRadioButton;
    protected JRadioButton trafficALotRadioButton;
    protected JRadioButton illuminationGoodRadioButton;
    protected JRadioButton illuminationBadRadioButton;
    protected JRadioButton illuminationOkRadioButton;
    protected JComboBox<String> userComboBox;
    protected JLabel osmNodeIdLabel;
    protected JLabel userLabel;
    protected JLabel spatialClarityLabel;
    protected JLabel illuminationLabel;
    protected JLabel trafficLabel;
    protected JLabel commentLabel;
    protected JLabel imageLabel;
    protected JButton validateButton;
    protected JRadioButton illuminationNoneRadioButton;
    protected ButtonGroup spatialClarityButtonGroupe;
    protected ButtonGroup illuminationButtonGroup;
    protected ButtonGroup trafficButtonGroup;

    protected ObservableHelper observable;
    protected MainGUI mainGUI;

    public CreateUpdateGUI(MainGUI mainGUI, String title) {
        super(mainGUI, title, true);
        $$$setupUI$$$();
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        observable = new ObservableHelper(this);
        observable.addObserver(mainGUI);
        this.mainGUI = mainGUI;

        for (User u : mainGUI.getUsers()) {
            userComboBox.addItem(u.getName());
        }

        initListeners();
    }

    private void initListeners() {
        sendButton.addActionListener(e -> {
            if (!checkValues()) {
                return;
            }

            onSendClick();
        });

        cancelButton.addActionListener(e -> dispose());
    }

    protected abstract void onSendClick();

    protected int getSelectedButtonInt(ButtonGroup bg) {
        Enumeration<AbstractButton> buttons = bg.getElements();

        for (int i = 1; buttons.hasMoreElements(); i++) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return i;
            }
        }

        return 0;
    }

    protected boolean checkValues() {
        if (spatialClarityButtonGroupe.getSelection() == null || illuminationButtonGroup.getSelection() == null || trafficButtonGroup.getSelection() == null || osmNodeIdTextField.getText() == null) {
            JOptionPane.showMessageDialog(this, "There is an Input missing", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public void addObserver(Observer observer) {
        observable.addObserver(observer);
    }

    //<editor-fold desc="GUI Builder">
    private void createUIComponents() {
        userComboBox = new JComboBox<String>();
        spatialClarityButtonGroupe = new ButtonGroup();
        illuminationButtonGroup = new ButtonGroup();
        trafficButtonGroup = new ButtonGroup();
        spatialClarityGoodRadioButton = new JRadioButton();
        spatialClarityOkRadioButton = new JRadioButton();
        spatialClarityBadRadioButton = new JRadioButton();
        illuminationGoodRadioButton = new JRadioButton();
        illuminationOkRadioButton = new JRadioButton();
        illuminationBadRadioButton = new JRadioButton();
        illuminationNoneRadioButton = new JRadioButton();
        trafficFewRadioButton = new JRadioButton();
        trafficLittleRadioButton = new JRadioButton();
        trafficALotRadioButton = new JRadioButton();
        spatialClarityButtonGroupe.add(spatialClarityGoodRadioButton);
        spatialClarityButtonGroupe.add(spatialClarityOkRadioButton);
        spatialClarityButtonGroupe.add(spatialClarityBadRadioButton);
        illuminationButtonGroup.add(illuminationGoodRadioButton);
        illuminationButtonGroup.add(illuminationOkRadioButton);
        illuminationButtonGroup.add(illuminationBadRadioButton);
        illuminationButtonGroup.add(illuminationNoneRadioButton);
        trafficButtonGroup.add(trafficFewRadioButton);
        trafficButtonGroup.add(trafficLittleRadioButton);
        trafficButtonGroup.add(trafficALotRadioButton);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(mainPanel.getFont().getName(), mainPanel.getFont().getStyle(), mainPanel.getFont().getSize())));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        mainPanel.add(panel1, BorderLayout.SOUTH);
        sendButton = new JButton();
        sendButton.setText("Erstellen");
        panel1.add(sendButton, BorderLayout.WEST);
        cancelButton = new JButton();
        cancelButton.setText("Abbruch");
        panel1.add(cancelButton, BorderLayout.EAST);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(9, 5, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel2, BorderLayout.CENTER);
        osmNodeIdLabel = new JLabel();
        osmNodeIdLabel.setText("OSM Node ID");
        panel2.add(osmNodeIdLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userLabel = new JLabel();
        userLabel.setText("User");
        panel2.add(userLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spatialClarityLabel = new JLabel();
        spatialClarityLabel.setText("Spatial Clarity");
        panel2.add(spatialClarityLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        illuminationLabel = new JLabel();
        illuminationLabel.setText("Illumination");
        panel2.add(illuminationLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        trafficLabel = new JLabel();
        trafficLabel.setText("Traffic");
        panel2.add(trafficLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        osmNodeIdTextField = new JTextField();
        panel2.add(osmNodeIdTextField, new GridConstraints(0, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        panel2.add(userComboBox, new GridConstraints(1, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        commentTextArea = new JTextArea();
        panel2.add(commentTextArea, new GridConstraints(5, 1, 3, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        commentLabel = new JLabel();
        commentLabel.setText("Comment");
        panel2.add(commentLabel, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        imageLabel = new JLabel();
        imageLabel.setText("Image");
        panel2.add(imageLabel, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        imageTextField = new JTextField();
        panel2.add(imageTextField, new GridConstraints(8, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        spatialClarityGoodRadioButton.setText("Gut");
        panel2.add(spatialClarityGoodRadioButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spatialClarityOkRadioButton.setText("Mittel");
        panel2.add(spatialClarityOkRadioButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        spatialClarityBadRadioButton.setText("Schlecht");
        panel2.add(spatialClarityBadRadioButton, new GridConstraints(2, 3, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        trafficFewRadioButton.setSelected(false);
        trafficFewRadioButton.setText("Wenig");
        panel2.add(trafficFewRadioButton, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        trafficLittleRadioButton.setText("Mittel");
        panel2.add(trafficLittleRadioButton, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        trafficALotRadioButton.setText("Viel");
        panel2.add(trafficALotRadioButton, new GridConstraints(4, 3, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(6, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        illuminationGoodRadioButton.setText("Gut");
        panel2.add(illuminationGoodRadioButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        illuminationOkRadioButton.setText("Mittel");
        panel2.add(illuminationOkRadioButton, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        illuminationBadRadioButton.setText("Schlecht");
        panel2.add(illuminationBadRadioButton, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        validateButton = new JButton();
        validateButton.setText("Prüfen");
        panel2.add(validateButton, new GridConstraints(8, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        illuminationNoneRadioButton.setText("Keine");
        panel2.add(illuminationNoneRadioButton, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    //</editor-fold>
}
