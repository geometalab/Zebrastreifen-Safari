/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.ifs.zebrastreifensafari.jpa.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author aeugster
 */
@Entity
@Table(name = "crossing.user")
@NamedQueries({
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
        @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
        @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name"),
        @NamedQuery(name = "User.findByInitials", query = "SELECT u FROM User u WHERE u.initials = :initials")
})
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "initials")
    private String initials;
    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
    private List<Rating> ratingList;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String name, String initials) {
        this.id = id;
        this.name = name;
        this.initials = initials;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public List<Rating> getRatingList() {
        return ratingList;
    }

    public void setRatingList(List<Rating> ratingList) {
        this.ratingList = ratingList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "User[ id=" + id + " ]";
    }

}
