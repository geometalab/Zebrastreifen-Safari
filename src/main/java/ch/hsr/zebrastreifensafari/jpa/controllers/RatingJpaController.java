/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.zebrastreifensafari.jpa.controllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ch.hsr.zebrastreifensafari.jpa.controllers.exceptions.NonexistentEntityException;
import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;
import ch.hsr.zebrastreifensafari.jpa.entities.Illumination;
import ch.hsr.zebrastreifensafari.jpa.entities.Rating;
import ch.hsr.zebrastreifensafari.jpa.entities.SpatialClarity;
import ch.hsr.zebrastreifensafari.jpa.entities.Traffic;
import ch.hsr.zebrastreifensafari.jpa.entities.User;

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
            Crossing crossingId = rating.getCrossingId();

            if (crossingId != null) {
                crossingId = em.getReference(crossingId.getClass(), crossingId.getId());
                rating.setCrossingId(crossingId);
            }

            em.persist(rating);

            if (crossingId != null) {
                crossingId.getRatingList().add(rating);
                crossingId = em.merge(crossingId);
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
            Rating persistentRating = em.find(Rating.class, rating.getId());
            Crossing crossingIdOld = persistentRating.getCrossingId();
            Crossing crossingIdNew = rating.getCrossingId();

            if (crossingIdNew != null) {
                crossingIdNew = em.getReference(crossingIdNew.getClass(), crossingIdNew.getId());
                rating.setCrossingId(crossingIdNew);
            }

            rating = em.merge(rating);

            if (crossingIdOld != null && !crossingIdOld.equals(crossingIdNew)) {
                crossingIdOld.getRatingList().remove(rating);
                crossingIdOld = em.merge(crossingIdOld);
            }
            if (crossingIdNew != null && !crossingIdNew.equals(crossingIdOld)) {
                crossingIdNew.getRatingList().add(rating);
                crossingIdNew = em.merge(crossingIdNew);
            }

            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = rating.getId();
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
                rating.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rating with id " + id + " no longer exists.", enfe);
            }
            Crossing crossingId = rating.getCrossingId();
            if (crossingId != null) {
                crossingId.getRatingList().remove(rating);
                crossingId = em.merge(crossingId);
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

    public List<Rating> findRatingByCrossing(Crossing crossing) {
        return getEntityManager().createNamedQuery("Rating.findByCrossing", Rating.class).setParameter("crossingId", crossing).getResultList();
    }
}
