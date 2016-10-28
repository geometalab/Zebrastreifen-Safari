package ch.hsr.zebrastreifensafari.main;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ch.hsr.zebrastreifensafari.controller.MainController;
import ch.hsr.zebrastreifensafari.view.screen.MainGUI;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import ch.hsr.zebrastreifensafari.service.Properties;
import ch.hsr.zebrastreifensafari.service.crossing.CrossingDataService;

import javax.persistence.PersistenceException;
import javax.swing.*;
import java.util.Locale;

/**
 * @author aeugster
 */
public class Main {

    public static void main(String[] args) {
        while (true) {
            try {
                Properties.setLanguage(Locale.GERMAN);
                DataServiceLoader.provideCrossingData(new CrossingDataService("ZebraPU"));
                Model model = new Model();
                MainController mainController = new MainController(model);
                MainGUI mainGUI = new MainGUI(mainController, model);
                mainGUI.setVisible(true);
                break;
            } catch (PersistenceException pex) {
                Object[] choices = {Properties.get("retry"), Properties.get("close")};

                if (JOptionPane.showOptionDialog(null,
                        Properties.get("connectionError"), Properties.get("mainGuiTitle") + Properties.get("versionNumber") + Properties.get("connectionErrorTitle"),
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.ERROR_MESSAGE,
                        null,
                        choices,
                        choices[0]) != 0) {
                    break;
                }
            }
        }
    }
}
