/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zebra;

import java.util.ArrayList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import jpa.controllers.UserJpaController;
import jpa.entities.User;
import zebra.create.GUI;

/**
 *
 * @author aeugster
 */
public class Zebra {
    
    private static EntityManagerFactory emFactory;
    private static ArrayList<User> users = new ArrayList<>();
    
    static{
        emFactory = Persistence.createEntityManagerFactory("ZebraPU");
    }
    
    public static void addUsers(){
        UserJpaController ujc = new UserJpaController(emFactory);
        for(User u : ujc.findUserEntities()){
            users.add(u);
        }
    }
    
    public static void main(String[] args) {
        
        addUsers();      
        /*
        users.add(new User("Alex", "AE"));
        users.add(new User("Joël", "JS"));
        users.add(new User("Stefan", "SK"));
        users.add(new User("Mike", "MM"));
        users.add(new User("Fabienne", "FK"));
        */
        
        
        GUI gui = new GUI(users);
        gui.setVisible(true);
        
    }
}
