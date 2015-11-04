package ch.hsr.zebrastreifensafari.gui.view;

import ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import ch.hsr.zebrastreifensafari.gui.create.CreateCrossingGUI;
import ch.hsr.zebrastreifensafari.gui.create.CreateRatingGUI;
import ch.hsr.zebrastreifensafari.gui.update.UpdateCrossingGUI;
import ch.hsr.zebrastreifensafari.gui.update.UpdateRatingGUI;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

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
    private JButton changeButton;
    private JButton deleteButton;
    private JButton reloadButton;
    private JLabel searchLabel;
    private JTable ratingDataTable;
    private JMenuItem beendenItem, hilfeItem, ueberItem;
    private String url = "http://www.google.com";
    private JTabbedPane dataTabbedPane;

    private final Model model;
    private DefaultTableModel ratingTM;
    private DefaultTableModel zebraTM;

    public MainGUI(Model model) throws HeadlessException {
        super("Zebrastreifen Administration Tool v1.0");
        $$$setupUI$$$();
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.model = model;
        initListeners();
        addDataToTable();
        pack();
        setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    private void initListeners() {
        dataTabbedPane.addChangeListener(e -> {
            try {
                changeView();
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                JOptionPane.showMessageDialog(this, "Es ist kein Zebrastreifen ausgewählt", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchTextField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (searchTextField.getText().isEmpty()) {
                    addDataToTable();
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
                createUpdateGUI = new CreateRatingGUI(this, getRatingFromTable().getCrossingId().getOsmNodeId());
            } else {
                createUpdateGUI = new CreateCrossingGUI(this);
            }

            //createUpdateGUI.addObserver(model);
            createUpdateGUI.setVisible(true);
        });

        changeButton.addActionListener(e -> {
            try {
                CreateUpdateGUI createUpdateGUI;

                if (model.isRatingMode()) {
                    createUpdateGUI = new UpdateRatingGUI(this, getRatingFromTable());
                } else {
                    createUpdateGUI = new UpdateCrossingGUI(this, getCrossingFromTable());
                }

                //createUpdateGUI.addObserver(model);
                createUpdateGUI.setVisible(true);
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                JOptionPane.showMessageDialog(this, "Es wurde keine Zeile zum überarbeiten ausgewählt", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                if (model.isRatingMode()) {
                    Rating removeRating = getRatingFromTable();
                    DataServiceLoader.getCrossingData().removeRating(removeRating.getId());
                    model.reloadRating(removeRating.getCrossingId());
                    model.getCrossing(removeRating.getCrossingId().getId()).decreaseRatingAmount();
                    addCrossingDataToTable(model.getCrossings());

                    if (model.getRatings().isEmpty()) {
                        changeView();
                        DataServiceLoader.getCrossingData().removeCrossing(removeRating.getCrossingId().getId());
                        model.reloadCrossing();
                    }
                } else {
                    DataServiceLoader.getCrossingData().removeCrossing(getCrossingFromTable().getId());
                    model.reloadCrossing();
                }

                addDataToTable();
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                JOptionPane.showMessageDialog(this, "Es wurde keine Zeile zum überarbeiten ausgewählt", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        reloadButton.addActionListener(e -> {
            if (model.isRatingMode()) {
                model.reloadRating(getRatingFromTable().getCrossingId());
            } else {
                model.reloadCrossing();
            }

            model.reloadUsers();
            addDataToTable();
        });

        crossingDataTable.getTableHeader().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
            	String col = crossingDataTable.getColumnName(crossingDataTable.columnAtPoint(e.getPoint()));

                switch(col){ 
                case "OSM Node ID": 
                	model.sortByNode();
                    break; 
                case "Anzahl Bewertungen": 
                	model.sortByNumberOfRatings();
                    break;                 	  
                } 
                addDataToTable();
            }
        });

        ratingDataTable.getTableHeader().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                String col = ratingDataTable.getColumnName(ratingDataTable.columnAtPoint(e.getPoint()));

                switch(col){ 
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
                addDataToTable();
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

    private void changeView() {
        model.setRatingMode(!model.isRatingMode());

        if (model.isRatingMode()) {
            model.reloadRating(getCrossingFromTable());
            searchLabel.setVisible(false);
            searchTextField.setVisible(false);
        } else {
            searchLabel.setVisible(true);
            searchTextField.setVisible(true);
        }

        addDataToTable();
    }

    private Crossing getCrossingFromTable() {
        return model.getCrossing(Integer.parseInt(zebraTM.getValueAt(crossingDataTable.getSelectedRow(), 3).toString()));
    }

    private Rating getRatingFromTable() {
        return model.getRating(Integer.parseInt(ratingTM.getValueAt(ratingDataTable.getSelectedRow(), 7).toString()));
    }

    public void addDataToTable() {
        if (model.isRatingMode()) {
            addRatingDataToTable(model.getRatings());
        } else {
            addCrossingDataToTable(model.getCrossings());
        }
    }

    private void addCrossingDataToTable(List<Crossing> list) {
        zebraTM.setRowCount(0);
        //model.getCrossings().stream().sorted((o1, o2) -> Integer.compare(o1.getId(), o2.getId())).forEach(crossing -> zebraTM.addRow(new String[]{crossing.getId().toString(), Long.toString(crossing.getOsmNodeId()), Integer.toString(crossing.getStatus())}));
        for (Crossing z : list) {
            zebraTM.addRow(new String[]{Long.toString(z.getOsmNodeId()), Long.toString(z.getRatingAmount()), Integer.toString(z.getStatus()), z.getId().toString()});
        }

        crossingDataTable.changeSelection(0, 0, false, false);
    }

    private void addRatingDataToTable(List<Rating> list) {
        ratingTM.setRowCount(0);
        for (Rating r : list) {
            ratingTM.addRow(new String[]{
                            r.getUserId().getName(),
                            r.getTrafficId().getValue(),
                            r.getSpatialClarityId().getValue(),
                            r.getIlluminationId().getValue(),
                            r.getComment() == null ? "" : r.getComment(),
                            r.getImageWeblink(),
                            r.getLastChanged().toString(),
                            r.getId().toString()
                    }
            );
        }

        ratingDataTable.changeSelection(0, 0, false, false);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Crossing) {
            Crossing crossing = (Crossing) arg;
            DataServiceLoader.getCrossingData().addCrossing(crossing);
            model.getCrossings().add(crossing);
        } else if (arg instanceof Rating) {
            Rating rating = (Rating) arg;
            DataServiceLoader.getCrossingData().addRating(rating);
            model.getRatings().add(rating);
            model.getCrossing(rating.getCrossingId().getId()).increaseRatingAmount();
            addCrossingDataToTable(model.getCrossings());
        }

        addDataToTable();
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
        zebraTM = new DefaultTableModel(new String[]{"OSM Node ID", "Anzahl Bewertungen", "Status", "ID"}, 0) {

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

        ratingTM = new DefaultTableModel(new String[]{"Benutzer", "Verkehr", "Übersicht", "Beleuchtung", "Kommentar", "Bild", "Letzte Änderung", "ID"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        crossingDataTable = new JTable(zebraTM);
        ratingDataTable = new JTable(ratingTM);
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
        panel1.setLayout(new FormLayout("fill:d:grow", "center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        mainPanel.add(panel1, BorderLayout.EAST);
        addButton = new JButton();
        addButton.setText("Hinzufügen");
        CellConstraints cc = new CellConstraints();
        panel1.add(addButton, cc.xy(1, 1));
        changeButton = new JButton();
        changeButton.setText("Bearbeiten");
        panel1.add(changeButton, cc.xy(1, 3));
        deleteButton = new JButton();
        deleteButton.setText("Löschen");
        panel1.add(deleteButton, cc.xy(1, 5));
        reloadButton = new JButton();
        reloadButton.setIcon(new ImageIcon(getClass().getResource("/RefreshIcon.png")));
        reloadButton.setText("");
        panel1.add(reloadButton, cc.xy(1, 9));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, cc.xy(1, 7, CellConstraints.DEFAULT, CellConstraints.FILL));
        dataTabbedPane = new JTabbedPane();
        mainPanel.add(dataTabbedPane, BorderLayout.CENTER);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        dataTabbedPane.addTab("Zebracrossings", panel2);
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
        dataTabbedPane.addTab("Ratings", scrollPane2);
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
