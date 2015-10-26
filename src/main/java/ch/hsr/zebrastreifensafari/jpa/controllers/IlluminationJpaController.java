/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.zebrastreifensafari.jpa.controllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ch.hsr.zebrastreifensafari.jpa.entities.Rating;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import ch.hsr.zebrastreifensafari.jpa.controllers.exceptions.NonexistentEntityException;
import ch.hsr.zebrastreifensafari.jpa.entities.Illumination;

/**
 *
 * @author aeugster
 */
public class IlluminationJpaController implements Serializable {

    public IlluminationJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Illumination illumination) {
        if (illumination.getRatingList() == null) {
            illumination.setRatingList(new ArrayList<Rating>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Rating> attachedRatingList = new ArrayList<Rating>();
            for (Rating ratingListRatingToAttach : illumination.getRatingList()) {
                ratingListRatingToAttach = em.getReference(ratingListRatingToAttach.getClass(), ratingListRatingToAttach.getId());
                attachedRatingList.add(ratingListRatingToAttach);
            }
            illumination.setRatingList(attachedRatingList);
            em.persist(illumination);
            for (Rating ratingListRating : illumination.getRatingList()) {
                Illumination oldIlluminationIdOfRatingListRating = ratingListRating.getIlluminationId();
                ratingListRating.setIlluminationId(illumination);
                ratingListRating = em.merge(ratingListRating);
                if (oldIlluminationIdOfRatingListRating != null) {
                    oldIlluminationIdOfRatingListRating.getRatingList().remove(ratingListRating);
                    oldIlluminationIdOfRatingListRating = em.merge(oldIlluminationIdOfRatingListRating);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Illumination illumination) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Illumination persistentIllumination = em.find(Illumination.class, illumination.getId());
            List<Rating> ratingListOld = persistentIllumination.getRatingList();
            List<Rating> ratingListNew = illumination.getRatingList();
            List<Rating> attachedRatingListNew = new ArrayList<Rating>();
            for (Rating ratingListNewRatingToAttach : ratingListNew) {
                ratingListNewRatingToAttach = em.getReference(ratingListNewRatingToAttach.getClass(), ratingListNewRatingToAttach.getId());
                attachedRatingListNew.add(ratingListNewRatingToAttach);
            }
            ratingListNew = attachedRatingListNew;
            illumination.setRatingList(ratingListNew);
            illumination = em.merge(illumination);
            for (Rating ratingListOldRating : ratingListOld) {
                if (!ratingListNew.contains(ratingListOldRating)) {
                    ratingListOldRating.setIlluminationId(null);
                    ratingListOldRating = em.merge(ratingListOldRating);
                }
            }
            for (Rating ratingListNewRating : ratingListNew) {
                if (!ratingListOld.contains(ratingListNewRating)) {
                    Illumination oldIlluminationIdOfRatingListNewRating = ratingListNewRating.getIlluminationId();
                    ratingListNewRating.setIlluminationId(illumination);
                    ratingListNewRating = em.merge(ratingListNewRating);
                    if (oldIlluminationIdOfRatingListNewRating != null && !oldIlluminationIdOfRatingListNewRating.equals(illumination)) {
                        oldIlluminationIdOfRatingListNewRating.getRatingList().remove(ratingListNewRating);
                        oldIlluminationIdOfRatingListNewRating = em.merge(oldIlluminationIdOfRatingListNewRating);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = illumination.getId();
                if (findIllumination(id) == null) {
                    throw new NonexistentEntityException("The illumination with id " + id + " no longer exists.");
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
            Illumination illumination;
            try {
                illumination = em.getReference(Illumination.class, id);
                illumination.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The illumination with id " + id + " no longer exists.", enfe);
            }
            List<Rating> ratingList = illumination.getRatingList();
            for (Rating ratingListRating : ratingList) {
                ratingListRating.setIlluminationId(null);
                ratingListRating = em.merge(ratingListRating);
            }
            em.remove(illumination);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Illumination> findIlluminationEntities() {
        return findIlluminationEntities(true, -1, -1);
    }

    public List<Illumination> findIlluminationEntities(int maxResults, int firstResult) {
        return findIlluminationEntities(false, maxResults, firstResult);
    }

    private List<Illumination> findIlluminationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Illumination.class));
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

    public Illumination findIllumination(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Illumination.class, id);
        } finally {
            em.close();
        }
    }

    public int getIlluminationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Illumination> rt = cq.from(Illumination.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
