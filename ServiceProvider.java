import java.sql.*;

public class ServiceProvider {
    
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private int phoneNumber;
    private String qualification;
    private int yearGraduated;
    private Time startTime;
    private Time stopTime;
    private String[] days;

    public ServiceProvider (String first, String last, String emailAddress, String pw, int phone, String qualif, int year, Time start, Time stop, String[] weekDays) {
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

    public void setOfficeHours(Time start, Time stop, String[] days){
        startTime = start;
        stopTime = stop;
        this.days = days;
    }

    public Time getOfficeHoursStart(){
        return startTime;
    }
    public Time getOfficeHoursStop(){
        return stopTime;
    }
    public String[] getOfficeHoursDays(){
        return days;
    }
}
