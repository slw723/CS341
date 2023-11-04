import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// Light Black: 31, 36, 33
// Dark Teal: 33, 104, 105
// Mint Green: 73, 160, 120
// Light Green: 156, 197, 161
// Off white: 220, 225, 222

public class UserHomePage extends DefaultTableCellRenderer {
        JFrame f, f2, alertFrame;
        JMenuBar mb;
        JMenuItem menu, home, makeAppt, history;
        JButton logout,cancel, modify, alerts;
        JPanel p;
        JLabel hello, upcoming, noappts;
        DefaultTableModel model;
        JTable appointments, newTable, oldTable;
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

        alerts = new JButton("Alerts");
        if(db.haveAlerts(user)){
            alerts.setBackground(Color.RED);
        }
        else{
            alerts.setBackground(new Color(73, 160, 120));
        }

        mb.add(menu);
        mb.setBackground(new Color(73, 160, 120));
        mb.add(Box.createHorizontalGlue());
        mb.add(alerts);
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

        alerts.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                alertsActionPerformed(evt);
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

        // show the upcoming appointments
        appointments = new JTable();
        populateUpcoming();

        cancel = new JButton("Cancel Selection Now");
        Dimension cancelSize = cancel.getPreferredSize();
        cancel.setBounds(600, 40, cancelSize.width+10, cancelSize.height);
        cancel.setBackground(new Color(156, 197, 161));
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cancelActionPerformed(evt);
            }
        });
        p.add(cancel);

        modify = new JButton("Modify Selection Now");
        Dimension modSize = modify.getPreferredSize();
        modify.setBounds(cancelSize.width+620, 40, modSize.width+10, modSize.height);
        modify.setBackground(new Color(156, 197, 161));
        modify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                modifyActionPerformed(evt);
            }
        });
        p.add(modify);

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
        updateUpcoming();
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
            
    }

    public void logoutActionPerformed(ActionEvent e) {
         f.setVisible(false);
         LogInPage lp = new LogInPage(db);
    }

    public void alertsActionPerformed(ActionEvent e){
        alertFrame = new JFrame("Your Alerts");
        //new alerts table
        String [] newHeader = {"New Alerts"};
        newTable = new JTable();
        newTable.setModel(new DefaultTableModel(newHeader, 0));
        newTable.getTableHeader().setBackground(new Color(33, 104, 105));
        newTable.getTableHeader().setForeground(Color.WHITE);
        DefaultTableModel tblModel = (DefaultTableModel)newTable.getModel();

        ArrayList<String> notifs = db.getNewAlerts(user);
        if(notifs.size() == 0){
            String[] data = {"No new alerts"};
            tblModel.addRow(data);
        }
        else{
            for(String s : notifs){
                //add string array into jtable
                String[] data = {s};
                tblModel.addRow(data);
            }
        }
        
        //add to frame
        JScrollPane scroll2 = new JScrollPane(newTable);
        scroll2.setBounds(10, 30, 450, 200);
        scroll2.validate();
        newTable.validate();
        alertFrame.add(scroll2);

        //previous alerts table
        String [] oldHeader = {"Previous Alerts"};
        oldTable = new JTable();
        oldTable.setModel(new DefaultTableModel(oldHeader, 0));
        oldTable.getTableHeader().setBackground(new Color(33, 104, 105));
        oldTable.getTableHeader().setForeground(Color.WHITE);
        DefaultTableModel tblModel2 = (DefaultTableModel)oldTable.getModel();
        
        ArrayList<String> oldNotifs = db.getPastAlerts(user);
        if(oldNotifs.size() == 0){
            String[] data = {"No previous alerts"};
            tblModel2.addRow(data);
        }
        else{
            for(String s : oldNotifs){
                //add string array into jtable
                String[] data = {s};
                tblModel2.addRow(data);
            }
        }
        //add to frame
        JScrollPane scroll3 = new JScrollPane(oldTable);
        scroll3.setBounds(10, 250, 450, 200);
        scroll3.validate();
        oldTable.validate();
        alertFrame.add(scroll3);

        JButton ok = new JButton("OK");
        ok.setBackground(new Color(156, 197, 161));
        Dimension okSize = ok.getPreferredSize();
        ok.setBounds(10, 500, okSize.width+10, okSize.height);
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                alertCloseActionPerformed(evt);
            }
        });
        alertFrame.add(ok);

        alertFrame.setLayout(null);
        alertFrame.setSize(500,580);
        alertFrame.setVisible(true);
        alertFrame.validate();

    }

    private void alertCloseActionPerformed(ActionEvent e){

        //update that new appts have been alerted
        db.sawNewAlerts(user);

        //change color of alerts button
        alerts.setBackground(new Color(73, 160, 120));
        f.validate();

        alertFrame.dispose();
    }

    private void cancelActionPerformed(ActionEvent e) {
        int rowIndex = appointments.getSelectedRow();
        if (rowIndex == -1) {
            JOptionPane.showMessageDialog(null, "Please select an appointment.");
            return;
        }

        int selection = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to cancel your appointment on "
                + appointments.getValueAt(rowIndex, 0) + " at "
                + appointments.getValueAt(rowIndex, 1) + "?");
        if (selection == 1 || selection == 2) {
            return;
        }
        else {
            int apptId = db.getApptIdUser(String.valueOf(appointments.getValueAt(rowIndex, 0)),
                    String.valueOf(appointments.getValueAt(rowIndex, 1)), user.getEmail());

            db.cancelAppointment(apptId);
            JOptionPane.showMessageDialog(null, "Successfully canceled.");
            updateUpcoming();
        }
    }

    private void modifyActionPerformed(ActionEvent e) {
        JFrame f2;
        JPanel p2;
        JLabel date, time, user, descr;
        JTextField dateField, timeField, userField, descrField;
        int rowIndex = appointments.getSelectedRow();
        if (rowIndex == -1) {
            JOptionPane.showMessageDialog(null, "Please select an appointment.");
            return;
        }
        f2 = new JFrame();
        p2 = new JPanel(new GridLayout(5, 2, 10, 20));
        f2.add(p2);

        descr = new JLabel("Description: ");
        descr.setSize(100, 20);
        descrField = new JTextField();

        String d = String.valueOf(appointments.getValueAt(rowIndex, 0));
        descrField.setText(d);
        descrField.setEditable(false);
        descrField.setSize(100, 20);

        p2.add(descr);
        p2.add(descrField);

        f2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f2.setSize(500, 500);
        f2.setLocation(275, 150);
        f2.setVisible(true);
    }

    /* Populate full table view -> good for admin view*/
    private void populateUpcoming() {
        appointments = new JTable();
        String [] apptHeaders = {"Date", "Time", "Description", "Service Provider", "Canceled"};
            appointments.setModel(new DefaultTableModel(apptHeaders, 0));
            appointments.getTableHeader().setBackground(new Color(33, 104, 105));
            appointments.getTableHeader().setForeground(Color.WHITE);
        try{
            String sql = "SELECT * FROM Appointment WHERE UserEmail = \"" + user.getEmail() + 
                        "\" AND Date >= date(NOW());";
            ResultSet rs = db.executeSQL(sql);
    
            DefaultTableModel tblModel = (DefaultTableModel)appointments.getModel();

            //no appts to be shown
            if(rs.next() == false){
                noappts = new JLabel("No Upcoming Appointments");
                noappts.setFont(new Font("Sarif", Font.PLAIN, 10));
                Dimension noSize = noappts.getPreferredSize();
                noappts.setBounds(20, 80, noSize.width+20, noSize.height);
                p.add(noappts);
            }
            while(rs.next()){
                //data will be added until finished
                String descr = rs.getString("Description");
                String date = String.valueOf(rs.getDate("Date"));
                String time = String.valueOf(rs.getTime("Time"));
                String spEmail = rs.getString("SPEmail");
                int isCanceled = rs.getInt("Canceled");
                String yesno;
                if(isCanceled == 1){
                    yesno = "Yes";
                }
                else{
                    yesno = "No";
                }

                String spName = getSPName(spEmail);

                Object tbData[] = {date, time, descr, spName, yesno};

                //addstring array into jtable
                tblModel.addRow(tbData);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        scroll = new JScrollPane(appointments);
        scroll.setBounds(10, 80, 950, 350);
        scroll.validate();
        appointments.getColumnModel().getColumn(0).setMaxWidth(100);
        appointments.getColumnModel().getColumn(1).setMaxWidth(100);
        appointments.getColumnModel().getColumn(2).setPreferredWidth(150);
        f.add(scroll);
        f.validate();
    }   

    private void updateUpcoming(){
        try{
            String sql = "SELECT * FROM Appointment WHERE UserEmail = \"" + user.getEmail() + 
                        "\" AND Date >= date(NOW());";
            ResultSet rs = db.executeSQL(sql);
    
            DefaultTableModel tblModel = (DefaultTableModel)appointments.getModel();

            //no appts to be shown
            if(rs.next() == false){
                noappts = new JLabel("No Upcoming Appointments");
                noappts.setFont(new Font("Sarif", Font.PLAIN, 10));
                Dimension noSize = noappts.getPreferredSize();
                noappts.setBounds(20, 80, noSize.width+20, noSize.height);
                p.add(noappts);
            }
            int row = 0;
            while(rs.next()){
                //data will be added until finished
                String descr = rs.getString("Description");
                String date = String.valueOf(rs.getDate("Date"));
                String time = String.valueOf(rs.getTime("Time"));
                String spEmail = rs.getString("SPEmail");
                int isCanceled = rs.getInt("Canceled");
                String yesno;
                if(isCanceled == 1){
                    yesno = "Yes";
                }
                else{
                    yesno = "No";
                }

                String spName = getSPName(spEmail);
                //if model already contains the value... don't add it
                if(tblModel.getValueAt(row, 0).equals(date) && 
                   tblModel.getValueAt(row, 1).equals(time)){
                    continue;
                }

                Object tbData[] = {date, time, descr, spName, yesno};

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

    private void greyOutCanceled() {
        for (int i = 0; i <appointments.getRowCount(); i++) {
            int apptId = db.getApptIdUser(String.valueOf(appointments.getValueAt(i, 0)),
                    String.valueOf(appointments.getValueAt(i, 1)), user.getEmail());
            int canceled = db.getCanceled(apptId);
            if (canceled == 1) {
                //need to do
            }
        }
    }

    
}
