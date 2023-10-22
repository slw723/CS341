//package src;

// import src.Database;
// import src.SPBookPage;
// import src.ServiceProvider;

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

public class AdminHomePage {
    JFrame f;
    JButton logout;
    JMenuBar mb;

    JMenuItem menu, home, trends;
    JTable users, serviceProviders;
    Admin admin;
    static Database db = new Database();

    public AdminHomePage(Database db, Admin admin){
        this.db = db;
        this.admin = admin;

        /*Make frame*/
        f = new JFrame("Admin Home Page");
        f.setBackground(new Color(220, 225, 222));

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

        /* Make visible */
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(1000,800);
        f.setVisible(true);
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
