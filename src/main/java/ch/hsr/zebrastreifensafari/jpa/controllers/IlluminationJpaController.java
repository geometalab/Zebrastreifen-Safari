/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.zebrastreifensafari.jpa.controllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import ch.hsr.zebrastreifensafari.jpa.entities.Illumination;

/**
 *
 * @author aeugster
 */
public class IlluminationJpaController extends EntityController implements Serializable {

    public IlluminationJpaController(EntityManagerFactory emf) {
        super(emf);
    }

    public List<Illumination> findEntities() {
        return getEntityManager().createNamedQuery("Illumination.findAll", Illumination.class).getResultList();
    }
}
