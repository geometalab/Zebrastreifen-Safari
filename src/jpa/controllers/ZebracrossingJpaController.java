/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.controllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.entities.Rating;
import jpa.entities.Zebracrossing;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.controllers.exceptions.NonexistentEntityException;

/**
 *
 * @author aeugster
 */
public class ZebracrossingJpaController implements Serializable {

    public ZebracrossingJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Zebracrossing zebracrossing) {
        if (zebracrossing.getRatingList() == null) {
            zebracrossing.setRatingList(new ArrayList<Rating>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Rating> attachedRatingList = new ArrayList<Rating>();
            for (Rating ratingListRatingToAttach : zebracrossing.getRatingList()) {
                ratingListRatingToAttach = em.getReference(ratingListRatingToAttach.getClass(), ratingListRatingToAttach.getRatingId());
                attachedRatingList.add(ratingListRatingToAttach);
            }
            zebracrossing.setRatingList(attachedRatingList);
            em.persist(zebracrossing);
            for (Rating ratingListRating : zebracrossing.getRatingList()) {
                Zebracrossing oldZebracrossingFkOfRatingListRating = ratingListRating.getZebracrossingFk();
                ratingListRating.setZebracrossingFk(zebracrossing);
                ratingListRating = em.merge(ratingListRating);
                if (oldZebracrossingFkOfRatingListRating != null) {
                    oldZebracrossingFkOfRatingListRating.getRatingList().remove(ratingListRating);
                    oldZebracrossingFkOfRatingListRating = em.merge(oldZebracrossingFkOfRatingListRating);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Zebracrossing zebracrossing) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Zebracrossing persistentZebracrossing = em.find(Zebracrossing.class, zebracrossing.getZebracrossingId());
            List<Rating> ratingListOld = persistentZebracrossing.getRatingList();
            List<Rating> ratingListNew = zebracrossing.getRatingList();
            List<Rating> attachedRatingListNew = new ArrayList<Rating>();
            for (Rating ratingListNewRatingToAttach : ratingListNew) {
                ratingListNewRatingToAttach = em.getReference(ratingListNewRatingToAttach.getClass(), ratingListNewRatingToAttach.getRatingId());
                attachedRatingListNew.add(ratingListNewRatingToAttach);
            }
            ratingListNew = attachedRatingListNew;
            zebracrossing.setRatingList(ratingListNew);
            zebracrossing = em.merge(zebracrossing);
            for (Rating ratingListOldRating : ratingListOld) {
                if (!ratingListNew.contains(ratingListOldRating)) {
                    ratingListOldRating.setZebracrossingFk(null);
                    ratingListOldRating = em.merge(ratingListOldRating);
                }
            }
            for (Rating ratingListNewRating : ratingListNew) {
                if (!ratingListOld.contains(ratingListNewRating)) {
                    Zebracrossing oldZebracrossingFkOfRatingListNewRating = ratingListNewRating.getZebracrossingFk();
                    ratingListNewRating.setZebracrossingFk(zebracrossing);
                    ratingListNewRating = em.merge(ratingListNewRating);
                    if (oldZebracrossingFkOfRatingListNewRating != null && !oldZebracrossingFkOfRatingListNewRating.equals(zebracrossing)) {
                        oldZebracrossingFkOfRatingListNewRating.getRatingList().remove(ratingListNewRating);
                        oldZebracrossingFkOfRatingListNewRating = em.merge(oldZebracrossingFkOfRatingListNewRating);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = zebracrossing.getZebracrossingId();
                if (findZebracrossing(id) == null) {
                    throw new NonexistentEntityException("The zebracrossing with id " + id + " no longer exists.");
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
            Zebracrossing zebracrossing;
            try {
                zebracrossing = em.getReference(Zebracrossing.class, id);
                zebracrossing.getZebracrossingId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The zebracrossing with id " + id + " no longer exists.", enfe);
            }
            //When a zebracrossing gets deleted, the ratings should be deleted as well. (on delete cascade)
            /*List<Rating> ratingList = zebracrossing.getRatingList();
            for (Rating ratingListRating : ratingList) {
                ratingListRating.setZebracrossingFk(null);
                ratingListRating = em.merge(ratingListRating);
            }*/
            em.remove(zebracrossing);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Zebracrossing> findZebracrossingEntities() {
        return findZebracrossingEntities(true, -1, -1);
    }

    public List<Zebracrossing> findZebracrossingEntities(int maxResults, int firstResult) {
        return findZebracrossingEntities(false, maxResults, firstResult);
    }

    private List<Zebracrossing> findZebracrossingEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Zebracrossing.class));
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

    public Zebracrossing findZebracrossing(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Zebracrossing.class, id);
        } finally {
            em.close();
        }
    }

    public int getZebracrossingCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Zebracrossing> rt = cq.from(Zebracrossing.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
