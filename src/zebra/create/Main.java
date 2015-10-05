/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zebra.create;

import java.util.ArrayList;
import zebra.User;

/**
 *
 * @author aeugster
 */
public class Main {
    
    
    public static void main(String[] args) {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Alex", "AE"));
        users.add(new User("Joël", "JS"));
        users.add(new User("Stefan", "SK"));
        users.add(new User("Mike", "MM"));
        users.add(new User("Fabienne", "FK"));
        
        GUI gui = new GUI(users);
        gui.setVisible(true);
        
    }
}
