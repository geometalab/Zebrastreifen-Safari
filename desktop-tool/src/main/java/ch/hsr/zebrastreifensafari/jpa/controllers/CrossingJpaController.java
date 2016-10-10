/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.zebrastreifensafari.jpa.controllers;

import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author aeugster
 */
public class CrossingJpaController extends EntityController implements Serializable {

    public CrossingJpaController(EntityManagerFactory emf) {
        super(emf);
    }

    public void create(Crossing crossing) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(crossing);
        em.getTransaction().commit();
        em.close();
    }

    public void edit(Crossing crossing) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        if (em.find(Crossing.class, crossing.getId()) == null) {
            throw new EntityNotFoundException("The crossing with id " + crossing.getId() + " no longer exists.");
        }

        em.merge(crossing);
        em.getTransaction().commit();
        em.close();
    }

    public void destroy(Integer id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.remove(em.getReference(Crossing.class, id));
        em.getTransaction().commit();
        em.close();
    }

    public List<Crossing> findEntities() {
        EntityManager em = getEntityManager();
        List<Crossing> resultList = new ArrayList<Crossing>();

        for (Object[] objects : em.createNamedQuery("Crossing.findAll", Object[].class).getResultList()) {
            Crossing crossing = (Crossing) objects[0];
            crossing.setRatingAmount((long) objects[1]);
            resultList.add(crossing);
        }

        em.close();
        return resultList;
    }
}
