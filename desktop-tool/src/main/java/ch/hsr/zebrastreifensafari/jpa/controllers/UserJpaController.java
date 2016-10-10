package ch.hsr.zebrastreifensafari.jpa.controllers;

import ch.hsr.zebrastreifensafari.jpa.entities.User;

import javax.persistence.EntityManagerFactory;
import java.io.Serializable;
import java.util.List;

/**
 * @author aeugster
 */
public class UserJpaController extends EntityController implements Serializable {

    public UserJpaController(EntityManagerFactory emf) {
        super(emf);
    }

    public List<User> findEntities() {
        return getEntityManager().createNamedQuery("User.findAll", User.class).getResultList();
    }
}
