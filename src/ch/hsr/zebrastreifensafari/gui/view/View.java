/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.zebrastreifensafari.gui.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import ch.hsr.zebrastreifensafari.gui.CreateUpdateGUI;
import ch.hsr.zebrastreifensafari.gui.update.UpdateRatingGUI;
import ch.hsr.zebrastreifensafari.gui.update.UpdateZebracrossingGUI;
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.gui.create.*;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;

import java.awt.event.ActionEvent;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author aeugster
 */
public class View extends JFrame implements Observer {

    private final Model model;
    private DefaultTableModel ratingTM;
    private DefaultTableModel zebraTM;

    public View(Model model) {
        this.model = model;

        zebraTM = new DefaultTableModel(new String[]{"ID", "Node", "Bild"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ratingTM = new DefaultTableModel(new String[]{"ID", "Benutzer", "Verkehr", "Übersicht", "Beleuchtung", "Kommentar"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        initComponents();
        this.setTitle("Zebrastreifen Administration Tool");
        addDataToTable();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addButton = new javax.swing.JButton();
        changeButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        switchButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        updateDBButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        addButton.setText("Hinzufügen");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        changeButton.setText("Bearbeiten");
        changeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("Löschen");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        switchButton.setText("Bewertungen");
        switchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                switchButtonActionPerformed(evt);
            }
        });

        jTable1.setModel(getCurrentTableModel());
        jScrollPane1.setViewportView(jTable1);

        updateDBButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ch/hsr/zebrastreifensafari/gui/view/images/RefreshIcon.png"))); // NOI18N
        updateDBButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(switchButton))
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(deleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(changeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(addButton, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
                                        .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(updateDBButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(addButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(changeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(updateDBButton)
                        .addGap(30, 30, 30)
                        .addComponent(switchButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void switchButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_switchButtonActionPerformed
        try {
            changeView();
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            JOptionPane.showMessageDialog(this, "There is no selected zebracrossing", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_switchButtonActionPerformed

    private void addButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        CreateUpdateGUI createGUI;

        if (model.isRatingMode()) {
            createGUI = new CreateRatingGUI(model, this, getRatingFromTable().getZebracrossingFk().getNode());
        } else {
            createGUI = new CreateZebracrossingGUI(model, this);
        }

        createGUI.setVisible(true);
    }//GEN-LAST:event_addButtonActionPerformed

    private void changeButtonActionPerformed(ActionEvent evt) {
        try {
            CreateUpdateGUI updateGUI;

            if (model.isRatingMode()) {
                updateGUI = new UpdateRatingGUI(model, this, getRatingFromTable());
            } else {
                updateGUI = new UpdateZebracrossingGUI(model, this, getZebracrossingFromTable());
            }

            updateGUI.setVisible(true);
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            JOptionPane.showMessageDialog(this, "There is no data selected to change", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        try {
            if (model.isRatingMode()) {
                Rating removeRating = getRatingFromTable();
                DataServiceLoader.getZebraData().removeRating(removeRating.getRatingId());
                model.reloadRating(removeRating.getZebracrossingFk());

                if (model.getRatings().isEmpty()) {
                    changeView();
                    DataServiceLoader.getZebraData().removeZebracrossing(removeRating.getZebracrossingFk().getZebracrossingId());
                    model.reloadZebracrossing();
                }
            } else {
                DataServiceLoader.getZebraData().removeZebracrossing(getZebracrossingFromTable().getZebracrossingId());
                model.reloadZebracrossing();
            }

            addDataToTable();
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            JOptionPane.showMessageDialog(this, "There is no data to delete", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void reloadButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_updateDBButtonActionPerformed
        if (model.isRatingMode()) {
            model.reloadRating(getRatingFromTable().getZebracrossingFk());
        } else {
            model.reloadZebracrossing();
        }

        model.reloadUsers();
        addDataToTable();
    }//GEN-LAST:event_updateDBButtonActionPerformed

    private void changeView() {
        model.setRatingMode(!model.isRatingMode());

        if (model.isRatingMode()) {
            model.reloadRating(getZebracrossingFromTable());
            switchButton.setText("Zebrastreifen");
        } else {
            model.reloadZebracrossing();
            switchButton.setText("Bewertungen");
        }

        jTable1.setModel(getCurrentTableModel());
        addDataToTable();
    }

    private Zebracrossing getZebracrossingFromTable() {
        return model.getZebracrossing(Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString()));
    }

    private Rating getRatingFromTable() {
        return model.getRating(Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString()));
    }

    private TableModel getCurrentTableModel() {
        if (model.isRatingMode()) {
            return ratingTM;
        }

        return zebraTM;
    }

    private void addDataToTable() {
        zebraTM.setRowCount(0);
        ratingTM.setRowCount(0);

        if (model.isRatingMode()) {
            for (Rating r : model.getRatings()) {
                ratingTM.addRow(new String[]{r.getRatingId().toString(), r.getUserFk().getName(), r.getTrafficFk().getTrafficValue(),
                        r.getOverviewFk().getOverviewValue(), r.getIlluminationFk().getIlluminationValue(), r.getComment() == null ? "" : r.getComment()});
            }
        } else {
            for (Zebracrossing z : model.getZebracrossings()) {
                zebraTM.addRow(new String[]{z.getZebracrossingId().toString(), Long.toString(z.getNode()), z.getImage()});
            }
        }

        jTable1.changeSelection(0, 0, false, false);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Zebracrossing) {
            model.reloadRating((Zebracrossing)arg);
        } else {
            model.reloadZebracrossing();
        }

        addDataToTable();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton changeButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton switchButton;
    private javax.swing.JButton updateDBButton;
    // End of variables declaration//GEN-END:variables
}
