package ch.hsr.zebrastreifensafari.jpa.controllers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author : Mike Marti
 * @version : 1.0
 * @project : Zebrastreifen-Safari
 * @time : 16:38
 * @date : 30.11.2015
 */

public abstract class EntityController {

    private EntityManagerFactory emf;

    public EntityController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
