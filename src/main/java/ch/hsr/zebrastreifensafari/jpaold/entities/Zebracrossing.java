/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.ch.hsr.zebrastreifensafari.jpaold.entities;

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
@Table(name = "crossing.zebracrossing")
@NamedQueries({
    @NamedQuery(name = "Zebracrossing.findAll", query = "SELECT z FROM Zebracrossing z"),
    @NamedQuery(name = "Zebracrossing.findByZebracrossingId", query = "SELECT z FROM Zebracrossing z WHERE z.zebracrossingId = :zebracrossingId"),
    @NamedQuery(name = "Zebracrossing.findByNode", query = "SELECT z FROM Zebracrossing z WHERE z.node = :node"),
    @NamedQuery(name = "Zebracrossing.findByImage", query = "SELECT z FROM Zebracrossing z WHERE z.image = :image")})
public class Zebracrossing implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "zebracrossing_id")
    private Integer zebracrossingId;
    @Basic(optional = false)
    @Column(name = "node")
    private long node;
    @Column(name = "image")
    private String image;
    @OneToMany(mappedBy = "zebracrossingFk", fetch = FetchType.LAZY)
    private List<Rating> ratingList;

    public Zebracrossing() {
    }

    public Zebracrossing(Integer zebracrossingId, long node, String image, List<Rating> ratingList) {
        this(node, image);
        this.zebracrossingId = zebracrossingId;
        this.ratingList = ratingList;
    }

    public Zebracrossing(long node, String image) {
        this.node = node;
        this.image = image;
    }

    public Zebracrossing(Integer zebracrossingId) {
        this.zebracrossingId = zebracrossingId;
    }

    public Zebracrossing(Integer zebracrossingId, long node) {
        this.zebracrossingId = zebracrossingId;
        this.node = node;
    }

    public Integer getZebracrossingId() {
        return zebracrossingId;
    }

    public void setZebracrossingId(Integer zebracrossingId) {
        this.zebracrossingId = zebracrossingId;
    }

    public long getNode() {
        return node;
    }

    public void setNode(long node) {
        this.node = node;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
        hash += (zebracrossingId != null ? zebracrossingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Zebracrossing)) {
            return false;
        }
        Zebracrossing other = (Zebracrossing) object;
        if ((this.zebracrossingId == null && other.zebracrossingId != null) || (this.zebracrossingId != null && !this.zebracrossingId.equals(other.zebracrossingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Zebracrossing[ zebracrossingId=" + zebracrossingId + " ]";
    }

}
