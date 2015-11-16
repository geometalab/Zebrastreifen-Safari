package ch.hsr.zebrastreifensafari.gui.view;

import ch.hsr.zebrastreifensafari.gui.CreateEditGUI;
import ch.hsr.zebrastreifensafari.gui.create.CreateCrossingGUI;
import ch.hsr.zebrastreifensafari.gui.create.CreateRatingGUI;
import ch.hsr.zebrastreifensafari.gui.edit.EditCrossingGUI;
import ch.hsr.zebrastreifensafari.gui.edit.EditRatingGUI;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultEditorKit;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 09:49
 * @date : 27.10.2015
 */

public class MainGUI extends JFrame implements Observer {

    private JPanel mainPanel;
    private JTextField searchTextField;
    private JButton addButton, editButton, deleteButton;
    private JTable crossingDataTable, ratingDataTable;
    private JLabel searchLabel;
    private JMenuItem exitMenuItem, refreshMenuItem, addMenuItem, editMenuItem, deleteMenuItem, helpMenuItem, aboutMenuItem;
    private String helpURL = "http://www.google.com";
    private JTabbedPane dataTabbedPane;

    private final Model model;
    private DefaultTableModel ratingTableModel;
    private DefaultTableModel crossingTableModel;

    public MainGUI(Model model) throws HeadlessException {
        super(DataServiceLoader.getBundleString("mainGuiTitle"));
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
            model.setRatingMode(!model.isRatingMode());

            if (model.isRatingMode()) {
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
            errorMessage(DataServiceLoader.getBundleString("changeSelectionError"));
        }
    }

    private void onCrossingSelection() {
        if (!crossingDataTable.getSelectionModel().isSelectionEmpty() && crossingTableModel.getRowCount() > 0) {
            dataTabbedPane.setTitleAt(1, DataServiceLoader.getBundleString("specificRatingTabbedPaneTitle") + crossingTableModel.getValueAt(
                    crossingDataTable.getSelectedRow(),
                    crossingDataTable.getColumn(DataServiceLoader.getBundleString("osmNodeId")).getModelIndex()
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
        CreateEditGUI createEditGUI;

        if (model.isRatingMode()) {
            createEditGUI = new CreateRatingGUI(this, getCrossingFromTable().getOsmNodeId());
        } else {
            createEditGUI = new CreateCrossingGUI(this);
        }

        createEditGUI.setVisible(true);
    }

    private void onEditClick() {
        try {
            CreateEditGUI createEditGUI;

            if (model.isRatingMode()) {
                createEditGUI = new EditRatingGUI(this, getRatingFromTable());
            } else {
                createEditGUI = new EditCrossingGUI(this, getCrossingFromTable());
            }

            createEditGUI.setVisible(true);
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            errorMessage(DataServiceLoader.getBundleString("editSelectionError"));
        }
    }

    private void onDeleteClick() {
        //todo: overwork
        if (warningMessage(DataServiceLoader.getBundleString("deleteWarning"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                int selectedRow;

                if (model.isRatingMode()) {
                    selectedRow = ratingDataTable.getSelectedRow();
                    Rating removeRating = getRatingFromTable();
                    DataServiceLoader.getCrossingData().removeRating(removeRating.getId(), model, ratingTableModel);

                    if (model.getRatings().isEmpty()) {
                        selectedRow = crossingDataTable.getSelectedRow();
                        dataTabbedPane.setSelectedIndex(0);
                        DataServiceLoader.getCrossingData().removeCrossing(removeRating.getCrossingId().getId(), model, crossingTableModel);

                        if (crossingTableModel.getRowCount() == selectedRow) {
                            selectedRow--;
                        }

                        changeTableSelection(crossingDataTable, selectedRow);
                    } else {
                        if (ratingTableModel.getRowCount() == selectedRow) {
                            selectedRow--;
                        }

                        changeTableSelection(ratingDataTable, selectedRow);
                        Crossing crossingOfRating = model.getCrossing(removeRating.getCrossingId().getId());
                        crossingOfRating.decreaseRatingAmount();
                        crossingTableModel.setValueAt(crossingOfRating.getRatingAmount(),
                                crossingDataTable.getSelectedRow(),
                                crossingDataTable.getColumn(DataServiceLoader.getBundleString("ratingAmount")).getModelIndex()
                        );
                    }
                } else {
                    selectedRow = crossingDataTable.getSelectedRow();
                    DataServiceLoader.getCrossingData().removeCrossing(getCrossingFromTable().getId(), model, crossingTableModel);

                    if (crossingTableModel.getRowCount() == selectedRow) {
                        selectedRow--;
                    }

                    changeTableSelection(crossingDataTable, selectedRow);
                }
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                errorMessage(DataServiceLoader.getBundleString("deleteSelectionError"));
            }
        }
    }

    private void onRefreshClick() {
        model.reloadUsers();

        if (model.isRatingMode()) {
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
        JDialog aboutDialog = new JDialog();
        aboutDialog.setTitle("Über");
        aboutDialog.setSize(300, 200);
        aboutDialog.setLocationRelativeTo(getParent());
        aboutDialog.setModal(true);
        aboutDialog.setVisible(true);
    }

    private void onHelpClick() {
        Runtime rt = Runtime.getRuntime();

        try {
            rt.exec("rundll32 helpURL.dll,FileProtocolHandler " + helpURL);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void onCrossingSort(MouseEvent event) {
        String col = crossingDataTable.getColumnName(crossingDataTable.columnAtPoint(event.getPoint()));

        if (col.equals(DataServiceLoader.getBundleString("osmNodeId")))
            model.sortByNode();
        else if (col.equals(DataServiceLoader.getBundleString("ratingAmount")))
            model.sortByNumberOfRatings();

        addCrossingDataToTable(model.getCrossings());

        //todo: better implementation
        if (!searchTextField.getText().isEmpty()) {
            try {
                Robot robot = new Robot();
                searchTextField.requestFocus();
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            } catch (AWTException awtex) {
                errorMessage(DataServiceLoader.getBundleString("inputDeviceError"));
            }
        }
    }

    private void onRatingSort(MouseEvent event) {
        String col = ratingDataTable.getColumnName(ratingDataTable.columnAtPoint(event.getPoint()));

        if (col.equals(DataServiceLoader.getBundleString("user")))
            model.sortByUser();
        else if (col.equals(DataServiceLoader.getBundleString("traffic")))
            model.sortByTraffic();
        else if (col.equals(DataServiceLoader.getBundleString("spacialClarity")))
            model.sortByClarity();
        else if (col.equals(DataServiceLoader.getBundleString("illumination")))
            model.sortByIllumination();
        else if (col.equals(DataServiceLoader.getBundleString("comment")))
            model.sortByComment();
        else if (col.equals(DataServiceLoader.getBundleString("image")))
            model.sortByImage();
        else
            model.sortByLastChanged();

        addRatingDataToTable(model.getRatings());
    }

    private void onCrossingDoubleClick(MouseEvent event) {
        if (model.isRatingMode() || event.getClickCount() < 2 || model.getCrossings().isEmpty()) return;

        dataTabbedPane.setSelectedIndex(1);
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
        JOptionPane.showMessageDialog(this, message, DataServiceLoader.getBundleString("error"), JOptionPane.ERROR_MESSAGE);
    }

    private int warningMessage(String message, int option) {
        return JOptionPane.showConfirmDialog(this, message, DataServiceLoader.getBundleString("warning"), option);
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

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ObservableHelper) {
            CreateEditGUI observable = ((ObservableHelper) o).getObservable();

            if (observable instanceof CreateCrossingGUI) {
                createCrossing((Crossing) arg);
            } else if (observable instanceof CreateRatingGUI) {
                createRating((Rating) arg);
            } else if (observable instanceof EditCrossingGUI) {
                editCrossing((Crossing) arg);
            } else if (observable instanceof EditRatingGUI) {
                editRating((Rating) arg);
            }
        }
    }

    private void createCrossing(Crossing crossing) {
        if (model.getCrossings().contains(crossing)) {
            crossing.increaseRatingAmount();

            if (searchTextField.getText().isEmpty()) {
                changeTableSelection(crossingDataTable, model.getCrossings().indexOf(crossing));
                crossingTableModel.setValueAt(crossing.getRatingAmount(), crossingDataTable.getSelectedRow(), crossingDataTable.getColumn(DataServiceLoader.getBundleString("ratingAmount")).getModelIndex());
            } else if (Long.toString(crossing.getOsmNodeId()).startsWith(searchTextField.getText())) {
                for (int i = 0; i < crossingTableModel.getRowCount(); i++) {
                    if ((long) crossingTableModel.getValueAt(i, crossingDataTable.getColumn(DataServiceLoader.getBundleString("osmNodeId")).getModelIndex()) == crossing.getOsmNodeId()) {
                        changeTableSelection(crossingDataTable, i);
                        crossingTableModel.setValueAt(crossing.getRatingAmount(), crossingDataTable.getSelectedRow(), crossingDataTable.getColumn(DataServiceLoader.getBundleString("ratingAmount")).getModelIndex());
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

    private void createRating(Rating rating) {
        DataServiceLoader.getCrossingData().addRating(rating, model, ratingTableModel);
        changeTableSelection(ratingDataTable, ratingDataTable.getRowCount() - 1);
        Crossing crossingOfRating = model.getCrossing(rating.getCrossingId().getId());
        crossingOfRating.increaseRatingAmount();
        crossingTableModel.setValueAt(crossingOfRating.getRatingAmount(), crossingDataTable.getSelectedRow(), crossingDataTable.getColumn(DataServiceLoader.getBundleString("ratingAmount")).getModelIndex());
    }

    private void editCrossing(Crossing crossing) {
        if (searchTextField.getText().isEmpty() || Long.toString(crossing.getOsmNodeId()).startsWith(searchTextField.getText())) {
            crossingTableModel.setValueAt(crossing.getOsmNodeId(), crossingDataTable.getSelectedRow(), crossingDataTable.getColumn(DataServiceLoader.getBundleString("osmNodeId")).getModelIndex());
        } else {
            crossingTableModel.removeRow(crossingDataTable.getSelectedRow());
        }
    }

    private void editRating(Rating rating) {
        ratingTableModel.setValueAt(rating.getUserId().getName(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn(DataServiceLoader.getBundleString("user")).getModelIndex());
        ratingTableModel.setValueAt(rating.getTrafficId().getValue(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn(DataServiceLoader.getBundleString("traffic")).getModelIndex());
        ratingTableModel.setValueAt(rating.getSpatialClarityId().getValue(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn(DataServiceLoader.getBundleString("spacialClarity")).getModelIndex());
        ratingTableModel.setValueAt(rating.getIlluminationId().getValue(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn(DataServiceLoader.getBundleString("illumination")).getModelIndex());
        ratingTableModel.setValueAt(rating.getComment(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn(DataServiceLoader.getBundleString("comment")).getModelIndex());
        ratingTableModel.setValueAt(rating.getImageWeblink(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn(DataServiceLoader.getBundleString("image")).getModelIndex());
        ratingTableModel.setValueAt(rating.getLastChanged(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn(DataServiceLoader.getBundleString("lastChange")).getModelIndex());
    }

    //<editor-fold desc="Model methods">
    public User getUser(String name) {
        return model.getUser(name);
    }

    public Crossing getCrossing(int id) {
        return model.getCrossing(id);
    }

    public Crossing getCrossing(long node) {
        return model.getCrossing(node);
    }

    public Rating getRating(int id) {
        return model.getRating(id);
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
                DataServiceLoader.getBundleString("osmNodeId"),
                DataServiceLoader.getBundleString("ratingAmount"),
                DataServiceLoader.getBundleString("status"),
                DataServiceLoader.getBundleString("id")
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
                DataServiceLoader.getBundleString("user"),
                DataServiceLoader.getBundleString("traffic"),
                DataServiceLoader.getBundleString("spacialClarity"),
                DataServiceLoader.getBundleString("illumination"),
                DataServiceLoader.getBundleString("comment"),
                DataServiceLoader.getBundleString("image"),
                DataServiceLoader.getBundleString("lastChange"),
                DataServiceLoader.getBundleString("id")
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
        JMenu fileMenu = new JMenu(DataServiceLoader.getBundleString("file"));
        JMenu editMenu = new JMenu(DataServiceLoader.getBundleString("edit"));
        JMenu helpMenu = new JMenu(DataServiceLoader.getBundleString("help"));
        refreshMenuItem = new JMenuItem(DataServiceLoader.getBundleString("refresh"));
        exitMenuItem = new JMenuItem(DataServiceLoader.getBundleString("exit"));
        JMenuItem cutMenuItem = new JMenuItem(new DefaultEditorKit.CutAction());
        cutMenuItem.setText(DataServiceLoader.getBundleString("cut"));
        JMenuItem copyMenuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
        copyMenuItem.setText(DataServiceLoader.getBundleString("copy"));
        JMenuItem pasteMenuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
        pasteMenuItem.setText(DataServiceLoader.getBundleString("paste"));
        addMenuItem = new JMenuItem(DataServiceLoader.getBundleString("add"));
        editMenuItem = new JMenuItem(DataServiceLoader.getBundleString("edit"));
        deleteMenuItem = new JMenuItem(DataServiceLoader.getBundleString("delete"));
        helpMenuItem = new JMenuItem(DataServiceLoader.getBundleString("help"));
        aboutMenuItem = new JMenuItem(DataServiceLoader.getBundleString("about"));

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
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        mainPanel.add(panel1, BorderLayout.SOUTH);
        addButton = new JButton();
        this.$$$loadButtonText$$$(addButton, ResourceBundle.getBundle("Bundle").getString("add"));
        panel1.add(addButton);
        editButton = new JButton();
        this.$$$loadButtonText$$$(editButton, ResourceBundle.getBundle("Bundle").getString("edit"));
        panel1.add(editButton);
        deleteButton = new JButton();
        this.$$$loadButtonText$$$(deleteButton, ResourceBundle.getBundle("Bundle").getString("delete"));
        panel1.add(deleteButton);
        dataTabbedPane = new JTabbedPane();
        mainPanel.add(dataTabbedPane, BorderLayout.CENTER);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        dataTabbedPane.addTab(ResourceBundle.getBundle("Bundle").getString("defaultCrossingTabbedPaneTitle"), panel2);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        panel2.add(panel3, BorderLayout.NORTH);
        searchTextField = new JTextField();
        panel3.add(searchTextField, BorderLayout.CENTER);
        searchLabel = new JLabel();
        this.$$$loadLabelText$$$(searchLabel, ResourceBundle.getBundle("Bundle").getString("searchLabelText"));
        panel3.add(searchLabel, BorderLayout.WEST);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, BorderLayout.CENTER);
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
