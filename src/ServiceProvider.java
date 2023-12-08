public class ServiceProvider {
    
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private long phoneNumber;
    private String qualification;
    private int yearGraduated;
    private String type;
    private int active;

    // constructor
    public ServiceProvider (String first, String last, String emailAddress, String pw, long phone, String qualif, int year, String type, int active) {
        firstName = first;
        lastName = last;
        email = emailAddress;
        password = pw;
        phoneNumber = phone;
        qualification = qualif;
        yearGraduated = year;
        this.type = type;
        this.active = active;
    }

    // constructor
    public ServiceProvider() {

    }

    // constructor
    public ServiceProvider(String emailAddress, String pw){
        email = emailAddress;
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

    public void setQualification(String qualif){
        qualification = qualif;
    }

    public String getQualifcation(){
        return qualification;
    }

    public void setYearGraduated(int year){
        yearGraduated = year;
    }

    public int getYearGraduated(){
        return yearGraduated;
    }
    
    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getActive() {
        return active;
    }
}
