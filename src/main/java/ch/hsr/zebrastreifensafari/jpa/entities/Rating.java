/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.ch.hsr.zebrastreifensafari.jpa.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author aeugster
 */
@Entity
@Table(name = "crossing.rating")
@NamedQueries({
    @NamedQuery(name = "Rating.findAll", query = "SELECT r FROM Rating r"),
    @NamedQuery(name = "Rating.findById", query = "SELECT r FROM Rating r WHERE r.id = :id"),
    @NamedQuery(name = "Rating.findByImageWeblink", query = "SELECT r FROM Rating r WHERE r.imageWeblink = :imageWeblink"),
    @NamedQuery(name = "Rating.findByComment", query = "SELECT r FROM Rating r WHERE r.comment = :comment"),
    @NamedQuery(name = "Rating.findByLastChanged", query = "SELECT r FROM Rating r WHERE r.lastChanged = :lastChanged"),
    @NamedQuery(name = "Rating.findByCrossing", query = "SELECT r FROM Rating r WHERE r.crossingId = :crossingId")})
public class Rating implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "image_weblink")
    private String imageWeblink;
    @Column(name = "comment")
    private String comment;
    @Basic(optional = false)
    @Column(name = "last_changed")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastChanged;
    @JoinColumn(name = "crossing_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Crossing crossingId;
    @JoinColumn(name = "illumination_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Illumination illuminationId;
    @JoinColumn(name = "spatial_clarity_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private SpatialClarity spatialClarityId;
    @JoinColumn(name = "traffic_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Traffic trafficId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User userId;

    public Rating() {
    }

    public Rating(Integer id) {
        this.id = id;
    }

    public Rating(Integer id, Date lastChanged) {
        this.id = id;
        this.lastChanged = lastChanged;
    }

    public Rating(Integer id, String comment, Illumination illuminationId, SpatialClarity spatialClarityId, Traffic trafficId, User userId, Crossing crossingId, String imageWeblink, Date lastChanged) {
        this(id, lastChanged);
        this.comment = comment;
        this.illuminationId = illuminationId;
        this.spatialClarityId = spatialClarityId;
        this.trafficId = trafficId;
        this.userId = userId;
        this.crossingId = crossingId;
        this.imageWeblink = imageWeblink;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageWeblink() {
        return imageWeblink;
    }

    public void setImageWeblink(String imageWeblink) {
        this.imageWeblink = imageWeblink;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        this.lastChanged = lastChanged;
    }

    public Crossing getCrossingId() {
        return crossingId;
    }

    public void setCrossingId(Crossing crossingId) {
        this.crossingId = crossingId;
    }

    public Illumination getIlluminationId() {
        return illuminationId;
    }

    public void setIlluminationId(Illumination illuminationId) {
        this.illuminationId = illuminationId;
    }

    public SpatialClarity getSpatialClarityId() {
        return spatialClarityId;
    }

    public void setSpatialClarityId(SpatialClarity spatialClarityId) {
        this.spatialClarityId = spatialClarityId;
    }

    public Traffic getTrafficId() {
        return trafficId;
    }

    public void setTrafficId(Traffic trafficId) {
        this.trafficId = trafficId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
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
        if (!(object instanceof Rating)) {
            return false;
        }
        Rating other = (Rating) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "main.java.ch.hsr.zebrastreifensafari.jpa.entities.Rating[ id=" + id + " ]";
    }
    
}
