/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.zebrastreifensafari.jpa.controllers;

import ch.hsr.zebrastreifensafari.jpa.entities.Crossing;
import ch.hsr.zebrastreifensafari.jpa.entities.Rating;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import java.io.Serializable;
import java.util.List;

/**
 * @author aeugster
 */
public class RatingJpaController extends EntityController implements Serializable {

    public RatingJpaController(EntityManagerFactory emf) {
        super(emf);
    }

    public void create(Rating rating) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        rating.setCrossingId(em.getReference(rating.getCrossingId().getClass(), rating.getCrossingId().getId()));
        em.persist(rating);
        em.getTransaction().commit();
        em.close();
    }

    public void edit(Rating rating) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();

        if (em.find(Rating.class, rating.getId()) == null) {
            throw new EntityNotFoundException("The rating with id " + rating.getId() + " no longer exists.");
        }

        em.merge(rating);
        em.getTransaction().commit();
        em.close();
    }

    public void destroy(Integer id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.remove(em.getReference(Rating.class, id));
        em.getTransaction().commit();
        em.close();
    }

    public List<Rating> findEntities(Crossing crossing) {
        return getEntityManager().createNamedQuery("Rating.findByCrossing", Rating.class).setParameter("crossingId", crossing).getResultList();
    }
}
