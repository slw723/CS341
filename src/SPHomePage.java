//package src;

// import src.Database;
// import src.SPBookPage;
// import src.ServiceProvider;

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

public class SPHomePage {
    JFrame f, f2;
    JMenuBar mb;
    JMenuItem menu, home, makeAppt, history;
    JButton logout;
    JPanel p;
    JLabel hello, upcoming, noappts;
    DefaultTableModel model;
    JScrollPane scroll;
    JTable appointments;
    ServiceProvider sp;
    static Database db = new Database();

    public SPHomePage(Database db, ServiceProvider sp){
        this.db = db;
        this.sp = sp;
    
        /* Make frame */
        f = new JFrame("Appointment Booker for Service Provider");
        f.setBackground(new Color(220, 225, 222));

        /* Set up the menu bar */
        mb = new JMenuBar();
        menu = new JMenu("Menu");
        menu.setFont(new Font("Sarif", Font.PLAIN, 15));
        menu.setForeground(new Color(31, 36, 33));

        logout = new JButton("Log Out");

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
        String str = "Hello, " + sp.getFirstName() + "!";
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

        //panel specifications
        p.setLayout(null);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //add panel to frame
        f.add(p);

        /* Make visible */
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(1000,800);
        f.setVisible(true);
    }

    public void setHomeVisible(){
        f.setVisible(true);
        for(Component c : f.getComponents()){
            if(c == noappts){
                f.remove(noappts);
            }
        }
        populateUpcoming();
    }

    public ServiceProvider getSP(){
        return sp;
    }

    public void goHomeActionPerformed(ActionEvent e){

    }

    public void makeApptActionPerformed(ActionEvent e){

        f.setVisible(false);
        SPBookPage bp = new SPBookPage(db, this);
    }

    public void historyActionPerformed(ActionEvent e){

        f.setVisible(false);

    }

    public void logoutActionPerformed(ActionEvent e) {
        f.setVisible(false);
        LogInPage lp = new LogInPage(db);
    }

    /* Populate full table view -> good for admin view*/
    private int populateUpcoming() {
        appointments = new JTable();
        String [] apptHeaders = {"Date", "Time", "Description", "Booked", "Booked By"};
        appointments.setModel(new DefaultTableModel(apptHeaders, 0));
        appointments.getTableHeader().setBackground(new Color(33, 104, 105));
        appointments.getTableHeader().setForeground(Color.WHITE);
        try{
            String sql = "SELECT * FROM Appointment WHERE SPEmail = \"" + sp.getEmail() +
                    "\" AND Date >= date(NOW());";
            ResultSet rs = db.executeSQL(sql);
        
            if(!rs.next()){
                return -1;
            }
            
        //     DefaultTableModel tblModel = (DefaultTableModel)appointments.getModel();
        //     while(rs.next()){
        //         //data will be added until finished
        //         String descr = rs.getString("Description");
        //         String date = String.valueOf(rs.getDate("Date"));
        //         String time = String.valueOf(rs.getTime("Time"));
        //         String user = rs.getString("UserEmail");
        //         int book = rs.getInt("Booked");
        //         String yesno, userName;
        //         if(book == 1){
        //             yesno = "Yes";
        //         }
        //         else{
        //             yesno = "No";
        //         }

        //         ResultSet r = db.getUserName(user);
        //         if(r.next()){
        //             userName = r.getString("FirstName") + " " + r.getString("LastName");
        //         }
        //         else{
        //             userName = "No Client";
        //         }


        //         String tbData[] = {date, time, descr, yesno, userName};

        //         //addstring array into jtable
        //         tblModel.addRow(tbData);
        //     }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        // scroll = new JScrollPane(appointments);
        // scroll.setBounds(10, 150, 950, 350);
        // appointments.getColumnModel().getColumn(0).setMaxWidth(400);
        // appointments.getColumnModel().getColumn(1).setMaxWidth(100);
        // appointments.getColumnModel().getColumn(2).setMaxWidth(100);
        // f.add(scroll);
        // f.validate();
        return 0;
    }


    private void displayUpcoming(){
        JPanel p2 = new JPanel();
        p2.setBounds(10, 80, 400, 500);
        

        try{
            String sql = "SELECT * FROM Appointment WHERE SPEmail = \"" + sp.getEmail() +
                    "\" AND Date >= date(Now());";
            ResultSet rs = db.executeSQL(sql);
            while(rs.next()){
                String descr = rs.getString("Description");
                String date = String.valueOf(rs.getDate("Date"));
                String time = String.valueOf(rs.getTime("Time"));
                String user = rs.getString("UserEmail");
                int book = rs.getInt("Booked");
                ResultSet r = db.getUserName(user);
                String userName, str;
                if(r.next()){
                    userName = r.getString("FirstName") + " " + r.getString("LastName");
                }
                else{
                    userName = "No Client";
                }
                
                
                if(book == 1){
                    str = descr + " on " + date + " at " + time + " booked with " + userName;
                }
                else{
                    str = descr + " on " + date + " at " + time + " not booked";
                }  
                JLabel appt = new JLabel(str);
                appt.setBounds(0, 0, 300, 20);
                appt.setFont(new Font("Sarif", Font.BOLD, 12));
                p2.add(appt);
                p2.validate();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        f.add(p2);
        f.validate();
    }
}
