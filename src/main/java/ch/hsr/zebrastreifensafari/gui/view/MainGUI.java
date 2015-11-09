package ch.hsr.zebrastreifensafari.gui.view;

import ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import ch.hsr.zebrastreifensafari.gui.create.CreateCrossingGUI;
import ch.hsr.zebrastreifensafari.gui.create.CreateRatingGUI;
import ch.hsr.zebrastreifensafari.gui.update.UpdateCrossingGUI;
import ch.hsr.zebrastreifensafari.gui.update.UpdateRatingGUI;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

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
    private JButton addButton;
    private JTable crossingDataTable;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton reloadButton;
    private JLabel searchLabel;
    private JTable ratingDataTable;
    private JMenuItem beendenItem, hilfeItem, ueberItem;
    private String url = "http://www.google.com";
    private JTabbedPane dataTabbedPane;

    private final Model model;
    private DefaultTableModel ratingTableModel;
    private DefaultTableModel crossingTableModel;

    public MainGUI(Model model) throws HeadlessException {
        super("Zebrastreifen Administration Tool v1.0");
        $$$setupUI$$$();
        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.model = model;
        initListeners();
        addCrossingDataToTable(model.getCrossings());
        pack();
        setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    private void initListeners() {
        dataTabbedPane.addChangeListener(e -> {
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
                }
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                JOptionPane.showMessageDialog(this, "Es ist kein Zebrastreifen ausgewählt", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchTextField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (searchTextField.getText().isEmpty()) {
                    addCrossingDataToTable(model.getCrossings());
                } else {
                    addCrossingDataToTable(model.getCrossings().stream()
                            .filter(crossing -> String.valueOf(crossing.getOsmNodeId()).startsWith(searchTextField.getText()))
                            .collect(Collectors.toList()));
                }
            }
        });

        addButton.addActionListener(e -> {
            CreateUpdateGUI createUpdateGUI;

            if (model.isRatingMode()) {
                createUpdateGUI = new CreateRatingGUI(this, getCrossingFromTable().getOsmNodeId());
            } else {
                createUpdateGUI = new CreateCrossingGUI(this);
            }

            createUpdateGUI.setVisible(true);
        });

        updateButton.addActionListener(e -> {
            try {
                CreateUpdateGUI createUpdateGUI;

                if (model.isRatingMode()) {
                    createUpdateGUI = new UpdateRatingGUI(this, getRatingFromTable());
                } else {
                    createUpdateGUI = new UpdateCrossingGUI(this, getCrossingFromTable());
                }

                createUpdateGUI.setVisible(true);
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                JOptionPane.showMessageDialog(this, "Es wurde keine Zeile zum überarbeiten ausgewählt", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
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
                        changeTableSelection(crossingDataTable, selectedRow);
                    } else {
                        changeTableSelection(ratingDataTable, selectedRow);
                        Crossing crossingOfRating = model.getCrossing(removeRating.getCrossingId().getId());
                        crossingOfRating.decreaseRatingAmount();
                        crossingTableModel.setValueAt(crossingOfRating.getRatingAmount(), crossingDataTable.getSelectedRow(), crossingDataTable.getColumn("Anzahl Bewertungen").getModelIndex());
                    }
                } else {
                    selectedRow = crossingDataTable.getSelectedRow();
                    DataServiceLoader.getCrossingData().removeCrossing(getCrossingFromTable().getId(), model, crossingTableModel);
                    changeTableSelection(crossingDataTable, selectedRow);
                }
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                JOptionPane.showMessageDialog(this, "Es wurde keine Zeile zum löschen ausgewählt", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        reloadButton.addActionListener(e -> {
            model.reloadUsers();

            if (model.isRatingMode()) {
                model.reloadRating(getCrossingFromTable());
                addRatingDataToTable(model.getRatings());
            } else {
                model.reloadCrossing();
                addCrossingDataToTable(model.getCrossings());
            }
        });

        crossingDataTable.getTableHeader().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                String col = crossingDataTable.getColumnName(crossingDataTable.columnAtPoint(e.getPoint()));

                switch (col) {
                    case "OSM Node ID":
                        model.sortByNode();
                        break;
                    case "Anzahl Bewertungen":
                        model.sortByNumberOfRatings();
                        break;
                }

                addCrossingDataToTable(model.getCrossings());

                if (!searchTextField.getText().isEmpty()) {
                    try {
                        Robot robot = new Robot();
                        searchTextField.requestFocus();
                        robot.keyPress(KeyEvent.VK_ENTER);
                        robot.keyRelease(KeyEvent.VK_ENTER);
                    } catch (AWTException awtex) {
                        JOptionPane.showMessageDialog(null, "Ihr System unterstützt keine Ein- oder Ausgabegeräte", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        ratingDataTable.getTableHeader().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                String col = ratingDataTable.getColumnName(ratingDataTable.columnAtPoint(e.getPoint()));

                switch (col) {
                    case "Benutzer":
                        model.sortByUser();
                        break;
                    case "Verkehr":
                        model.sortByTraffic();
                        break;
                    case "Übersicht":
                        model.sortByClarity();
                        break;
                    case "Beleuchtung":
                        model.sortByIllumination();
                        break;
                    case "Kommentar":
                        model.sortByComment();
                        break;
                    case "Bild":
                        model.sortByImage();
                        break;
                    default:
                        model.sortByLastChanged();
                }

                addRatingDataToTable(model.getRatings());
            }
        });

        crossingDataTable.getSelectionModel().addListSelectionListener(e -> {
            if (!crossingDataTable.getSelectionModel().isSelectionEmpty() && crossingTableModel.getRowCount() > 0) {
                dataTabbedPane.setTitleAt(1, "Bewertungen von " + crossingTableModel.getValueAt(crossingDataTable.getSelectedRow(), crossingDataTable.getColumn("OSM Node ID").getModelIndex()));
            }
        });

        crossingDataTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (model.isRatingMode() || e.getClickCount() < 2 || model.getCrossings().isEmpty()) return;

                dataTabbedPane.setSelectedIndex(1);
            }
        });

        beendenItem.addActionListener(e -> System.exit(0));

        hilfeItem.addActionListener(e -> {
            Runtime rt = Runtime.getRuntime();

            try {
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        ueberItem.addActionListener(e -> {
            JDialog ueberDialog = new JDialog();
            ueberDialog.setTitle("Über");
            ueberDialog.setSize(300, 200);
            ueberDialog.setLocationRelativeTo(getParent());
            ueberDialog.setModal(true);
            ueberDialog.setVisible(true);
        });
    }

    private Crossing getCrossingFromTable() {
        return model.getCrossing((int) crossingTableModel.getValueAt(crossingDataTable.getSelectedRow(), 3));
    }

    private Rating getRatingFromTable() {
        return model.getRating((int) ratingTableModel.getValueAt(ratingDataTable.getSelectedRow(), 7));
    }

    private void changeTableSelection(JTable table, int index) {
        table.changeSelection(index, 0, false, false);
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
            CreateUpdateGUI observable = ((ObservableHelper) o).getObservable();

            if (observable instanceof CreateCrossingGUI) {
                Crossing crossing = (Crossing) arg;

                if (model.getCrossings().contains(crossing)) {
                    crossing.increaseRatingAmount();

                    if (searchTextField.getText().isEmpty()) {
                        changeTableSelection(crossingDataTable, model.getCrossings().indexOf(crossing));
                        crossingTableModel.setValueAt(crossing.getRatingAmount(), crossingDataTable.getSelectedRow(), crossingDataTable.getColumn("Anzahl Bewertungen").getModelIndex());
                    } else if (Long.toString(crossing.getOsmNodeId()).startsWith(searchTextField.getText())) {
                        for (int i = 0; i < crossingTableModel.getRowCount(); i++) {
                            if ((long) crossingTableModel.getValueAt(i, crossingDataTable.getColumn("OSM Node ID").getModelIndex()) == crossing.getOsmNodeId()) {
                                changeTableSelection(crossingDataTable, i);
                                crossingTableModel.setValueAt(crossing.getRatingAmount(), crossingDataTable.getSelectedRow(), crossingDataTable.getColumn("Anzahl Bewertungen").getModelIndex());
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
            } else if (observable instanceof CreateRatingGUI) {
                Rating rating = (Rating) arg;
                DataServiceLoader.getCrossingData().addRating(rating, model, ratingTableModel);
                changeTableSelection(ratingDataTable, ratingDataTable.getRowCount() - 1);
                Crossing crossingOfRating = model.getCrossing(rating.getCrossingId().getId());
                crossingOfRating.increaseRatingAmount();
                crossingTableModel.setValueAt(crossingOfRating.getRatingAmount(), crossingDataTable.getSelectedRow(), crossingDataTable.getColumn("Anzahl Bewertungen").getModelIndex());
            } else if (observable instanceof UpdateCrossingGUI) {
                Crossing crossing = (Crossing) arg;

                if (searchTextField.getText().isEmpty() || Long.toString(crossing.getOsmNodeId()).startsWith(searchTextField.getText())) {
                    crossingTableModel.setValueAt(crossing.getOsmNodeId(), crossingDataTable.getSelectedRow(), crossingDataTable.getColumn("OSM Node ID").getModelIndex());
                } else {
                    crossingTableModel.removeRow(crossingDataTable.getSelectedRow());
                }

            } else if (observable instanceof UpdateRatingGUI) {
                Rating rating = (Rating) arg;
                ratingTableModel.setValueAt(rating.getUserId().getName(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn("Benutzer").getModelIndex());
                ratingTableModel.setValueAt(rating.getTrafficId().getValue(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn("Verkehr").getModelIndex());
                ratingTableModel.setValueAt(rating.getSpatialClarityId().getValue(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn("Übersicht").getModelIndex());
                ratingTableModel.setValueAt(rating.getIlluminationId().getValue(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn("Beleuchtung").getModelIndex());
                ratingTableModel.setValueAt(rating.getComment(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn("Kommentar").getModelIndex());
                ratingTableModel.setValueAt(rating.getImageWeblink(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn("Bild").getModelIndex());
                ratingTableModel.setValueAt(rating.getLastChanged(), ratingDataTable.getSelectedRow(), ratingDataTable.getColumn("Letzte Änderung").getModelIndex());
            }
        }
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
        crossingTableModel = new DefaultTableModel(new String[]{"OSM Node ID", "Anzahl Bewertungen", "Status", "ID"}, 0) {

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

        ratingTableModel = new DefaultTableModel(new String[]{"Benutzer", "Verkehr", "Übersicht", "Beleuchtung", "Kommentar", "Bild", "Letzte Änderung", "ID"}, 0) {

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
        JMenu datei = new JMenu("Datei");
        JMenu hilfe = new JMenu("Hilfe");
        beendenItem = new JMenuItem("Beenden");
        hilfeItem = new JMenuItem("Hilfe");
        ueberItem = new JMenuItem("Über");

        datei.add(beendenItem);
        hilfe.add(hilfeItem);
        hilfe.add(new JSeparator());
        hilfe.add(ueberItem);
        bar.add(datei);
//        bar.add(new JSeparator(JSeparator.VERTICAL));
        bar.add(hilfe);
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
        addButton.setText("Hinzufügen");
        panel1.add(addButton);
        updateButton = new JButton();
        updateButton.setText("Bearbeiten");
        panel1.add(updateButton);
        deleteButton = new JButton();
        deleteButton.setText("Löschen");
        panel1.add(deleteButton);
        reloadButton = new JButton();
        reloadButton.setIcon(new ImageIcon(getClass().getResource("/RefreshIcon.png")));
        reloadButton.setText("");
        panel1.add(reloadButton);
        dataTabbedPane = new JTabbedPane();
        mainPanel.add(dataTabbedPane, BorderLayout.CENTER);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        dataTabbedPane.addTab("Zebrastreifen", panel2);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        panel2.add(panel3, BorderLayout.NORTH);
        searchTextField = new JTextField();
        panel3.add(searchTextField, BorderLayout.CENTER);
        searchLabel = new JLabel();
        searchLabel.setText("Suchen: ");
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
        dataTabbedPane.addTab("Bewertungen", scrollPane2);
        ratingDataTable.setRowSelectionAllowed(true);
        scrollPane2.setViewportView(ratingDataTable);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    //</editor-fold>
}
