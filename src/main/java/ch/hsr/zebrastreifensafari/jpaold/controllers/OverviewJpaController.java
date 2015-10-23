/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.ch.hsr.zebrastreifensafari.jpaold.controllers;

import main.java.ch.hsr.zebrastreifensafari.jpaold.entities.Overview;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import main.java.ch.hsr.zebrastreifensafari.jpaold.entities.Rating;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import main.java.ch.hsr.zebrastreifensafari.jpaold.controllers.exceptions.NonexistentEntityException;

/**
 *
 * @author aeugster
 */
public class OverviewJpaController implements Serializable {

    public OverviewJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Overview overview) {
        if (overview.getRatingList() == null) {
            overview.setRatingList(new ArrayList<Rating>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Rating> attachedRatingList = new ArrayList<Rating>();
            for (Rating ratingListRatingToAttach : overview.getRatingList()) {
                ratingListRatingToAttach = em.getReference(ratingListRatingToAttach.getClass(), ratingListRatingToAttach.getRatingId());
                attachedRatingList.add(ratingListRatingToAttach);
            }
            overview.setRatingList(attachedRatingList);
            em.persist(overview);
            for (Rating ratingListRating : overview.getRatingList()) {
                Overview oldOverviewFkOfRatingListRating = ratingListRating.getOverviewFk();
                ratingListRating.setOverviewFk(overview);
                ratingListRating = em.merge(ratingListRating);
                if (oldOverviewFkOfRatingListRating != null) {
                    oldOverviewFkOfRatingListRating.getRatingList().remove(ratingListRating);
                    oldOverviewFkOfRatingListRating = em.merge(oldOverviewFkOfRatingListRating);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Overview overview) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Overview persistentOverview = em.find(Overview.class, overview.getOverviewId());
            List<Rating> ratingListOld = persistentOverview.getRatingList();
            List<Rating> ratingListNew = overview.getRatingList();
            List<Rating> attachedRatingListNew = new ArrayList<Rating>();
            for (Rating ratingListNewRatingToAttach : ratingListNew) {
                ratingListNewRatingToAttach = em.getReference(ratingListNewRatingToAttach.getClass(), ratingListNewRatingToAttach.getRatingId());
                attachedRatingListNew.add(ratingListNewRatingToAttach);
            }
            ratingListNew = attachedRatingListNew;
            overview.setRatingList(ratingListNew);
            overview = em.merge(overview);
            for (Rating ratingListOldRating : ratingListOld) {
                if (!ratingListNew.contains(ratingListOldRating)) {
                    ratingListOldRating.setOverviewFk(null);
                    ratingListOldRating = em.merge(ratingListOldRating);
                }
            }
            for (Rating ratingListNewRating : ratingListNew) {
                if (!ratingListOld.contains(ratingListNewRating)) {
                    Overview oldOverviewFkOfRatingListNewRating = ratingListNewRating.getOverviewFk();
                    ratingListNewRating.setOverviewFk(overview);
                    ratingListNewRating = em.merge(ratingListNewRating);
                    if (oldOverviewFkOfRatingListNewRating != null && !oldOverviewFkOfRatingListNewRating.equals(overview)) {
                        oldOverviewFkOfRatingListNewRating.getRatingList().remove(ratingListNewRating);
                        oldOverviewFkOfRatingListNewRating = em.merge(oldOverviewFkOfRatingListNewRating);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = overview.getOverviewId();
                if (findOverview(id) == null) {
                    throw new NonexistentEntityException("The overview with id " + id + " no longer exists.");
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
            Overview overview;
            try {
                overview = em.getReference(Overview.class, id);
                overview.getOverviewId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The overview with id " + id + " no longer exists.", enfe);
            }
            List<Rating> ratingList = overview.getRatingList();
            for (Rating ratingListRating : ratingList) {
                ratingListRating.setOverviewFk(null);
                ratingListRating = em.merge(ratingListRating);
            }
            em.remove(overview);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Overview> findOverviewEntities() {
        return findOverviewEntities(true, -1, -1);
    }

    public List<Overview> findOverviewEntities(int maxResults, int firstResult) {
        return findOverviewEntities(false, maxResults, firstResult);
    }

    private List<Overview> findOverviewEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Overview.class));
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

    public Overview findOverview(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Overview.class, id);
        } finally {
            em.close();
        }
    }

    public int getOverviewCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Overview> rt = cq.from(Overview.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
