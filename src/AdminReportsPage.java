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
public class AdminReportsPage {
    JFrame f;
    JPanel p;
    JLabel reportsLabel;
    JButton logout, userGenButton, userManual, apptGenButton;
    JMenuBar mb;
    JPanel userReportPanel, apptReportPanel;
    JMenuItem menu, home, reports;
    AdminHomePage ahp;
    Database db;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    JTabbedPane tabbedPane;
    JComboBox<String> userMonth1CB, userMonth2CB, apptMonth1CB, apptMonth2CB;
    JComboBox<Integer> userDay1CB, userDay2CB, userYear1CB, userYear2CB, apptDay1CB, apptDay2CB, apptYear1CB, apptYear2CB;

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
        reportsLabel.setBounds(450, 10, wcSize.width+10, wcSize.height);
        f.add(reportsLabel);


        /*Create JTabbedPane and its tabs (panels)*/
        userReportPanel = new JPanel();
        userReportPanel.setLayout(null);


        userGenButton = new JButton("Generate Report");
        userGenButton.setBounds(700, 25, 150, 25);
        userGenButton.setBackground(new Color(73, 160, 120));
        userGenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                genUserReportActionPerformed(evt);
            }
        });

        userReportPanel.add(userGenButton);





        apptReportPanel = new JPanel();
        apptReportPanel.setLayout(null);

        apptGenButton = new JButton("Generate Report");
        apptGenButton.setBounds(700, 25, 150, 25);
        apptGenButton.setBackground(new Color(73, 160, 120));
        apptGenButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                genApptReportActionPerformed(evt);
            }
        });
        apptReportPanel.add(apptGenButton);

        UIManager.put("TabbedPane.selected", new Color(31, 36, 33));
        tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(50,75, 900, 650);
        tabbedPane.add("User Report", userReportPanel);
        tabbedPane.add("Appointment Report", apptReportPanel);
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
}
