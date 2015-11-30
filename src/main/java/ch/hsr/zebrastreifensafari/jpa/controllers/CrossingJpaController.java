/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.zebrastreifensafari.jpa.controllers;

import java.io.Serializable;
import javax.persistence.EntityNotFoundException;
import ch.hsr.zebrastreifensafari.jpa.entities.Rating;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import ch.hsr.zebrastreifensafari.jpa.controllers.exceptions.NonexistentEntityException;
import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;

/**
 *
 * @author aeugster
 */
public class CrossingJpaController extends EntityController implements Serializable {

    public CrossingJpaController(EntityManagerFactory emf) {
        super(emf);
    }

    public void create(Crossing crossing) {
        if (crossing.getRatingList() == null) {
            crossing.setRatingList(new ArrayList<Rating>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(crossing);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Crossing crossing) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            crossing = em.merge(crossing);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = crossing.getId();
                if (findCrossing(id) == null) {
                    throw new NonexistentEntityException("The crossing with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Crossing crossing;
            try {
                crossing = em.getReference(Crossing.class, id);
                crossing.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The crossing with id " + id + " no longer exists.", enfe);
            }

            em.remove(crossing);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Crossing> findEntities() {
        EntityManager em = getEntityManager();
        List<Crossing> resultList = new ArrayList<Crossing>();

        try {
            for (Object[] objects : em.createNamedQuery("Crossing.findAll", Object[].class).getResultList()) {
                Crossing crossing = (Crossing) objects[0];
                crossing.setRatingAmount((long) objects[1]);
                resultList.add(crossing);
            }
        } finally {
            em.close();
        }

        return resultList;
    }

    public Crossing findCrossing(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Crossing.class, id);
        } finally {
            em.close();
        }
    }
}
