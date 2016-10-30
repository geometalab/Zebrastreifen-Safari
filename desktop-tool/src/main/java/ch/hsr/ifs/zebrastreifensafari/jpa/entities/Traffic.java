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
@Table(name = "crossing.traffic")
@NamedQueries({
        @NamedQuery(name = "Traffic.findAll", query = "SELECT t FROM Traffic t"),
        @NamedQuery(name = "Traffic.findById", query = "SELECT t FROM Traffic t WHERE t.id = :id"),
        @NamedQuery(name = "Traffic.findByValue", query = "SELECT t FROM Traffic t WHERE t.value = :value")
})
public class Traffic implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "value")
    private String value;
    @OneToMany(mappedBy = "trafficId", fetch = FetchType.LAZY)
    private List<Rating> ratingList;

    public Traffic() {
    }

    public Traffic(Integer id) {
        this.id = id;
    }

    public Traffic(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        return "Traffic[ id=" + id + " ]";
    }

}
