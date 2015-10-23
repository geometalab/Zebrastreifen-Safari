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
import main.java.ch.hsr.zebrastreifensafari.jpa.entities.SpatialClarity;

/**
 *
 * @author aeugster
 */
public class SpatialClarityJpaController implements Serializable {

    public SpatialClarityJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SpatialClarity spatialClarity) {
        if (spatialClarity.getRatingList() == null) {
            spatialClarity.setRatingList(new ArrayList<Rating>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Rating> attachedRatingList = new ArrayList<Rating>();
            for (Rating ratingListRatingToAttach : spatialClarity.getRatingList()) {
                ratingListRatingToAttach = em.getReference(ratingListRatingToAttach.getClass(), ratingListRatingToAttach.getId());
                attachedRatingList.add(ratingListRatingToAttach);
            }
            spatialClarity.setRatingList(attachedRatingList);
            em.persist(spatialClarity);
            for (Rating ratingListRating : spatialClarity.getRatingList()) {
                SpatialClarity oldSpatialClarityIdOfRatingListRating = ratingListRating.getSpatialClarityId();
                ratingListRating.setSpatialClarityId(spatialClarity);
                ratingListRating = em.merge(ratingListRating);
                if (oldSpatialClarityIdOfRatingListRating != null) {
                    oldSpatialClarityIdOfRatingListRating.getRatingList().remove(ratingListRating);
                    oldSpatialClarityIdOfRatingListRating = em.merge(oldSpatialClarityIdOfRatingListRating);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SpatialClarity spatialClarity) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SpatialClarity persistentSpatialClarity = em.find(SpatialClarity.class, spatialClarity.getId());
            List<Rating> ratingListOld = persistentSpatialClarity.getRatingList();
            List<Rating> ratingListNew = spatialClarity.getRatingList();
            List<Rating> attachedRatingListNew = new ArrayList<Rating>();
            for (Rating ratingListNewRatingToAttach : ratingListNew) {
                ratingListNewRatingToAttach = em.getReference(ratingListNewRatingToAttach.getClass(), ratingListNewRatingToAttach.getId());
                attachedRatingListNew.add(ratingListNewRatingToAttach);
            }
            ratingListNew = attachedRatingListNew;
            spatialClarity.setRatingList(ratingListNew);
            spatialClarity = em.merge(spatialClarity);
            for (Rating ratingListOldRating : ratingListOld) {
                if (!ratingListNew.contains(ratingListOldRating)) {
                    ratingListOldRating.setSpatialClarityId(null);
                    ratingListOldRating = em.merge(ratingListOldRating);
                }
            }
            for (Rating ratingListNewRating : ratingListNew) {
                if (!ratingListOld.contains(ratingListNewRating)) {
                    SpatialClarity oldSpatialClarityIdOfRatingListNewRating = ratingListNewRating.getSpatialClarityId();
                    ratingListNewRating.setSpatialClarityId(spatialClarity);
                    ratingListNewRating = em.merge(ratingListNewRating);
                    if (oldSpatialClarityIdOfRatingListNewRating != null && !oldSpatialClarityIdOfRatingListNewRating.equals(spatialClarity)) {
                        oldSpatialClarityIdOfRatingListNewRating.getRatingList().remove(ratingListNewRating);
                        oldSpatialClarityIdOfRatingListNewRating = em.merge(oldSpatialClarityIdOfRatingListNewRating);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = spatialClarity.getId();
                if (findSpatialClarity(id) == null) {
                    throw new NonexistentEntityException("The spatialClarity with id " + id + " no longer exists.");
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
            SpatialClarity spatialClarity;
            try {
                spatialClarity = em.getReference(SpatialClarity.class, id);
                spatialClarity.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The spatialClarity with id " + id + " no longer exists.", enfe);
            }
            List<Rating> ratingList = spatialClarity.getRatingList();
            for (Rating ratingListRating : ratingList) {
                ratingListRating.setSpatialClarityId(null);
                ratingListRating = em.merge(ratingListRating);
            }
            em.remove(spatialClarity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SpatialClarity> findSpatialClarityEntities() {
        return findSpatialClarityEntities(true, -1, -1);
    }

    public List<SpatialClarity> findSpatialClarityEntities(int maxResults, int firstResult) {
        return findSpatialClarityEntities(false, maxResults, firstResult);
    }

    private List<SpatialClarity> findSpatialClarityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SpatialClarity.class));
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

    public SpatialClarity findSpatialClarity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SpatialClarity.class, id);
        } finally {
            em.close();
        }
    }

    public int getSpatialClarityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SpatialClarity> rt = cq.from(SpatialClarity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
