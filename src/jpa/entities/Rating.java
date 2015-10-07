/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author aeugster
 */
@Entity
@Table(name = "rating")
@NamedQueries({
    @NamedQuery(name = "Rating.findAll", query = "SELECT r FROM Rating r"),
    @NamedQuery(name = "Rating.findByRatingId", query = "SELECT r FROM Rating r WHERE r.ratingId = :ratingId"),
    @NamedQuery(name = "Rating.findByComment", query = "SELECT r FROM Rating r WHERE r.comment = :comment")})
public class Rating implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rating_id")
    private Integer ratingId;
    @Column(name = "comment")
    private String comment;
    @JoinColumn(name = "illumination_fk", referencedColumnName = "illumination_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Illumination illuminationFk;
    @JoinColumn(name = "overview_fk", referencedColumnName = "overview_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Overview overviewFk;
    @JoinColumn(name = "traffic_fk", referencedColumnName = "traffic_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Traffic trafficFk;
    @JoinColumn(name = "user_fk", referencedColumnName = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User userFk;
    @JoinColumn(name = "zebracrossing_fk", referencedColumnName = "zebracrossing_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Zebracrossing zebracrossingFk;

    public Rating() {
    }

    public Rating(Integer ratingId) {
        this.ratingId = ratingId;
    }

    public Integer getRatingId() {
        return ratingId;
    }

    public void setRatingId(Integer ratingId) {
        this.ratingId = ratingId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Illumination getIlluminationFk() {
        return illuminationFk;
    }

    public void setIlluminationFk(Illumination illuminationFk) {
        this.illuminationFk = illuminationFk;
    }

    public Overview getOverviewFk() {
        return overviewFk;
    }

    public void setOverviewFk(Overview overviewFk) {
        this.overviewFk = overviewFk;
    }

    public Traffic getTrafficFk() {
        return trafficFk;
    }

    public void setTrafficFk(Traffic trafficFk) {
        this.trafficFk = trafficFk;
    }

    public User getUserFk() {
        return userFk;
    }

    public void setUserFk(User userFk) {
        this.userFk = userFk;
    }

    public Zebracrossing getZebracrossingFk() {
        return zebracrossingFk;
    }

    public void setZebracrossingFk(Zebracrossing zebracrossingFk) {
        this.zebracrossingFk = zebracrossingFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ratingId != null ? ratingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rating)) {
            return false;
        }
        Rating other = (Rating) object;
        if ((this.ratingId == null && other.ratingId != null) || (this.ratingId != null && !this.ratingId.equals(other.ratingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Rating[ ratingId=" + ratingId + " ]";
    }
    
}
