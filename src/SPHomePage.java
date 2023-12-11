import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.util.ArrayList;

// Light Black: 31, 36, 33
// Dark Teal: 33, 104, 105
// Mint Green: 73, 160, 120
// Light Green: 156, 197, 161
// Off white: 220, 225, 222

public class SPHomePage {
    /* GLOBALS */
    JFrame f, f2, alertFrame;
    JMenuBar mb;
    JMenuItem menu, home, makeAppt;
    JButton logout, cancel, modify, alerts, userManual;
    JPanel p;
    JLabel hello, upcoming, noappts;
    DefaultTableModel model;
    JScrollPane scroll;
    JTable appointments, newTable, oldTable;
    ServiceProvider sp;
    static Database db = new Database();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    // constructor that calls appropriate methods upon user actions
    public SPHomePage(Database db, ServiceProvider sp){
        this.db = db;
        this.sp = sp;

        // Make frame 
        f = new JFrame("Appointment Booker for Service Provider");
        f.setBackground(new Color(220, 225, 222));

        // Set up the menu bar
        mb = new JMenuBar();
        menu = new JMenu("Menu");
        menu.setFont(new Font("Sarif", Font.PLAIN, 15));
        menu.setForeground(new Color(31, 36, 33));

        logout = new JButton("Log Out");
        logout.setBackground(new Color(73, 160, 120));

        alerts = new JButton("Alerts");
        if(db.haveAlerts(sp)){
            alerts.setBackground(Color.RED);
        }
        else{
            alerts.setBackground(new Color(73, 160, 120));
        }

        userManual = new JButton("User Help");
        userManual.setBackground(new Color(73, 160, 120));

        mb.add(menu);
        mb.setBackground(new Color(73, 160, 120));
        mb.add(Box.createHorizontalGlue());
        mb.add(alerts);
        mb.add(userManual);
        mb.add(logout);

        home = new JMenuItem("Home");
        home.setFont(new Font("Sarif", Font.PLAIN, 15));
        makeAppt = new JMenuItem("Make Appointment");
        makeAppt.setFont(new Font("Sarif", Font.PLAIN, 15));

        menu.add(home);
        menu.add(makeAppt);

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

        userManual.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    manualActionPerformed(evt);
                }
                catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
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

        // show the upcoming appointments
        appointments = new JTable();
        populateUpcoming();

        // make and show cancel appt button
        cancel = new JButton("Cancel Selection Now");
        Dimension cancelSize = cancel.getPreferredSize();
        cancel.setBounds(1125, 40, cancelSize.width+10, cancelSize.height);
        cancel.setBackground(new Color(156, 197, 161));
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                cancelActionPerformed(evt);
            }
        });
        p.add(cancel);

        // panel specifications
        p.setLayout(null);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // add panel to frame
        f.add(p);

        // Make visible
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(screenSize.width, screenSize.height);
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

    /* Populate full table view */
    private void populateUpcoming() {
        // make appointments table
        String [] apptHeaders = {"Date", "Time", "Description", "Booked", "Booked By", "Canceled"};
        appointments.setModel(new DefaultTableModel(apptHeaders, 0));
        appointments.getTableHeader().setBackground(new Color(33, 104, 105));
        appointments.getTableHeader().setForeground(Color.WHITE);
        try{
            String sql = "SELECT * FROM Appointment WHERE SPEmail = \"" + sp.getEmail() +
                    "\" AND Date >= date(NOW());";
            ResultSet rs = db.executeSQL(sql);      // get the results of all future appts
            
            DefaultTableModel tblModel = (DefaultTableModel)appointments.getModel();
            
            boolean currentRS = rs.next();
            // no appts to be shown
            if(!currentRS){
                noappts = new JLabel("No Upcoming Appointments");
                noappts.setFont(new Font("Sarif", Font.PLAIN, 10));
                Dimension noSize = noappts.getPreferredSize();
                noappts.setBounds(20, 80, noSize.width+20, noSize.height);
                p.add(noappts);
                p.validate();
            }
            // while there are more results to be added to table
            while(currentRS){
                //data will be added until finished
                String descr = rs.getString("Description");
                String date = String.valueOf(rs.getDate("Date"));
                String time = String.valueOf(rs.getTime("Time"));
                String user = rs.getString("UserEmail");
                int book = rs.getInt("Booked");
                int isCanceled = rs.getInt("Canceled");
                String yesno, userName, canceledStr;
                if(book == 1){
                    yesno = "Yes";
                }
                else{
                    yesno = "No";
                }

                if(isCanceled == 1){
                    canceledStr = "Yes";
                }
                else{
                    canceledStr = "No";
                }

                ResultSet r = db.getUserName(user);
                if(r.next()){
                    userName = r.getString("FirstName") + " " + r.getString("LastName");
                }
                else{
                    userName = "No Client";
                }
                String tbData[] = {date, time, descr, yesno, userName, canceledStr};

                //add string array into jtable
                tblModel.addRow(tbData);
                currentRS = rs.next();
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        // add table and scroll pane to main frame and set column widths
        scroll = new JScrollPane(appointments);
        scroll.setBounds(10, 80, 1300, 350);
        scroll.validate();
        appointments.validate();
        appointments.getColumnModel().getColumn(0).setMaxWidth(500);
        appointments.getColumnModel().getColumn(1).setMaxWidth(200);
        appointments.getColumnModel().getColumn(2).setPreferredWidth(350);
        appointments.getColumnModel().getColumn(3).setMaxWidth(200);
        appointments.getColumnModel().getColumn(4).setPreferredWidth(275);
        f.add(scroll);
        f.validate();
    }

    private void updateUpcoming(){
        try{
            String sql = "SELECT * FROM Appointment WHERE SPEmail = \"" + sp.getEmail() +
                    "\" AND Date >= date(NOW());";
            ResultSet rs = db.executeSQL(sql);      // get the results of all future appts
            
            DefaultTableModel tblModel = (DefaultTableModel)appointments.getModel();
            
            // no appts to be shown
            boolean currentRS = rs.next();
            if(!currentRS){
                noappts = new JLabel("No Upcoming Appointments");
                noappts.setFont(new Font("Sarif", Font.PLAIN, 10));
                Dimension noSize = noappts.getPreferredSize();
                noappts.setBounds(20, 80, noSize.width+20, noSize.height);
                p.add(noappts);
                p.validate();
            }
            tblModel.setRowCount(0);
            while(currentRS){
                //data will be added until finished
                String descr = rs.getString("Description");
                String date = String.valueOf(rs.getDate("Date"));
                String time = String.valueOf(rs.getTime("Time"));
                String user = rs.getString("UserEmail");
                int book = rs.getInt("Booked");
                int isCanceled = rs.getInt("Canceled");

                String yesno, userName, canceledStr;
                if(book == 1){
                    yesno = "Yes";
                }
                else{
                    yesno = "No";
                }

                if(isCanceled == 1){
                    canceledStr = "Yes";
                }
                else{
                    canceledStr = "No";
                }

                ResultSet r = db.getUserName(user);
                if(r.next()){
                    userName = r.getString("FirstName") + " " + r.getString("LastName");
                }
                else{
                    userName = "No Client";
                }

                String tbData[] = {date, time, descr, yesno, userName, canceledStr};

                // add string array into jtable
                tblModel.addRow(tbData);
                currentRS = rs.next();
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
        // update the main page
        scroll.validate();
        f.validate();
    }

    /* ACTION LISTENERS */
    private void goHomeActionPerformed(ActionEvent e){

    }

    private void makeApptActionPerformed(ActionEvent e){

        f.setVisible(false);
        new SPBookPage(db, this);
    }

    private void logoutActionPerformed(ActionEvent e) {
        f.setVisible(false);
        new LogInPage(db);
    }

    private void alertsActionPerformed(ActionEvent e){
        alertFrame = new JFrame("Your Alerts");
        // new alerts table
        String [] newHeader = {"New Alerts"};
        newTable = new JTable();
        newTable.setModel(new DefaultTableModel(newHeader, 0));
        newTable.getTableHeader().setBackground(new Color(33, 104, 105));
        newTable.getTableHeader().setForeground(Color.WHITE);
        DefaultTableModel tblModel = (DefaultTableModel)newTable.getModel();

        ArrayList<String> notifs = db.getNewAlerts(sp);
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

        // add table and scroll pane to frame
        JScrollPane scroll2 = new JScrollPane(newTable);
        scroll2.setBounds(10, 30, 450, 200);
        scroll2.validate();
        newTable.validate();
        alertFrame.add(scroll2);

        // previous alerts table
        String [] oldHeader = {"Previous Alerts"};
        oldTable = new JTable();
        oldTable.setModel(new DefaultTableModel(oldHeader, 0));
        oldTable.getTableHeader().setBackground(new Color(33, 104, 105));
        oldTable.getTableHeader().setForeground(Color.WHITE);
        DefaultTableModel tblModel2 = (DefaultTableModel)oldTable.getModel();

        ArrayList<String> oldNotifs = db.getPastAlerts(sp);
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
        // add table and scroll pane to frame
        JScrollPane scroll3 = new JScrollPane(oldTable);
        scroll3.setBounds(10, 250, 450, 200);
        scroll3.validate();
        oldTable.validate();
        alertFrame.add(scroll3);

        // add close button
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
        db.sawNewAlerts(sp);

        //change color of alerts button
        alerts.setBackground(new Color(73, 160, 120));
        f.validate();

        // discard the frame
        alertFrame.dispose();
    }

    private void cancelActionPerformed(ActionEvent e){
        // find selected appointment to be canceled
        int rowIndex = appointments.getSelectedRow();
        if(rowIndex == -1){
            JOptionPane.showMessageDialog(null,
                    "Please select an appointment.");
            return;
        }

        // show confirmation message
        int selection = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to cancel your appointment on "
                        + appointments.getValueAt(rowIndex, 0) + " at "
                        + appointments.getValueAt(rowIndex, 1) + "?");
        if(selection == 1 || selection == 2){ //no || cancel
            return;
        }
        else{ //yes
            int apptId = db.getApptId(String.valueOf(appointments.getValueAt(rowIndex, 0)),
                    String.valueOf(appointments.getValueAt(rowIndex, 1)), sp.getEmail());

            db.cancelAppointment(apptId);
            // show confirmation message
            JOptionPane.showMessageDialog(null,
                    "Successfully canceled.");
            updateUpcoming();
        }
    }

    public void manualActionPerformed(ActionEvent e) throws MalformedURLException {
        URL manualURL = new URL("file:///C:/Users/slw72/Downloads/User%20Guide.pdf");
        try {
            openWebpage(manualURL.toURI());
        }
        catch (URISyntaxException exc) {
            exc.printStackTrace();
        }
    }

    public static void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
