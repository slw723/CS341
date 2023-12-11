import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import javax.management.Descriptor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;

public class AdminReportsPage {
    JFrame f, userReportFrame, apptReportFrame;
    JPanel p;
    JLabel reportsLabel, userDateRange, userStartDate,
            userEndDate, apptMonthLabel, chooseLabel,
            apptCategoryLabel, apptMonthHeader, apptCategoryHeader,
            totalLabel, canceledLabel, bookedLabel, hLine, hLineAppt, userMonthHeader,
            totalUserLabel, activeLabel, inactiveLabel, usersWithApptsLabel;
    JButton logout, userGenButton, userManual, apptGenButton;
    JMenuBar mb;
    JPanel userReportFieldsPanel, apptReportFieldsPanel, apptReportHeader, apptReportStats, apptTablePanel
            , userReportHeader, userReportStats, userTablePanel;
    JScrollPane userScrollPane, apptScrollPane;
    JMenuItem menu, home, reports;
    AdminHomePage ahp;
    Database db;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    JTabbedPane tabbedPane;
    JScrollBar scroll, scroll3, scroll4, scroll6, scroll7, scroll8;
    JComboBox<String> userMonth1CB, userMonth2CB, apptMonthCB, categoryCB;
    JComboBox<Integer> userYear1CB, userYear2CB;
    JTable apptReportData, userReportData;
    String [] months, apptCategories;
    Integer [] years;
    int numActiveUsers,numInactiveUsers, totalUsers, numUsersWithAppts;

    public AdminReportsPage(Database db, AdminHomePage ahp){
        this.ahp = ahp;
        this.db = db;

        /*Make frame*/
        f = new JFrame("Reports");
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

        home.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                goHomeActionPerformed(evt);
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
                }
                catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        f.setJMenuBar(mb);
        //Reports label
        p = new JPanel();
        f.getContentPane();
        String str = "Report Viewer";
        reportsLabel = new JLabel(str);
        reportsLabel.setFont(new Font("Sarif", Font.PLAIN, 20));
        reportsLabel.setForeground(new Color(31, 36, 33));
        Dimension wcSize = reportsLabel.getPreferredSize();
        reportsLabel.setBounds((screenSize.width/2)-100, 30, wcSize.width+10, wcSize.height);
        f.add(reportsLabel);

        /*Create JTabbedPane and its tabs (panels)*/
        userReportFieldsPanel = new JPanel();
        userReportFieldsPanel.setLayout(null);
        showUserReportFields();

        apptReportFieldsPanel = new JPanel();
        apptReportFieldsPanel.setLayout(null);
        showApptReportFields();

        /*tabbedPane Setup*/
        UIManager.put("TabbedPane.selected", new Color(31, 36, 33));
        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(350,100, 800, 450);
        tabbedPane.add("User Report", userReportFieldsPanel);
        tabbedPane.add("Appointment Report", apptReportFieldsPanel);
        tabbedPane.setBackgroundAt(0, new Color(33, 104, 105));
        tabbedPane.setForegroundAt(0, Color.WHITE);
        tabbedPane.setBackgroundAt(1, new Color(33, 104, 105));
        tabbedPane.setForegroundAt(1, Color.WHITE);
        f.add(tabbedPane);

        /* Make visible. */
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(screenSize.width, screenSize.height);
        f.setVisible(true);
    }

    private void genUserReportActionPerformed(ActionEvent e){
        if (userMonth1CB.getSelectedItem().equals("") || userMonth2CB.getSelectedItem().equals("")
        ||userYear1CB.getSelectedItem()==null || userYear2CB.getSelectedItem()==null){
            JOptionPane.showMessageDialog(null, "Month/Day/Year fields cannot be blank.");
        }

        else {
            //create a new frame
            userReportFrame = new JFrame();
            userReportFrame.setLayout(null);

            /*Generate Report Data*/
            try {
                //get button data
                String[] months = {"", "January", "February", "March", "April", "May", "June", "July",
                        "August", "September", "October", "November", "December"};

                String[] splits = userMonth1CB.getSelectedItem().toString().split(" - ", 2);
                int startMonthIndex = Integer.valueOf(splits[0]); //9
                String displayStartMonth = months[startMonthIndex]; //used for report header
                String sMonthFormat; //for use in the SQL
                if (startMonthIndex < 10) {sMonthFormat = "0" + startMonthIndex;}
                else{sMonthFormat = startMonthIndex + "";}

                String[] splits2 = userMonth2CB.getSelectedItem().toString().split(" - ", 2);
                int endMonthIndex = Integer.valueOf(splits2[0]); //12
                String displayEndMonth = months[endMonthIndex]; //used for report header
                String eMonthFormat; //for use in the SQL
                if (endMonthIndex < 10) {eMonthFormat = "0" + endMonthIndex;}
                else{eMonthFormat = endMonthIndex + "";}

                System.out.println(eMonthFormat);

                generateUserReportHeader(displayStartMonth, displayEndMonth, sMonthFormat, eMonthFormat);
                //generate table data
                generateUserTable(sMonthFormat, eMonthFormat);

            } catch (Exception e1) {
                System.out.println(e1.getMessage());
            }

            /*Breakdown of users with appointments??*/
            userReportFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            userReportFrame.setSize(900, 900);
            userReportFrame.setVisible(true);
        }
    }

    public void generateUserReportHeader(String startMonth, String endMonth, String sMonthFormat, String eMonthFormat){
        userReportHeader = new JPanel();
        userReportHeader.setBounds(50, 50, 500, 50);
        userReportHeader.setLayout(new BoxLayout(userReportHeader, BoxLayout.PAGE_AXIS));

        String monthLabel = "Report for months: " + startMonth + "-" + endMonth;
        userMonthHeader = new JLabel(monthLabel);
        userMonthHeader.setFont(new Font("Sarif", Font.BOLD, 15));

        hLineAppt = new JLabel("____________________________________________________");
        hLineAppt.setFont(new Font("Sarif", Font.BOLD, 15));

        userReportHeader.add(userMonthHeader);
        userReportHeader.add(hLineAppt);

        //Add general stats
        numActiveUsers = countActiveUsers(); //count active users
        numInactiveUsers = countInactiveUsers(); //count inactive users
        totalUsers = numActiveUsers + numInactiveUsers; //total users
        numUsersWithAppts = countUsersWithAppts(sMonthFormat,eMonthFormat); //count users with appointments

        userReportStats = new JPanel();
        userReportStats.setBounds(50, 130, 500, 70);
        userReportStats.setLayout(new BoxLayout(userReportStats, BoxLayout.PAGE_AXIS));
        String total = "Total Users in System: " + totalUsers;
        totalUserLabel = new JLabel(total);
        totalUserLabel.setFont(new Font("Sarif", Font.PLAIN, 13));
        String active = "Total Active Users: " + numActiveUsers;
        activeLabel = new JLabel(active);
        activeLabel.setFont(new Font("Sarif", Font.PLAIN, 13));
        String inactive = "Total Inactive Users: " + numInactiveUsers;
        inactiveLabel = new JLabel(inactive);
        inactiveLabel.setFont(new Font("Sarif", Font.PLAIN, 13));
        String withAppts = "Number of Users with Appointments: " + numUsersWithAppts;
        usersWithApptsLabel = new JLabel(withAppts);
        usersWithApptsLabel.setFont(new Font("Sarif", Font.PLAIN, 13));

        userReportStats.add(totalUserLabel);
        userReportStats.add(activeLabel);
        userReportStats.add(inactiveLabel);
        userReportStats.add(usersWithApptsLabel);

        userReportFrame.add(userReportHeader);
        userReportFrame.add(userReportStats);

    }

    public void generateUserTable(String sMonthFormat, String eMonthFormat){
        userTablePanel = new JPanel();
        userTablePanel.setLayout(null);
        userTablePanel.setBounds(50, 130, 800, 300);
        //SQL to get the table data
        String sql = "SELECT User.FirstName, User.LastName, User.Email, User.PhoneNum, User.Active, " +
                "appointment.Date AS ApptDate, appointment.Type AS ApptType, appointment.Canceled, " +
                "appointment.Description, appointment.SPEmail " +
                "FROM user JOIN appointment ON user.Email = appointment.UserEmail " +
                "WHERE appointment.Date between '2023-" + sMonthFormat + "-01' and '2023-" + eMonthFormat + "-31' " +
                "ORDER BY User.email, appointment.Type;";

         System.out.println("Generated sql: " + sql);

        //now execute sql and generate the table
        userReportData = new JTable();
        String [] apptHeaders = {"First Name", "Last Name", "Email",
                "Phone Number", "Active", "Appt Date", "Appt Type", "Canceled", "Description", "SP Email"};
        userReportData.setModel(new DefaultTableModel(apptHeaders, 0));
        userReportData.getTableHeader().setBackground(new Color(33, 104, 105));
        userReportData.getTableHeader().setForeground(Color.WHITE);

        try{
            ResultSet rs = db.executeSQL(sql);
            DefaultTableModel tblModel = (DefaultTableModel)userReportData.getModel();
            while(rs.next()){
                //data will be added until finished
                String first = rs.getString("FirstName");
                String last = rs.getString("LastName");
                String email = rs.getString("Email");
                String phone = rs.getLong("PhoneNum") + "";
                int isActive = rs.getInt("Active");
                String active;
                if(isActive == 1){
                    active = "Yes";
                }
                else{
                    active = "No";
                }
                String date = String.valueOf(rs.getDate("ApptDate"));
                String type = rs.getString("ApptType");
                int isCanceled = rs.getInt("Canceled");
                String canceled;

                if(isCanceled == 1){
                    canceled = "Yes";
                }
                else{
                    canceled = "No";
                }
                String desc = rs.getString("Description");
                String spEmail = rs.getString("SPEmail");

                String tbData[] = {first, last, email, phone, active, date, type, canceled, desc, spEmail};

                //add string array into jtable
                tblModel.addRow(tbData);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }

        userScrollPane = new JScrollPane(userReportData);
        userScrollPane.setBounds(00, 120, 900, 300);
        userScrollPane.validate();
        userReportData.validate();
        userReportData.getColumnModel().getColumn(0).setMaxWidth(80);
        userReportData.getColumnModel().getColumn(1).setMaxWidth(80);
        userReportData.getColumnModel().getColumn(2).setMaxWidth(85);
        userReportData.getColumnModel().getColumn(3).setMaxWidth(95);
        userReportData.getColumnModel().getColumn(4).setMaxWidth(50);
        userReportData.getColumnModel().getColumn(5).setMaxWidth(75);
        userReportData.getColumnModel().getColumn(6).setMaxWidth(75);
        userReportData.getColumnModel().getColumn(7).setMaxWidth(70);
        userReportData.getColumnModel().getColumn(8).setMaxWidth(100);
        userReportData.getColumnModel().getColumn(9).setMaxWidth(85);
        userTablePanel.add(userScrollPane);

        userReportData.validate();
        userReportFrame.add(userTablePanel);
    }


    public int countInactiveUsers(){
        try {
            int numInactive;
            String sql2 = "SELECT count(Email) FROM user WHERE Active = \"0\";";
            ResultSet rs2 = db.executeSQL(sql2);      // get the results of all inactive users
            rs2.next();
            numInactive = rs2.getInt("count(Email)");

            return numInactive;
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public int countUsersWithAppts(String sMonthFormat, String eMonthFormat){
        try {
            int numUsers;
            String sql = "SELECT COUNT(DISTINCT user.EMAIL) " +
                    "FROM user JOIN appointment ON user.Email = appointment.UserEmail " +
                    "WHERE appointment.Date between '2023-" + sMonthFormat + "-01' and '2023-" + eMonthFormat +"-31';";
            ResultSet rs = db.executeSQL(sql);      // get the results of all inactive users
            rs.next();
            numUsers = rs.getInt("COUNT(DISTINCT user.EMAIL)");
            return numUsers;
        }catch (Exception e1){
            System.out.println(e1.getMessage());
            System.out.println(e1.getStackTrace());
        }
        return 0;
    }
    public int countActiveUsers(){
        try {
            int numActive;
            String sql = "SELECT count(Email) FROM user WHERE Active = \"1\";";
            ResultSet rs = db.executeSQL(sql);      // get the results of all inactive users
            rs.next();
            numActive = rs.getInt("count(Email)");
            return numActive;
        }catch (Exception e1){
            System.out.println(e1.getMessage());
        }

        return 0;
    }

    public void genApptReportActionPerformed(ActionEvent evt){
        if (apptMonthCB.getSelectedItem().equals("") || categoryCB.getSelectedItem().equals("")){
            JOptionPane.showMessageDialog(null, "Month and Appointment Category cannot be blank.");
        }

        else {
            apptReportFrame = new JFrame();
            apptReportFrame.setLayout(null);

            /*Generate Report Data*/
            try {
                String[] months = {"", "January", "February", "March", "April", "May", "June", "July",
                        "August", "September", "October", "November", "December"};

                String[] splits = apptMonthCB.getSelectedItem().toString().split(" - ", 2);
                int month = Integer.valueOf(splits[0]);
                String displayMonth = months[month]; //used for report header
                //format month so it works with SQL query (2 digits)
                String monthNumString;
                if (month < 10) {monthNumString = "0" + month;}
                else{monthNumString = month + "";}
                String category = categoryCB.getSelectedItem().toString(); //get category

                int totalAppts = getTotalApptCount(monthNumString, category);
                System.out.println("Total appointments: " + totalAppts);
                int totalCanceled = getCanceledByMonthCategory(monthNumString, category);
                int totalBooked = getBookedByMonthCategory(monthNumString, category);
                //generate header and general stats
                generateApptReportHeader(displayMonth, category, totalAppts, totalCanceled, totalBooked);
                //generate table data
                generateApptTable(monthNumString,displayMonth, category);
            } catch (Exception e1) {
                System.out.println("Error: " + e1.getMessage());
                System.out.println(e1.getStackTrace());
            }

            apptReportFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            apptReportFrame.setSize(900, 900);
            apptReportFrame.setVisible(true);
        }
    }

    private void generateApptReportHeader(String displayMonth, String category, int totalAppts, int totalCanceled, int totalBooked){
        apptReportHeader = new JPanel();
        apptReportHeader.setBounds(50, 50, 500, 60);
        apptReportHeader.setLayout(new BoxLayout(apptReportHeader, BoxLayout.PAGE_AXIS));

        String monthLabel = "Report for Month: " + displayMonth;
        String categoryLabel = "Appointment Category: " + category;
        apptMonthHeader = new JLabel(monthLabel);
        apptMonthHeader.setFont(new Font("Sarif", Font.BOLD, 15));
        apptCategoryHeader = new JLabel(categoryLabel);
        apptCategoryHeader.setFont(new Font("Sarif", Font.BOLD, 15));

        hLine = new JLabel("____________________________________________________");
        hLine.setFont(new Font("Sarif", Font.BOLD, 15));

        apptReportHeader.add(apptMonthHeader);
        apptReportHeader.add(apptCategoryHeader);
        apptReportHeader.add(hLine);

        //Add general stats
        apptReportStats = new JPanel();
        apptReportStats.setBounds(50, 130, 500, 60);
        apptReportStats.setLayout(new BoxLayout(apptReportStats, BoxLayout.PAGE_AXIS));
        String total = "Total Created Appointments: " + totalAppts;
        totalLabel = new JLabel(total);
        totalLabel.setFont(new Font("Sarif", Font.PLAIN, 13));
        String canceled = "Total Canceled Appointments: " + totalCanceled;
        canceledLabel = new JLabel(canceled);
        canceledLabel.setFont(new Font("Sarif", Font.PLAIN, 13));
        String booked = "Total Booked Appointments: " + totalBooked;
        bookedLabel = new JLabel(booked);
        bookedLabel.setFont(new Font("Sarif", Font.PLAIN, 13));
        apptReportStats.add(totalLabel);
        apptReportStats.add(canceledLabel);
        apptReportStats.add(bookedLabel);

        apptReportFrame.add(apptReportHeader);
        apptReportFrame.add(apptReportStats);

    }

    private void generateApptTable(String monthNumString, String displayMonth, String category){
        apptTablePanel = new JPanel();
        apptTablePanel.setLayout(null);
        apptTablePanel.setBounds(50, 130, 800, 200);
        //SQL to get the table data
        String sql = "select appointment.Date, appointment.Description, appointment.Time, appointment.Type, appointment.Booked, " +
                "appointment.Canceled, serviceprovider.FirstName as SPFirst, serviceprovider.LastName as SPLast, " +
                "user.FirstName as UserFirst, user.LastName as UserLast from appointment " +
                "inner join user on appointment.UserEmail=user.Email " +
                "inner join serviceprovider on appointment.SPEmail = serviceprovider.Email " +
                "where appointment.Date like \"%-" + monthNumString + "-%\" " +
                "and appointment.type like \"" + category + "\" order by Date;";

        //now execute sql and generate the table
        apptReportData = new JTable();
        String [] apptHeaders = {"Date", "Description", "Time", "Type", "Booked", "Canceled", "Service Provider", "User"};
        apptReportData.setModel(new DefaultTableModel(apptHeaders, 0));
        apptReportData.getTableHeader().setBackground(new Color(33, 104, 105));
        apptReportData.getTableHeader().setForeground(Color.WHITE);

        try{
            ResultSet rs = db.executeSQL(sql);
            DefaultTableModel tblModel = (DefaultTableModel)apptReportData.getModel();
            while(rs.next()){
                //data will be added until finished
                String date = String.valueOf(rs.getDate("Date"));
                String desc = rs.getString("Description");
                String time = String.valueOf(rs.getTime("Time"));
                String type = rs.getString("Type");
                int book = rs.getInt("Booked");
                int isCanceled = rs.getInt("Canceled");
                String booked, canceled;
                if(book == 1){
                    booked = "Yes";
                }
                else{
                    booked = "No";
                }

                if(isCanceled == 1){
                    canceled = "Yes";
                }
                else{
                    canceled = "No";
                }

                String spFirst = rs.getString("SPFirst");
                String spLast = rs.getString("SPLast");
                String spFull = spFirst + " " + spLast; //concatenated sp names
                String uFirst = rs.getString("UserFirst");
                String uLast = rs.getString("UserLast");
                String uFull = uFirst + " " + uLast;  //concatenated user names

                String tbData[] = {date, desc, time, type, booked, canceled, spFull, uFull};

                //add string array into jtable
                tblModel.addRow(tbData);
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
        }

        apptScrollPane = new JScrollPane(apptReportData);
        apptScrollPane.setBounds(00, 100, 900, 200);
        apptScrollPane.validate();
        apptReportData.validate();
        apptReportData.getColumnModel().getColumn(0).setMaxWidth(85);
        apptReportData.getColumnModel().getColumn(1).setMaxWidth(105);
        apptReportData.getColumnModel().getColumn(2).setMaxWidth(70);
        apptReportData.getColumnModel().getColumn(3).setMaxWidth(65);
        apptReportData.getColumnModel().getColumn(4).setMaxWidth(85);
        apptReportData.getColumnModel().getColumn(5).setMaxWidth(85);
        apptReportData.getColumnModel().getColumn(6).setMaxWidth(150);
        apptReportData.getColumnModel().getColumn(7).setMaxWidth(150);
        apptTablePanel.add(apptScrollPane);
        apptTablePanel.validate();

        apptReportFrame.add(apptTablePanel);
    }

    private int getTotalApptCount(String monthNumString, String category){
        //SQL to find the total number appointments for that month and category
        String apptCountSql = "select count(ApptId) " +
                "from appointment inner join user on appointment.UserEmail=user.Email " +
                "inner join serviceprovider on appointment.SPEmail = serviceprovider.Email " +
                "where appointment.Date like \"%-" + monthNumString + "-%\" " +
                "and appointment.type like \"" + category + "\" order by Date;";

        int totalAppts;
        try {
            ResultSet totalCountRS = db.executeSQL(apptCountSql);
            totalCountRS.next();
            totalAppts = totalCountRS.getInt("count(ApptId)");
            return totalAppts;
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    private int getCanceledByMonthCategory(String monthNumString, String category){
        //SQL to find only the number cancelled appointments for that month and category
        String apptCountSql = "select count(ApptId) " +
                "from appointment inner join user on appointment.UserEmail=user.Email " +
                "inner join serviceprovider on appointment.SPEmail = serviceprovider.Email " +
                "where appointment.Date like \"%-" + monthNumString + "-%\" " +
                "and appointment.type like \"" + category + "\" and appointment.Canceled like \"1\" " +
                "order by Date;";

        int totalCanceled;
        try {
            ResultSet totalCanceledRS = db.executeSQL(apptCountSql);
            totalCanceledRS.next();
            totalCanceled = totalCanceledRS.getInt("count(ApptId)");
            return totalCanceled;
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    private int getBookedByMonthCategory(String monthNumString, String category){
        //SQL to find only the number booked appointments for that month and category
        String apptCountSql = "select count(ApptId) " +
                "from appointment inner join user on appointment.UserEmail=user.Email " +
                "inner join serviceprovider on appointment.SPEmail = serviceprovider.Email " +
                "where appointment.Date like \"%-" + monthNumString + "-%\" " +
                "and appointment.type like \"" + category + "\" and appointment.Booked like \"1\" " +
                "order by Date;";

        int totalBooked;
        try {
            ResultSet totalBookedRS = db.executeSQL(apptCountSql);
            totalBookedRS.next();
            totalBooked = totalBookedRS.getInt("count(ApptId)");
            return totalBooked;
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    private void goHomeActionPerformed(ActionEvent e){
        f.setVisible(false);
        ahp.setHomeVisible();
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

    public void showUserReportFields(){
        // add text field for User Report Start Date
        userDateRange = new JLabel("Choose a Month Range (Inclusive)");
        userDateRange.setFont(new Font("Sarif", Font.BOLD, 14));
        userDateRange.setForeground(new Color(31, 36, 33));
        userDateRange.setBounds(250, 20, 300, 20);
        userReportFieldsPanel.add(userDateRange);

        // add text field for User Report Start Date
        userStartDate = new JLabel("Start Month (MM-YYYY): ");
        userStartDate.setFont(new Font("Sarif", Font.BOLD, 12));
        userStartDate.setForeground(new Color(33, 104, 105));
        userStartDate.setBounds(78, 150, 200, 20);
        userReportFieldsPanel.add(userStartDate);

        /*Date range*/
        // set up months drop down and add
        months = new String[]{"", "01 - Jan", "02 - Feb", "03 - Mar", "04 - Apr",
                "05 - May", "06 - Jun", "07 - Jul", "08 - Aug",
                "09 - Sep", "10 - Oct", "11 - Nov", "12 - Dec"};
        userMonth1CB = new JComboBox<>(months);
        userMonth1CB.setFont(new Font("Sarif", Font.BOLD, 11));
        userMonth1CB.setBounds(220, 150, 70, 20);
        userMonth1CB.setSelectedIndex(0);
        scroll = new JScrollBar();
        userMonth1CB.add(scroll);
        userReportFieldsPanel.add(userMonth1CB);

        // set up years drop down and add
        years = new Integer[3];
        years[0] = null;
        years[1] = 2023;
        userYear1CB = new JComboBox<>(years);
        userYear1CB.setFont(new Font("Sarif", Font.PLAIN, 11));
        userYear1CB.setBounds(300, 150, 60, 20);
        userYear1CB.setSelectedIndex(0);
        scroll3 = new JScrollBar();
        userYear1CB.add(scroll3);
        userReportFieldsPanel.add(userYear1CB);

        //Second date range
        // add text field for User Report End Date
        userEndDate = new JLabel("End Month (MM-YYYY): ");
        userEndDate.setFont(new Font("Sarif", Font.BOLD, 12));
        userEndDate.setForeground(new Color(33, 104, 105));
        userEndDate.setBounds(80, 230, 200, 20);
        userReportFieldsPanel.add(userEndDate);

        userMonth2CB = new JComboBox<>(months);
        userMonth2CB.setFont(new Font("Sarif", Font.PLAIN, 11));
        userMonth2CB.setBounds(220, 230, 70, 20);
        userMonth2CB.setSelectedIndex(0);
        scroll4 = new JScrollBar();
        userMonth2CB.add(scroll4);
        userReportFieldsPanel.add(userMonth2CB);

        // set up years drop down and add
        userYear2CB = new JComboBox<>(years);
        userYear2CB.setFont(new Font("Sarif", Font.PLAIN, 11));
        userYear2CB.setBounds(300, 230, 60, 20);
        userYear2CB.setSelectedIndex(0);
        scroll6 = new JScrollBar();
        userYear2CB.add(scroll6);
        userReportFieldsPanel.add(userYear2CB);

        userGenButton = new JButton("Generate Report");
        userGenButton.setBounds(500, 190, 170, 25);
        userGenButton.setBackground(new Color(73, 160, 120));
        userGenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                genUserReportActionPerformed(evt);
            }
        });

        userReportFieldsPanel.add(userGenButton);
    }

    public void showApptReportFields(){
        // add text field for User Report Start Date
        chooseLabel = new JLabel("Choose a Month and a Category");
        chooseLabel.setFont(new Font("Sarif", Font.BOLD, 14));
        chooseLabel.setForeground(new Color(31, 36, 33));
        chooseLabel.setBounds(280, 20, 300, 20);
        apptReportFieldsPanel.add(chooseLabel);

        /*Date range*/
        // set up months drop down and add
        apptMonthLabel = new JLabel("Month: ");
        apptMonthLabel.setFont(new Font("Sarif", Font.BOLD, 12));
        apptMonthLabel.setForeground(new Color(33, 104, 105));
        apptMonthLabel.setBounds(200, 125, 200, 20);
        apptReportFieldsPanel.add(apptMonthLabel);
        months = new String[]{"", "01 - Jan", "02 - Feb", "03 - Mar", "04 - Apr",
                "05 - May", "06 - Jun", "07 - Jul", "08 - Aug",
                "09 - Sep", "10 - Oct", "11 - Nov", "12 - Dec"};
        apptMonthCB = new JComboBox<>(months);
        apptMonthCB.setFont(new Font("Sarif", Font.BOLD, 11));
        apptMonthCB.setBounds(250, 125, 70, 20);
        apptMonthCB.setSelectedIndex(0);
        scroll7 = new JScrollBar();
        apptMonthCB.add(scroll7);
        apptReportFieldsPanel.add(apptMonthCB);

        //Set up category combo box
        apptCategoryLabel = new JLabel("Appointment Category: ");
        apptCategoryLabel.setBounds(400, 125, 200, 20);
        apptCategoryLabel.setForeground(new Color(33, 104, 105));
        apptReportFieldsPanel.add(apptCategoryLabel);

        apptCategories = new String[]{"", "Fitness", "Beauty", "Health" };
        categoryCB = new JComboBox<>(apptCategories);
        categoryCB.setFont(new Font("Sarif", Font.BOLD, 11));
        categoryCB.setBounds(550, 125, 70, 20);
        categoryCB.setSelectedIndex(0);
        scroll8 = new JScrollBar();
        categoryCB.add(scroll8);
        apptReportFieldsPanel.add(categoryCB);

        apptGenButton = new JButton("Generate Report");
        apptGenButton.setBounds(325, 260, 170, 25);
        apptGenButton.setBackground(new Color(73, 160, 120));
        apptGenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                genApptReportActionPerformed(evt);
            }
        });
        apptReportFieldsPanel.add(apptGenButton);
    }
}
