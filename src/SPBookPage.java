import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import javax.swing.*;

public class SPBookPage implements ActionListener {
    JFrame f;
    JMenuBar mb;
    JMenuItem menu, home, makeAppt;
    JLabel title, descr, date, time;
    JTextField described;
    String[] times, months;
    Integer[] days, years;
    JComboBox<String> timeCB, monthCB;
    JComboBox<Integer> dayCB, yearCB;
    JPanel bookPanel;
    JButton bookButton;
    JScrollBar scroll, scroll2, scroll3, scroll4;
    JButton go, userManual;
    SPHomePage hp;
    Database db;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public SPBookPage(Database db, SPHomePage hp){
        this.hp = hp;
        this.db = db;

        // Make frame
        f = new JFrame("Appointment Booker");
        f.setBackground(new Color(220, 225, 222));

        // Set up the menu bar
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

        home = new JMenuItem("Home");
        home.setFont(new Font("Sarif", Font.PLAIN, 15));
        menu.add(home);

        home.addActionListener(this);
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


        // make form
        // add text field for appt description
        descr = new JLabel("Description: ");
        descr.setFont(new Font("Sarif", Font.PLAIN, 15));
        descr.setForeground(new Color(33, 104, 105));
        Dimension descrSize = descr.getPreferredSize();
        descr.setBounds(10, 50, descrSize.width+10, descrSize.height);
        bookPanel.add(descr);

        described = new JTextField();
        described.setFont(new Font("Sarif", Font.PLAIN, 15));
        described.setSize(300, 20);
        described.setLocation(descrSize.width + 20, 55);
        bookPanel.add(described);

        // add text field for date
        date = new JLabel("Date: ");
        date.setFont(new Font("Sarif", Font.PLAIN, 15));
        date.setForeground(new Color(33, 104, 105));
        Dimension dateSize = date.getPreferredSize();
        date.setBounds(10, 90, dateSize.width+10, dateSize.height);
        bookPanel.add(date);

        // set up months drop down and add
        months = new String[]{"", "01 - Jan", "02 - Feb", "03 - Mar", "04 - Apr",
                "05 - May", "06 - Jun", "07 - Jul", "08 - Aug",
                "09 - Sep", "10 - Oct", "11 - Nov", "12 - Dec"};
        monthCB = new JComboBox<>(months);
        monthCB.setFont(new Font("Sarif", Font.PLAIN, 10));
        monthCB.setBounds(descrSize.width + 20, 90,
                80, 20);
        monthCB.setSelectedIndex(0);
        scroll2 = new JScrollBar();
        monthCB.add(scroll2);
        bookPanel.add(monthCB);

        // set up days drop down and add
        days = new Integer[32];
        days[0] = null;
        for(Integer i = 1; i < 32; i++){
            days[i] = i;
        }
        dayCB = new JComboBox<>(days);
        Dimension daySize = dayCB.getPreferredSize();
        dayCB.setBounds(descrSize.width + 110, 90,
                daySize.width + 20, 20);
        dayCB.setSelectedIndex(0);
        scroll3 = new JScrollBar();
        dayCB.add(scroll3);
        bookPanel.add(dayCB);

        // set up years drop down and add
        years = new Integer[3];
        years[0] = null;
        years[1] = Integer.valueOf(String.valueOf(Year.now()));
        if(LocalDate.now().getDayOfYear() != 1){
            years[2] = Integer.valueOf(String.valueOf(Year.now())) + 1;
        }
        yearCB = new JComboBox<>(years);
        Dimension yearSize = yearCB.getPreferredSize();
        yearCB.setBounds(descrSize.width + daySize.width + 140, 90,
                yearSize.width, 20);
        yearCB.setSelectedIndex(0);
        scroll4 = new JScrollBar();
        yearCB.add(scroll4);
        bookPanel.add(yearCB);

        // add date field
        time = new JLabel("Time: ");
        time.setFont(new Font("Sarif", Font.PLAIN, 15));
        time.setForeground(new Color(33, 104, 105));
        Dimension timeSize = time.getPreferredSize();
        time.setBounds(10, 130, timeSize.width+10, timeSize.height);
        bookPanel.add(time);

        // add time drop down
        times = new String[]{ "", "12:00 AM", "12:30 AM", "1:00 AM", "1:30 AM", "2:00 AM",
                "2:30 AM", "3:00 AM", "3:30 AM", "4:00 AM", "4:30 AM", "5:00 AM",
                "5:30 AM", "6:00 AM", "6:30 AM", "7:00 AM", "7:30 AM", "8:00 AM",
                "8:30 AM", "9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM", "11:00 AM",
                "11:30 AM", "12:00 PM", "12:30 PM", "1:00 PM", "1:30 PM", "2:00 PM",
                "2:30 PM","3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM", "5:00 PM",
                "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM", "8:00 PM",
                "8:30 PM", "9:00 PM", "9:30 PM", "10:00 PM", "10:30 PM", "11:00 PM",
                "11:30 PM"};
        timeCB = new JComboBox<>(times);
        timeCB.setBounds(descrSize.width + 20, 130, 90, 20);
        timeCB.setSelectedIndex(0);
        scroll = new JScrollBar();
        timeCB.add(scroll);
        bookPanel.add(timeCB);

        // add go button
        go = new JButton("Submit");
        Dimension goSize = go.getPreferredSize();
        go.setBounds(10, 160, goSize.width+10, goSize.height);
        go.setBackground(new Color(156, 197, 161));
        go.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                submitActionPerformed(evt);
            }
        });
        bookPanel.add(go);

        // bookPanel specifications
        bookPanel.setLayout(null);
        bookPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // add panel to frame
        f.add(bookPanel);

        // Make visible
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(screenSize.width, screenSize.height);
        f.setVisible(true);
    }

    /* HELPERS */
    private int dataValidation(){
        // check date is valid day/months
        String[] splits = monthCB.getSelectedItem().toString().split(" - ", 2);
        int month = Integer.valueOf(splits[0]);
        int day = (int)dayCB.getSelectedItem();
        int year = (int)yearCB.getSelectedItem();
        try{
            String date = year + "-" + month + "-" + day;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            sdf.parse(date);

            String today = String.valueOf(java.time.LocalDate.now());
            LocalDate today2024 = LocalDate.now().plusYears(1);
            // check time is in the future
            if(sdf.parse(date).equals(sdf.parse(today))){
                LocalTime t = convertTime(timeCB.getSelectedItem().toString());
                LocalTime now = java.time.LocalTime.now();
                if(t.isBefore(now)){
                    timeCB.setSelectedIndex(0);
                    JOptionPane.showMessageDialog(null,
                            "Enter a valid time");
                    return -1;
                }
            }
            // check if only a year in advance
            else if(sdf.parse(date).before(sdf.parse(today)) ||
                    sdf.parse(date).after(sdf.parse(String.valueOf(today2024)))){
                JOptionPane.showMessageDialog(null,
                        "Please enter a valid date.");
                return -1;
            }
        }
        catch(ParseException p){
            JOptionPane.showMessageDialog(null,
                    "Please enter a valid date.");
            return -1;
        }
        return 0;
    }

    // convert a string to LocalTime object
    private LocalTime convertTime(String oldTime){

        String[] str = oldTime.split(":", 2);
        String hr = str[0];
        int hour = Integer.parseInt(hr);
        String[] str2 = str[1].split(" ", 2);
        int minute = Integer.parseInt(str2[0]);
        String timeOfDay = str2[1];

        if(timeOfDay.equals("AM")){
            if(hour == 12){
                hour = 0;
            }
            return LocalTime.of(hour, minute, 0, 0);
        }
        else{ //PM
            if(hour != 12){
                hour = hour + 12;
            }
            return LocalTime.of(hour, minute, 0, 0);
        }
    }

    /* ACTION LISTENERS */
    public void submitActionPerformed(ActionEvent evt){
        // check if data entered is valid
        int valid = dataValidation();
        if(valid < 0){
            return;
        }
        else{
            // gather the information
            String des = described.getText();
            String[] splits = monthCB.getSelectedItem().toString().split(" - ", 2);
            int month = Integer.valueOf(splits[0]);
            int day = (int)dayCB.getSelectedItem();
            int year = (int)yearCB.getSelectedItem();
            Date dateStr = Date.valueOf(year + "-" + month + "-" + day);
            Time timeStr = Time.valueOf(convertTime(timeCB.getSelectedItem().toString()));
            ServiceProvider sp = hp.getSP();
            // put info into appt object
            Appointment appt = new Appointment(des, dateStr, timeStr, sp.getType(), 0, null, sp.getEmail());

            //check if SP has appt at that time already,
            int ret = db.apptConflict(sp, appt);
            if(ret < 0){
                // show conflict message
                JOptionPane.showMessageDialog(f, "You have an appointment conflict. Pick a different time",
                        "Appointment Conflict", JOptionPane.ERROR_MESSAGE);
                // reset fields
                timeCB.setSelectedIndex(0);
                monthCB.setSelectedIndex(0);
                dayCB.setSelectedIndex(0);
                yearCB.setSelectedIndex(0);
                bookPanel.validate();
            }
            else{ // success
                db.insertAppt(appt);
                JOptionPane.showMessageDialog(null,
                        "Successfully created " + des + " on " + dateStr + " at " + timeStr);
                f.setVisible(false);
                hp.setHomeVisible();
            }
        }
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

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == home){
            f.setVisible(false);
            hp.setHomeVisible();
        }
        else if(e.getSource() == makeAppt){

        }
    }
}

