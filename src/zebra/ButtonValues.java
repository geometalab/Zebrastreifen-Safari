/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zebra;

import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

/**
 *
 * @author aeugster
 */
public class ButtonValues {

    public int getSelectedButtonInt(ButtonGroup bg) {
        
        int i = 1;
        
        for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            
            if(button.isSelected()){
                return i;
            }
            
            i++;
            
        }
        return 0;
    }
}
