// package src;

public class Admin {

    private String userId;
    private byte[] password;


    public Admin(String id, byte[] pw){
        userId = id;
        password = pw;
    }

    public Admin() { //empty constructor

    }
    public void setUserId(String id) {userId = id;}
    public String getID(){
        return userId;
    }

    public void setPassword(byte[] pw){
        password = pw;
    }

    public byte[] getPassword(){
        return password;
    }
}
