
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

// Light Black: 31, 36, 33
// Dark Teal: 33, 104, 105
// Mint Green: 73, 160, 120
// Light Green: 156, 197, 161
// Off white: 220, 225, 222

public class UserHomePage {
        JFrame f, f2;
        JMenuBar mb;
        JMenuItem menu, home, makeAppt, history;
        JPanel p;
        JLabel hello;
        static Database db = new Database();

    public static void main(String[] args){
        // try{
        //     db.connect();
        //     System.out.println("Successful connection!");
        // }
        // catch(SQLException e){
        //     System.out.println("Something went wrong.");
        //     e.printStackTrace();
        // }
        // UserHomePage homePage = new UserHomePage(db);
        
    }

    public UserHomePage(Database db, User user){
        // default font
        Font defaultFont = UIManager.getFont("Label.font");

        /* Make frame */
        f = new JFrame("Appointment Booker");
        f.setBackground(new Color(220, 225, 222));

        /* Set up the menu bar */
        mb = new JMenuBar();
        menu = new JMenu("Menu");

        mb.add(menu);
        mb.setBackground(new Color(73, 160, 120));

        home = new JMenuItem("Home");
        makeAppt = new JMenuItem("Make Appointment");
        history = new JMenuItem("History");

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
        String str = "Hello, " + user.getFirstName() + "!";
        hello = new JLabel(str);
        hello.setFont(new Font(defaultFont.getFontName(), Font.PLAIN, 15));
        hello.setForeground(new Color(31, 36, 33));
        Dimension helloSize = hello.getPreferredSize();
        hello.setBounds(10, 0, helloSize.width, helloSize.height);
        p.add(hello);

        //panel sepecifications
        p.setLayout(null);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //add panel to frame
        f.add(p);

        /* Make visible */
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(800,800);
        f.setVisible(true);
    }

    public void setHomeVisible(){
        f.setVisible(true);
    }

     public void goHomeActionPerformed(ActionEvent e){
      
    }

    public void makeApptActionPerformed(ActionEvent e){
      
        f.setVisible(false);
        UserBookPage bp = new UserBookPage(db, this);        
    }

    public void historyActionPerformed(ActionEvent e){
      
        f.setVisible(false);
            
    }

    /* Populate full table view -> good for admin view*/
    // private void populateBuysTable() {
    //     try{
    //         String sql = "SELECT * FROM Buys";
    //         ResultSet rs = db.runQuery(sql);
    //         DefaultTableModel tblModel = (DefaultTableModel)Buys.getModel();
    //         tblModel.setRowCount(0);
    //         while(rs.next()){
    //             //data will be added until finished
    //             String paymentType = rs.getString("PaymentType");
    //             String receiptNumber = String.valueOf(rs.getInt("ReceiptNumber"));
    //             String totalCost = String.valueOf(rs.getInt("TotalCost"));
    //             String quantity = String.valueOf(rs.getInt("Quantity"));
    //             String size = String.valueOf(rs.getInt("Size"));
    //             String color = rs.getString("Color");
    //             String customerId = String.valueOf(rs.getInt("CustomerId"));
    //             String modelId = rs.getString("ModelId");

    //             String tbData[] = {paymentType, receiptNumber, totalCost, quantity,
    //                 size, color, customerId, modelId};

    //             //addstring array into jtable
    //             tblModel.addRow(tbData);
    //         }
    //     }
    //     catch(Exception e){
    //         System.out.println(e.getMessage());
    //     }
        
    // }   
}
