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
@Table(name = "crossing.illumination")
@NamedQueries({
    @NamedQuery(name = "Illumination.findAll", query = "SELECT i FROM Illumination i"),
    @NamedQuery(name = "Illumination.findByIlluminationId", query = "SELECT i FROM Illumination i WHERE i.illuminationId = :illuminationId"),
    @NamedQuery(name = "Illumination.findByIlluminationValue", query = "SELECT i FROM Illumination i WHERE i.illuminationValue = :illuminationValue")})
public class Illumination implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "illumination_id")
    private Integer illuminationId;
    @Basic(optional = false)
    @Column(name = "illumination_value")
    private String illuminationValue;
    @OneToMany(mappedBy = "illuminationFk", fetch = FetchType.LAZY)
    private List<Rating> ratingList;

    public Illumination() {
    }

    public Illumination(Integer illuminationId) {
        this.illuminationId = illuminationId;
    }

    public Illumination(Integer illuminationId, String illuminationValue) {
        this.illuminationId = illuminationId;
        this.illuminationValue = illuminationValue;
    }

    public Integer getIlluminationId() {
        return illuminationId;
    }

    public void setIlluminationId(Integer illuminationId) {
        this.illuminationId = illuminationId;
    }

    public String getIlluminationValue() {
        return illuminationValue;
    }

    public void setIlluminationValue(String illuminationValue) {
        this.illuminationValue = illuminationValue;
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
        hash += (illuminationId != null ? illuminationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Illumination)) {
            return false;
        }
        Illumination other = (Illumination) object;
        if ((this.illuminationId == null && other.illuminationId != null) || (this.illuminationId != null && !this.illuminationId.equals(other.illuminationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Illumination[ illuminationId=" + illuminationId + " ]";
    }
    
}
