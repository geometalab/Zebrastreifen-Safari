package ch.hsr.zebrastreifensafari.main;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.gui.view.View;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import ch.hsr.zebrastreifensafari.service.crossing.CrossingDataService;

/**
 *
 * @author aeugster
 */
public class Main {

    public static void main(String[] args) {
        DataServiceLoader.provideCrossingData(new CrossingDataService("ZebraPU"));
        Model model = new Model();
        View view = new View(model);
        view.setVisible(true);
    }
}
