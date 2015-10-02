/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zebra;


import com.google.gson.Gson;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
/**
 *
 * @author aeugster
 */

@Entity
public class Zebra implements Serializable {
    
    private int zebracrossingid;
    private Long node;
    private int user;
    private int illumination;
    private int traffic;
    private int overview;
    private String comment;
    private File image;

    public Zebra() {
    }

    public Zebra( long node, int user, int illumination, int traffic, int overview, String comment, File image) {
        this.node = node;
        this.user = user;
        this.illumination = illumination;
        this.traffic = traffic;
        this.overview = overview;
        this.comment = comment;
        this.image = image;
    }
    
    public void print(){
        System.out.println(" Node: "+getNode()+" User "+getUser()+" Illumination: "+getIllumination()+" Traffic: "+getTraffic()+
                " Overview "+getOverview()+" Comment: "+getComment()+" Image: "+getImage());
    }
    
    public void toJson(File file){
        Gson gson = new Gson();
        
        String json = gson.toJson(this);

	try {
		//write converted json data to a file named "file.json"
		FileWriter writer = new FileWriter("zebra.json");
		writer.write(json);
		writer.close();

	} catch (IOException e) {
		e.printStackTrace();
	}

	System.out.println(json);
    }    

    /**
     * @return the zebracrossingid
     */
    @Id
    @GeneratedValue
    public int getZebracrossingid() {
        return zebracrossingid;
    }

    /**
     * @param zebracrossingid the zebracrossingid to set
     */
    public void setZebracrossingid(int zebracrossingid) {
        this.zebracrossingid = zebracrossingid;
    }

    /**
     * @return the node
     */
    @Column(name = "node")
    public Long getNode() {
        return node;
    }

    /**
     * @param node the node to set
     */
    public void setNode(Long node) {
        this.node = node;
    }

    /**
     * @return the user
     */
    @Column(name = "userFk")
    public int getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(int user) {
        this.user = user;
    }

    /**
     * @return the illumination
     */
    @Column(name = "illuminationfFk")
    public int getIllumination() {
        return illumination;
    }

    /**
     * @param illumination the illumination to set
     */
    public void setIllumination(int illumination) {
        this.illumination = illumination;
    }

    /**
     * @return the traffic
     */
    @Column(name = "trafficFk")
    public int getTraffic() {
        return traffic;
    }

    /**
     * @param traffic the traffic to set
     */
    public void setTraffic(int traffic) {
        this.traffic = traffic;
    }

    /**
     * @return the overview
     */
    @Column(name = "overviewFk")
    public int getOverview() {
        return overview;
    }

    /**
     * @param overview the overview to set
     */
    public void setOverview(int overview) {
        this.overview = overview;
    }

    /**
     * @return the comment
     */
    @Column(name = "comment")
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the image
     */
    @Column(name = "image")
    public File getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(File image) {
        this.image = image;
    }
}
