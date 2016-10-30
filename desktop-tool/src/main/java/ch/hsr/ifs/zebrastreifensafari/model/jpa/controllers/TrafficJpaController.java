/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.ifs.zebrastreifensafari.model.jpa.controllers;

import ch.hsr.ifs.zebrastreifensafari.model.jpa.entities.Traffic;

import javax.persistence.EntityManagerFactory;
import java.io.Serializable;
import java.util.List;

/**
 * @author aeugster
 */
public class TrafficJpaController extends EntityController implements Serializable {

    public TrafficJpaController(EntityManagerFactory emf) {
        super(emf);
    }

    public List<Traffic> findEntities() {
        return getEntityManager().createNamedQuery("Traffic.findAll", Traffic.class).getResultList();
    }
}
