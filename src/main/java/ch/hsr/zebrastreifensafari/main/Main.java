package main.java.ch.hsr.zebrastreifensafari.main;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ch.hsr.zebrastreifensafari.model.Model;
import ch.hsr.zebrastreifensafari.gui.view.View;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import ch.hsr.zebrastreifensafari.service.zebracrossing.ZebracrossingDataService;

/**
 *
 * @author aeugster
 */
public class Main {

    public static void main(String[] args) {
        DataServiceLoader.provideZebraData(new ZebracrossingDataService());
        Model model = new Model();
        View view = new View(model);
        view.setVisible(true);
    }
}
