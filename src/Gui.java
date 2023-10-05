import java.awt.Color;
import java.awt.Dimension;
import java.sql.*;
import javax.swing.*;

// Light Black: 31, 36, 33
// Dark Teal: 33, 104, 105
// Mint Green: 73, 160, 120
// Light Green: 156, 197, 161
// Off white: 220, 225, 222

public class Gui {
    
    public static void main(String[] args){

        homePageUser();
    }


    private static void homePageUser(){
        /* Make frame */
        JFrame f = new JFrame("Appointment Booker");
        

        /* Set up the menu bar */
        JMenuBar mb = new JMenuBar();
        JMenuItem menu = new JMenu("Menu");

        mb.add(menu);
        mb.setBackground(new Color(73, 160, 120));
        
        JMenuItem home = new JMenuItem("Home"); 
        JMenuItem makeAppt = new JMenuItem("Make Appointment");  
        JMenuItem history = new JMenuItem("History");      
        
        menu.add(home);
        menu.add(makeAppt);
        menu.add(history);

        f.setJMenuBar(mb);
        
        /* Make visible */
        f.setSize(800,800);
        f.setVisible(true);
    }


    public void actionPerformed(ActionEvent e){
        
    }
}
