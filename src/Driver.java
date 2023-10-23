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
        
       
    }
}
