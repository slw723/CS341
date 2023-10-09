package src;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserBookPage implements ActionListener {
    JFrame f;
    JMenuBar mb;
    JMenuItem menu, home, makeAppt, history;
    JLabel title, type, start, end, apptAvailable;
    JPanel bookPanel;
    String[] apptTypes;
    JComboBox<String> typesCB;
    JButton bookButton;
    JButton endButton;
    JButton go;
    DefaultTableModel model;
    JTable appointments;
    JScrollPane scroll;
    String apptSelected;
    UserHomePage hp;
    Database db;

    public static void main(String[] args){
        
    }
    
    public UserBookPage(Database db, UserHomePage hp){
        this.hp = hp;
        this.db = db;
        // default font
        Font defaultFont = UIManager.getFont("Label.font");

        /* Make frame */
        f = new JFrame("Appointment Booker for User");
        f.setBackground(new Color(220, 225, 222));

        /* Set up the menu bar */
        mb = new JMenuBar();
        menu = new JMenu("Menu");

        mb.add(menu);
        mb.setBackground(new Color(73, 160, 120));
        menu.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));
        menu.setForeground(new Color(31, 36, 33));

        mb.add(menu);
        mb.setBackground(new Color(73, 160, 120));
        
        home = new JMenuItem("Home");
        home.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));
        history = new JMenuItem("History");
        history.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));

        menu.add(home);
        menu.add(history);

        home.addActionListener(this);
        history.addActionListener(this);

        f.setJMenuBar(mb);

        // add title
        bookPanel = new JPanel();
        f.getContentPane();
        title = new JLabel("Make Appointment");
        title.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 25));
        title.setForeground(new Color(31, 36, 33));
        Dimension titleSize = title.getPreferredSize();
        title.setBounds(10, 0, titleSize.width, titleSize.height);
        bookPanel.add(title);

         // add type of appointment text
        type = new JLabel("Type of Appointment: ");
        type.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));
        type.setForeground(new Color(31, 36, 33));
        Dimension typeSize = type.getPreferredSize();
        type.setBounds(10, 50, typeSize.width, typeSize.height);
        bookPanel.add(type);

        // add dropdown for type of appointment
        apptTypes = new String[]{"","Medical", "Beauty", "Fitness"};
        typesCB = new JComboBox<>(apptTypes);
        typesCB.setBounds(10, 75, 90, 20);
        typesCB.setSelectedIndex(0);
        bookPanel.add(typesCB);

        // add go button
        go = new JButton("Go");
        Dimension goSize = go.getPreferredSize();
        go.setBounds(110, 75, goSize.width, goSize.height);
        go.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                goActionPerformed(evt);
            }
        });
        bookPanel.add(go);


        // add text for picking appointment timeframe
        apptAvailable = new JLabel("Available appointments: ");
        apptAvailable.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));
        apptAvailable.setForeground(new Color(33, 104, 105));
        Dimension availSize = apptAvailable.getPreferredSize();
        apptAvailable.setBounds(10, 125, availSize.width, availSize.height);
        bookPanel.add(apptAvailable);

        /*
        // add start text and start button
        start = new JLabel("Start: ");
        Dimension startSize = start.getPreferredSize();
        start.setBounds(10, 175, startSize.width, startSize.height);
        bookPanel.add(start);

        startButton = new JButton("Start Button");
        Dimension startBSize = startButton.getPreferredSize();
        startButton.setBounds(50, 175, startBSize.width, startBSize.height);
        bookPanel.add(startButton);

        // add end text and end button
        end = new JLabel("End: ");
        Dimension endSize = end.getPreferredSize();
        end.setBounds(10, 225, endSize.width, endSize.height);
        bookPanel.add(end);

        endButton = new JButton("End Button");
        Dimension endBSize = endButton.getPreferredSize();
        endButton.setBounds(50, 225, endBSize.width, endBSize.height);
        bookPanel.add(endButton);
        */

        // bookPanel specifications
        bookPanel.setLayout(null);
        bookPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // add table to frame
        scroll = new JScrollPane(appointments);
        f.add(scroll);

        // add panel to frame
        f.add(bookPanel);

        // Make visible
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(1000, 800);
        f.setVisible(true);
    }

    /*HELPERS */

    private void makeBookButton(){
        bookButton = new JButton("Book Selection Now");
        Dimension startBSize = bookButton.getPreferredSize();
        bookButton.setBounds(10, 520, startBSize.width, startBSize.height);
        bookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                bookActionPerformed(evt);
            }
        });
        bookPanel.add(bookButton);
        f.add(bookPanel);
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

    private String getSPQualif(String email){
        try{
            String sql = "Select Qualification FROM ServiceProvider WHERE Email = \"" + email + "\"";
            ResultSet rs = db.executeSQL(sql);
            if(rs.next()){
                return rs.getString("Qualification");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());

        }
        return null;
    }

    private String getSPEmail(String name){
        try{
            String[] names = name.split(" ", 2);
            String sql = "Select Email FROM ServiceProvider" +
                         " WHERE FirstName = \"" +  names[0]+ "\"" +
                         " AND LastName = \"" + names[1] + "\";";
            ResultSet rs = db.executeSQL(sql);
            if(rs.next()){
                return rs.getString("Email");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());

        }
        return null;
    }

    private int getApptId(String date, String time, String email){
         try{
            String sql = "Select ApptId FROM Appointment" +
                         " WHERE SPEmail = \"" +  email + "\" " +
                         " AND Time = \"" + time + "\"" +
                         " AND Date = \"" + date + "\";";
            ResultSet rs = db.executeSQL(sql);
            if(rs.next()){
                return rs.getInt("ApptId");
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());

        }
        return -1;
    }

    /*
     * ACTION LISTENERS
     */
 
    public void goActionPerformed(ActionEvent evt){
        apptSelected = typesCB.getSelectedItem().toString();
        appointments = new JTable();
        String [] apptHeaders = {"", "Date", "Time", "Description","Service Provider", "Qualification"};
        appointments.setModel(new DefaultTableModel(apptHeaders, 0));
        try{
            // Show the available time slots 
            ResultSet rs = db.getApptType(apptSelected);
            DefaultTableModel tblmodel = (DefaultTableModel)appointments.getModel();
            while(rs.next()){
                //data will be added until finished
                String descr = rs.getString("Description");
                String date = String.valueOf(rs.getDate("Date"));
                String time = String.valueOf(rs.getTime("Time"));
                String spEmail = rs.getString("SPEmail");
                String spName = getSPName(spEmail);
                String spQualif = getSPQualif(spEmail);

                Object tbData[] = {date, time, descr, spName, spQualif};

                // add data into jtable
                tblmodel.addRow(tbData);
                }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        scroll = new JScrollPane(appointments);
        // set sizes
        scroll.setBounds(10, 150, 950, 350);
        appointments.getColumnModel().getColumn(0).setMaxWidth(100);
        appointments.getColumnModel().getColumn(1).setMaxWidth(100);
        appointments.getColumnModel().getColumn(2).setMaxWidth(200);
        f.add(scroll);

        makeBookButton();
    }

    private void bookActionPerformed(ActionEvent evt){
        int rowIndex = appointments.getSelectedRow();
        String date = String.valueOf(appointments.getValueAt(rowIndex, 0));
        String time = String.valueOf(appointments.getValueAt(rowIndex, 1));
        String spName = (String)appointments.getValueAt(rowIndex, 3);

        //get primary key of Service Provider
        String spEmail = getSPEmail(spName);

        //get primary key of appointment selected
        int apptId = getApptId(date, time, spEmail);

        //update appointment in db
        db.bookAppt(spEmail, apptId);

        //return home
        f.setVisible(false);
        hp.setHomeVisible();
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == home){
            f.setVisible(false);
            hp.setHomeVisible();
        }
        else if(e.getSource() == makeAppt){
           
        }
        else if (e.getSource() == history) {

        }
    }
}
