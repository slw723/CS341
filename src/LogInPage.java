
import src.Appointment;
import src.Database;
import src.ServiceProvider;
import src.User;

import java.sql.SQLException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

// Light Black: 31, 36, 33
// Dark Teal: 33, 104, 105
// Mint Green: 73, 160, 120
// Light Green: 156, 197, 161
// Off white: 220, 225, 222


public class LogInPage extends JFrame implements ActionListener {
    private static User user;
    private static Database db;

    ArrayList<User> users = new ArrayList<>();
    ArrayList<ServiceProvider> sp = new ArrayList<>();
    ArrayList<Appointment> appointments = new ArrayList<>();

    /*Global variables below for log-in gui*/
    JFrame loginWin;
    JPanel wcPanel, shPanel, loginPanel;
    JLabel userLabel, passwordLabel, welcome, enterInfo;
    JTextField userText;
    JPasswordField passwordText;
    JButton loginButton, registerButton;

    public static void main(String[] args){
        db = new Database(); //.getInstance?
        new LogInPage(db);

    }

    public LogInPage(Database db){

        user = new User();
        user.setFirstName("Alex");

        try{
            // db.connect();
            System.out.println("Successful connection!");
            loadLogin(db);

        }
        catch(Exception e){ //SQLException e
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }
    }

    private void loadLogin(Database db) {
        //load the page
        Font wcFont = new Font ("Sarif", Font.BOLD, 25);
        Font shFont = new Font ("Sarif", Font.BOLD, 15);

        loginWin = new JFrame(); //creates the log in frame
        loginWin.setSize(720, 405);
        loginWin.setLayout(null);
        loginWin.setResizable(false); //prevents frame from being resized
        loginWin.setTitle("Appointment Booker Login");
        loginWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //ends program when loginWin is closed
        loginWin.setBackground(new Color(220, 225, 222));

        wcPanel = new JPanel();
        wcPanel.setBounds(285, 25, 150, 45);
        welcome = new JLabel("Welcome!");
        welcome.setFont(wcFont);

        shPanel = new JPanel(); //subheading panel
        shPanel.setBounds(210, 75, 300, 30);
        enterInfo = new JLabel("Please enter your log in information.");
        enterInfo.setFont(shFont);
        wcPanel.add(welcome);
        shPanel.add(enterInfo);
        loginWin.add(wcPanel);
        loginWin.add(shPanel);

        /*Green panel to hold text fields and buttons*/
        loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(new Color(33, 104, 105)); //dark teal
        loginPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        loginPanel.setBounds(200, 130, 300, 175);

        /*Text fields, labels, and buttons*/
        userLabel = new JLabel("Username");
        userLabel.setBounds(20, 20, 80, 20);
        userLabel.setForeground(Color.WHITE);
        userText = new JTextField(20);
        userText.setBounds(105, 20, 150, 20);
        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(20, 45, 150, 20);
        passwordLabel.setForeground(Color.WHITE);
        passwordText = new JPasswordField(20);
        passwordText.setBounds(105, 45, 150, 20);

        loginButton = new JButton("Log In");
        loginButton.setBounds(100, 100, 100, 25);
        loginButton.setBackground(new Color(156, 197, 161));
        loginButton.addActionListener(this);
        registerButton = new JButton("Register");
        registerButton.setBounds(100, 130, 100, 25);
        registerButton.setBackground(new Color(156, 197, 161));
        registerButton.addActionListener(this);

        /*add components to panel and loginWin*/
        loginPanel.add(userLabel);
        loginPanel.add(userText);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordText);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);
        loginWin.add(loginPanel);

        loginWin.setVisible(true);

    }
    /******************************************************************************/
    /******************************************************************************/
    /*REGISTRATION WINDOW BELOW*/
    JFrame regWin;
    JPanel arPanel, instPanel, regPanel, cbPanel;
    JLabel arLabel, instruct, unLabel, pwLabel, cbLabel, qualLabel, yearGradLabel;
    JButton signUpButton;
    JTextField unText, qualText, yearGradText;
    JPasswordField pwText;
    JComboBox<String> cb;
    String selectedAcctType;

    public void loadRegister(Database db) {
        //load the page
        Font wcFont = new Font ("Sarif", Font.BOLD, 15);
        Font shFont = new Font ("Sarif", Font.BOLD, 12);

        regWin = new JFrame(); //creates the log in frame
        regWin.setSize(720, 405);
        regWin.setLayout(null);
        regWin.setResizable(false); //prevents frame from being resized
        regWin.setTitle("Account Registration");
        regWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //ends program when loginWin is closed
        regWin.setBackground(new Color(220, 225, 222));

        arPanel = new JPanel();
        arPanel.setBounds(260, 10, 200, 25);
        arLabel = new JLabel("Account Registration");
        arLabel.setFont(wcFont);

        instPanel = new JPanel(); //subheading panel
        instPanel.setBounds(110, 40, 500, 30);
        instruct = new JLabel("Please select and Account type and fill in the applicable information.");
        instruct.setFont(shFont);
        arPanel.add(arLabel);
        instPanel.add(instruct);
        regWin.add(arPanel);
        regWin.add(instPanel);

        /*Drop down for Service Provider and User*/
        cbPanel = new JPanel();
        cbPanel.setBounds(110, 75, 700, 40);
        cbLabel = new JLabel("Account Type: ");
        cbLabel.setBounds(50, 75, 100, 25);
        String[] acctTypes = {"User", "Service Provider"};
        cb = new JComboBox<String>(acctTypes);
        cb.setBackground(Color.WHITE);
        cb.setVisible(true);
        cbPanel.add(cbLabel);
        cbPanel.add(cb);
        regWin.add(cbPanel);

        /*Green panel to hold text fields and buttons*/
        regPanel = new JPanel();
        regPanel.setLayout(null);
        regPanel.setBackground(new Color(33, 104, 105)); //dark teal
        regPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        regPanel.setBounds(200, 130, 300, 175);

        /*Default text fields, labels*/
        unLabel = new JLabel("Username: ");
        unLabel.setBounds(20, 20, 80, 20);
        unLabel.setForeground(Color.WHITE);
        unText = new JTextField(20);
        unText.setBounds(105, 20, 150, 20);
        pwLabel = new JLabel("Password: ");
        pwLabel.setBounds(20, 45, 150, 20);
        pwLabel.setForeground(Color.WHITE);
        pwText = new JPasswordField(20);
        pwText.setBounds(105, 45, 150, 20);
        cb.addActionListener(this);

        signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(100, 130, 100, 25);
        signUpButton.setBackground(new Color(156, 197, 161));
        signUpButton.addActionListener(this);

        /*add components to panel and loginWin*/
        regPanel.add(unLabel);
        regPanel.add(unText);
        regPanel.add(pwLabel);
        regPanel.add(pwText);
        regPanel.add(signUpButton);
        regWin.add(regPanel);
        regWin.setVisible(true);


    }


    public void actionPerformed(ActionEvent e) {
        /*IF THE LOGIN BUTTON IS CLICKED*/
        if (e.getSource() == loginButton) {
            try {
                String username = userText.getText();
                String password = passwordText.getName();
                //ResultSet results = db.findUser(username, password); //executing the query method from Database class
                //int count = 0;
                //while(results.next()) {
                //count = count + 1;
                //}
                //if (count == 1) { //user was found
                //JOptionPane.showMessageDialog(null, "User Login Successful.");
                //UserHomePage userHP = new UserHomePage(db, user);
                //loginWin.dispose();
                //}
                //else { //user was not found, check service provider table
                //ResultSet results2 = db.findServiceProvider(username, password);
                //int count2 = 0;
                //while(results2.next()) {
                //count2 = count2 + 1;
                //}
                //if (count2 == 1) { //service provider was found
                //JOptionPane.showMessageDialog(null, "Service Provider Login Successful.");
                //UserHomePage userHP = new UserHomePage(db, user); //Service Provider page??
                //loginWin.dispose();
                //}

                //else { //service provider not found, check admin table
                //ResultSet results3 = db.findAdmin(username, password); //checks on UserID in the Admin table
                //int count3 = 0;
                //while(results3.next()) {
                //count3 = count3 + 1;
                //}
                //if (count3 == 1) { //Admin was found
                //JOptionPane.showMessageDialog(null, "Admin Login Successful.");
                //UserHomePage userHP = new UserHomePage(db, user); //Admin page??
                //loginWin.dispose();
                //}
                //else{
                JOptionPane.showMessageDialog(null, "User not found. Use the 'Register' button to create a new account");
                //}
                //}

                //}

            }catch(Exception d) {
                JOptionPane.showMessageDialog(null,d);
            }
        }

        /*IF THE REGISTER BUTTON IS CLICKED GO TO REGISTER PAGE*/
        else if (e.getSource() == registerButton) {
            loadRegister(db);
            loginWin.dispose();

        }

        else if (e.getSource() == signUpButton) {

            selectedAcctType = cb.getSelectedItem().toString();

            if (selectedAcctType.equals("User")){
                //create new user
                User newUser = new User();
                newUser.setEmail(unText.getText()); //CURRENTLY USERNAME NOT EMAIL
                //newUser.setPassword(pwText.getPassword()); //datatype mismatch
                newUser.setFirstName("First");
                newUser.setLastName("Last");
                newUser.setPhoneNumber(1112223334);
                //db.insertUser(newUser);
                JOptionPane.showMessageDialog(null, "New user created under username: " + unText.getText());

            }

            else if (selectedAcctType.equals("Service Provider")) {
                //create new service provider
                String qualifications = JOptionPane.showInputDialog("Please enter your qualifications: ");
                ServiceProvider sp = new ServiceProvider();
                sp.setEmail(unText.getText());
                //	sp.setPassword(pwText.getPassword()); //datatype  mismatch
                sp.setFirstName("FirstSP");
                sp.setLastName("LastSP");
                sp.setQualification(qualifications);
                sp.setYearGraduated(2023);
                sp.setType("Beauty");
                //db.insertSP(sp);
                JOptionPane.showMessageDialog(null, "New Service Provider created under username: " + unText.getText());
            }

            //go back to log-in page
            loadLogin(db);
        }

        /*Qualification label and textfield only becomes visible if Service Provider is selected - NOT WORKING */
        /**
         * else if (e.getSource() == cb && cb.getSelectedItem().toString().equals("Service Provider")) {
         qualLabel = new JLabel("Qualifications: ");
         qualLabel.setForeground(Color.WHITE);
         qualLabel.setBounds(40, 70, 75, 20);
         qualText = new JTextField(20);
         qualText.setBounds(95, 70, 150, 20);

         qualLabel.setVisible(true);
         qualText.setVisible(true);
         regPanel.add(qualLabel);
         regPanel.add(qualText);
         regWin.add(regPanel);

         }
         /*Qualification label and textfield only becomes hidden if user is selected*/
        /**
         else if (e.getSource() == cb && cb.getSelectedItem().toString().equals("User")) {

         qualLabel = new JLabel("Qualifications: ");
         qualLabel.setVisible(false);
         qualText = new JTextField(20);
         qualText.setVisible(false);

         //if (cb.getSelectedItem().toString().equals("Service Provider")) {
         //qualLabel.setVisible(true);
         //qualText.set
         //}
         }
         **/
    }
}

