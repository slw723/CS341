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

public class AdminReportsPage {
    JFrame f, userReportFrame;
    JPanel p;
    JLabel reportsLabel, userDateRange, userStartDate, userEndDate, apptMonthLabel, chooseLabel;
    JButton logout, userGenButton, userManual, apptGenButton;
    JMenuBar mb;
    JPanel userReportFieldsPanel, apptReportFieldsPanel;
    JScrollPane userGenReportPanel;
    JMenuItem menu, home, reports;
    AdminHomePage ahp;
    Database db;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    JTabbedPane tabbedPane;
    JScrollBar scroll, scroll2, scroll3, scroll4, scroll5, scroll6, scroll7;
    JComboBox<String> userMonth1CB, userMonth2CB, apptMonthCB;
    JComboBox<Integer> userDay1CB, userDay2CB, userYear1CB, userYear2CB, apptDay1CB, apptDay2CB, apptYear1CB, apptYear2CB;
    String [] months;
    Integer [] days, years;

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
        userGenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                genUserReportActionPerformed(evt);
            }
        });


        /* Make visible. */
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(screenSize.width, screenSize.height);
        f.setVisible(true);
    }

    private void genUserReportActionPerformed(ActionEvent e){
        //create a new frame
        userReportFrame= new JFrame();
        userReportFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        userReportFrame.setSize(screenSize.width, screenSize.height);




        userReportFrame.setVisible(true);

    }

    private void genApptReportActionPerformed(ActionEvent e){

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
        URL manualURL = new URL("file:///C:/Users/slw72/OneDrive/Documents/CS%20341/TestManual.pdf");
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
        userDateRange = new JLabel("Choose a Date Range (Inclusive)");
        userDateRange.setFont(new Font("Sarif", Font.BOLD, 14));
        userDateRange.setForeground(new Color(31, 36, 33));
        userDateRange.setBounds(250, 20, 300, 20);
        userReportFieldsPanel.add(userDateRange);

        // add text field for User Report Start Date
        userStartDate = new JLabel("Start Date (MM-DD-YYY): ");
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

        // set up days drop down and add
        days = new Integer[32];
        days[0] = null;
        for(Integer i = 1; i < 32; i++){
            days[i] = i;
        }
        userDay1CB = new JComboBox<>(days);
        userDay1CB.setFont(new Font("Sarif", Font.PLAIN, 11));
        userDay1CB.setBounds(300, 150, 40, 20);
        userDay1CB.setSelectedIndex(0);
        scroll2 = new JScrollBar();
        userDay1CB.add(scroll2);
        userReportFieldsPanel.add(userDay1CB);

        // set up years drop down and add
        years = new Integer[3];
        years[0] = null;
        years[1] = 2023;
        userYear1CB = new JComboBox<>(years);
        userYear1CB.setFont(new Font("Sarif", Font.PLAIN, 11));
        userYear1CB.setBounds(350, 150, 60, 20);
        userYear1CB.setSelectedIndex(0);
        scroll3 = new JScrollBar();
        userYear1CB.add(scroll3);
        userReportFieldsPanel.add(userYear1CB);

        //Second date range
        // add text field for User Report End Date
        userEndDate = new JLabel("End Date (MM-DD-YYY): ");
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

        // set up days drop down and add
        userDay2CB = new JComboBox<>(days);
        userDay2CB.setFont(new Font("Sarif", Font.PLAIN, 11));
        userDay2CB.setBounds(300, 230, 40, 20);
        userDay2CB.setSelectedIndex(0);
        scroll5 = new JScrollBar();
        userDay2CB.add(scroll5);
        userReportFieldsPanel.add(userDay2CB);

        // set up years drop down and add
        userYear2CB = new JComboBox<>(years);
        userYear2CB.setFont(new Font("Sarif", Font.PLAIN, 11));
        userYear2CB.setBounds(350, 230, 60, 20);
        userYear2CB.setSelectedIndex(0);
        scroll6 = new JScrollBar();
        userYear2CB.add(scroll6);
        userReportFieldsPanel.add(userYear2CB);

        userGenButton = new JButton("Generate Report");
        userGenButton.setBounds(500, 190, 130, 25);
        userGenButton.setBackground(new Color(73, 160, 120));

        userReportFieldsPanel.add(userGenButton);
    }

    public void showApptReportFields(){
        // add text field for User Report Start Date
        chooseLabel = new JLabel("Choose a Month and a Category");
        chooseLabel.setFont(new Font("Sarif", Font.BOLD, 14));
        chooseLabel.setForeground(new Color(31, 36, 33));
        chooseLabel.setBounds(200, 20, 300, 20);
        apptReportFieldsPanel.add(chooseLabel);

        /*Date range*/
        // set up months drop down and add
        apptMonthLabel = new JLabel("Month: ");
        apptMonthLabel.setFont(new Font("Sarif", Font.BOLD, 12));
        apptMonthLabel.setForeground(new Color(33, 104, 105));
        apptMonthLabel.setBounds(78, 150, 200, 20);
        apptReportFieldsPanel.add(apptMonthLabel);
        months = new String[]{"", "01 - Jan", "02 - Feb", "03 - Mar", "04 - Apr",
                "05 - May", "06 - Jun", "07 - Jul", "08 - Aug",
                "09 - Sep", "10 - Oct", "11 - Nov", "12 - Dec"};
        apptMonthCB = new JComboBox<>(months);
        apptMonthCB.setFont(new Font("Sarif", Font.BOLD, 11));
        apptMonthCB.setBounds(220, 150, 70, 20);
        apptMonthCB.setSelectedIndex(0);
        scroll7 = new JScrollBar();
        apptMonthCB.add(scroll7);
        apptReportFieldsPanel.add(apptMonthCB);
    }
}
