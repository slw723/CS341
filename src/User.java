public class User extends Appointment {
    private String firstName;
    private String lastName;
    private String email;
    private byte[] password;
    private long phoneNumber;

    public User (String first, String last, String emailAddress, byte[] pw, long phone) {
        firstName = first;
        lastName = last;
        email = emailAddress;
        password = pw;
        phoneNumber = phone;
    }

    public User(){
        
    }

    public User(String emailAdress, byte[] pw){
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

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(byte[] pw) {
        password = pw;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phone) {
        phoneNumber = phone;
    }
}
