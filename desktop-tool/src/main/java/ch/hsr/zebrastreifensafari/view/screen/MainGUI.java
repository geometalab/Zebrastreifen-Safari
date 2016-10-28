package ch.hsr.zebrastreifensafari.view.screen;

import ch.hsr.zebrastreifensafari.controller.AboutController;
import ch.hsr.zebrastreifensafari.controller.MainController;
import ch.hsr.zebrastreifensafari.controller.callback.IMainCallback;
import ch.hsr.zebrastreifensafari.controller.callback.table.ICrossingTable;
import ch.hsr.zebrastreifensafari.controller.callback.table.IRatingTable;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import ch.hsr.zebrastreifensafari.service.Properties;
import ch.hsr.zebrastreifensafari.view.component.JTextPlaceHolder;
import ch.hsr.zebrastreifensafari.view.screen.modify.create.CreateCrossingGUI;
import ch.hsr.zebrastreifensafari.view.screen.modify.create.CreateRatingGUI;
import ch.hsr.zebrastreifensafari.view.screen.modify.edit.EditCrossingGUI;
import ch.hsr.zebrastreifensafari.view.screen.modify.edit.EditRatingGUI;
import ch.hsr.zebrastreifensafari.view.table.CrossingTable;
import ch.hsr.zebrastreifensafari.view.table.RatingTable;

import javax.persistence.EntityNotFoundException;
import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 09:49
 * @date : 27.10.2015
 */

public class MainGUI extends JFrame implements IMainCallback {

    private final Model model;
    private final MainController controller;
    private JPanel mainPanel;
    private JTextField searchTextField;
    private JButton addCrossingButton;
    private JButton editCrossingButton;
    private JButton deleteCrossingButton;
    private CrossingTable crossingTable;
    private RatingTable ratingTable;
    private JMenuItem exitMenuItem;
    private JMenuItem refreshMenuItem;
    private JMenuItem addMenuItem;
    private JMenuItem editMenuItem;
    private JMenuItem deleteMenuItem;
    private JMenuItem helpMenuItem;
    private JMenuItem aboutMenuItem;
    private JTabbedPane dataTabbedPane;
    private JButton addRatingButton;
    private JButton editRatingButton;
    private JButton deleteRatingButton;
    private JButton previousCrossingButton;
    private JButton nextCrossingButton;

    public MainGUI(MainController controller, Model model) throws HeadlessException {
        super(Properties.get("mainGuiTitle") + Properties.get("versionNumber"));
        this.controller = controller;
        this.model = model;
        controller.subscribe(this);
        $$$setupUI$$$();
        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initListeners();
        getCrossingTable().drawData(model.getCrossings());
        pack();
        setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    //<editor-fold desc="Listeners">
    private void initListeners() {
        dataTabbedPane.addChangeListener(e -> onTabbedPaneChange());
        initCrossingListeners();
        initRatingListeners();
        initMenuListeners();
    }

    private void initCrossingListeners() {
        addCrossingButton.addActionListener(e -> onAddClick());
        editCrossingButton.addActionListener(e -> onEditClick());
        deleteCrossingButton.addActionListener(e -> onDeleteClick());
        crossingTable.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onCrossingSort(e);
            }
        });
        crossingTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onTableDoubleClick(e);
            }
        });
        crossingTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    setSelectedTabbedPaneIndex(1);
                }
            }
        });
        crossingTable.getSelectionModel().addListSelectionListener(e -> onCrossingSelection());
        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                onSearch();
            }
        });
    }

    private void initRatingListeners() {
        addRatingButton.addActionListener(e -> onAddClick());
        editRatingButton.addActionListener(e -> onEditClick());
        deleteRatingButton.addActionListener(e -> onDeleteClick());
        ratingTable.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onRatingSort(e);
            }
        });
        ratingTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onTableDoubleClick(e);
            }
        });
        ratingTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    setSelectedTabbedPaneIndex(0);
                }
            }
        });
        previousCrossingButton.addActionListener(e -> onPreviousCrossingClick());
        nextCrossingButton.addActionListener(e -> onNextCrossingClick());
    }

    private void initMenuListeners() {
        refreshMenuItem.addActionListener(e -> onRefreshClick());
        exitMenuItem.addActionListener(e -> onExitClick());
        addMenuItem.addActionListener(e -> onAddClick());
        editMenuItem.addActionListener(e -> onEditClick());
        deleteMenuItem.addActionListener(e -> onDeleteClick());
        helpMenuItem.addActionListener(e -> onHelpClick());
        aboutMenuItem.addActionListener(e -> onAboutClick());
    }
    //</editor-fold>

    //<editor-fold desc="Actions">
    private void onTabbedPaneChange() {
        controller.onTabbedPaneChange();
    }

    private void onCrossingSelection() {
        controller.onCrossingSelection();
    }

    private void onSearch() {
        controller.onSearch(searchTextField.getText());
    }

    private void onAddClick() {
        controller.onAddClick();
    }

    private void onEditClick() {
        controller.onEditClick();
    }

    private void onDeleteClick() {
        if (warningMessage(Properties.get("deleteWarning"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            controller.onDelete();
        }
    }

    private void onPreviousCrossingClick() {
        controller.onPreviousCrossingClick();
    }

    private void onNextCrossingClick() {
        controller.onNextCrossingClick();
    }

    private void onRefreshClick() {
        controller.onRefreshClick();
    }

    private void onExitClick() {
        System.exit(0);
    }

    private void onAboutClick() {
        new AboutGUI(this, new AboutController()).setVisible(true);
    }

    private void onHelpClick() {
        controller.onHelpClick();
    }

    private void onCrossingSort(MouseEvent event) {
        controller.onCrossingSort(crossingTable.getColumnName(crossingTable.columnAtPoint(event.getPoint())));
        //todo move to controller
        searchTextField.getKeyListeners()[0].keyReleased(null);
    }

    private void onRatingSort(MouseEvent event) {
        controller.onRatingSort(ratingTable.getColumnName(ratingTable.columnAtPoint(event.getPoint())));
    }

    private void onTableDoubleClick(MouseEvent event) {
        controller.onTableDoubleClick(event.getClickCount());
    }
    //</editor-fold>

    private Crossing getCrossingFromTable() {
        return model.getCrossing(getCrossingTable().getSelectedId());
    }

    private Rating getRatingFromTable() {
        return model.getRating(getRatingTable().getSelectedId());
    }

    private int warningMessage(String message, int option) {
        return JOptionPane.showConfirmDialog(this, message, Properties.get("warning"), option);
    }

    public void createCrossing(Crossing crossing) {
        controller.createCrossing(crossing, searchTextField.getText());
    }

    public void editCrossing(Crossing crossing) throws EntityNotFoundException {
        controller.editCrossing(crossing, searchTextField.getText());
    }

    @Override
    public void removeCrossing() {
        controller.removeCrossing();
    }

    public void createRating(Rating rating) throws EntityNotFoundException {
        controller.createRating(rating);
    }

    public void editRating(Rating rating) throws EntityNotFoundException {
        controller.editRating(rating);
    }

    @Override
    public void removeRating() {
        controller.removeRating();
    }

    //<editor-fold desc="Model methods">
    public User getUser(String name) {
        return controller.getUser(name);
    }

    public Crossing getCrossing(long node) {
        return controller.getCrossing(node);
    }

    public Illumination getIllumination(int id) {
        return controller.getIllumination(id);
    }

    public SpatialClarity getSpatialClarity(int id) {
        return controller.getSpatialClarity(id);
    }

    public Traffic getTraffic(int id) {
        return controller.getTraffic(id);
    }

    public List<User> getUsers() {
        return controller.getUsers();
    }
    //</editor-fold>

    @Override
    public void createCrossing() {
        new CreateCrossingGUI(this).setVisible(true);
    }

    @Override
    public void createRating() {
        new CreateRatingGUI(this, getCrossingFromTable().getOsmNodeId()).setVisible(true);
    }

    @Override
    public void editCrossing() {
        new EditCrossingGUI(this, getCrossingFromTable()).setVisible(true);
    }

    @Override
    public void editRating() {
        new EditRatingGUI(this, getRatingFromTable()).setVisible(true);
    }

    @Override
    public ICrossingTable getCrossingTable() {
        return crossingTable;
    }

    @Override
    public IRatingTable getRatingTable() {
        return ratingTable;
    }

    @Override
    public void setSelectedTabbedPaneIndex(int index) {
        dataTabbedPane.setSelectedIndex(index);
    }

    @Override
    public void setRatingTabbedPaneTitle(String title) {
        dataTabbedPane.setTitleAt(1, title);
    }

    @Override
    public boolean isRatingMode() {
        return dataTabbedPane.getSelectedIndex() == 1;
    }

    @Override
    public void errorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, Properties.get("error"), JOptionPane.ERROR_MESSAGE);
    }

    //<editor-fold desc="GUI Builder">
    private void createUIComponents() {
        crossingTable = new CrossingTable();
        ratingTable = new RatingTable();
        searchTextField = new JTextPlaceHolder(Properties.get("searchPlaceHolder"));

        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu(Properties.get("file"));
        JMenu editMenu = new JMenu(Properties.get("edit"));
        JMenu helpMenu = new JMenu(Properties.get("help"));
        refreshMenuItem = new JMenuItem(Properties.get("refresh"));
        exitMenuItem = new JMenuItem(Properties.get("exit"));
        JMenuItem cutMenuItem = new JMenuItem(new DefaultEditorKit.CutAction());
        cutMenuItem.setText(Properties.get("cut"));
        JMenuItem copyMenuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
        copyMenuItem.setText(Properties.get("copy"));
        JMenuItem pasteMenuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
        pasteMenuItem.setText(Properties.get("paste"));
        addMenuItem = new JMenuItem(Properties.get("add"));
        editMenuItem = new JMenuItem(Properties.get("edit"));
        deleteMenuItem = new JMenuItem(Properties.get("delete"));
        helpMenuItem = new JMenuItem(Properties.get("help"));
        aboutMenuItem = new JMenuItem(Properties.get("about"));

        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
        refreshMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        addMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        editMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
        deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));

        fileMenu.add(refreshMenuItem);
        fileMenu.add(exitMenuItem);
        editMenu.add(cutMenuItem);
        editMenu.add(copyMenuItem);
        editMenu.add(pasteMenuItem);
        editMenu.add(new JSeparator());
        editMenu.add(addMenuItem);
        editMenu.add(editMenuItem);
        editMenu.add(deleteMenuItem);
        helpMenu.add(helpMenuItem);
        helpMenu.add(aboutMenuItem);
        bar.add(fileMenu);
        bar.add(editMenu);
        bar.add(helpMenu);
        setJMenuBar(bar);

        cutMenuItem.setEnabled(false);
        copyMenuItem.setEnabled(false);
        pasteMenuItem.setEnabled(false);
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
        dataTabbedPane = new JTabbedPane();
        mainPanel.add(dataTabbedPane, BorderLayout.CENTER);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        dataTabbedPane.addTab(ResourceBundle.getBundle("Bundle").getString("defaultCrossingTabbedPaneTitle"), panel1);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5), null));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        panel1.add(panel2, BorderLayout.NORTH);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0), null));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panel2.add(panel3, BorderLayout.EAST);
        addCrossingButton = new JButton();
        addCrossingButton.setActionCommand(ResourceBundle.getBundle("Bundle").getString("add"));
        addCrossingButton.setHideActionText(false);
        this.$$$loadButtonText$$$(addCrossingButton, ResourceBundle.getBundle("Bundle").getString("add"));
        panel3.add(addCrossingButton);
        editCrossingButton = new JButton();
        editCrossingButton.setActionCommand(ResourceBundle.getBundle("Bundle").getString("edit"));
        this.$$$loadButtonText$$$(editCrossingButton, ResourceBundle.getBundle("Bundle").getString("edit"));
        panel3.add(editCrossingButton);
        deleteCrossingButton = new JButton();
        deleteCrossingButton.setActionCommand(ResourceBundle.getBundle("Bundle").getString("delete"));
        this.$$$loadButtonText$$$(deleteCrossingButton, ResourceBundle.getBundle("Bundle").getString("delete"));
        panel3.add(deleteCrossingButton);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new BorderLayout(0, 0));
        panel2.add(panel4, BorderLayout.WEST);
        searchTextField.setColumns(19);
        searchTextField.setMargin(new Insets(0, 0, 0, 0));
        searchTextField.setPreferredSize(new Dimension(223, 24));
        panel4.add(searchTextField, BorderLayout.CENTER);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, BorderLayout.CENTER);
        scrollPane1.setViewportView(crossingTable);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new BorderLayout(0, 0));
        dataTabbedPane.addTab(ResourceBundle.getBundle("Bundle").getString("defaultRatingTabbedPaneTitle"), panel5);
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5), null));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel5.add(scrollPane2, BorderLayout.CENTER);
        ratingTable.setRowSelectionAllowed(true);
        scrollPane2.setViewportView(ratingTable);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new BorderLayout(0, 0));
        panel5.add(panel6, BorderLayout.NORTH);
        panel6.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0), null));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panel6.add(panel7, BorderLayout.EAST);
        panel7.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), null));
        addRatingButton = new JButton();
        addRatingButton.setActionCommand(ResourceBundle.getBundle("Bundle").getString("add"));
        addRatingButton.setHorizontalAlignment(4);
        this.$$$loadButtonText$$$(addRatingButton, ResourceBundle.getBundle("Bundle").getString("add"));
        panel7.add(addRatingButton);
        editRatingButton = new JButton();
        editRatingButton.setActionCommand(ResourceBundle.getBundle("Bundle").getString("edit"));
        editRatingButton.setHorizontalAlignment(4);
        this.$$$loadButtonText$$$(editRatingButton, ResourceBundle.getBundle("Bundle").getString("edit"));
        panel7.add(editRatingButton);
        deleteRatingButton = new JButton();
        deleteRatingButton.setActionCommand(ResourceBundle.getBundle("Bundle").getString("delete"));
        deleteRatingButton.setHorizontalAlignment(4);
        this.$$$loadButtonText$$$(deleteRatingButton, ResourceBundle.getBundle("Bundle").getString("delete"));
        panel7.add(deleteRatingButton);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panel6.add(panel8, BorderLayout.WEST);
        panel8.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), null));
        previousCrossingButton = new JButton();
        previousCrossingButton.setText("<");
        panel8.add(previousCrossingButton);
        nextCrossingButton = new JButton();
        nextCrossingButton.setText(">");
        panel8.add(nextCrossingButton);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    //</editor-fold>
}
