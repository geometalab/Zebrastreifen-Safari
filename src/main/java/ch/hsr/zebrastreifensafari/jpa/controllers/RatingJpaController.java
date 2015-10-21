/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.ch.hsr.zebrastreifensafari.jpa.controllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import main.java.ch.hsr.zebrastreifensafari.jpa.controllers.exceptions.NonexistentEntityException;
import main.java.ch.hsr.zebrastreifensafari.jpa.entities.Illumination;
import main.java.ch.hsr.zebrastreifensafari.jpa.entities.Overview;
import main.java.ch.hsr.zebrastreifensafari.jpa.entities.Rating;
import main.java.ch.hsr.zebrastreifensafari.jpa.entities.Traffic;
import main.java.ch.hsr.zebrastreifensafari.jpa.entities.Zebracrossing;

/**
 *
 * @author aeugster
 */
public class RatingJpaController implements Serializable {

    public RatingJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Rating rating) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Illumination illuminationFk = rating.getIlluminationFk();
            if (illuminationFk != null) {
                illuminationFk = em.getReference(illuminationFk.getClass(), illuminationFk.getIlluminationId());
                rating.setIlluminationFk(illuminationFk);
            }
            Overview overviewFk = rating.getOverviewFk();
            if (overviewFk != null) {
                overviewFk = em.getReference(overviewFk.getClass(), overviewFk.getOverviewId());
                rating.setOverviewFk(overviewFk);
            }
            Traffic trafficFk = rating.getTrafficFk();
            if (trafficFk != null) {
                trafficFk = em.getReference(trafficFk.getClass(), trafficFk.getTrafficId());
                rating.setTrafficFk(trafficFk);
            }
            Zebracrossing zebracrossingFk = rating.getZebracrossingFk();
            if (zebracrossingFk != null) {
                zebracrossingFk = em.getReference(zebracrossingFk.getClass(), zebracrossingFk.getZebracrossingId());
                rating.setZebracrossingFk(zebracrossingFk);
            }
            em.persist(rating);
            if (illuminationFk != null) {
                illuminationFk.getRatingList().add(rating);
                illuminationFk = em.merge(illuminationFk);
            }
            if (overviewFk != null) {
                overviewFk.getRatingList().add(rating);
                overviewFk = em.merge(overviewFk);
            }
            if (trafficFk != null) {
                trafficFk.getRatingList().add(rating);
                trafficFk = em.merge(trafficFk);
            }
            if (zebracrossingFk != null) {
                zebracrossingFk.getRatingList().add(rating);
                zebracrossingFk = em.merge(zebracrossingFk);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Rating rating) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Rating persistentRating = em.find(Rating.class, rating.getRatingId());
            Illumination illuminationFkOld = persistentRating.getIlluminationFk();
            Illumination illuminationFkNew = rating.getIlluminationFk();
            Overview overviewFkOld = persistentRating.getOverviewFk();
            Overview overviewFkNew = rating.getOverviewFk();
            Traffic trafficFkOld = persistentRating.getTrafficFk();
            Traffic trafficFkNew = rating.getTrafficFk();
            Zebracrossing zebracrossingFkOld = persistentRating.getZebracrossingFk();
            Zebracrossing zebracrossingFkNew = rating.getZebracrossingFk();
            if (illuminationFkNew != null) {
                illuminationFkNew = em.getReference(illuminationFkNew.getClass(), illuminationFkNew.getIlluminationId());
                rating.setIlluminationFk(illuminationFkNew);
            }
            if (overviewFkNew != null) {
                overviewFkNew = em.getReference(overviewFkNew.getClass(), overviewFkNew.getOverviewId());
                rating.setOverviewFk(overviewFkNew);
            }
            if (trafficFkNew != null) {
                trafficFkNew = em.getReference(trafficFkNew.getClass(), trafficFkNew.getTrafficId());
                rating.setTrafficFk(trafficFkNew);
            }
            if (zebracrossingFkNew != null) {
                zebracrossingFkNew = em.getReference(zebracrossingFkNew.getClass(), zebracrossingFkNew.getZebracrossingId());
                rating.setZebracrossingFk(zebracrossingFkNew);
            }
            rating = em.merge(rating);
            if (illuminationFkOld != null && !illuminationFkOld.equals(illuminationFkNew)) {
                illuminationFkOld.getRatingList().remove(rating);
                illuminationFkOld = em.merge(illuminationFkOld);
            }
            if (illuminationFkNew != null && !illuminationFkNew.equals(illuminationFkOld)) {
                illuminationFkNew.getRatingList().add(rating);
                illuminationFkNew = em.merge(illuminationFkNew);
            }
            if (overviewFkOld != null && !overviewFkOld.equals(overviewFkNew)) {
                overviewFkOld.getRatingList().remove(rating);
                overviewFkOld = em.merge(overviewFkOld);
            }
            if (overviewFkNew != null && !overviewFkNew.equals(overviewFkOld)) {
                overviewFkNew.getRatingList().add(rating);
                overviewFkNew = em.merge(overviewFkNew);
            }
            if (trafficFkOld != null && !trafficFkOld.equals(trafficFkNew)) {
                trafficFkOld.getRatingList().remove(rating);
                trafficFkOld = em.merge(trafficFkOld);
            }
            if (trafficFkNew != null && !trafficFkNew.equals(trafficFkOld)) {
                trafficFkNew.getRatingList().add(rating);
                trafficFkNew = em.merge(trafficFkNew);
            }
            if (zebracrossingFkOld != null && !zebracrossingFkOld.equals(zebracrossingFkNew)) {
                zebracrossingFkOld.getRatingList().remove(rating);
                zebracrossingFkOld = em.merge(zebracrossingFkOld);
            }
            if (zebracrossingFkNew != null && !zebracrossingFkNew.equals(zebracrossingFkOld)) {
                zebracrossingFkNew.getRatingList().add(rating);
                zebracrossingFkNew = em.merge(zebracrossingFkNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = rating.getRatingId();
                if (findRating(id) == null) {
                    throw new NonexistentEntityException("The rating with id " + id + " no longer exists.");
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
            Rating rating;
            try {
                rating = em.getReference(Rating.class, id);
                rating.getRatingId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rating with id " + id + " no longer exists.", enfe);
            }
            Illumination illuminationFk = rating.getIlluminationFk();
            if (illuminationFk != null) {
                illuminationFk.getRatingList().remove(rating);
                illuminationFk = em.merge(illuminationFk);
            }
            Overview overviewFk = rating.getOverviewFk();
            if (overviewFk != null) {
                overviewFk.getRatingList().remove(rating);
                overviewFk = em.merge(overviewFk);
            }
            Traffic trafficFk = rating.getTrafficFk();
            if (trafficFk != null) {
                trafficFk.getRatingList().remove(rating);
                trafficFk = em.merge(trafficFk);
            }
            Zebracrossing zebracrossingFk = rating.getZebracrossingFk();
            if (zebracrossingFk != null) {
                zebracrossingFk.getRatingList().remove(rating);
                zebracrossingFk = em.merge(zebracrossingFk);
            }
            em.remove(rating);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Rating> findRatingEntities() {
        return findRatingEntities(true, -1, -1);
    }

    public List<Rating> findRatingEntities(int maxResults, int firstResult) {
        return findRatingEntities(false, maxResults, firstResult);
    }

    private List<Rating> findRatingEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Rating.class));
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

    public Rating findRating(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Rating.class, id);
        } finally {
            em.close();
        }
    }

    public int getRatingCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Rating> rt = cq.from(Rating.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Rating> findRatingByZebracrossing(Zebracrossing zebracrossing) {
        return getEntityManager().createNamedQuery("Rating.findByZebracrossing", Rating.class).setParameter("zebracrossingId", zebracrossing).getResultList();
    }
}
