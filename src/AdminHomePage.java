import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

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
public class AdminHomePage {
    JFrame f;
    JButton logout;
    JMenuBar mb;
    JMenuItem menu, home, trends;
    JPanel p, userPanel, spPanel, apptPanel;
    JScrollPane scroll, scroll2;

    JLabel welcome, usersLabel, noUsers, spsLabel, noSPs;
    JTable users, serviceProviders;
    Admin admin;

    JTabbedPane tabbedPane;
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

        mb.add(menu);
        mb.setBackground(new Color(73, 160, 120));
        mb.add(Box.createHorizontalGlue());
        mb.add(logout);

        home = new JMenuItem("Home");
        home.setFont(new Font("Sarif", Font.PLAIN, 15));
        trends = new JMenuItem("History");
        trends.setFont(new Font("Sarif", Font.PLAIN, 15));

        menu.add(home);
        menu.add(trends);

        home.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                goHomeActionPerformed(evt);
            }
        });

        trends.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                trendsActionPerformed(evt);
            }
        });
        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                logoutActionPerformed(evt);
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
        spPanel = new JPanel();
        apptPanel = new JPanel();
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

        userPanel.setLayout(null);
        f.add(tabbedPane);

        // add current users title
        usersLabel = new JLabel("Current system Users");
        usersLabel.setFont(new Font("Sarif", Font.PLAIN, 15));
        usersLabel.setForeground(new Color(33, 104, 105));
        Dimension upSize = usersLabel.getPreferredSize();
        usersLabel.setBounds(10, 60, upSize.width+10, upSize.height);
       // p.add(usersLabel);

        // add current users title
        spsLabel = new JLabel("Current system Service Providers");
        spsLabel.setFont(new Font("Sarif", Font.PLAIN, 15));
        spsLabel.setForeground(new Color(33, 104, 105));
        Dimension upSize1 = spsLabel.getPreferredSize();
        spsLabel.setBounds(10, 350, upSize1.width+10, upSize.height);
      //  p.add(spsLabel);
        // show the database's current Users and Service Providers
        users = new JTable();
        populateUsers();
        serviceProviders = new JTable();
       // populateSPs();


        p.setLayout(null);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //f.add(p);
        /* Make visible */
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(1000,800);
        f.setVisible(true);
    }

    private void populateUsers(){
        String [] userHeaders = {"First Name", "Last Name", "Email", "Phone Number"};
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

                String tbData[] = {fName, lName, email, phoneNum};

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
        userPanel.add(scroll);
        userPanel.validate();

    }

    private void populateSPs(){
        String [] spHeaders = {"First Name", "Last Name", "Type", "Qualification", "Year Graduated", "Email", "Phone Number"};
        serviceProviders.setModel(new DefaultTableModel(spHeaders, 0));
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
        scroll2.setBounds(10, 370, 950, 250);
        scroll2.validate();
        serviceProviders.validate();
        serviceProviders.getColumnModel().getColumn(0).setMinWidth(75);
        serviceProviders.getColumnModel().getColumn(1).setMinWidth(75);
        serviceProviders.getColumnModel().getColumn(2).setMinWidth(50);
        serviceProviders.getColumnModel().getColumn(3).setMinWidth(250);
        serviceProviders.getColumnModel().getColumn(4).setMinWidth(50);
        serviceProviders.getColumnModel().getColumn(5).setMinWidth(100);
        serviceProviders.getColumnModel().getColumn(5).setMinWidth(75);
        f.add(scroll2);
        f.validate();
    }

    private void logoutActionPerformed(ActionEvent e) {
        f.setVisible(false);
        new LogInPage(db);
    }

    private void goHomeActionPerformed(ActionEvent e){

    }
    private void trendsActionPerformed(ActionEvent e){

    }
}
