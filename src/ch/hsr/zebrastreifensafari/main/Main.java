package ch.hsr.zebrastreifensafari.main;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ch.hsr.zebrastreifensafari.gui.view.Model;
import ch.hsr.zebrastreifensafari.gui.view.View;
import ch.hsr.zebrastreifensafari.service.DataServiceLoader;
import ch.hsr.zebrastreifensafari.service.zebracrossing.ZebracrossingDataService;

/**
 *
 * @author aeugster
 */

public class Main {

    public static void main(String[] args) {
        Model model = new Model();
        DataServiceLoader.provideZebraData(new ZebracrossingDataService());
        View view = new View(model);
        view.setVisible(true);
    }
}
