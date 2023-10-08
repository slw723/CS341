/* NOT NECESSARY LONG TERM */

import java.sql.Date;
import java.sql.SQLException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.sql.Time;
//import java.util.HexFormat;

public class Driver {
    Database db = new Database();
    
    public static void main(String[] args){

        new Driver();
    }

    public Driver(){
        try{
            db.connect();
            System.out.println("Successful connection!");
        }
        catch(SQLException e){
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }

        try{
            /*hashing a password*/
            String passwordPlainText = "1234password";
            //introduce the salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            //configure SHA-512
            
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            //generate hashed password
            byte[] pwd = md.digest(passwordPlainText.getBytes(StandardCharsets.UTF_8));
            
            /*insert into SP table using connection */
            String qualif = "Graduated from The Salon Professonal Academy";
            ServiceProvider sp = new ServiceProvider("Abby", "Anderson", 
                                        "abbyanderson@gmail.com", pwd, 
                                        6081113333L, qualif, 2015, "Beauty");
            // db.insertSP(sp);

            User user = new User("First", "Last", 
                                "user@user.com", pwd, 8889993333L);
            // db.insertUser(user);


            /* Try insert into appt */
            Appointment appt = new Appointment("124", "Haircut", new Date(20231223), new Time(1200), "Beauty", 0, null, "abbyanderson@gmail.com");
            // db.insertAppt(appt);
        }
        catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }
}
