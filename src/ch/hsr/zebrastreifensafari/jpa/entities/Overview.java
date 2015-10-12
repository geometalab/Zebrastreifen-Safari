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
@Table(name = "crossing.overview")
@NamedQueries({
    @NamedQuery(name = "Overview.findAll", query = "SELECT o FROM Overview o"),
    @NamedQuery(name = "Overview.findByOverviewId", query = "SELECT o FROM Overview o WHERE o.overviewId = :overviewId"),
    @NamedQuery(name = "Overview.findByOverviewValue", query = "SELECT o FROM Overview o WHERE o.overviewValue = :overviewValue")})
public class Overview implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "overview_id")
    private Integer overviewId;
    @Basic(optional = false)
    @Column(name = "overview_value")
    private String overviewValue;
    @OneToMany(mappedBy = "overviewFk", fetch = FetchType.LAZY)
    private List<Rating> ratingList;

    public Overview() {
    }

    public Overview(Integer overviewId) {
        this.overviewId = overviewId;
    }

    public Overview(Integer overviewId, String overviewValue) {
        this.overviewId = overviewId;
        this.overviewValue = overviewValue;
    }

    public Integer getOverviewId() {
        return overviewId;
    }

    public void setOverviewId(Integer overviewId) {
        this.overviewId = overviewId;
    }

    public String getOverviewValue() {
        return overviewValue;
    }

    public void setOverviewValue(String overviewValue) {
        this.overviewValue = overviewValue;
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
        hash += (overviewId != null ? overviewId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Overview)) {
            return false;
        }
        Overview other = (Overview) object;
        if ((this.overviewId == null && other.overviewId != null) || (this.overviewId != null && !this.overviewId.equals(other.overviewId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Overview[ overviewId=" + overviewId + " ]";
    }

}
