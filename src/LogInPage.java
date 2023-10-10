import java.sql.SQLException;


public class LogInPage {
    private static User user;
    private static ServiceProvider sp;
    private static Database db;

    public static void main(String[] args){
        
        user = new User();
        user.setFirstName("Name");

        sp = new ServiceProvider();
        sp.setFirstName("ServiceProvider");
        sp.setType("Beauty");
        sp.setEmail("abbyanderson@gmail.com");

        db = new Database();
        try{
            db.connect();
            System.out.println("Successful connection!");
            // UserHomePage home = new UserHomePage(db, user);
            SPHomePage home = new SPHomePage(db, sp);
        }
        catch(SQLException e){
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }

        
        
    }

    public LogInPage(Database db, User user){
        
        //do log in stuff here

        //when log in button is pushed... call new UserHomePage(...)
    }
}
