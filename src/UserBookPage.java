import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class UserBookPage implements ActionListener {
    JFrame f;
    JMenuBar mb;
    JMenuItem menu, home, makeAppt, history;

    public static void main(String[] args){

        
    }
    
    public UserBookPage(){
        /* Make frame */
        f = new JFrame("Appointment Booker");

        /* Set up the menu bar */
        mb = new JMenuBar();
        menu = new JMenu("Menu");

        mb.add(menu);
        mb.setBackground(new Color(73, 160, 120));
        
        home = new JMenuItem("Home"); 
        history = new JMenuItem("History");      
        
        menu.add(home);
        menu.add(history);

        home.addActionListener(this);
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
           
        }
    }
}
