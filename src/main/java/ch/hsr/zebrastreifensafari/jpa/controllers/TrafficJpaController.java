/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.ch.hsr.zebrastreifensafari.jpa.controllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import main.java.ch.hsr.zebrastreifensafari.jpa.entities.Rating;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import main.java.ch.hsr.zebrastreifensafari.jpa.controllers.exceptions.NonexistentEntityException;
import main.java.ch.hsr.zebrastreifensafari.jpa.entities.Traffic;

/**
 *
 * @author aeugster
 */
public class TrafficJpaController implements Serializable {

    public TrafficJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Traffic traffic) {
        if (traffic.getRatingList() == null) {
            traffic.setRatingList(new ArrayList<Rating>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Rating> attachedRatingList = new ArrayList<Rating>();
            for (Rating ratingListRatingToAttach : traffic.getRatingList()) {
                ratingListRatingToAttach = em.getReference(ratingListRatingToAttach.getClass(), ratingListRatingToAttach.getId());
                attachedRatingList.add(ratingListRatingToAttach);
            }
            traffic.setRatingList(attachedRatingList);
            em.persist(traffic);
            for (Rating ratingListRating : traffic.getRatingList()) {
                Traffic oldTrafficIdOfRatingListRating = ratingListRating.getTrafficId();
                ratingListRating.setTrafficId(traffic);
                ratingListRating = em.merge(ratingListRating);
                if (oldTrafficIdOfRatingListRating != null) {
                    oldTrafficIdOfRatingListRating.getRatingList().remove(ratingListRating);
                    oldTrafficIdOfRatingListRating = em.merge(oldTrafficIdOfRatingListRating);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Traffic traffic) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Traffic persistentTraffic = em.find(Traffic.class, traffic.getId());
            List<Rating> ratingListOld = persistentTraffic.getRatingList();
            List<Rating> ratingListNew = traffic.getRatingList();
            List<Rating> attachedRatingListNew = new ArrayList<Rating>();
            for (Rating ratingListNewRatingToAttach : ratingListNew) {
                ratingListNewRatingToAttach = em.getReference(ratingListNewRatingToAttach.getClass(), ratingListNewRatingToAttach.getId());
                attachedRatingListNew.add(ratingListNewRatingToAttach);
            }
            ratingListNew = attachedRatingListNew;
            traffic.setRatingList(ratingListNew);
            traffic = em.merge(traffic);
            for (Rating ratingListOldRating : ratingListOld) {
                if (!ratingListNew.contains(ratingListOldRating)) {
                    ratingListOldRating.setTrafficId(null);
                    ratingListOldRating = em.merge(ratingListOldRating);
                }
            }
            for (Rating ratingListNewRating : ratingListNew) {
                if (!ratingListOld.contains(ratingListNewRating)) {
                    Traffic oldTrafficIdOfRatingListNewRating = ratingListNewRating.getTrafficId();
                    ratingListNewRating.setTrafficId(traffic);
                    ratingListNewRating = em.merge(ratingListNewRating);
                    if (oldTrafficIdOfRatingListNewRating != null && !oldTrafficIdOfRatingListNewRating.equals(traffic)) {
                        oldTrafficIdOfRatingListNewRating.getRatingList().remove(ratingListNewRating);
                        oldTrafficIdOfRatingListNewRating = em.merge(oldTrafficIdOfRatingListNewRating);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = traffic.getId();
                if (findTraffic(id) == null) {
                    throw new NonexistentEntityException("The traffic with id " + id + " no longer exists.");
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
            Traffic traffic;
            try {
                traffic = em.getReference(Traffic.class, id);
                traffic.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The traffic with id " + id + " no longer exists.", enfe);
            }
            List<Rating> ratingList = traffic.getRatingList();
            for (Rating ratingListRating : ratingList) {
                ratingListRating.setTrafficId(null);
                ratingListRating = em.merge(ratingListRating);
            }
            em.remove(traffic);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Traffic> findTrafficEntities() {
        return findTrafficEntities(true, -1, -1);
    }

    public List<Traffic> findTrafficEntities(int maxResults, int firstResult) {
        return findTrafficEntities(false, maxResults, firstResult);
    }

    private List<Traffic> findTrafficEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Traffic.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Traffic findTraffic(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Traffic.class, id);
        } finally {
            em.close();
        }
    }

    public int getTrafficCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Traffic> rt = cq.from(Traffic.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}