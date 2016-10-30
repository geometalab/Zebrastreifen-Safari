package ch.hsr.zebrastreifensafari.controller;

import ch.hsr.zebrastreifensafari.service.Properties;
import ch.hsr.zebrastreifensafari.service.WebsiteService;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 2.0
 */
public class AboutController {

    public void openGeometalab() {
        WebsiteService.openWebsite(Properties.get("geometalabLink"));
    }

    public void openZebrastreifensafari() {
        WebsiteService.openWebsite(Properties.get("ourWebsiteLink"));
    }
}
