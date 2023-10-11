// package src;/* NOT NECESSARY LONG TERM */

// import src.Appointment;
// import src.Database;
// import src.ServiceProvider;
// import src.User;

import java.sql.Date;
import java.sql.PreparedStatement;
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
        
        /*insert into SP table using connection */
        //String qualif = "Graduated from The Salon Professonal Academy";
        //ServiceProvider sp = new ServiceProvider("Abby", "Anderson",
        //                            "abbyanderson@gmail.com", null,
        //                            6081113333L, qualif, 2015, "Beauty");
        // db.insertSP(sp);

        //User user = new User("First", "Last",
        //                    "user@user.com", null, 8889993333L);
        // db.insertUser(user);

        /* Try insert into appt */
        //Appointment appt = new Appointment( "Haircut", new Date(20231223), new Time(1200), "Beauty", 0, null, "abbyanderson@gmail.com");
        // db.insertAppt(appt);

        db.deleteAppointment(1);
        db.deleteServiceProvider("AbbyAndersen");
        db.deleteuser("JaneDoe");
    }
}
