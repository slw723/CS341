import java.sql.SQLException;


public class LogInPage {
    private static User user;
    private static Database db;

    public static void main(String[] args){
        
        user = new User();
        user.setFirstName("Name");

        db = new Database();
        try{
            db.connect();
            System.out.println("Successful connection!");
        }
        catch(SQLException e){
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }

        UserHomePage home = new UserHomePage(db, user);
        
    }

    public LogInPage(Database db, User user){
        
        //do log in stuff here

        //when log in button is pushed... call new UserHomePage(...)
    }
}
