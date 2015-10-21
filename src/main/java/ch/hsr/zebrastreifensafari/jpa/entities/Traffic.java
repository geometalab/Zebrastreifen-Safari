/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.ch.hsr.zebrastreifensafari.jpa.entities;

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
@Table(name = "crossing.traffic")
@NamedQueries({
    @NamedQuery(name = "Traffic.findAll", query = "SELECT t FROM Traffic t"),
    @NamedQuery(name = "Traffic.findByTrafficId", query = "SELECT t FROM Traffic t WHERE t.trafficId = :trafficId"),
    @NamedQuery(name = "Traffic.findByTrafficValue", query = "SELECT t FROM Traffic t WHERE t.trafficValue = :trafficValue")})
public class Traffic implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "traffic_id")
    private Integer trafficId;
    @Basic(optional = false)
    @Column(name = "traffic_value")
    private String trafficValue;
    @OneToMany(mappedBy = "trafficFk", fetch = FetchType.LAZY)
    private List<Rating> ratingList;

    public Traffic() {
    }

    public Traffic(Integer trafficId) {
        this.trafficId = trafficId;
    }

    public Traffic(Integer trafficId, String trafficValue) {
        this.trafficId = trafficId;
        this.trafficValue = trafficValue;
    }

    public Integer getTrafficId() {
        return trafficId;
    }

    public void setTrafficId(Integer trafficId) {
        this.trafficId = trafficId;
    }

    public String getTrafficValue() {
        return trafficValue;
    }

    public void setTrafficValue(String trafficValue) {
        this.trafficValue = trafficValue;
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
        hash += (trafficId != null ? trafficId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Traffic)) {
            return false;
        }
        Traffic other = (Traffic) object;
        if ((this.trafficId == null && other.trafficId != null) || (this.trafficId != null && !this.trafficId.equals(other.trafficId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Traffic[ trafficId=" + trafficId + " ]";
    }

}
