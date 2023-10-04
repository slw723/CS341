public class ServiceProvider {
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @Email
    @NotEmpty
    private String email;
    private String password;
    private int phoneNumber;
    private String qualification;
    private int yearGraduated;
    private int startTime;
    private int stopTime;
    private String[] days;

    public ServiceProvider (String first, String last, String emailAddress, String pw, int phone, String qualif, int year, int start, int stop, String[] weekDays) {
        firstName = first;
        lastName = last;
        email = emailAddress;
        password = pw;
        phoneNumber = phone;
        qualification = qualif;
        yearGraduated = year;
        startTime = start;
        stopTime = stop;
        days = weekDays;
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

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phone) {
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

    public void setOfficeHours(int start, int stop, String[] days){
        startTime = start;
        stopTime = stop;
        this.days = days;
    }

    public int getOfficeHoursStart(){
        return startTime;
    }
    public int getOfficeHoursStop(){
        return stopTime;
    }
    public String[] getOfficeHoursDays(){
        return days;
    }
}
