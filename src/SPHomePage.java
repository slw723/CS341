
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

// Light Black: 31, 36, 33
// Dark Teal: 33, 104, 105
// Mint Green: 73, 160, 120
// Light Green: 156, 197, 161
// Off white: 220, 225, 222

public class SPHomePage {
        JFrame f, f2;
        JMenuBar mb;
        JMenuItem menu, home, makeAppt, history;
        JPanel p;
        JLabel hello, upcoming, noappts;
        DefaultTableModel model;
        JTable appointments;
        ServiceProvider sp;
        static Database db = new Database();

    public SPHomePage(Database db, ServiceProvider sp){
        this.db = db;
        this.sp = sp;
        // default font
        Font defaultFont = UIManager.getFont("Label.font");

        /* Make frame */
        f = new JFrame("Appointment Booker");
        f.setBackground(new Color(220, 225, 222));

        /* Set up the menu bar */
        mb = new JMenuBar();
        menu = new JMenu("Menu");
        menu.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));
        menu.setForeground(new Color(31, 36, 33));

        mb.add(menu);
        mb.setBackground(new Color(73, 160, 120));
        
        home = new JMenuItem("Home");
        home.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));
        makeAppt = new JMenuItem("Make Appointment");
        makeAppt.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));
        history = new JMenuItem("History");
        history.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));

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

        f.setJMenuBar(mb);

        // add hello Name
        p = new JPanel();
        f.getContentPane();
        String str = "Hello, " + sp.getFirstName() + "!";
        hello = new JLabel(str);
        hello.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));
        hello.setForeground(new Color(31, 36, 33));
        Dimension helloSize = hello.getPreferredSize();
        hello.setBounds(10, 10, helloSize.width, helloSize.height);
        p.add(hello);

        // add upcoming appts title
        upcoming = new JLabel("Upcoming Appointments");
        upcoming.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));
        upcoming.setForeground(new Color(33, 104, 105));
        Dimension upSize = upcoming.getPreferredSize();
        upcoming.setBounds(10, 50, upSize.width, upSize.height);
        p.add(upcoming);

        // show the upcoming appointments if they exists
        
        int ret = populateUpcoming();
        if(ret == -1){
            noappts = new JLabel("No Upcoming Appointments");
            noappts.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 10));
            Dimension noSize = noappts.getPreferredSize();
            noappts.setBounds(20, 80, noSize.width, noSize.height);
            p.add(noappts);
        }

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

    public void setHomeVisible(){
        f.setVisible(true);
    }

    public ServiceProvider getSp(){
        return sp;
    }

     public void goHomeActionPerformed(ActionEvent e){
      
    }

    public void makeApptActionPerformed(ActionEvent e){
      
        f.setVisible(false);
        SPBookPage bp = new SPBookPage(db, this);        
    }

    public void historyActionPerformed(ActionEvent e){
      
        f.setVisible(false);
            
    }

    /* Populate full table view -> good for admin view*/
    private int populateUpcoming() {
        try{
            String sql = "SELECT * FROM Appointment WHERE UserEmail = \"" + sp.getEmail() + 
                        "\" AND Date >= date(NOW()) AND Time = time(NOW());";
            ResultSet rs = db.executeSQL(sql);

            if(rs.next() == false){
                return -1;
            }
    
            model = new DefaultTableModel(new String[]{"Description","Date","Time","Type","Service Provider Email"}, 0);
            appointments = new JTable(model);
            DefaultTableModel tblModel = (DefaultTableModel)appointments.getModel();
            tblModel.setRowCount(0);
            while(rs.next()){
                //data will be added until finished
                String descr = rs.getString("Description");
                String date = String.valueOf(rs.getDate("Date"));
                String time = String.valueOf(rs.getTime("Time"));
                String type = String.valueOf(rs.getInt("Type"));
                String spEmail = rs.getString("SPEmail");

                String tbData[] = {descr, date, time, type, spEmail};

                //addstring array into jtable
                tblModel.addRow(tbData);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        return 0;
    }   
}
