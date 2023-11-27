import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
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
    JButton logout, userGoButton, spGoButton, apptGoButton, apptCancel;
    JMenuBar mb;
    JMenuItem menu, home, reports;
    AdminHomePage ahp;
    Database db;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

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

        mb.add(menu);
        mb.setBackground(new Color(73, 160, 120));
        mb.add(Box.createHorizontalGlue());
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


        /* Make visible. */
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(screenSize.width, screenSize.height);
        f.setVisible(true);
    }


    private void goHomeActionPerformed(ActionEvent e){
        f.setVisible(false);
        ahp.setHomeVisible();
    }

    private void logoutActionPerformed(ActionEvent e) {
        f.setVisible(false);
        new LogInPage(db);
    }
}
