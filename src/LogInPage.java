//package src;

// import src.Database;
// import src.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.*;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
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

public class LogInPage extends JFrame  {
    private static User user;
    private static ServiceProvider sp;
    public static Database db;

    // ArrayList<User> users = new ArrayList<>();
    // ArrayList<ServiceProvider> sp = new ArrayList<>();
    // ArrayList<Appointment> appointments = new ArrayList<>();

    /*Global variables below for log-in gui*/
    JFrame loginWin;
    JPanel wcPanel, shPanel, loginPanel;
    JLabel userLabel, passwordLabel, welcome, enterInfo;
    JTextField userText;
    JPasswordField passwordText;
    JButton loginButton, registerButton;

    public static void main(String[] args){
        db = new Database();
        new LogInPage(db);

    }

    /* CONSTRUCTOR */
    public LogInPage(Database db){

        try{
            db.connect();
            System.out.println("Successful connection!");
            loadLogin(db);
        }
        catch(Exception e){ //SQLException e
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }
    }

    /* LOGIN PAGE */
    private void loadLogin(Database db) {
        //load the page
        Font wcFont = new Font ("Sarif", Font.BOLD, 25);
        Font shFont = new Font ("Sarif", Font.BOLD, 15);

        loginWin = new JFrame(); //creates the log in frame
        loginWin.setSize(1000, 1000);
        loginWin.setLayout(null);
        //loginWin.setResizable(false); //prevents frame from being resized
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
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                loginActionPerformed(e);
            }
        });
        registerButton = new JButton("Register");
        registerButton.setBounds(100, 130, 100, 25);
        registerButton.setBackground(new Color(156, 197, 161));
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                registerActionPerformed(e);
            }
        });

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

    /* HELPERS */

    /******************************************************************************/
    /*REGISTRATION WINDOW BELOW*/
    JFrame regWin;
    JPanel arPanel, instPanel, regPanel, cbPanel;
    JLabel arLabel, instruct, fLabel, lLabel, unLabel, pwLabel,
        phLabel, cbLabel, qualLabel, yearGradLabel, typeLabel;
    JButton signUpButton;
    JTextField fText, lText, unText, phText, qualText, yearGradText;
    JPasswordField pwText;
    JComboBox<String> cb;
    JRadioButton fitness, beauty, health;
    ButtonGroup radioButtons;
    String selectedAcctType, spType;

    public void loadRegister(Database db) {
        //load the page
        Font wcFont = new Font ("Sarif", Font.BOLD, 15);
        Font shFont = new Font ("Sarif", Font.BOLD, 12);

        regWin = new JFrame(); //creates the log in frame
        regWin.setSize(1000, 1000);
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
        cbPanel.setBounds(30, 75, 700, 40);
        cbLabel = new JLabel("Account Type: ");
        cbLabel.setBounds(30, 75, 100, 25);
        String[] acctTypes = {"", "User", "Service Provider"};
        cb = new JComboBox<String>(acctTypes);
        cb.setBackground(Color.WHITE);
        cb.setVisible(true);
        cb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent evt){
                selectAcctActionPerformed(evt);
            }
        });
        
        cbPanel.add(cbLabel);
        cbPanel.add(cb);
        regWin.add(cbPanel);
        regWin.setVisible(true);
    }

    private void showUserRegister(){
        /*Green panel to hold text fields and buttons*/
        regPanel = new JPanel();
        regPanel.setBackground(new Color(33, 104, 105)); //dark teal
        regPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        regPanel.setBounds(200, 140, 275, 300);

        hideOtherReg();

        /*Default text fields, labels*/
        fLabel = new JLabel("First Name: ");
        fLabel.setBounds(20, 20, 80, 20);
        fLabel.setForeground(Color.WHITE);
        fText = new JTextField(15);
        fText.setBounds(105, 20, 50, 20);
        lLabel = new JLabel("Last Name: ");
        lLabel.setBounds(20, 45, 80, 20);
        lLabel.setForeground(Color.WHITE);
        lText = new JTextField(15);
        lText.setBounds(105, 55, 50, 20);
        unLabel = new JLabel("Username: ");
        unLabel.setBounds(20, 70, 80, 20);
        unLabel.setForeground(Color.WHITE);
        unText = new JTextField(15);
        unText.setBounds(105, 70, 80, 20);
        pwLabel = new JLabel("Password: ");
        pwLabel.setBounds(20, 95, 80, 20);
        pwLabel.setForeground(Color.WHITE);
        pwText = new JPasswordField(15);
        pwText.setBounds(105, 95, 80, 20);
        phLabel = new JLabel("Phone Num:");
        phLabel.setBounds(20, 120, 80, 20);
        phLabel.setForeground(Color.WHITE);
        phText = new JTextField(15);
        phText.setBounds(105, 120, 80, 20);

        signUpButton = new JButton("Sign Up");
        signUpButton.setLocation(90, 170);
        signUpButton.setSize(100, 25);
        signUpButton.setBackground(new Color(156, 197, 161));
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                signUpActionPerformed(e);
            }
        });

        /*add components to panel and loginWin*/
        regPanel.add(fLabel);
        regPanel.add(fText);
        regPanel.add(lLabel);
        regPanel.add(lText);
        regPanel.add(unLabel);
        regPanel.add(unText);
        regPanel.add(pwLabel);
        regPanel.add(pwText);
        regPanel.add(phLabel);
        regPanel.add(phText);
        regPanel.add(signUpButton);
        regPanel.validate();
        regWin.add(regPanel);
        regWin.validate();
    }

    private void showSPRegister(){
        /*Green panel to hold text fields and buttons*/
        regPanel = new JPanel();
        regPanel.setBackground(new Color(33, 104, 105)); //dark teal
        regPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        regPanel.setBounds(200, 140, 275, 300);

        hideOtherReg();

        /*Default text fields, labels*/
        fLabel = new JLabel("First Name: ");
        fLabel.setBounds(20, 20, 80, 20);
        fLabel.setForeground(Color.WHITE);
        fText = new JTextField(15);
        fText.setBounds(105, 20, 100, 20);

        lLabel = new JLabel("Last Name: ");
        lLabel.setBounds(20, 45, 80, 20);
        lLabel.setForeground(Color.WHITE);
        lText = new JTextField(15);
        lText.setBounds(105, 45, 100, 20);

        unLabel = new JLabel("Username: ");
        unLabel.setBounds(20, 70, 80, 20);
        unLabel.setForeground(Color.WHITE);
        unText = new JTextField(15);
        unText.setBounds(105, 70, 100, 20);

        pwLabel = new JLabel("Password: ");
        pwLabel.setBounds(20, 95, 80, 20);
        pwLabel.setForeground(Color.WHITE);
        pwText = new JPasswordField(15);
        pwText.setBounds(105, 95, 100, 20);

        phLabel = new JLabel("Phone Num:");
        phLabel.setBounds(20, 120, 80, 20);
        phLabel.setForeground(Color.WHITE);
        phText = new JTextField(15);
        phText.setBounds(105, 120, 100, 20);

        qualLabel = new JLabel("Qualification:");
        qualLabel.setBounds(20, 140, 80, 20);
        qualLabel.setForeground(Color.WHITE);
        qualText = new JTextField(15);
        qualText.setBounds(105, 140, 100, 20);

        yearGradLabel = new JLabel("Year Graduated:");
        yearGradLabel.setBounds(20, 160, 80, 20);
        yearGradLabel.setForeground(Color.WHITE);
        yearGradText = new JTextField(14);
        yearGradText.setBounds(105, 160, 90, 20);

        typeLabel = new JLabel("Type:      ");
        typeLabel.setBounds(20, 190, 100, 20);
        typeLabel.setForeground(Color.WHITE);
        fitness = new JRadioButton("Fitness");
        fitness.setBackground(new Color(33, 104, 105));
        fitness.setForeground(Color.WHITE);
        // fitness.setLocation(120, 180);
        fitness.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                radioButtonActionPerformed(e);
            }
        });
        beauty = new JRadioButton("Beauty");
        beauty.setForeground(Color.WHITE);
        beauty.setBackground(new Color(33, 104, 105));
        // beauty.setLocation(160, 180);
        beauty.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                radioButtonActionPerformed(e);
            }
        });
        health = new JRadioButton("Health");
        health.setForeground(Color.WHITE);
        health.setBackground(new Color(33, 104, 105));
        health.setLocation(210, 180);
        health.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                radioButtonActionPerformed(e);
            }
        });

        //only allow one to be checked at a time
        radioButtons = new ButtonGroup();
        radioButtons.add(fitness);
        radioButtons.add(beauty);
        radioButtons.add(health);

        signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(200, 180, 100, 25);
        signUpButton.setBackground(new Color(156, 197, 161));
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                signUpActionPerformed(e);
            }
        });

        /*add components to panel and loginWin*/
        regPanel.add(fLabel);
        regPanel.add(fText);
        regPanel.add(lLabel);
        regPanel.add(lText);
        regPanel.add(unLabel);
        regPanel.add(unText);
        regPanel.add(pwLabel);
        regPanel.add(pwText);
        regPanel.add(phLabel);
        regPanel.add(phText);
        regPanel.add(qualLabel);
        regPanel.add(qualText);
        regPanel.add(yearGradLabel);
        regPanel.add(yearGradText);
        regPanel.add(typeLabel);
        regPanel.add(fitness);
        regPanel.add(beauty);
        regPanel.add(health);
        regPanel.add(signUpButton);
        regPanel.validate();
        regWin.add(regPanel);
        regWin.validate();
    }

    private void hideOtherReg(){
        for(Component c : regPanel.getComponents()){
            regPanel.remove(c);
        }
        regPanel.validate();
    }


    /*hashing a password*/
    private byte[] hash(char[] pw){
        try{
        //introduce the salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        //configure SHA-512
        KeySpec spec = new PBEKeySpec(pw, salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return factory.generateSecret(spec).getEncoded();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /* ACTION LISTENERS */

    private void loginActionPerformed(ActionEvent e){
         try {
                String username = userText.getText();
                byte[] password = hash(passwordText.getPassword());
                ResultSet results = db.findUser(username, password); //executing the query method from Database class
                if(results.next()){ //user was found
                    user = new User(username, password);
                    user.setFirstName(results.getString("FirstName"));
                    user.setLastName(results.getString("LastName"));
                    user.setPhoneNumber(results.getLong("PhoneNum"));
                    //JOptionPane.showMessageDialog(null, "User Login Successful.");
                    UserHomePage userHP = new UserHomePage(db, user);
                    loginWin.dispose();
                }
                else { //user was not found, check service provider table
                    ResultSet results2 = db.findServiceProvider(username, password);
                    if(results2.next()){ //service provider was found
                        sp = new ServiceProvider(username, password);
                        sp.setFirstName(results2.getString("FirstName"));
                        sp.setLastName(results2.getString("LastName"));
                        sp.setType(results2.getString("Type"));
                        sp.setPhoneNumber(results2.getLong("PhoneNum"));
                        sp.setQualification(results2.getString("Qualification"));
                        sp.setYearGraduated(results2.getInt("YearGraduated"));
                        //JOptionPane.showMessageDialog(null, "Service Provider Login Successful.");
                        SPHomePage spHP = new SPHomePage(db, sp); //Service Provider page??
                        loginWin.dispose();
                    }
                    else { //service provider not found, check admin table
                        // ResultSet results3 = db.findAdmin(username, password); //checks on UserID in the Admin table
                        // if(results3.next()){
                            // JOptionPane.showMessageDialog(null, "Admin Login Successful.");
                            // UserHomePage ad = new UserHomePage(db, user); //Admin page??
                            // loginWin.dispose();
                        // }
                        // else{
                            JOptionPane.showMessageDialog(null, 
                                "User not found. Use the 'Register' button to create a new account");
                        // }
                        
                    }
                }
            }
            catch(Exception d) {
                JOptionPane.showMessageDialog(null,d);
            }
    }

    private void registerActionPerformed(ActionEvent e){
        loadRegister(db);
        loginWin.dispose();
    }

    private void signUpActionPerformed(ActionEvent e){

        selectedAcctType = cb.getSelectedItem().toString();

        if (selectedAcctType.equals("User")){
            //create new user
            User newUser = new User();
            newUser.setEmail(unText.getText()); //CURRENTLY USERNAME NOT EMAIL
            newUser.setPassword(hash(pwText.getPassword())); 
            newUser.setFirstName(fText.getText());
            newUser.setLastName(lText.getText());
            newUser.setPhoneNumber(Long.parseLong(phText.getText()));

            //need to do data validation

            db.insertUser(newUser);
            JOptionPane.showMessageDialog(null, "New user created under username: " + unText.getText());
        }

        else if (selectedAcctType.equals("Service Provider")) {
            //create new service provider
            // String qualifications = JOptionPane.showInputDialog("Please enter your qualifications: ");
            ServiceProvider sp = new ServiceProvider();
            sp.setEmail(unText.getText()); //CURRENTLY USERNAME NOT EMAIL
            sp.setPassword(hash(pwText.getPassword())); 
            sp.setFirstName(fText.getText());
            sp.setLastName(lText.getText());
            sp.setQualification(qualText.getText());
            sp.setYearGraduated(Integer.valueOf(yearGradText.getText()));
            sp.setType(spType);
            sp.setPhoneNumber(Long.parseLong(phText.getText()));

            //need to do data validation

            db.insertSP(sp);
            JOptionPane.showMessageDialog(null, "New Service Provider created under username: " + unText.getText());
        
        }

        //go back to log-in page
        regWin.dispose();
        loadLogin(db);
    }

    private void selectAcctActionPerformed(ItemEvent e){
        if(e.getStateChange() == 1){
            selectedAcctType = (String)e.getItem();
            if(selectedAcctType.equals("User")){
                showUserRegister();
            }
            else if(selectedAcctType.equals("Service Provider")){
                showSPRegister();
            }
        }
    }

    private void radioButtonActionPerformed(ActionEvent e){
        if(e.getSource() == fitness){
            spType = "Fitness";
        }
        else if(e.getSource() == beauty){
            spType = "Beauty";
        }
        else if(e.getSource() == health){
            spType = "Health";
        }
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

