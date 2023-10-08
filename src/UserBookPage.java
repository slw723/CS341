
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class UserBookPage implements ActionListener {
    JFrame f;
    JMenuBar mb;
    JMenuItem menu, home, makeAppt, history;
    JLabel title, type, start, end, apptAvailable;
    JPanel bookPanel;
    String[] apptTypes;
    JComboBox<String> typesCB;
    JButton startButton;
    JButton endButton;
    JButton go;
    String apptSelected;
    UserHomePage hp;
    Database db;

    public static void main(String[] args){
        
    }
    
    public UserBookPage(Database db, UserHomePage hp){
         this.hp = hp;
        // default font
        Font defaultFont = UIManager.getFont("Label.font");

        /* Make frame */
        f = new JFrame("Appointment Booker");
        f.setBackground(new Color(220, 225, 222));

        /* Set up the menu bar */
        mb = new JMenuBar();
        menu = new JMenu("Menu");

        mb.add(menu);
        mb.setBackground(new Color(73, 160, 120));
        menu.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));
        menu.setForeground(new Color(31, 36, 33));

        mb.add(menu);
        mb.setBackground(new Color(73, 160, 120));
        
        home = new JMenuItem("Home");
        home.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));
        history = new JMenuItem("History");
        history.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));

        menu.add(home);
        menu.add(history);

        home.addActionListener(this);
        history.addActionListener(this);

        f.setJMenuBar(mb);

        // add title
        bookPanel = new JPanel();
        f.getContentPane();
        title = new JLabel("Make Appointment");
        title.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 25));
        title.setForeground(new Color(31, 36, 33));
        Dimension titleSize = title.getPreferredSize();
        title.setBounds(10, 0, titleSize.width, titleSize.height);
        bookPanel.add(title);

         // add type of appointment text
        type = new JLabel("Type of Appointment: ");
        type.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));
        type.setForeground(new Color(31, 36, 33));
        Dimension typeSize = type.getPreferredSize();
        type.setBounds(10, 50, typeSize.width, typeSize.height);
        bookPanel.add(type);

        // add dropdown for type of appointment
        apptTypes = new String[]{"Medical", "Beauty", "Fitness"};
        typesCB = new JComboBox<>(apptTypes);
        typesCB.setBounds(10, 75, 90, 20);
        typesCB.setSelectedIndex(2);
        typesCB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                apptTypeActionPerformed(evt);
            }
        });
        bookPanel.add(typesCB);

        // add text for picking appointment timeframe
        apptAvailable = new JLabel("Available appointments between: ");
        apptAvailable.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));
        Dimension availSize = apptAvailable.getPreferredSize();
        apptAvailable.setBounds(10, 125, availSize.width, availSize.height);
        bookPanel.add(apptAvailable);

        // add start text and start button
        start = new JLabel("Start: ");
        Dimension startSize = start.getPreferredSize();
        start.setBounds(10, 175, startSize.width, startSize.height);
        bookPanel.add(start);

        startButton = new JButton("Start Button");
        Dimension startBSize = startButton.getPreferredSize();
        startButton.setBounds(50, 175, startBSize.width, startBSize.height);
        bookPanel.add(startButton);

        // add end text and end button
        end = new JLabel("End: ");
        Dimension endSize = end.getPreferredSize();
        end.setBounds(10, 225, endSize.width, endSize.height);
        bookPanel.add(end);

        endButton = new JButton("End Button");
        Dimension endBSize = endButton.getPreferredSize();
        endButton.setBounds(50, 225, endBSize.width, endBSize.height);
        bookPanel.add(endButton);

        // add go button
        go = new JButton("Go");
        Dimension goSize = go.getPreferredSize();
        go.setBounds(10, 275, goSize.width, goSize.height);
        go.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                goActionPerformed(evt);
            }
        });
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

    public void apptTypeActionPerformed(ActionEvent e){
        apptSelected = typesCB.getSelectedItem().toString();
        // System.out.println(apptSelected);
        // Show the available time slots that are clickable
    }

    public void goActionPerformed(ActionEvent e){
        apptSelected = typesCB.getSelectedItem().toString();
        System.out.println(apptSelected);
        db.getApptType(apptSelected);
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == home){
            f.setVisible(false);
            hp.setHomeVisible();
        }
        else if(e.getSource() == makeAppt){
           
        }
        else if (e.getSource() == history) {

        }
    }
}
