package src;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.text.SimpleDateFormat;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SPBookPage implements ActionListener {
    JFrame f;
    JMenuBar mb;
    JMenuItem menu, home, makeAppt, history;
    JLabel title, descr, date, time;
    JTextField described, dated;
    String[] times;
    JComboBox<String> timeCB;
    JPanel bookPanel;
    JButton bookButton;
    JScrollBar scroll;
    JButton go;
    SPHomePage hp;
    Database db;
    
    public SPBookPage(Database db, SPHomePage hp){
        this.hp = hp;
        this.db = db;

        /* Make frame */
        f = new JFrame("Appointment Booker");
        f.setBackground(new Color(220, 225, 222));

        /* Set up the menu bar */
        mb = new JMenuBar();
        menu = new JMenu("Menu");

        mb.add(menu);
        mb.setBackground(new Color(73, 160, 120));
        menu.setFont(new Font("Sarif", Font.PLAIN, 15));
        menu.setForeground(new Color(31, 36, 33));

        mb.add(menu);
        mb.setBackground(new Color(73, 160, 120));
        
        home = new JMenuItem("Home");
        home.setFont(new Font("Sarif", Font.PLAIN, 15));
        history = new JMenuItem("History");
        history.setFont(new Font("Sarif", Font.PLAIN, 15));

        menu.add(home);
        menu.add(history);

        home.addActionListener(this);
        history.addActionListener(this);

        f.setJMenuBar(mb);

        // add title
        bookPanel = new JPanel();
        f.getContentPane();
        title = new JLabel("Make Appointment");
        title.setFont(new Font("Sarif", Font.PLAIN, 25));
        title.setForeground(new Color(31, 36, 33));
        Dimension titleSize = title.getPreferredSize();
        title.setBounds(10, 0, titleSize.width+10, titleSize.height);
        bookPanel.add(title);

        
        // make form
        // add text field for appt description
        descr = new JLabel("Description: ");
        descr.setFont(new Font("Sarif", Font.PLAIN, 15));
        descr.setForeground(new Color(33, 104, 105));
        Dimension descrSize = descr.getPreferredSize();
        descr.setBounds(10, 50, descrSize.width+10, descrSize.height);
        bookPanel.add(descr);

        described = new JTextField();
        described.setFont(new Font("Sarif", Font.PLAIN, 15));
        described.setSize(300, 20);
        described.setLocation(descrSize.width + 20, 55);
        bookPanel.add(described);

        // add text field for date
        date = new JLabel("Date: ");
        date.setFont(new Font("Sarif", Font.PLAIN, 15));
        date.setForeground(new Color(33, 104, 105));
        Dimension dateSize = date.getPreferredSize();
        date.setBounds(10, 90, dateSize.width+10, dateSize.height);
        bookPanel.add(date);

        dated = new JTextField();
        dated.setFont(new Font("Sarif", Font.PLAIN, 10));
        dated.setSize(100, 20);
        dated.setLocation(descrSize.width + 10, 95);
        dated.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e){
                if(dated.getText().isEmpty()){
                    dated.setText("yyyy-mm-dd");
                    dated.setForeground(Color.lightGray);
                }
            }
            public void focusGained(FocusEvent e){
                if(dated.getText().equals("yyyy-mm-dd")){
                    dated.setText("");
                    dated.setForeground(Color.BLACK);
                }
            }
        });
        bookPanel.add(dated);

        // add date field
        time = new JLabel("Time: ");
        time.setFont(new Font("Sarif", Font.PLAIN, 15));
        time.setForeground(new Color(33, 104, 105));
        Dimension timeSize = time.getPreferredSize();
        time.setBounds(10, 130, timeSize.width+10, timeSize.height+30);
        bookPanel.add(time);

        //add time drop down
        times = new String[]{ "", "12:00 AM","1:00 AM", "2:00 AM", "3:00 AM",
                            "4:00 AM", "5:00 AM", "6:00 AM", "7:00 AM", 
                            "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM",
                            "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM",
                            "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM",
                            "8:00 PM", "9:00 PM", "10:00 PM", "11:00 PM"};
        timeCB = new JComboBox<>(times);
        timeCB.setBounds(descrSize.width + 10, 130, 90, 20);
        timeCB.setSelectedIndex(0);
        scroll = new JScrollBar();
        timeCB.add(scroll);
        bookPanel.add(timeCB);
     
        // add go button
        go = new JButton("Submit");
        Dimension goSize = go.getPreferredSize();
        go.setBounds(10, 200, goSize.width+10, goSize.height);
        go.setBackground(new Color(156, 197, 161));
        go.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                submitActionPerformed(evt);
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
        f.setSize(1000, 800);
        f.setVisible(true);
    }

    /*
     * HELPERS
     */
    private int dataValidation(){

        // check date is formatted correctly and valid day/months
        String[] splits = dated.getText().split("-", 3);
        if(splits[0].length() == 4){
            if(splits[1].length() == 2){
                int month = Integer.valueOf(splits[1]);
                if(month <= 0 || month > 12){
                    dated.setText("");
                    JOptionPane.showMessageDialog(f, "Enter a valid month", 
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }
                else{
                    if(splits[2].length() == 2){
                        int day = Integer.valueOf(splits[2]);
                        if(day <= 0 || day > 31){
                            dated.setText("");
                            JOptionPane.showMessageDialog(f, "Enter a valid day",
                                 "Invalid Input", JOptionPane.ERROR_MESSAGE);

                            return -1;
                        }
                    }
                }
            }
        }
        else{
            dated.setText("");
            JOptionPane.showMessageDialog(f, "Enter a valid year", 
            "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }

        String today = String.valueOf(java.time.LocalDate.now());
        LocalDate today2024 = LocalDate.now().plusYears(1); //a year in advance
        LocalTime now = java.time.LocalTime.now();
        Date d = Date.valueOf(dated.getText());
        LocalTime t = convertTime(timeCB.getSelectedItem().toString());

        try{
            // check date is in the future but within the years
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(sdf.parse(dated.getText()).before(sdf.parse(today)) || 
               sdf.parse(dated.getText()).after(sdf.parse(String.valueOf(today2024)))){
                dated.setText("");
                JOptionPane.showMessageDialog(f, "Enter a valid date",
                                         "Invalid Input", JOptionPane.ERROR_MESSAGE);

                return -1;
            }
            else if(sdf.parse(dated.getText()).equals(sdf.parse(today))){
                // check time is in the future
                if(t.isBefore(now)){
                    timeCB.setSelectedIndex(0);
                    JOptionPane.showMessageDialog(f, "Enter a valid time",
                     "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return -1;
                }
            }
                       
        }
        catch(ParseException p){
            p.printStackTrace();
        }

        return 0;
    }

    private LocalTime convertTime(String oldTime){
        
        String[] str = oldTime.split(":", 2);
        String hr = str[0];
        int hour = Integer.parseInt(hr);
        String[] str2 = str[1].split(" ", 2);
        String timeOfDay = str2[1];

        if(timeOfDay.equals("AM")){
            if(hour == 12){
                hour = 0;
            }
            return LocalTime.of(hour, 0, 0, 0);
        }
        else{ //PM
            if(hour != 12){
                hour = hour + 12;
            }
            return LocalTime.of(hour, 0, 0, 0);
        }
    }

    /*
     * ACTION LISTENERS
     */
 
    public void submitActionPerformed(ActionEvent evt){
        
        int valid = dataValidation();
        if(valid < -1){
           return;
        }
        else{
            String des = described.getText();
            Date dateStr = Date.valueOf(dated.getText());
            Time timeStr = Time.valueOf(convertTime(timeCB.getSelectedItem().toString()));
            ServiceProvider sp = hp.getSP();
            Appointment appt = new Appointment(des, dateStr, timeStr, sp.getType(), 0, null, sp.getEmail());
            
            //check if SP has appt at that time already
            if(db.apptConflict(sp, appt) < 0){
                timeCB.setSelectedIndex(0);
                dated.setText("");
                JOptionPane.showMessageDialog(f, "You have an appointment conflict",
                "Appointment Conflict", JOptionPane.ERROR_MESSAGE);
            }
            else{
                db.insertAppt(appt);
            }
        }
        f.setVisible(false);
        hp.setHomeVisible();
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

