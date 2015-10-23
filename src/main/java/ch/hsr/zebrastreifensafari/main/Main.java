package main.java.ch.hsr.zebrastreifensafari.main;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import main.java.ch.hsr.zebrastreifensafari.model.Model;
import main.java.ch.hsr.zebrastreifensafari.gui.view.View;
import main.java.ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import main.java.ch.hsr.zebrastreifensafari.service.crossing.CrossingDataService;

/**
 *
 * @author aeugster
 */
public class Main {

    public static void main(String[] args) {
        DataServiceLoader.provideCrossingData(new CrossingDataService());
        Model model = new Model();
        View view = new View(model);
        view.setVisible(true);
    }
}
