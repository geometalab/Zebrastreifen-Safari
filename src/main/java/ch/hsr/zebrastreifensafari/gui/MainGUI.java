package ch.hsr.zebrastreifensafari.gui;

import ch.hsr.zebrastreifensafari.gui.create.CreateCrossingGUI;
import ch.hsr.zebrastreifensafari.gui.create.CreateRatingGUI;
import ch.hsr.zebrastreifensafari.gui.edit.EditCrossingGUI;
import ch.hsr.zebrastreifensafari.gui.edit.EditRatingGUI;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import ch.hsr.zebrastreifensafari.service.Properties;
import ch.hsr.zebrastreifensafari.service.WebsiteService;
import org.eclipse.persistence.exceptions.DatabaseException;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 09:49
 * @date : 27.10.2015
 */

public class MainGUI extends JFrame {

    private final Model model;
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

    public MainGUI(Model model) throws HeadlessException {
        super(Properties.get("mainGuiTitle") + Properties.get("versionNumber"));
        $$$setupUI$$$();
        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.model = model;
        initListeners();
        crossingTable.drawData(model.getCrossings());
        pack();
        setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    private void initListeners() {
        dataTabbedPane.addChangeListener(e -> onTabbedPaneChange());
        crossingTable.getSelectionModel().addListSelectionListener(e -> onCrossingSelection());
        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                onSearch();
            }
        });
        addCrossingButton.addActionListener(e -> onAddClick());
        editCrossingButton.addActionListener(e -> onEditClick());
        deleteCrossingButton.addActionListener(e -> onDeleteClick());
        addRatingButton.addActionListener(e -> onAddClick());
        editRatingButton.addActionListener(e -> onEditClick());
        deleteRatingButton.addActionListener(e -> onDeleteClick());
        previousCrossingButton.addActionListener(e -> onPreviousCrossingClick());
        nextCrossingButton.addActionListener(e -> onNextCrossingClick());
        refreshMenuItem.addActionListener(e -> onRefreshClick());
        exitMenuItem.addActionListener(e -> onExitClick());
        addMenuItem.addActionListener(e -> onAddClick());
        editMenuItem.addActionListener(e -> onEditClick());
        deleteMenuItem.addActionListener(e -> onDeleteClick());
        helpMenuItem.addActionListener(e -> onHelpClick());
        aboutMenuItem.addActionListener(e -> onAboutClick());
        crossingTable.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onCrossingSort(e);
            }
        });
        ratingTable.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onRatingSort(e);
            }
        });
        crossingTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onTableDoubleClick(e);
            }
        });
        ratingTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onTableDoubleClick(e);
            }
        });
        crossingTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    dataTabbedPane.setSelectedIndex(1);
                }
            }
        });
        ratingTable.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    dataTabbedPane.setSelectedIndex(0);
                }
            }
        });
    }

    //<editor-fold desc="Actions">
    private void onTabbedPaneChange() {
        try {
            if (isRatingMode()) {
                model.reloadRating(getCrossingFromTable());
                ratingTable.drawData(model.getRatings());
            }
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            dataTabbedPane.setSelectedIndex(0);
            errorMessage(Properties.get("changeSelectionError"));
        } catch (PersistenceException pex) {
            dataTabbedPane.setSelectedIndex(0);
            errorMessage(Properties.get("connectionError"));
        }
    }

    private void onCrossingSelection() {
        if (crossingTable.hasData()) {
            dataTabbedPane.setTitleAt(1, Properties.get("specificRatingTabbedPaneTitle") + crossingTable.getOsmNodeIdAtSelectedRow());
        }
    }

    private void onSearch() {
        if (searchTextField.getText().isEmpty()) {
            crossingTable.drawData(model.getCrossings());
        } else {
            crossingTable.drawData(model.getCrossings().stream()
                    .filter(crossing -> String.valueOf(crossing.getOsmNodeId()).startsWith(searchTextField.getText()))
                    .collect(Collectors.toList()));
        }
    }

    private void onAddClick() {
        if (isRatingMode()) {
            new CreateRatingGUI(this, getCrossingFromTable().getOsmNodeId()).setVisible(true);
        } else {
            new CreateCrossingGUI(this).setVisible(true);
        }
    }

    private void onEditClick() {
        try {
            if (isRatingMode()) {
                new EditRatingGUI(this, getRatingFromTable()).setVisible(true);
            } else {
                new EditCrossingGUI(this, getCrossingFromTable()).setVisible(true);
            }
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            errorMessage(Properties.get("editSelectionError"));
        }
    }

    private void onDeleteClick() {
        if (warningMessage(Properties.get("deleteWarning"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                if (isRatingMode()) {
                    removeRating();
                } else {
                    removeCrossing();
                }
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                errorMessage(Properties.get("deleteSelectionError"));
            } catch (DatabaseException dbex) {
                errorMessage(Properties.get("connectionError"));
            }
        }
    }

    private void onPreviousCrossingClick() {
        if (crossingTable.getSelectedRow() != 0) {
            crossingTable.changeTableSelection(crossingTable.getSelectedRow() - 1);
        } else {
            crossingTable.changeTableSelection(crossingTable.getRowCount() - 1);
        }

        onTabbedPaneChange();
    }

    private void onNextCrossingClick() {
        if (crossingTable.getSelectedRow() != crossingTable.getRowCount() - 1) {
            crossingTable.changeTableSelection(crossingTable.getSelectedRow() + 1);
        } else {
            crossingTable.changeTableSelection(0);
        }

        onTabbedPaneChange();
    }

    private void onRefreshClick() {
        try {
            model.reloadUsers();

            if (isRatingMode()) {
                model.reloadRating(getCrossingFromTable());
                ratingTable.drawData(model.getRatings());
            } else {
                model.reloadCrossing();
                crossingTable.drawData(model.getCrossings());
            }
        } catch (PersistenceException pex) {
            errorMessage(Properties.get("connectionError"));
        }
    }

    private void onExitClick() {
        System.exit(0);
    }

    private void onAboutClick() {
        new AboutGUI(this).setVisible(true);
    }

    private void onHelpClick() {
        WebsiteService.openWebsite(Properties.get("helpLink"));
    }

    private void onCrossingSort(MouseEvent event) {
        String col = crossingTable.getColumnName(crossingTable.columnAtPoint(event.getPoint()));

        if (col.equals(Properties.get("osmNodeId"))) {
            model.sortByNode();
        } else if (col.equals(Properties.get("ratingAmount"))) {
            model.sortByNumberOfRatings();
        } else if (col.equals(Properties.get("status"))) {
            model.sortByStatus();
        }

        crossingTable.drawData(model.getCrossings());
        searchTextField.getKeyListeners()[0].keyReleased(null);
    }

    private void onRatingSort(MouseEvent event) {
        String col = ratingTable.getColumnName(ratingTable.columnAtPoint(event.getPoint()));

        if (col.equals(Properties.get("user"))) {
            model.sortByUser();
        } else if (col.equals(Properties.get("traffic"))) {
            model.sortByTraffic();
        } else if (col.equals(Properties.get("spacialClarity"))) {
            model.sortByClarity();
        } else if (col.equals(Properties.get("illumination"))) {
            model.sortByIllumination();
        } else if (col.equals(Properties.get("comment"))) {
            model.sortByComment();
        } else if (col.equals(Properties.get("imageId"))) {
            model.sortByImage();
        } else if (col.equals(Properties.get("lastChange"))) {
            model.sortByLastChanged();
        } else if (col.equals(Properties.get("creationDate"))) {
            model.sortByCreationTime();
        }

        ratingTable.drawData(model.getRatings());
    }

    private void onTableDoubleClick(MouseEvent event) {
        if (event.getClickCount() >= 2) {
            onEditClick();
        }
    }
    //</editor-fold>

    private Crossing getCrossingFromTable() {
        return model.getCrossing(crossingTable.getSelectedId());
    }

    private Rating getRatingFromTable() {
        return model.getRating(ratingTable.getSelectedId());
    }

    private void errorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, Properties.get("error"), JOptionPane.ERROR_MESSAGE);
    }

    private int warningMessage(String message, int option) {
        return JOptionPane.showConfirmDialog(this, message, Properties.get("warning"), option);
    }

    private boolean isRatingMode() {
        return dataTabbedPane.getSelectedIndex() == 1;
    }

    //<editor-fold desc="CRUD Crossing">
    public void createCrossing(Crossing crossing) {
        if (model.contains(crossing)) {
            createExistingCrossing(crossing);
        } else {
            createNewCrossing(crossing);
        }
    }

    private void createExistingCrossing(Crossing crossing) {
        crossing.increaseRatingAmount();

        if (searchTextField.getText().isEmpty()) {
            crossingTable.changeTableSelection(model.indexOf(crossing));
            crossingTable.setRatingAmountAtSelectedRow(crossing);
        } else if (Long.toString(crossing.getOsmNodeId()).startsWith(searchTextField.getText())) {
            for (int i = 0; i < crossingTable.getModel().getRowCount(); i++) {
                if (crossingTable.getOsmNodeIdAt(i) == crossing.getOsmNodeId()) {
                    crossingTable.changeTableSelection(i);
                    crossingTable.setRatingAmountAtSelectedRow(crossing);
                    break;
                }
            }
        }
    }

    private void createNewCrossing(Crossing crossing) {
        DataServiceLoader.getCrossingData().createCrossing(crossing);
        model.add(crossing);
        crossingTable.add(crossing);

        if (Long.toString(crossing.getOsmNodeId()).startsWith(searchTextField.getText()) || searchTextField.getText().isEmpty()) {
            crossingTable.changeTableSelection(crossingTable.getModel().getRowCount() - 1);
        } else {
            crossingTable.getModel().removeRow(crossingTable.getModel().getRowCount() - 1);
        }
    }

    public void editCrossing(Crossing crossing) throws EntityNotFoundException {
        DataServiceLoader.getCrossingData().editCrossing(crossing);

        if (searchTextField.getText().isEmpty() || Long.toString(crossing.getOsmNodeId()).startsWith(searchTextField.getText())) {
            crossingTable.setOsmNodeIdAtSelectedRow(crossing);
            onCrossingSelection();
        } else {
            crossingTable.getModel().removeRow(crossingTable.getSelectedRow());
        }
    }

    public void removeCrossing() {
        try {
            int selectedRow = crossingTable.getSelectedRow();
            Crossing crossing = getCrossingFromTable();
            DataServiceLoader.getCrossingData().removeCrossing(crossing.getId());
            crossingTable.remove(model.indexOf(crossing));
            model.remove(crossing);

            if (crossingTable.getModel().getRowCount() == selectedRow) {
                selectedRow--;
            }

            crossingTable.changeTableSelection(selectedRow);
        } catch (EntityNotFoundException enfex) {
            errorMessage(Properties.get("crossingExistError"));
        }
    }
    //</editor-fold>

    //<editor-fold desc="CRUD Rating">
    public void createRating(Rating rating) throws EntityNotFoundException {
        DataServiceLoader.getCrossingData().createRating(rating);
        model.add(rating);
        ratingTable.add(rating);
        ratingTable.changeTableSelection(ratingTable.getRowCount() - 1);
        Crossing crossingOfRating = model.getCrossing(rating.getCrossingId().getId());
        crossingOfRating.increaseRatingAmount();
        crossingTable.setRatingAmountAtSelectedRow(crossingOfRating);
    }

    public void editRating(Rating rating) throws EntityNotFoundException {
        DataServiceLoader.getCrossingData().editRating(rating);
        ratingTable.setUserIdAtSelectedRow(rating);
        ratingTable.setTrafficIdAtSelectedRow(rating);
        ratingTable.setSpatialClarityIdAtSelected(rating);
        ratingTable.setIlluminationIdAtSelectedRow(rating);
        ratingTable.setCommentAtSelectedRow(rating);
        ratingTable.setImageWeblinkAtSelectedRow(rating);
        ratingTable.setLastChangedAtSelectedRow(rating);
        ratingTable.setValueAtSelectedRowCreationTime(rating);
    }

    public void removeRating() {
        try {
            int selectedRow = ratingTable.getSelectedRow();
            Rating rating = getRatingFromTable();
            DataServiceLoader.getCrossingData().removeRating(rating.getId());
            ratingTable.remove(model.indexOf(rating));
            model.remove(rating);

            if (model.getRatings().isEmpty()) {
                removeCrossing();
                dataTabbedPane.setSelectedIndex(0);
            } else {
                if (ratingTable.getModel().getRowCount() == selectedRow) {
                    selectedRow--;
                }

                ratingTable.changeTableSelection(selectedRow);
                Crossing crossingOfRating = model.getCrossing(rating.getCrossingId().getId());
                crossingOfRating.decreaseRatingAmount();
                crossingTable.setRatingAmountAtSelectedRow(crossingOfRating);
            }
        } catch (EntityNotFoundException enfex) {
            errorMessage(Properties.get("ratingCrossingExistError"));
        }
    }
    //</editor-fold>

    //<editor-fold desc="Model methods">
    public User getUser(String name) {
        return model.getUser(name);
    }

    public Crossing getCrossing(long node) {
        return model.getCrossing(node);
    }

    public Illumination getIllumination(int id) {
        return model.getIllumination(id);
    }

    public SpatialClarity getSpatialClarity(int id) {
        return model.getSpatialClarity(id);
    }

    public Traffic getTraffic(int id) {
        return model.getTraffic(id);
    }

    public List<User> getUsers() {
        return model.getUsers();
    }
    //</editor-fold>

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
