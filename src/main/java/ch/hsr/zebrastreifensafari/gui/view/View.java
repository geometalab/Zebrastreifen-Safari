package ch.hsr.zebrastreifensafari.gui.view;

import ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import ch.hsr.zebrastreifensafari.gui.create.CreateCrossingGUI;
import ch.hsr.zebrastreifensafari.gui.create.CreateRatingGUI;
import ch.hsr.zebrastreifensafari.gui.update.UpdateCrossingGUI;
import ch.hsr.zebrastreifensafari.gui.update.UpdateRatingGUI;
import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;
import ch.hsr.zebrastreifensafari.jpa.entities.Rating;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

public class View extends JFrame implements Observer {

    private JPanel mainPanel;
    private JButton searchButton;
    private JTextField searchTextField;
    private JButton addButton;
    private JTable dataTable;
    private JButton changeButton;
    private JButton deleteButton;
    private JButton reloadButton;
    private JButton switchButton;

    private final Model model;
    private DefaultTableModel ratingTM;
    private DefaultTableModel zebraTM;

    public View(Model model) throws HeadlessException {
        super("Zebrastreifen Administration Tool");
        $$$setupUI$$$();
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    	dataTable.setAutoCreateRowSorter(true);
        
        this.model = model;
        initListeners();
        addDataToTable();
        pack();
        setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    private void initListeners() {
        switchButton.addActionListener(e -> {
            try {
                changeView();
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                model.setRatingMode(!model.isRatingMode());
                JOptionPane.showMessageDialog(this, "There is no selected Crossing", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchButton.addActionListener(e -> {
            if (searchTextField.getText().isEmpty()) {
                addDataToTable();
                return;
            }

            addCrossingDataToTable(model.getCrossings().stream()
                    .filter(crossing -> crossing.getId() == Integer.parseInt(searchTextField.getText()))
                    .collect(Collectors.toList()));
        });

        addButton.addActionListener(e -> {
            CreateUpdateGUI createGUI;

            if (model.isRatingMode()) {
                createGUI = new CreateRatingGUI(model, this, getRatingFromTable().getCrossingId().getOsmNodeId());
            } else {
                createGUI = new CreateCrossingGUI(model, this);
            }

            createGUI.setVisible(true);
        });

        changeButton.addActionListener(e -> {
            try {
                CreateUpdateGUI updateGUI;

                if (model.isRatingMode()) {
                    updateGUI = new UpdateRatingGUI(model, this, getRatingFromTable());
                } else {
                    updateGUI = new UpdateCrossingGUI(model, this, getCrossingFromTable());
                }

                updateGUI.setVisible(true);
            } catch (ArrayIndexOutOfBoundsException aioobe) {
                JOptionPane.showMessageDialog(this, "There is no data selected to change", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                if (model.isRatingMode()) {
                    Rating removeRating = getRatingFromTable();
                    DataServiceLoader.getCrossingData().removeRating(removeRating.getId());
                    model.reloadRating(removeRating.getCrossingId());

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
                JOptionPane.showMessageDialog(this, "There is no data to delete", "Error", JOptionPane.ERROR_MESSAGE);
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
    

	    dataTable.getTableHeader().addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            int col = dataTable.columnAtPoint(e.getPoint());

	        	dataTable.setAutoCreateRowSorter(true);
	            if (col == 0) {
		            model.sortById();
	            }
	            else if (col == 1) {
	            	model.sortByNode();
	            }
	            else {
	            	dataTable.setAutoCreateRowSorter(true);
	            }
		        addDataToTable();
	        }});
        
    }
    
    private void changeView() {
        model.setRatingMode(!model.isRatingMode());

        if (model.isRatingMode()) {
            model.reloadRating(getCrossingFromTable());
            switchButton.setText("Zebrastreifen");
            searchButton.setVisible(false);
            searchTextField.setVisible(false);
        } else {
            model.reloadCrossing();
            switchButton.setText("Bewertungen");
            searchButton.setVisible(true);
            searchTextField.setVisible(true);
        }

        dataTable.setModel(getCurrentTableModel());
        addDataToTable();
    }

    private Crossing getCrossingFromTable() {
        return model.getCrossing(Integer.parseInt(dataTable.getValueAt(dataTable.getSelectedRow(), 0).toString()));
    }

    private Rating getRatingFromTable() {
        return model.getRating(Integer.parseInt(dataTable.getValueAt(dataTable.getSelectedRow(), 0).toString()));
    }

    private TableModel getCurrentTableModel() {
        if (model.isRatingMode()) {
            return ratingTM;
        }

        return zebraTM;
    }

    private void addDataToTable() {
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
            zebraTM.addRow(new String[]{z.getId().toString(), Long.toString(z.getOsmNodeId()), Integer.toString(z.getStatus())});
        }

        dataTable.changeSelection(0, 0, false, false);
    }

    private void addRatingDataToTable(List<Rating> list) {
        ratingTM.setRowCount(0);
        for (Rating r : list) {
            ratingTM.addRow(
                    new String[]{r.getId().toString(),
                            r.getUserId().getName(),
                            r.getTrafficId().getValue(),
                            r.getSpatialClarityId().getValue(),
                            r.getIlluminationId().getValue(),
                            r.getComment() == null ? "" : r.getComment(),
                            r.getImageWeblink(),
                            r.getLastChanged().toString()
                    }
            );
        }

        dataTable.changeSelection(0, 0, false, false);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Crossing) {
            model.reloadRating((Crossing) arg);
        } else {
            model.reloadCrossing();
        }

        addDataToTable();
    }

    //<editor-fold desc="GUI Builder">
    private void createUIComponents() {
        zebraTM = new DefaultTableModel(new String[]{"ID", "Osm Node", "Status"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
    	    @Override
            public Class<?> getColumnClass(int column) {
            	if(column == 0) {
            		return Integer.class;
            	}
            	else if(column == 1){
            		return Long.class;
            	}
            	
            	return super.getColumnClass(column);
            }
    	    
        };

        ratingTM = new DefaultTableModel(new String[]{"ID", "Benutzer", "Verkehr", "Übersicht", "Beleuchtung", "Kommentar", "Bild", "Letzte Änderung"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            public Class<?> getColumnClass(int column) {
            	if(column == 0) {
            		return Integer.class;
            	}
            	
            	return super.getColumnClass(column);
            }
            
        };

        dataTable = new JTable(zebraTM);
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
        panel1.setLayout(new BorderLayout(0, 0));
        mainPanel.add(panel1, BorderLayout.NORTH);
        searchTextField = new JTextField();
        panel1.add(searchTextField, BorderLayout.CENTER);
        searchButton = new JButton();
        searchButton.setText("Suchen");
        panel1.add(searchButton, BorderLayout.EAST);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new FormLayout("fill:d:grow", "center:d:noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        mainPanel.add(panel2, BorderLayout.EAST);
        addButton = new JButton();
        addButton.setText("Hinzufügen");
        CellConstraints cc = new CellConstraints();
        panel2.add(addButton, cc.xy(1, 1));
        changeButton = new JButton();
        changeButton.setText("Bearbeiten");
        panel2.add(changeButton, cc.xy(1, 3));
        deleteButton = new JButton();
        deleteButton.setText("Löschen");
        panel2.add(deleteButton, cc.xy(1, 5));
        reloadButton = new JButton();
        reloadButton.setIcon(new ImageIcon(getClass().getResource("/RefreshIcon.png")));
        reloadButton.setText("");
        panel2.add(reloadButton, cc.xy(1, 9));
        switchButton = new JButton();
        switchButton.setText("Bewertungen");
        panel2.add(switchButton, cc.xy(1, 13));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, cc.xy(1, 11, CellConstraints.DEFAULT, CellConstraints.FILL));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, cc.xy(1, 7, CellConstraints.DEFAULT, CellConstraints.FILL));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainPanel.add(scrollPane1, BorderLayout.CENTER);
        dataTable.setAutoCreateRowSorter(false);
        dataTable.setCellSelectionEnabled(false);
        dataTable.setColumnSelectionAllowed(false);
        dataTable.setFillsViewportHeight(false);
        dataTable.setRowSelectionAllowed(true);
        scrollPane1.setViewportView(dataTable);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

    //</editor-fold>
}
