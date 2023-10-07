package src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

// Light Black: 31, 36, 33
// Dark Teal: 33, 104, 105
// Mint Green: 73, 160, 120
// Light Green: 156, 197, 161
// Off white: 220, 225, 222

public class UserHomePage implements ActionListener {
        JFrame f, f2;
        JMenuBar mb;
        JMenuItem menu, home, makeAppt, history;

    public static void main(String[] args){

        UserHomePage homePage = new UserHomePage();
        
    }

    public UserHomePage(){
        /* Make frame */
        f = new JFrame("Appointment Booker");

        /* Set up the menu bar */
        mb = new JMenuBar();
        menu = new JMenu("Menu");

        mb.add(menu);
        mb.setBackground(new Color(73, 160, 120));

        home = new JMenuItem("Home");
        makeAppt = new JMenuItem("Make Appointment");
        history = new JMenuItem("History");

        menu.add(home);
        menu.add(makeAppt);
        menu.add(history);

        home.addActionListener(this);
        makeAppt.addActionListener(this);
        history.addActionListener(this);

        f.setJMenuBar(mb);

        /* Make visible */
        f.setSize(800,800);
        f.setVisible(true);
    }


    public void actionPerformed(ActionEvent e){
        if(e.getSource() == home){

        }
        else if(e.getSource() == makeAppt){
            f.setVisible(false);
            UserBookPage bp = new UserBookPage();

        }
        else if (e.getSource() == history) {

        }
    }
}
