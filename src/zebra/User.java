/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zebra;

import java.io.Serializable;
import javax.persistence.*;


/**
 *
 * @author aeugster
 */
@Entity(name= "userEntity")
@Table(name = "user")
@NamedQueries({@NamedQuery(name="user.findAll", query="SELECT u FROM userEntity u") }) 
public class User implements Serializable {

    private int id;
    private String name;
    private String initials;

    public User() {
    }
    
    public User(String name, String initials) {
        this.name = name;
        this.initials = initials;
    }

    /**
     * @return the id
     */
    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    @Column(name = "name")
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the initials
     */
    @Column(name = "initials")
    public String getInitials() {
        return initials;
    }

    /**
     * @param initials the initials to set
     */
    public void setInitials(String initials) {
        this.initials = initials;
    }

}
