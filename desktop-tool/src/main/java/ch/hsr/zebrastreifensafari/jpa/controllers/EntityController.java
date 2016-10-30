package ch.hsr.zebrastreifensafari.jpa.controllers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author : SeboCode
 * @version : 1.0
 * @since : 1.0
 */
public abstract class EntityController {

    private final EntityManagerFactory emf;

    protected EntityController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    protected EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
