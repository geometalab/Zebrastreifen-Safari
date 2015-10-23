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
import main.java.ch.hsr.zebrastreifensafari.jpa.entities.Crossing;

/**
 *
 * @author aeugster
 */
public class CrossingJpaController implements Serializable {

    public CrossingJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Crossing crossing) {
        if (crossing.getRatingList() == null) {
            crossing.setRatingList(new ArrayList<Rating>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Rating> attachedRatingList = new ArrayList<Rating>();
            for (Rating ratingListRatingToAttach : crossing.getRatingList()) {
                ratingListRatingToAttach = em.getReference(ratingListRatingToAttach.getClass(), ratingListRatingToAttach.getId());
                attachedRatingList.add(ratingListRatingToAttach);
            }
            crossing.setRatingList(attachedRatingList);
            em.persist(crossing);
            for (Rating ratingListRating : crossing.getRatingList()) {
                Crossing oldCrossingIdOfRatingListRating = ratingListRating.getCrossingId();
                ratingListRating.setCrossingId(crossing);
                ratingListRating = em.merge(ratingListRating);
                if (oldCrossingIdOfRatingListRating != null) {
                    oldCrossingIdOfRatingListRating.getRatingList().remove(ratingListRating);
                    oldCrossingIdOfRatingListRating = em.merge(oldCrossingIdOfRatingListRating);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Crossing crossing) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Crossing persistentCrossing = em.find(Crossing.class, crossing.getId());
            List<Rating> ratingListOld = persistentCrossing.getRatingList();
            List<Rating> ratingListNew = crossing.getRatingList();
            List<Rating> attachedRatingListNew = new ArrayList<Rating>();
            for (Rating ratingListNewRatingToAttach : ratingListNew) {
                ratingListNewRatingToAttach = em.getReference(ratingListNewRatingToAttach.getClass(), ratingListNewRatingToAttach.getId());
                attachedRatingListNew.add(ratingListNewRatingToAttach);
            }
            ratingListNew = attachedRatingListNew;
            crossing.setRatingList(ratingListNew);
            crossing = em.merge(crossing);
            for (Rating ratingListOldRating : ratingListOld) {
                if (!ratingListNew.contains(ratingListOldRating)) {
                    ratingListOldRating.setCrossingId(null);
                    ratingListOldRating = em.merge(ratingListOldRating);
                }
            }
            for (Rating ratingListNewRating : ratingListNew) {
                if (!ratingListOld.contains(ratingListNewRating)) {
                    Crossing oldCrossingIdOfRatingListNewRating = ratingListNewRating.getCrossingId();
                    ratingListNewRating.setCrossingId(crossing);
                    ratingListNewRating = em.merge(ratingListNewRating);
                    if (oldCrossingIdOfRatingListNewRating != null && !oldCrossingIdOfRatingListNewRating.equals(crossing)) {
                        oldCrossingIdOfRatingListNewRating.getRatingList().remove(ratingListNewRating);
                        oldCrossingIdOfRatingListNewRating = em.merge(oldCrossingIdOfRatingListNewRating);
                    }
                }
            }
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
            List<Rating> ratingList = crossing.getRatingList();
            for (Rating ratingListRating : ratingList) {
                ratingListRating.setCrossingId(null);
                ratingListRating = em.merge(ratingListRating);
            }
            em.remove(crossing);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Crossing> findCrossingEntities() {
        return findCrossingEntities(true, -1, -1);
    }

    public List<Crossing> findCrossingEntities(int maxResults, int firstResult) {
        return findCrossingEntities(false, maxResults, firstResult);
    }

    private List<Crossing> findCrossingEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Crossing.class));
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

    public Crossing findCrossing(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Crossing.class, id);
        } finally {
            em.close();
        }
    }

    public int getCrossingCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Crossing> rt = cq.from(Crossing.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
