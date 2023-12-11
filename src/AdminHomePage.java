import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

// Light Black: 31, 36, 33
// Dark Teal: 33, 104, 105
// Mint Green: 73, 160, 120
// Light Green: 156, 197, 161
// Off white: 220, 225, 222

public class AdminHomePage {
    JFrame f;
    JButton logout, userGoButton, spGoButton, apptGoButton, apptCancel, userDeleteButton, userManual;
    JMenuBar mb;
    JMenuItem menu, home, reports;
    JPanel p, userPanel, spPanel, apptPanel;
    JScrollPane scroll, scroll2, scroll3;
    JLabel welcome, userSearchLabel, spSearchLabel, apptSearchLabel;
    JTextField userSearchText, spSearchText, apptSearchText;
    JTable users, serviceProviders, appointments;
    Admin admin;

    JTabbedPane tabbedPane;

    TableRowSorter userSorter, spSorter, apptSorter;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    static Database db = new Database();

    public AdminHomePage(Database db, Admin admin){
        this.db = db;
        this.admin = admin;

        /*Make frame*/
        f = new JFrame("Admin Home Page");
        f.setBackground(new Color(220, 225, 222));
        f.setLayout(null);

        mb = new JMenuBar();
        menu = new JMenu("Menu");
        menu.setFont(new Font("Sarif", Font.PLAIN, 15));
        menu.setForeground(new Color(31, 36, 33));

        logout = new JButton("Log Out");
        logout.setBackground(new Color(73, 160, 120));

        userManual = new JButton("User Help");
        userManual.setBackground(new Color(73, 160, 120));

        mb.add(menu);
        mb.setBackground(new Color(73, 160, 120));
        mb.add(Box.createHorizontalGlue());
        mb.add(userManual);
        mb.add(logout);

        home = new JMenuItem("Home");
        home.setFont(new Font("Sarif", Font.PLAIN, 15));
        reports = new JMenuItem("Reports");
        reports.setFont(new Font("Sarif", Font.PLAIN, 15));

        menu.add(home);
        menu.add(reports);

        reports.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                reportsActionPerformed(evt);
            }
        });
        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                logoutActionPerformed(evt);
            }
        });

        userManual.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    manualActionPerformed(evt);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        f.setJMenuBar(mb);
        // welcome
        p = new JPanel();
        f.getContentPane();
        String str = "Welcome!";
        welcome = new JLabel(str);
        welcome.setFont(new Font("Sarif", Font.PLAIN, 20));
        welcome.setForeground(new Color(31, 36, 33));
        Dimension wcSize = welcome.getPreferredSize();
        welcome.setBounds(450, 10, wcSize.width+10, wcSize.height);
        f.add(welcome);

        /*Create JTabbedPane and its tabs (panels)*/
        userPanel = new JPanel();
        userPanel.setLayout(null);
        userSearchLabel = new JLabel("Search: ");
        userSearchLabel.setBounds(20, 25, 50, 25);
        userSearchText = new JTextField();
        userSearchText.setBounds(80, 25, 200, 25);
        userGoButton = new JButton("Go");
        userGoButton.setBounds(290, 25, 75, 25);
        userGoButton.setBackground(new Color(73, 160, 120));
        userGoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                goUserActionPerformed(evt);
            }
        });

        userDeleteButton = new JButton("Delete User");
        userDeleteButton.setBounds(700, 25, 150, 25);
        userDeleteButton.setBackground(new Color(73, 160, 120));
        userDeleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                deleteUserActionPerformed(evt);
            }
        });
        
        userPanel.add(userSearchLabel);
        userPanel.add(userSearchText);
        userPanel.add(userGoButton);
        userPanel.add(userDeleteButton);

        spPanel = new JPanel();
        spPanel.setLayout(null);
        spSearchLabel = new JLabel("Search: ");
        spSearchLabel.setBounds(20, 25, 50, 25);
        spSearchText = new JTextField();
        spSearchText.setBounds(80, 25, 200, 25);
        spGoButton = new JButton("Go");
        spGoButton.setBounds(290, 25, 75, 25);
        spGoButton.setBackground(new Color(73, 160, 120));
        spGoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                goSPActionPerformed(evt);
            }
        });
        
        spPanel.add(spSearchLabel);
        spPanel.add(spSearchText);
        spPanel.add(spGoButton);


        apptPanel = new JPanel();
        apptPanel.setLayout(null);
        apptSearchLabel = new JLabel("Search: ");
        apptSearchLabel.setBounds(20, 25, 50, 25);
        apptSearchText = new JTextField();
        apptSearchText.setBounds(80, 25, 200, 25);
        apptGoButton = new JButton("Go");
        apptGoButton.setBounds(290, 25, 75, 25);
        apptGoButton.setBackground(new Color(73, 160, 120));
        apptGoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                goApptActionPerformed(evt);
            }
        });
        apptCancel = new JButton("Cancel Selection Now");
        Dimension apptCancelSize = apptCancel.getPreferredSize();
        apptCancel.setBounds(700, 25, apptCancelSize.width+10, apptCancelSize.height);
        apptCancel.setBackground(new Color(73, 160, 120));
        apptCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                cancelApptActionPerformed(evt);
            }
        });

        apptPanel.add(apptCancel);
        apptPanel.add(apptSearchLabel);
        apptPanel.add(apptSearchText);
        apptPanel.add(apptGoButton);

        UIManager.put("TabbedPane.selected", new Color(31, 36, 33));

        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(50,75, 900, 650);
        tabbedPane.add("Users", userPanel);
        tabbedPane.add("Service Providers", spPanel);
        tabbedPane.add("Appointments", apptPanel);
        tabbedPane.setBackgroundAt(0, new Color(33, 104, 105));
        tabbedPane.setForegroundAt(0, Color.WHITE);
        tabbedPane.setBackgroundAt(1, new Color(33, 104, 105));
        tabbedPane.setForegroundAt(1, Color.WHITE);
        tabbedPane.setBackgroundAt(2, new Color(33, 104, 105));
        tabbedPane.setForegroundAt(2, Color.WHITE);
        f.add(tabbedPane);

        // show the database's current Users and Service Providers
        users = new JTable();
        populateUsers();
        userSorter = new TableRowSorter(users.getModel());
        users.setRowSorter(userSorter);


        serviceProviders = new JTable();
        populateSPs();
        spSorter = new TableRowSorter(serviceProviders.getModel());
        serviceProviders.setRowSorter(spSorter);

        appointments = new JTable();
        populateAppts();
        apptSorter = new TableRowSorter(appointments.getModel());
        appointments.setRowSorter(apptSorter);

        /* Make visible */
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(screenSize.width, screenSize.height);
        f.setVisible(true);
    }

    private void populateUsers(){
        String [] userHeaders = {"First Name", "Last Name", "Email", "Phone Number", "Active"};
        users.setModel(new DefaultTableModel(userHeaders, 0));
        users.getTableHeader().setBackground(new Color(33, 104, 105));
        users.getTableHeader().setForeground(Color.WHITE);

        try{
            String sql = "SELECT * FROM user";
            ResultSet rs = db.executeSQL(sql);

            DefaultTableModel tblModel = (DefaultTableModel)users.getModel();
            while(rs.next()){
                //data will be added until finished
                String fName = rs.getString("FirstName");
                String lName = rs.getString("LastName");
                String email = rs.getString("Email");
                String phoneNum = String.valueOf(rs.getLong("PhoneNum"));
                int active = rs.getInt("Active");

                String yesno;
                if(active == 1){
                    yesno = "Yes";
                }
                else{
                    yesno = "No";
                }

                String tbData[] = {fName, lName, email, phoneNum, yesno};

                //add string array into jtable
                tblModel.addRow(tbData);
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
        scroll = new JScrollPane(users);
        scroll.setBounds(0, 100, 900, 700);
        scroll.validate();
        users.validate();
        users.getColumnModel().getColumn(0).setMinWidth(100);
        users.getColumnModel().getColumn(1).setMinWidth(100);
        users.getColumnModel().getColumn(2).setMinWidth(100);
        users.getColumnModel().getColumn(3).setMinWidth(100);
        users.getColumnModel().getColumn(4).setMinWidth(100);
        userPanel.add(scroll);
        userPanel.validate();
    }

    private void populateSPs(){
        String [] spHeaders = {"First Name", "Last Name", "Type", "Qualification", "Year Graduated", "Email", "Phone Number"};
        DefaultTableModel model = new DefaultTableModel(spHeaders, 0);
        serviceProviders.setModel(model);
        serviceProviders.getTableHeader().setBackground(new Color(33, 104, 105));
        serviceProviders.getTableHeader().setForeground(Color.WHITE);

        try{
            String sql = "SELECT * FROM serviceprovider";
            ResultSet rs = db.executeSQL(sql);
            DefaultTableModel tblModel = (DefaultTableModel)serviceProviders.getModel();

            while(rs.next()){
                //data will be added until finished
                String fName = rs.getString("FirstName");
                String lName = rs.getString("LastName");
                String type = rs.getString("Type");
                String qual = rs.getString("Qualification");
                String year = String.valueOf(rs.getInt("YearGraduated"));
                String email = rs.getString("Email");
                String phoneNum = String.valueOf(rs.getLong("PhoneNum"));

                String tbData[] = {fName, lName, type, qual, year, email, phoneNum};

                //add string array into jtable
                tblModel.addRow(tbData);
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
        scroll2 = new JScrollPane(serviceProviders);
        scroll2.setBounds(00, 100, 900, 700);
        scroll2.validate();
        serviceProviders.validate();
        serviceProviders.getColumnModel().getColumn(0).setMinWidth(75);
        serviceProviders.getColumnModel().getColumn(1).setMinWidth(75);
        serviceProviders.getColumnModel().getColumn(2).setMinWidth(50);
        serviceProviders.getColumnModel().getColumn(3).setMinWidth(250);
        serviceProviders.getColumnModel().getColumn(4).setMinWidth(50);
        serviceProviders.getColumnModel().getColumn(5).setMinWidth(100);
        serviceProviders.getColumnModel().getColumn(6).setMinWidth(75);
        spPanel.add(scroll2);
        spPanel.validate();
    }

    private void populateAppts(){
        String [] apptHeaders = {"Description", "Date", "Time", "Type", "User", "Service Provider", "Booked", "Cancelled"};
        appointments.setModel(new DefaultTableModel(apptHeaders, 0));
        appointments.getTableHeader().setBackground(new Color(33, 104, 105));
        appointments.getTableHeader().setForeground(Color.WHITE);

        try{
            String sql = "SELECT * FROM appointment";
            ResultSet rs = db.executeSQL(sql);
            DefaultTableModel tblModel = (DefaultTableModel)appointments.getModel();
            while(rs.next()){
                //data will be added until finished
                String descr = rs.getString("Description");
                String date = String.valueOf(rs.getDate("Date"));
                String time = String.valueOf(rs.getTime("Time"));
                String type = rs.getString("Type");
                String user = rs.getString("UserEmail");
                String sp = rs.getString("SPEmail");
                int book = rs.getInt("Booked");
                int isCanceled = rs.getInt("Canceled");
                String yesno, userName, spName, canceledStr;
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

                ResultSet r2 = db.getSPName(sp);
                if(r2.next()){
                    spName = r2.getString("FirstName") + " " + r2.getString("LastName");
                }
                else{
                    spName = "No Service Provider";
                }
                String tbData[] = {descr, date, time, type, userName, spName, yesno, canceledStr};

                //add string array into jtable
                tblModel.addRow(tbData);
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
        scroll3 = new JScrollPane(appointments);
        scroll3.setBounds(00, 100, 900, 700);
        scroll3.validate();
        appointments.validate();
        appointments.getColumnModel().getColumn(0).setMinWidth(250);
        appointments.getColumnModel().getColumn(1).setMinWidth(75);
        appointments.getColumnModel().getColumn(2).setMinWidth(50);
        appointments.getColumnModel().getColumn(3).setMinWidth(75);
        appointments.getColumnModel().getColumn(4).setMinWidth(50);
        appointments.getColumnModel().getColumn(5).setMinWidth(100);
        appointments.getColumnModel().getColumn(6).setMinWidth(75);
        apptPanel.add(scroll3);
        apptPanel.validate();
    }

    private void updateAppts(){
        try{
            String sql = "SELECT * FROM appointment";
            ResultSet rs = db.executeSQL(sql);
            DefaultTableModel tblModel = (DefaultTableModel)appointments.getModel();
            tblModel.setRowCount(0);
            while(rs.next()){
                //data will be added until finished
                String descr = rs.getString("Description");
                String date = String.valueOf(rs.getDate("Date"));
                String time = String.valueOf(rs.getTime("Time"));
                String type = rs.getString("Type");
                String user = rs.getString("UserEmail");
                String sp = rs.getString("SPEmail");
                int book = rs.getInt("Booked");
                int isCanceled = rs.getInt("Canceled");
                String yesno, userName, spName, canceledStr;
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

                ResultSet r2 = db.getSPName(sp);
                if(r2.next()){
                    spName = r2.getString("FirstName") + " " + r2.getString("LastName");
                }
                else{
                    spName = "No Service Provider";
                }
                String tbData[] = {descr, date, time, type, userName, spName, yesno, canceledStr};

                //add string array into jtable
                tblModel.addRow(tbData);
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
        scroll3.validate();
        appointments.validate();
        apptPanel.validate();
    }

    private void updateUsers(){

        try{
            String sql = "SELECT * FROM user";
            ResultSet rs = db.executeSQL(sql);
            DefaultTableModel tblModel = (DefaultTableModel)users.getModel();
            tblModel.setRowCount(0);
            while(rs.next()){
                //data will be added until finished
                String fName = rs.getString("FirstName");
                String lName = rs.getString("LastName");
                String email = rs.getString("Email");
                String phoneNum = String.valueOf(rs.getLong("PhoneNum"));
                int active = rs.getInt("Active");

                String yesno;
                if(active == 1){
                    yesno = "Yes";
                }
                else{
                    yesno = "No";
                }

                String tbData[] = {fName, lName, email, phoneNum, yesno};

                //add string array into jtable
                tblModel.addRow(tbData);
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }
        scroll3.validate();
        users.validate();
        userPanel.validate();
    }

    private void logoutActionPerformed(ActionEvent e) {
        f.setVisible(false);
        new LogInPage(db);
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

    private void reportsActionPerformed(ActionEvent e){
        f.setVisible(false);
        new AdminReportsPage(db, this);
    }

    private void cancelApptActionPerformed(ActionEvent e){
        int rowIndex = appointments.getSelectedRow();
        if(rowIndex == -1){
            JOptionPane.showMessageDialog(null,
                    "Please select an appointment.");
            return;
        }

        int selection = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to cancel the appointment on "
                        + appointments.getValueAt(rowIndex, 0) + " at "
                        + appointments.getValueAt(rowIndex, 1) + " for "
                        + appointments.getValueAt(rowIndex, 4) + " with "
                        + appointments.getValueAt(rowIndex, 5));
        if(selection == 1 || selection == 2){ //no || cancel
            return;
        }
        else{ //yes
            String spName = String.valueOf(appointments.getValueAt(rowIndex, 5));
            String email = getSPEmail(spName);
            int apptId = db.getApptId(String.valueOf(appointments.getValueAt(rowIndex, 1)),
                    String.valueOf(appointments.getValueAt(rowIndex, 2)), email);

            db.cancelAppointment(apptId);
            JOptionPane.showMessageDialog(null,
                    "Successfully canceled.");
            updateAppts();
        }
    }

    private String getSPEmail(String name){
        try{
            String[] names = name.split(" ", 2);
            ResultSet rs = db.getSPEmail(names[0], names[1]);
            if(rs.next()){
                return rs.getString("Email");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());

        }
        return null;
    }

    public void goSPActionPerformed(ActionEvent e){
        RowFilter filter = new RowFilter() {
            String searchText = spSearchText.getText();
            public boolean include(Entry entry) {
                boolean result = entry.getStringValue(0).indexOf(searchText) >=0 || entry.getStringValue(1).indexOf(searchText) >=0
                                || entry.getStringValue(2).indexOf(searchText) >=0 || entry.getStringValue(3).indexOf(searchText) >=0
                                || entry.getStringValue(4).indexOf(searchText) >=0 || entry.getStringValue(5).indexOf(searchText) >=0
                                || entry.getStringValue(6).indexOf(searchText) >=0;
                return result;
            }
        };
        spSorter.setRowFilter(filter);
    }

    public void goUserActionPerformed(ActionEvent e){
        RowFilter filter = new RowFilter() {
            String searchText = userSearchText.getText();
            public boolean include(Entry entry) {
                boolean result = entry.getStringValue(0).indexOf(searchText) >=0 || entry.getStringValue(1).indexOf(searchText) >=0
                        || entry.getStringValue(2).indexOf(searchText) >=0 || entry.getStringValue(3).indexOf(searchText) >=0;
                return result;
            }
        };
        userSorter.setRowFilter(filter);
    }

    public void goApptActionPerformed(ActionEvent e){
        RowFilter filter = new RowFilter() {
            String searchText = apptSearchText.getText();
            public boolean include(Entry entry) {
                boolean result = entry.getStringValue(0).indexOf(searchText) >=0 || entry.getStringValue(1).indexOf(searchText) >=0
                        || entry.getStringValue(2).indexOf(searchText) >=0 || entry.getStringValue(3).indexOf(searchText) >=0
                        || entry.getStringValue(4).indexOf(searchText) >=0 || entry.getStringValue(5).indexOf(searchText) >=0
                        || entry.getStringValue(6).indexOf(searchText) >=0;
                return result;
            }
        };
        apptSorter.setRowFilter(filter);
    }

    public void deleteUserActionPerformed(ActionEvent e) {
        int rowIndex = users.getSelectedRow();
        if (rowIndex == -1) {
            JOptionPane.showMessageDialog(null, "Please select a user.");
            return;
        }

        int selection = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete "
                        + users.getValueAt(rowIndex, 0) + " "
                        + users.getValueAt(rowIndex, 1) + "?");
        if (selection == 1 || selection == 2) {
            return;
        }
        else {
            db.deleteUser(String.valueOf(users.getValueAt(rowIndex, 2)));
            JOptionPane.showMessageDialog(null, "Successfully deleted.");
            //update table
            updateUsers();

            //cancel appointments
            db.cancelAppointment(String.valueOf(users.getValueAt(rowIndex, 2)));
        }
    }
    public void setHomeVisible(){
        f.setVisible(true);
    }
}
