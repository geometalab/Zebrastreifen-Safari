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
import ch.hsr.zebrastreifensafari.jpa.entities.*;
import ch.hsr.zebrastreifensafari.gui.create.*;
import ch.hsr.zebrastreifensafari.gui.update.UpdateGUI;
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
        updateButton.setEnabled(false);
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
        updateButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        switchButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        addButton.setText("Hinzufügen");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        updateButton.setText("Bearbeiten");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addButton)
                    .addComponent(switchButton)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(deleteButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(updateButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(19, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(updateButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(switchButton)))
                .addGap(36, 36, 36))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void switchButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_switchButtonActionPerformed
        try {
            model.setRatingMode(!model.isRatingMode());

            if (!model.isRatingMode()) {
                switchButton.setText("Bewertungen");
                updateButton.setEnabled(false);
            } else {
                int row = jTable1.getSelectedRow();
                Zebracrossing z = new Zebracrossing(Integer.parseInt(zebraTM.getValueAt(row, 0).toString()), Long.parseLong(zebraTM.getValueAt(row, 1).toString()), zebraTM.getValueAt(row, 2) == null ? "" : zebraTM.getValueAt(row, 2).toString(), null);
                model.setRatings(DataServiceLoader.getZebraData().getRatingsOfZebra(z));
                switchButton.setText("Zebrastreifen");
                updateButton.setEnabled(true);
            }

            addDataToTable();
            jTable1.setModel(getCurrentTableModel());
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            model.setRatingMode(!model.isRatingMode());
            JOptionPane.showMessageDialog(this, "Select a row to show its ratings!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_switchButtonActionPerformed

    private void addButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        CreateUpdateGUI gc;

        if (!model.isRatingMode()) {
            gc = new CreateZebracrossingGUI(DataServiceLoader.getZebraData().getUsers(), this);
        } else {
           Rating  r = getSelectedRatingFromTable();
            gc = new CreateRatingGUI(DataServiceLoader.getZebraData().getUsers(), getSelectedRatingFromTable().getZebracrossingFk().getNode(), this);
        }

        gc.setVisible(rootPaneCheckingEnabled);
    }//GEN-LAST:event_addButtonActionPerformed

    private void updateButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        UpdateGUI ug;
        try {
            ug = new UpdateGUI(DataServiceLoader.getZebraData().getUsers(), model.getRatingById(Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString())), this);
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            ug = new UpdateGUI(DataServiceLoader.getZebraData().getUsers(), model.getRatingById(Integer.parseInt(jTable1.getValueAt(0, 0).toString())), this);
        }
        ug.setVisible(rootPaneCheckingEnabled);

    }//GEN-LAST:event_updateButtonActionPerformed

    private void deleteButtonActionPerformed(ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        try {
            if (!model.isRatingMode()) {
                DataServiceLoader.getZebraData().removeZebracrossing(Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString()));
                model.getZebras().remove(jTable1.getSelectedRow());
            } else {
                DataServiceLoader.getZebraData().removeRating(Integer.parseInt(jTable1.getValueAt(jTable1.getSelectedRow(), 0).toString()));
                model.getRatings().remove(jTable1.getSelectedRow());
            }

            addDataToTable();
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            JOptionPane.showMessageDialog(this, "Select a row to delete it!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    public TableModel getCurrentTableModel() {
        if (!model.isRatingMode()) {
            return zebraTM;
        }
        return ratingTM;
    }

    public Rating getSelectedRatingFromTable() {
        int row = jTable1.getSelectedRow();

        if (row == -1) {
            row = 0;
        }

        return model.getRatingById(Integer.parseInt(jTable1.getValueAt(row, 0).toString()));
    }

    public void addDataToTable() {
        zebraTM.setRowCount(0);
        ratingTM.setRowCount(0);

        if (!model.isRatingMode()) {
            for (Zebracrossing z : model.getZebras()) {
                zebraTM.addRow(new String[]{z.getZebracrossingId().toString(), Long.toString(z.getNode()), z.getImage()});
            }
        } else {
            for (Rating r : model.getRatings()) {
                ratingTM.addRow(new String[]{r.getRatingId().toString(), r.getUserFk().getName(), r.getTrafficFk().getTrafficValue(),
                    r.getOverviewFk().getOverviewValue(), r.getIlluminationFk().getIlluminationValue(), r.getComment() == null ? "" : r.getComment()});
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton switchButton;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Rating) {
            model.getRatings().add((Rating) arg);
        } else if (arg instanceof Zebracrossing) {
            model.getZebras().add((Zebracrossing) arg);
        }

        addDataToTable();
    }
}
