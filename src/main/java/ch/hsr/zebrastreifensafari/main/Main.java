package ch.hsr.zebrastreifensafari.main;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ch.hsr.zebrastreifensafari.gui.main.MainGUI;
import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import ch.hsr.zebrastreifensafari.service.Properties;
import ch.hsr.zebrastreifensafari.service.crossing.CrossingDataService;

import java.util.Locale;

/**
 *
 * @author aeugster
 */
public class Main {

    public static void main(String[] args) {
        DataServiceLoader.provideCrossingData(new CrossingDataService("ZebraPU"));
        Properties.setLanguage(Locale.GERMAN);
        Model model = new Model();
        MainGUI mainGUI = new MainGUI(model);
        mainGUI.setVisible(true);
    }
}
