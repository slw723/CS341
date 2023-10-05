import java.sql.SQLException;
import java.nio.charset.StandardCharsets;
import java.security.*;

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

            db.insertSP(sp);
            System.out.println("Inserted " + sp.getEmail());
        }
        catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }
}
