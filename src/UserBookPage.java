package src;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class UserBookPage implements ActionListener {
    JFrame f;
    JMenuBar mb;
    JMenuItem menu, home, makeAppt, history;
    JLabel title;
    JPanel bookPanel;
    JLabel type;
    String[] apptTypes;
    JComboBox<String> typesCB;
    JLabel apptAvailable;
    JLabel start;
    JButton startButton;
    JLabel end;
    JButton endButton;
    JButton go;

    public static void main(String[] args){
        
    }
    
    public UserBookPage(){
        Font defaultFont = UIManager.getFont("Label.font");
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

        // Add title
        bookPanel = new JPanel();
        f.getContentPane();
        title = new JLabel("Make Appointment");
        title.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 25));
        Dimension titleSize = title.getPreferredSize();
        title.setBounds(0, 0, titleSize.width, titleSize.height);
        bookPanel.add(title);

         // add type of appointment text
        type = new JLabel("Type of Appointment: ");
        type.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));
        Dimension typeSize = type.getPreferredSize();
        type.setBounds(0, 50, typeSize.width, typeSize.height);
        bookPanel.add(type);

        // add dropdown for type of appointment
        apptTypes = new String[]{"Medical", "Beauty", "Fitness"};
        typesCB = new JComboBox<>(apptTypes);
        typesCB.setBounds(0, 75, 90, 20);
        typesCB.setSelectedIndex(2);
        typesCB.addActionListener(this);
        bookPanel.add(typesCB);

        // add text for picking appointment timeframe
        apptAvailable = new JLabel("Available appointments between: ");
        apptAvailable.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));
        Dimension availSize = apptAvailable.getPreferredSize();
        apptAvailable.setBounds(0, 125, availSize.width, availSize.height);
        bookPanel.add(apptAvailable);

        // add start text and start button
        start = new JLabel("Start: ");
        Dimension startSize = start.getPreferredSize();
        start.setBounds(0, 175, startSize.width, startSize.height);
        bookPanel.add(start);

        startButton = new JButton("Start Button");
        Dimension startBSize = startButton.getPreferredSize();
        startButton.setBounds(50, 175, startBSize.width, startBSize.height);
        bookPanel.add(startButton);

        // add end text and end button
        end = new JLabel("End: ");
        Dimension endSize = end.getPreferredSize();
        end.setBounds(0, 225, endSize.width, endSize.height);
        bookPanel.add(end);

        endButton = new JButton("End Button");
        Dimension endBSize = endButton.getPreferredSize();
        endButton.setBounds(50, 225, endBSize.width, endBSize.height);
        bookPanel.add(endButton);

        // add go button
        go = new JButton("Go");
        Dimension goSize = go.getPreferredSize();
        go.setBounds(0, 275, goSize.width, goSize.height);
        bookPanel.add(go);

        // bookPanel specifications
        bookPanel.setLayout(null);
        bookPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // add panel to frame
        f.add(bookPanel);

        // Make visible
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(800, 800);
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == home){
            f.setVisible(false);
            UserHomePage hp = new UserHomePage();
        }
        else if(e.getSource() == makeAppt){
           
        }
        else if (e.getSource() == history) {

        }
    }
}
