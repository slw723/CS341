package src;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

// Light Black: 31, 36, 33
// Dark Teal: 33, 104, 105
// Mint Green: 73, 160, 120
// Light Green: 156, 197, 161
// Off white: 220, 225, 222

public class UserHomePage {
        JFrame f, f2;
        JMenuBar mb;
        JMenuItem menu, home, makeAppt, history;
        JButton logout;
        JPanel p;
        JLabel hello, upcoming, noappts;
        DefaultTableModel model;
        JTable appointments;
        JScrollPane scroll;
        User user;
        static Database db = new Database();

    public UserHomePage(Database db, User user){
        this.db = db;
        this.user = user;

        /* Make frame */
        f = new JFrame("Appointment Booker for User");
        f.setBackground(new Color(220, 225, 222));

        /* Set up the menu bar */
        mb = new JMenuBar();
        menu = new JMenu("Menu");
        menu.setFont(new Font("Sarif", Font.PLAIN, 15));
        menu.setForeground(new Color(31, 36, 33));

        logout = new JButton("Log Out");
        logout.setBackground(new Color(73, 160, 120));

        mb.add(menu);
        mb.setBackground(new Color(73, 160, 120));
        mb.add(Box.createHorizontalGlue());
        mb.add(logout);
        
        home = new JMenuItem("Home");
        home.setFont(new Font("Sarif", Font.PLAIN, 15));
        makeAppt = new JMenuItem("Make Appointment");
        makeAppt.setFont(new Font("Sarif", Font.PLAIN, 15));
        history = new JMenuItem("History");
        history.setFont(new Font("Sarif", Font.PLAIN, 15));

        menu.add(home);
        menu.add(makeAppt);
        menu.add(history);

        home.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                goHomeActionPerformed(evt);
            }
        });
        makeAppt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                makeApptActionPerformed(evt);
            }
        });
            
        history.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                historyActionPerformed(evt);
            }
        });

        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });

        f.setJMenuBar(mb);

        // add hello Name
        p = new JPanel();
        f.getContentPane();
        String str = "Hello, " + user.getFirstName() + "!";
        hello = new JLabel(str);
        hello.setFont(new Font("Sarif", Font.PLAIN, 15));
        hello.setForeground(new Color(31, 36, 33));
        Dimension helloSize = hello.getPreferredSize();
        hello.setBounds(10, 10, helloSize.width+10, helloSize.height);
        p.add(hello);

        // add upcoming appts title
        upcoming = new JLabel("Upcoming Appointments");
        upcoming.setFont(new Font("Sarif", Font.PLAIN, 15));
        upcoming.setForeground(new Color(33, 104, 105));
        Dimension upSize = upcoming.getPreferredSize();
        upcoming.setBounds(10, 50, upSize.width+10, upSize.height);
        p.add(upcoming);

        // show the upcoming appointments if they exists
        int ret = populateUpcoming();
        if(ret == -1){
            noappts = new JLabel("No Upcoming Appointments");
            noappts.setFont(new Font("Sarif", Font.PLAIN, 10));
            Dimension noSize = noappts.getPreferredSize();
            noappts.setBounds(20, 80, noSize.width+20, noSize.height);
            p.add(noappts);
        }
        else{
            displayUpcoming();
        }

        //panel sepecifications
        p.setLayout(null);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //add panel to frame
        f.add(p);

        /* Make visible */
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(1000,800);
        f.setVisible(true);
    }

    /* HELPERS */

    public void setHomeVisible(){
        f.setVisible(true);
    }

    public User getUser(){
        return user;
    }

    private String getSPName(String email){
        try{
            String sql = "Select FirstName, LastName FROM ServiceProvider WHERE Email = \"" + email + "\"";
            ResultSet rs = db.executeSQL(sql);
            if(rs.next()){
                return rs.getString("FirstName") + " " + rs.getString("Lastname");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());

        }
        return null;
    }

    /* ACTION PERFORMED */

     public void goHomeActionPerformed(ActionEvent e){
      
    }

    public void makeApptActionPerformed(ActionEvent e){
      
        f.setVisible(false);
        UserBookPage bp = new UserBookPage(db, this);
    }

    public void historyActionPerformed(ActionEvent e){
      
        f.setVisible(false);
        for(Component c : f.getComponents()){
            if(c == noappts){
                f.remove(noappts);
            }
        }
        populateUpcoming();
            
    }

    public void logoutActionPerformed(ActionEvent e) {
         f.setVisible(false);
         LogInPage lp = new LogInPage(db);
    }

    /* Populate full table view -> good for admin view*/
    private int populateUpcoming() {
        appointments = new JTable();
        try{
            String sql = "SELECT * FROM Appointment WHERE UserEmail = \"" + user.getEmail() + 
                        "\" AND Date >= date(NOW());";
            ResultSet rs = db.executeSQL(sql);
    
            String [] apptHeaders = {"Date", "Time", "Description","Service Provider"};
            appointments.setModel(new DefaultTableModel(apptHeaders, 0));
            appointments.getTableHeader().setBackground(new Color(33, 104, 105));
            appointments.getTableHeader().setForeground(Color.WHITE);
            DefaultTableModel tblModel = (DefaultTableModel)appointments.getModel();

            if(rs.next() == false){
                return -1;
            }
            // while(rs.next()){
            //     //data will be added until finished
            //     String descr = rs.getString("Description");
            //     String date = String.valueOf(rs.getDate("Date"));
            //     String time = String.valueOf(rs.getTime("Time"));
            //     String spEmail = rs.getString("SPEmail");
            //     String spName = getSPName(spEmail);

            //     Object tbData[] = {date, time, descr, spName};

            //     //addstring array into jtable
            //     tblModel.addRow(tbData);
            // }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        // scroll = new JScrollPane(appointments);
        // // set sizes
        // scroll.setBounds(10, 150, 950, 350);
        // appointments.getColumnModel().getColumn(0).setMaxWidth(100);
        // appointments.getColumnModel().getColumn(1).setMaxWidth(100);
        // appointments.getColumnModel().getColumn(2).setMaxWidth(200);
        // f.add(scroll);
        // f.validate();
        return 0;
    }   

    private void displayUpcoming(){
        JPanel p2 = new JPanel();
        p2.setBounds(10, 80, 400, 500);
        try{
            String sql = "SELECT * FROM Appointment WHERE UserEmail = \"" + user.getEmail() + 
                        "\" AND Date >= date(NOW());";
            ResultSet rs = db.executeSQL(sql);

            while(rs.next()){
                //data will be added until finished
                String descr = rs.getString("Description");
                String date = String.valueOf(rs.getDate("Date"));
                String time = String.valueOf(rs.getTime("Time"));
                String spEmail = rs.getString("SPEmail");
                String spName = getSPName(spEmail);
                
                String str = descr + " on " + date + " at " + time + " booked with " + spName;


                JLabel appt = new JLabel(str);
                appt.setBounds(0, 0, 400, 20);
                appt.setFont(new Font("Sarif", Font.BOLD, 12));
                p2.add(appt);
                p2.validate();
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        f.add(p2);
        f.validate();
    }
}
