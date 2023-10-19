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
    JButton logout, cancel, modify;
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
        upcoming.setBounds(10, 40, upSize.width+10, upSize.height);
        p.add(upcoming);

        // show the upcoming appointments if they exists
        appointments = new JTable();
        populateUpcoming();

        cancel = new JButton("Cancel Selection Now");
        Dimension cancelSize = cancel.getPreferredSize();
        cancel.setBounds(600, 40, cancelSize.width+10, cancelSize.height);
        cancel.setBackground(new Color(156, 197, 161));
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                cancelActionPerformed(evt);
            }
        });
        p.add(cancel);

        modify = new JButton("Modify Selection Now");
        Dimension modSize = modify.getPreferredSize();
        modify.setBounds(620+cancelSize.width, 40, modSize.width+10, modSize.height);
        modify.setBackground(new Color(156, 197, 161));
        modify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                modifyActionPerformed(evt);
            }
        });
        p.add(modify);

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

    /* HELPERS */
    public void setHomeVisible(){
        f.setVisible(true);
        updateUpcoming();
    }

    public ServiceProvider getSP(){
        return sp;
    }

    /* Populate full table view -> good for admin view*/
    private void populateUpcoming() {
        
        String [] apptHeaders = {"Date", "Time", "Description", "Booked", "Booked By"};
        appointments.setModel(new DefaultTableModel(apptHeaders, 0));
        appointments.getTableHeader().setBackground(new Color(33, 104, 105));
        appointments.getTableHeader().setForeground(Color.WHITE);
        try{
            String sql = "SELECT * FROM Appointment WHERE SPEmail = \"" + sp.getEmail() +
                    "\" AND Date >= date(NOW());";
            ResultSet rs = db.executeSQL(sql);
        
            // no appts to be shown
            if(!rs.next()){
                noappts = new JLabel("No Upcoming Appointments");
                noappts.setFont(new Font("Sarif", Font.PLAIN, 10));
                Dimension noSize = noappts.getPreferredSize();
                noappts.setBounds(20, 80, noSize.width+20, noSize.height);
                p.add(noappts);
                p.validate();
            }
            DefaultTableModel tblModel = (DefaultTableModel)appointments.getModel();
            while(rs.next()){
                //data will be added until finished
                String descr = rs.getString("Description");
                String date = String.valueOf(rs.getDate("Date"));
                String time = String.valueOf(rs.getTime("Time"));
                String user = rs.getString("UserEmail");
                int book = rs.getInt("Booked");
                String yesno, userName;
                if(book == 1){
                    yesno = "Yes";
                }
                else{
                    yesno = "No";
                }

                ResultSet r = db.getUserName(user);
                if(r.next()){
                    userName = r.getString("FirstName") + " " + r.getString("LastName");
                }
                else{
                    userName = "No Client";
                }
                String tbData[] = {date, time, descr, yesno, userName};

                //addstring array into jtable
                tblModel.addRow(tbData);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        scroll = new JScrollPane(appointments);
        scroll.setBounds(10, 80, 940, 350);
        scroll.validate();
        appointments.validate();
        appointments.getColumnModel().getColumn(0).setMaxWidth(400);
        appointments.getColumnModel().getColumn(1).setMaxWidth(100);
        appointments.getColumnModel().getColumn(2).setPreferredWidth(250);
        appointments.getColumnModel().getColumn(3).setMaxWidth(100);
        f.add(scroll);
        f.validate();
    }

    private void updateUpcoming(){

        try{
            String sql = "SELECT * FROM Appointment WHERE SPEmail = \"" + sp.getEmail() +
                    "\" AND Date >= date(NOW());";
            ResultSet rs = db.executeSQL(sql);
        
            // no appts to be shown
            if(!rs.next()){
                noappts = new JLabel("No Upcoming Appointments");
                noappts.setFont(new Font("Sarif", Font.PLAIN, 10));
                Dimension noSize = noappts.getPreferredSize();
                noappts.setBounds(20, 80, noSize.width+20, noSize.height);
                p.add(noappts);
                p.validate();
            }
            DefaultTableModel tblModel = (DefaultTableModel)appointments.getModel();
            int row = 0;
            while(rs.next()){
                //data will be added until finished
                String descr = rs.getString("Description");
                String date = String.valueOf(rs.getDate("Date"));
                String time = String.valueOf(rs.getTime("Time"));
                String user = rs.getString("UserEmail");
                int book = rs.getInt("Booked");
                //if model already contains the value... don't add it
                if(tblModel.getValueAt(row, 0).equals(date) && 
                   tblModel.getValueAt(row, 1).equals(time)){
                    continue;
                }
                
                String yesno, userName;
                if(book == 1){
                    yesno = "Yes";
                }
                else{
                    yesno = "No";
                }

                ResultSet r = db.getUserName(user);
                if(r.next()){
                    userName = r.getString("FirstName") + " " + r.getString("LastName");
                }
                else{
                    userName = "No Client";
                }

                String tbData[] = {date, time, descr, yesno, userName};

                //add string array into jtable
                tblModel.addRow(tbData);
                row++;
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        scroll.validate();
        f.validate();
    }

    /* ACTION LISTENERS */
    private void goHomeActionPerformed(ActionEvent e){
        
    }

    private void makeApptActionPerformed(ActionEvent e){

        f.setVisible(false);
        SPBookPage bp = new SPBookPage(db, this);
    }

    private void historyActionPerformed(ActionEvent e){

        f.setVisible(false);

    }

    private void logoutActionPerformed(ActionEvent e) {
        f.setVisible(false);
        LogInPage lp = new LogInPage(db);
    }

    private void cancelActionPerformed(ActionEvent e){
        int rowIndex = appointments.getSelectedRow();
        if(rowIndex == -1){
            JOptionPane.showMessageDialog(null, 
            "Please select an appointment.");
            return;
        }
        
    }

    private void modifyActionPerformed(ActionEvent e){
        JFrame f2;
        JPanel p2;
        JLabel date, time, sp, descr;
        JTextField dateField, timeField, spField, descrField;
        int rowIndex = appointments.getSelectedRow();
        if(rowIndex == -1){
            JOptionPane.showMessageDialog(null, 
            "Please select an appointment.");
            return;
        }
        f2 = new JFrame();
        p2 = new JPanel(new GridLayout(5, 2, 10, 20));
        f2.add(p2);

        descr = new JLabel("Description: ");
        descr.setSize(100, 20);
        descrField = new JTextField();
        // set default text uneditable
        String d = String.valueOf(appointments.getValueAt(rowIndex, 0));
        descrField.setText(d);
        descrField.setEditable(false);       
        descrField.setSize(100, 20);
        

        /* add components */
        p2.add(descr);
        p2.add(descrField);

        /* Make visible */
        f2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f2.setSize(500,500);
        f2.setLocation(275, 150);
        f2.setVisible(true);
    }
   
}
