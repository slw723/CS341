public class User extends Appointment {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private long phoneNumber;
    private int active;

    // constructor
    public User (String first, String last, String emailAddress, String pw, long phone, int active) {
        firstName = first;
        lastName = last;
        email = emailAddress;
        password = pw;
        phoneNumber = phone;
        this.active = active;
    }

    public User(){
        
    }

    public User(String emailAdress, String pw){
        email = emailAdress;
        password = pw;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String first) {
        firstName = first;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String last) {
        lastName = last;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String emailAddress) {
        email = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pw) {
        password = pw;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phone) {
        phoneNumber = phone;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
