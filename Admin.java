public class Admin {

    private String userId;
    private String password;


    public Admin(String id, String pw){
        userId = id;
        password = pw;
    }

    public Admin() { //empty constructor

    }

    public String getID(){
        return userId;
    }
}
