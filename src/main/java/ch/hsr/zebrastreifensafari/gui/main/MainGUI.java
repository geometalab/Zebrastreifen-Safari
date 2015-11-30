package ch.hsr.zebrastreifensafari.gui.main;

import ch.hsr.zebrastreifensafari.gui.about.AboutGUI;
import ch.hsr.zebrastreifensafari.gui.modify.ModifyGUI;
import ch.hsr.zebrastreifensafari.gui.modify.create.CreateCrossingGUI;
import ch.hsr.zebrastreifensafari.gui.modify.create.CreateRatingGUI;
import ch.hsr.zebrastreifensafari.gui.modify.edit.EditCrossingGUI;
import ch.hsr.zebrastreifensafari.gui.modify.edit.EditRatingGUI;
import ch.hsr.zebrastreifensafari.jpa.controllers.exceptions.NonexistentEntityException;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import ch.hsr.zebrastreifensafari.service.Properties;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultEditorKit;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
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

    private JPanel mainPanel;
    private JTextField searchTextField;
    private JButton addButton, editButton, deleteButton;
    private JTable crossingDataTable, ratingDataTable;
    private JLabel searchLabel;
    private JMenuItem exitMenuItem, refreshMenuItem, addMenuItem, editMenuItem, deleteMenuItem, helpMenuItem, aboutMenuItem;
    private JTabbedPane dataTabbedPane;

    private final Model model;
    private DefaultTableModel ratingTableModel;
    private DefaultTableModel crossingTableModel;

    public MainGUI(Model model) throws HeadlessException {
        super(Properties.get("mainGuiTitle") + Properties.get("versionNumber"));
        $$$setupUI$$$();
        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.model = model;
        initListeners();
        addCrossingDataToTable(model.getCrossings());
        pack();
        setExtendedState(Frame.MAXIMIZED_BOTH);
        searchTextField.requestFocusInWindow();
    }

    private void initListeners() {
        dataTabbedPane.addChangeListener(e -> onTabbedPaneChange());
        crossingDataTable.getSelectionModel().addListSelectionListener(e -> onCrossingSelection());
        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                onSearch();
            }
        });
        addButton.addActionListener(e -> onAddClick());
        editButton.addActionListener(e -> onEditClick());
        deleteButton.addActionListener(e -> onDeleteClick());
        refreshMenuItem.addActionListener(e -> onRefreshClick());
        exitMenuItem.addActionListener(e -> onExitClick());
        addMenuItem.addActionListener(e -> onAddClick());
        editMenuItem.addActionListener(e -> onEditClick());
        deleteMenuItem.addActionListener(e -> onDeleteClick());
        helpMenuItem.addActionListener(e -> onHelpClick());
        aboutMenuItem.addActionListener(e -> onAboutClick());
        crossingDataTable.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onCrossingSort(e);
            }
        });
        ratingDataTable.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onRatingSort(e);
            }
        });
        crossingDataTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onCrossingDoubleClick(e);
            }
        });
    }

    //<editor-fold desc="Actions">
    private void onTabbedPaneChange() {
        try {
            if (isRatingMode()) {
                model.reloadRating(getCrossingFromTable());
                searchLabel.setVisible(false);
                searchTextField.setVisible(false);
                addRatingDataToTable(model.getRatings());
            } else {
                searchLabel.setVisible(true);
                searchTextField.setVisible(true);
                searchTextField.requestFocusInWindow();
            }
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            dataTabbedPane.setSelectedIndex(0);
            errorMessage(Properties.get("changeSelectionError"));
        }
    }

    private void onCrossingSelection() {
        if (!crossingDataTable.getSelectionModel().isSelectionEmpty() && crossingTableModel.getRowCount() > 0) {
            dataTabbedPane.setTitleAt(1, Properties.get("specificRatingTabbedPaneTitle") + crossingTableModel.getValueAt(
                    crossingDataTable.getSelectedRow(),
                    crossingDataTable.getColumn(Properties.get("osmNodeId")).getModelIndex()
            ));
        }
    }

    private void onSearch() {
        if (searchTextField.getText().isEmpty()) {
            addCrossingDataToTable(model.getCrossings());
        } else {
            addCrossingDataToTable(model.getCrossings().stream()
                    .filter(crossing -> String.valueOf(crossing.getOsmNodeId()).startsWith(searchTextField.getText()))
                    .collect(Collectors.toList()));
        }
    }

    private void onAddClick() {
        ModifyGUI modifyGUI;

        if (isRatingMode()) {
            modifyGUI = new CreateRatingGUI(this, getCrossingFromTable().getOsmNodeId());
        } else {
            modifyGUI = new CreateCrossingGUI(this);
        }

        modifyGUI.setVisible(true);
    }

    private void onEditClick() {
        try {
            ModifyGUI modifyGUI;

            if (isRatingMode()) {
                modifyGUI = new EditRatingGUI(this, getRatingFromTable());
            } else {
                modifyGUI = new EditCrossingGUI(this, getCrossingFromTable());
            }

            modifyGUI.setVisible(true);
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
            }
        }
    }

    private void onRefreshClick() {
        model.reloadUsers();

        if (isRatingMode()) {
            model.reloadRating(getCrossingFromTable());
            addRatingDataToTable(model.getRatings());
        } else {
            model.reloadCrossing();
            addCrossingDataToTable(model.getCrossings());
        }
    }

    private void onExitClick() {
        System.exit(0);
    }

    private void onAboutClick() {
        new AboutGUI(this).setVisible(true);
    }

    private void onHelpClick() {
        String helpURL = "http://www.google.com";
        Runtime rt = Runtime.getRuntime();

        try {
            rt.exec("rundll32 helpURL.dll,FileProtocolHandler " + helpURL);
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }

    private void onCrossingSort(MouseEvent event) {
        String col = crossingDataTable.getColumnName(crossingDataTable.columnAtPoint(event.getPoint()));

        if (col.equals(Properties.get("osmNodeId"))) {
            model.sortByNode();
        } else if (col.equals(Properties.get("ratingAmount"))) {
            model.sortByNumberOfRatings();
        } else if (col.equals(Properties.get("status"))) {
            model.sortByStatus();
        }

        addCrossingDataToTable(model.getCrossings());
        searchTextField.getKeyListeners()[0].keyReleased(null);
    }

    private void onRatingSort(MouseEvent event) {
        String col = ratingDataTable.getColumnName(ratingDataTable.columnAtPoint(event.getPoint()));

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
        } else if (col.equals(Properties.get("image"))) {
            model.sortByImage();
        } else if (col.equals(Properties.get("lastChange"))) {
            model.sortByLastChanged();
        }

        addRatingDataToTable(model.getRatings());
    }

    private void onCrossingDoubleClick(MouseEvent event) {
        if (!isRatingMode() && event.getClickCount() >= 2 && !model.getCrossings().isEmpty()) {
            dataTabbedPane.setSelectedIndex(1);
        }
    }
    //</editor-fold>

    private Crossing getCrossingFromTable() {
        return model.getCrossing((int) crossingTableModel.getValueAt(crossingDataTable.getSelectedRow(), 3));
    }

    private Rating getRatingFromTable() {
        return model.getRating((int) ratingTableModel.getValueAt(ratingDataTable.getSelectedRow(), 7));
    }

    private void changeTableSelection(JTable table, int index) {
        table.changeSelection(index, 0, false, false);
    }

    private void errorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, Properties.get("error"), JOptionPane.ERROR_MESSAGE);
    }

    private int warningMessage(String message, int option) {
        return JOptionPane.showConfirmDialog(this, message, Properties.get("warning"), option);
    }

    private void addCrossingDataToTable(List<Crossing> list) {
        crossingTableModel.setRowCount(0);

        for (Crossing crossing : list) {
            crossingTableModel.addRow(new Object[]{
                    crossing.getOsmNodeId(),
                    crossing.getRatingAmount(),
                    crossing.getStatus(),
                    crossing.getId()
            });
        }

        changeTableSelection(crossingDataTable, 0);
    }

    private boolean isRatingMode() {
        return dataTabbedPane.getSelectedIndex() == 1;
    }

    private void addRatingDataToTable(List<Rating> list) {
        ratingTableModel.setRowCount(0);

        for (Rating rating : list) {
            ratingTableModel.addRow(new Object[]{
                    rating.getUserId().getName(),
                    rating.getTrafficId().getValue(),
                    rating.getSpatialClarityId().getValue(),
                    rating.getIlluminationId().getValue(),
                    rating.getComment(),
                    rating.getImageWeblink(),
                    rating.getLastChanged().toString(),
                    rating.getId()
            });
        }

        changeTableSelection(ratingDataTable, 0);
    }

    public void createCrossing(Crossing crossing) {
        if (model.getCrossings().contains(crossing)) {
            crossing.increaseRatingAmount();

            if (searchTextField.getText().isEmpty()) {
                changeTableSelection(crossingDataTable, model.getCrossings().indexOf(crossing));
                crossingTableModel.setValueAt(crossing.getRatingAmount(), crossingDataTable.getSelectedRow(), crossingDataTable.getColumn(Properties.get("ratingAmount")).getModelIndex());
            } else if (Long.toString(crossing.getOsmNodeId()).startsWith(searchTextField.getText())) {
                for (int i = 0; i < crossingTableModel.getRowCount(); i++) {
                    if ((long) crossingTableModel.getValueAt(i, crossingDataTable.getColumn(Properties.get("osmNodeId")).getModelIndex()) == crossing.getOsmNodeId()) {
                        changeTableSelection(crossingDataTable, i);
                        crossingTableModel.setValueAt(crossing.getRatingAmount(), crossingDataTable.getSelectedRow(), crossingDataTable.getColumn(Properties.get("ratingAmount")).getModelIndex());
                        break;
                    }
                }
            }
        } else {
            DataServiceLoader.getCrossingData().addCrossing(crossing, model, crossingTableModel);

            if (Long.toString(crossing.getOsmNodeId()).startsWith(searchTextField.getText()) || searchTextField.getText().isEmpty()) {
                changeTableSelection(crossingDataTable, crossingTableModel.getRowCount() - 1);
            } else {
                crossingTableModel.removeRow(crossingTableModel.getRowCount() - 1);
            }
        }
    }

    public void createRating(Rating rating) {
        DataServiceLoader.getCrossingData().addRating(rating, model, ratingTableModel);
        changeTableSelection(ratingDataTable, ratingDataTable.getRowCount() - 1);
        Crossing crossingOfRating = model.getCrossing(rating.getCrossingId().getId());
        crossingOfRating.increaseRatingAmount();
        crossingTableModel.setValueAt(crossingOfRating.getRatingAmount(), crossingDataTable.getSelectedRow(), crossingDataTable.getColumn(Properties.get("ratingAmount")).getModelIndex());
    }

    public void editCrossing(Crossing crossing) {
        if (searchTextField.getText().isEmpty() || Long.toString(crossing.getOsmNodeId()).startsWith(searchTextField.getText())) {
            crossingTableModel.setValueAt(crossing.getOsmNodeId(), crossingDataTable.getSelectedRow(), crossingDataTable.getColumn(Properties.get("osmNodeId")).getModelIndex());
            onCrossingSelection();
        } else {
            crossingTableModel.removeRow(crossingDataTable.getSelectedRow());
        }
    }

    public void editRating(Rating rating) {
        ratingTableModel.setValueAt(rating.getUserId().getName(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn(Properties.get("user")).getModelIndex());
        ratingTableModel.setValueAt(rating.getTrafficId().getValue(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn(Properties.get("traffic")).getModelIndex());
        ratingTableModel.setValueAt(rating.getSpatialClarityId().getValue(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn(Properties.get("spacialClarity")).getModelIndex());
        ratingTableModel.setValueAt(rating.getIlluminationId().getValue(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn(Properties.get("illumination")).getModelIndex());
        ratingTableModel.setValueAt(rating.getComment(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn(Properties.get("comment")).getModelIndex());
        ratingTableModel.setValueAt(rating.getImageWeblink(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn(Properties.get("image")).getModelIndex());
        ratingTableModel.setValueAt(rating.getLastChanged(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn(Properties.get("lastChange")).getModelIndex());
    }

    public void removeCrossing() {
        try {
            int selectedRow = crossingDataTable.getSelectedRow();
            DataServiceLoader.getCrossingData().removeCrossing(getCrossingFromTable().getId(), model, crossingTableModel);

            if (crossingTableModel.getRowCount() == selectedRow) {
                selectedRow--;
            }

            changeTableSelection(crossingDataTable, selectedRow);
        } catch (NonexistentEntityException neeex) {
            errorMessage(Properties.get("crossingExistError"));
        }
    }

    public void removeRating() {
        try {
            int selectedRow = ratingDataTable.getSelectedRow();
            Rating removeRating = getRatingFromTable();
            DataServiceLoader.getCrossingData().removeRating(removeRating.getId(), model, ratingTableModel);

            if (model.getRatings().isEmpty()) {
                removeCrossing();
                dataTabbedPane.setSelectedIndex(0);
            } else {
                if (ratingTableModel.getRowCount() == selectedRow) {
                    selectedRow--;
                }

                changeTableSelection(ratingDataTable, selectedRow);
                Crossing crossingOfRating = model.getCrossing(removeRating.getCrossingId().getId());
                crossingOfRating.decreaseRatingAmount();
                crossingTableModel.setValueAt(crossingOfRating.getRatingAmount(),
                        crossingDataTable.getSelectedRow(),
                        crossingDataTable.getColumn(Properties.get("ratingAmount")).getModelIndex()
                );
            }
        } catch (NonexistentEntityException neeex) {
            errorMessage(Properties.get("ratingCrossingExistError"));
        }
    }

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
        crossingTableModel = new DefaultTableModel(new String[]{
                Properties.get("osmNodeId"),
                Properties.get("ratingAmount"),
                Properties.get("status"),
                Properties.get("id")
        }, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) {
                    return Long.class;
                } else if (column == 1 || column == 2) {
                    return Integer.class;
                }

                return super.getColumnClass(column);
            }

        };

        ratingTableModel = new DefaultTableModel(new String[]{
                Properties.get("user"),
                Properties.get("traffic"),
                Properties.get("spacialClarity"),
                Properties.get("illumination"),
                Properties.get("comment"),
                Properties.get("image"),
                Properties.get("lastChange"),
                Properties.get("id")
        }, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        crossingDataTable = new JTable(crossingTableModel);
        ratingDataTable = new JTable(ratingTableModel);
        crossingDataTable.removeColumn(crossingDataTable.getColumnModel().getColumn(3));
        ratingDataTable.removeColumn(ratingDataTable.getColumnModel().getColumn(7));

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

        KeyStroke keyStrokeToClose = KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK);
        KeyStroke keyStrokeToRefresh = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0);
        KeyStroke keyStrokeToCut = KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke keyStrokeToCopy = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke keyStrokeToPaste = KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke keyStrokeToAdd = KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke keyStrokeToEdit = KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK);
        KeyStroke keyStrokeToDelete = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);

        exitMenuItem.setAccelerator(keyStrokeToClose);
        refreshMenuItem.setAccelerator(keyStrokeToRefresh);
        cutMenuItem.setAccelerator(keyStrokeToCut);
        copyMenuItem.setAccelerator(keyStrokeToCopy);
        pasteMenuItem.setAccelerator(keyStrokeToPaste);
        addMenuItem.setAccelerator(keyStrokeToAdd);
        editMenuItem.setAccelerator(keyStrokeToEdit);
        deleteMenuItem.setAccelerator(keyStrokeToDelete);

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
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        panel1.add(panel2, BorderLayout.NORTH);
        searchTextField = new JTextField();
        panel2.add(searchTextField, BorderLayout.CENTER);
        searchLabel = new JLabel();
        this.$$$loadLabelText$$$(searchLabel, ResourceBundle.getBundle("Bundle").getString("searchLabelText"));
        panel2.add(searchLabel, BorderLayout.WEST);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, BorderLayout.CENTER);
        crossingDataTable.setAutoCreateRowSorter(false);
        crossingDataTable.setCellSelectionEnabled(false);
        crossingDataTable.setColumnSelectionAllowed(false);
        crossingDataTable.setFillsViewportHeight(false);
        crossingDataTable.setRowSelectionAllowed(true);
        scrollPane1.setViewportView(crossingDataTable);
        final JScrollPane scrollPane2 = new JScrollPane();
        dataTabbedPane.addTab(ResourceBundle.getBundle("Bundle").getString("defaultRatingTabbedPaneTitle"), scrollPane2);
        ratingDataTable.setRowSelectionAllowed(true);
        scrollPane2.setViewportView(ratingDataTable);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        mainPanel.add(panel3, BorderLayout.NORTH);
        addButton = new JButton();
        this.$$$loadButtonText$$$(addButton, ResourceBundle.getBundle("Bundle").getString("add"));
        panel3.add(addButton);
        editButton = new JButton();
        this.$$$loadButtonText$$$(editButton, ResourceBundle.getBundle("Bundle").getString("edit"));
        panel3.add(editButton);
        deleteButton = new JButton();
        this.$$$loadButtonText$$$(deleteButton, ResourceBundle.getBundle("Bundle").getString("delete"));
        panel3.add(deleteButton);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
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
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
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
