import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class UserBookPage implements ActionListener {
    JFrame f;
    JMenuBar mb;
    JMenuItem menu, home, makeAppt, history;
    JLabel title, type, start, end, apptAvailable, search;
    JPanel bookPanel;
    String[] apptTypes;
    JComboBox<String> typesCB;
    JTextField searchBox;
    JButton bookButton, userManual;
    JButton endButton;
    JButton go, searchButton;
    DefaultTableModel model;
    JTable appointments;
    JScrollPane scroll;
    String apptSelected;
    UserHomePage hp;
    Database db;
    TableRowSorter apptSorter;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public UserBookPage(Database db, UserHomePage hp){
        this.hp = hp;
        this.db = db;

        /* Make frame */
        f = new JFrame("Appointment Booker for User");
        f.setBackground(new Color(220, 225, 222));

        /* Set up the menu bar */
        mb = new JMenuBar();
        menu = new JMenu("Menu");

        userManual = new JButton("User Help");
        userManual.setBackground(new Color(73, 160, 120));

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

        mb.add(menu);
        mb.setBackground(new Color(73, 160, 120));
        mb.add(Box.createHorizontalGlue());
        mb.add(userManual);
        menu.setFont(new Font("Sarif", Font.PLAIN, 15));
        menu.setForeground(new Color(31, 36, 33));

//        mb.add(menu);
//        mb.setBackground(new Color(73, 160, 120));

        home = new JMenuItem("Home");
        home.setFont(new Font("Sarif", Font.PLAIN, 15));
        history = new JMenuItem("History");
        history.setFont(new Font("Sarif", Font.PLAIN, 15));

        menu.add(home);
        menu.add(history);

        home.addActionListener(this);
        history.addActionListener(this);

        f.setJMenuBar(mb);

        // add title
        bookPanel = new JPanel();
        f.getContentPane();
        title = new JLabel("Make Appointment");
        title.setFont(new Font("Sarif", Font.PLAIN, 25));
        title.setForeground(new Color(31, 36, 33));
        Dimension titleSize = title.getPreferredSize();
        title.setBounds(10, 0, titleSize.width+10, titleSize.height);
        bookPanel.add(title);

        // add type of appointment text
        type = new JLabel("Type of Appointment: ");
        type.setFont(new Font("Sarif", Font.PLAIN, 15));
        type.setForeground(new Color(31, 36, 33));
        Dimension typeSize = type.getPreferredSize();
        type.setBounds(10, 50, typeSize.width+10, typeSize.height);
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
        go.setBounds(110, 75, goSize.width+110, goSize.height);
        go.setBackground(new Color(156, 197, 161));
        go.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                goActionPerformed(evt);
            }
        });
        bookPanel.add(go);

        // add search text
        search = new JLabel("Search available appointments: ");
        type.setFont(new Font("Sarif", Font.PLAIN, 15));
        type.setForeground(new Color(31, 36, 33));
        Dimension searchSize = search.getPreferredSize();
        search.setBounds(10, 125, searchSize.width+10, searchSize.height);
        bookPanel.add(search);

        // add search text box
        searchBox = new JTextField();
        searchBox.setFont(new Font("Sarif", Font.PLAIN, 15));
        searchBox.setSize(300, 20);
        searchBox.setLocation(10, 150);
        bookPanel.add(searchBox);

        // add search button
        searchButton = new JButton("Search");
        Dimension searchButtonSize = searchButton.getPreferredSize();
        searchButton.setBounds(10, 175, searchButtonSize.width+10, searchButtonSize.height);
        searchButton.setBackground(new Color(156, 197, 161));
        bookPanel.add(searchButton);
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        // add text for picking appointment timeframe
        apptAvailable = new JLabel("Available appointments: ");
        apptAvailable.setFont(new Font("Sarif", Font.BOLD, 15));
        apptAvailable.setForeground(new Color(33, 104, 105));
        Dimension availSize = apptAvailable.getPreferredSize();
        apptAvailable.setBounds(10, 225, availSize.width+10, availSize.height);
        bookPanel.add(apptAvailable);

        // bookPanel specifications
        bookPanel.setLayout(null);
        bookPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // add table to frame
        scroll = new JScrollPane(appointments);
        f.add(scroll);

        makeBookButton();

        // add panel to frame
        f.add(bookPanel);

        // Make visible
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(screenSize.width, screenSize.height);
        f.setVisible(true);
    }

    /*HELPERS */

    private void makeBookButton(){
        bookButton = new JButton("Book Selection Now");
        bookButton.setFont(new Font("Sarif", Font.PLAIN, 15));
        bookButton.setBackground(new Color(156, 197, 161));
        Dimension startBSize = bookButton.getPreferredSize();
        bookButton.setBounds(10, 600, startBSize.width+10, startBSize.height);
        bookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                bookActionPerformed(evt);
            }
        });
        bookPanel.add(bookButton);
        bookPanel.validate();
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

    /*
     * ACTION LISTENERS
     */

    public void goActionPerformed(ActionEvent evt){
        populateAppt();
        apptSorter = new TableRowSorter(appointments.getModel());
        appointments.setRowSorter(apptSorter);
        makeBookButton();
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

    public void populateAppt() {
        apptSelected = typesCB.getSelectedItem().toString();
        appointments = new JTable();
        String [] apptHeaders = {"Date", "Time", "Description","Service Provider", "Qualification"};
        appointments.setModel(new DefaultTableModel(apptHeaders, 0));
        appointments.getTableHeader().setBackground(new Color(33, 104, 105));
        appointments.getTableHeader().setForeground(Color.WHITE);
        try{
            // Show the available time slots
            ResultSet rs = db.getUnbookedAppts(apptSelected);
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
        scroll.setBounds(10, 250, 1300, 350);
        scroll.validate();
        appointments.validate();
        appointments.getColumnModel().getColumn(0).setMaxWidth(200);
        appointments.getColumnModel().getColumn(1).setMaxWidth(200);
        appointments.getColumnModel().getColumn(2).setMinWidth(175);
        bookPanel.add(scroll);
        bookPanel.validate();
    }

    public void populateApptSearch() {
        apptSelected = typesCB.getSelectedItem().toString();
        appointments = new JTable();
        String [] apptHeaders = {"Date", "Time", "Description","Service Provider", "Qualification"};
        appointments.setModel(new DefaultTableModel(apptHeaders, 0));
        appointments.getTableHeader().setBackground(new Color(33, 104, 105));
        appointments.getTableHeader().setForeground(Color.WHITE);
        try{
            // Show the available time slots
            ResultSet rs = db.getUnbookedAppts(apptSelected);
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
        scroll.setBounds(10, 250, 950, 350);
        scroll.validate();
        appointments.validate();
        appointments.getColumnModel().getColumn(0).setMaxWidth(100);
        appointments.getColumnModel().getColumn(1).setMaxWidth(100);
        appointments.getColumnModel().getColumn(2).setMinWidth(200);
        bookPanel.add(scroll);
        bookPanel.validate();
    }

    private void bookActionPerformed(ActionEvent evt){
        int rowIndex = appointments.getSelectedRow();
        String date = String.valueOf(appointments.getValueAt(rowIndex, 0));
        String time = String.valueOf(appointments.getValueAt(rowIndex, 1));
        String spName = String.valueOf(appointments.getValueAt(rowIndex, 3));

        //get primary key of Service Provider
        String spEmail = getSPEmail(spName);

        //get primary key of appointment selected
        int apptId = db.getApptId(date, time, spEmail);
        User user = hp.getUser();

        //check for conflicts
        if (db.apptConflictUser(user.getEmail(), date, time) > 0){
            JOptionPane.showMessageDialog(null,
                    "Unable to book. You have an appointment conflict with this time.");
        }
        else{
            //update appointment in db
            db.bookAppt(user.getEmail(), apptId);

            //confirmation message
            JOptionPane.showMessageDialog(null,
                    "Appointment with " + spName + " on " + date + " at " + time + " successfully booked.");

            //reset selections
            appointments.clearSelection();
            DefaultTableModel tblModel = (DefaultTableModel)appointments.getModel();
            tblModel.setRowCount(0);
            typesCB.setSelectedIndex(0);
        }
    }

    // searches available appointments from text box
    public void searchButtonActionPerformed(ActionEvent evt) {
        //checking if user searched by appointment type
//        if (searchText.equals("Facial") || searchText.equals("facial")) {
//            searchTable("Appointment type", "Facial");
//        }
//        else if (searchText.equals("Beauty") || searchText.equals("beauty")) {
//            searchTable("Appointment type", "Beauty");
//        }
//        else if (searchText.equals("Medical") || searchText.equals("medical")) {
//            searchTable("Appointment type", "Medical");
//        }
//
//        //checking if user searched by service provider
//        String[] splitFirstLast = searchText.split(" ");
//        ResultSet email = db.getSPEmail(splitFirstLast[0], splitFirstLast[1]);
//        if (email != null) {
//            searchTable("Service Provider", String.valueOf(email));
//        }

        populateApptSearch();
        apptSorter = new TableRowSorter(appointments.getModel());
        appointments.setRowSorter(apptSorter);
        makeBookButton();
        RowFilter filter = new RowFilter() {
            String searchText = searchBox.getText();
            public boolean include(Entry entry) {
                boolean result = entry.getStringValue(0).indexOf(searchText) >=0 || entry.getStringValue(1).indexOf(searchText) >=0
                        || entry.getStringValue(2).indexOf(searchText) >=0;
                return result;
            }
        };
        apptSorter.setRowFilter(filter);
    }

    public void searchTable (String type, String searchText) {
        appointments = new JTable();
        String [] apptHeaders = {"Date", "Time", "Description","Service Provider", "Qualification"};
        appointments.setModel(new DefaultTableModel(apptHeaders, 0));
        appointments.getTableHeader().setBackground(new Color(33, 104, 105));
        appointments.getTableHeader().setForeground(Color.WHITE);
        try{
            // Show the available time slots
            ResultSet rs = null;
            if (type.equals("Appointment type")) {
                rs = db.getUnbookedAppts(searchText);
            }
            else if (type.equals("Service Provider")) {
                rs = db.getApptSP(searchText);
            }
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
        scroll.setBounds(10, 250, 1300, 350);
        scroll.validate();
        appointments.getColumnModel().getColumn(0).setMaxWidth(100);
        appointments.getColumnModel().getColumn(1).setMaxWidth(100);
        appointments.getColumnModel().getColumn(2).setPreferredWidth(300);
        bookPanel.add(scroll);
        bookPanel.validate();
        makeBookButton();
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == home){
            f.setVisible(false);
            hp.setHomeVisible();
        }
        else if(e.getSource() == makeAppt){
            appointments.removeAll();
            typesCB.setSelectedIndex(0);
        }
        else if (e.getSource() == history) {

        }
    }
}

