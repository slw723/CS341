public class Admin {
    @Id
    private String userId;

    @NotEmpty
    private String password;


    public Admin(String id, String pw){
        userId = id;
        password = pw;
    }

    public Admin() { //empty constructor

    }
}
