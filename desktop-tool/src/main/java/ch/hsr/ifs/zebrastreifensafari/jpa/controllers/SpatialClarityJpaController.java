/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.ifs.zebrastreifensafari.jpa.controllers;

import ch.hsr.ifs.zebrastreifensafari.jpa.entities.SpatialClarity;

import javax.persistence.EntityManagerFactory;
import java.io.Serializable;
import java.util.List;

/**
 * @author aeugster
 */
public class SpatialClarityJpaController extends EntityController implements Serializable {

    public SpatialClarityJpaController(EntityManagerFactory emf) {
        super(emf);
    }

    public List<SpatialClarity> findEntities() {
        return getEntityManager().createNamedQuery("SpatialClarity.findAll", SpatialClarity.class).getResultList();
    }
}
