public class Admin {

    private String userId;
    private String password;


    public Admin(String id, String pw){
        userId = id;
        password = pw;
    }

    public Admin() { //empty constructor

    }
    public void setUserId(String id) {userId = id;}
    public String getID(){
        return userId;
    }

    public void setPassword(String pw){
        password = pw;
    }

    public String getPassword(){
        return password;
    }
}
