/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.zebrastreifensafari.jpa.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author aeugster
 */
@Entity
@Table(name = "spatial_clarity")
@NamedQueries({
    @NamedQuery(name = "SpatialClarity.findAll", query = "SELECT s FROM SpatialClarity s"),
    @NamedQuery(name = "SpatialClarity.findById", query = "SELECT s FROM SpatialClarity s WHERE s.id = :id"),
    @NamedQuery(name = "SpatialClarity.findByValue", query = "SELECT s FROM SpatialClarity s WHERE s.value = :value")})
public class SpatialClarity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "value")
    private String value;
    @OneToMany(mappedBy = "spatialClarityId", fetch = FetchType.LAZY)
    private List<Rating> ratingList;

    public SpatialClarity() {
    }

    public SpatialClarity(Integer id) {
        this.id = id;
    }

    public SpatialClarity(Integer id, String value) {
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
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SpatialClarity)) {
            return false;
        }
        SpatialClarity other = (SpatialClarity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ch.hsr.zebrastreifensafari.jpa.entities.SpatialClarity[ id=" + id + " ]";
    }
    
}
