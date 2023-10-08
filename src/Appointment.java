
import java.sql.*;

public class Appointment {
    private String apptId;
    private Date date;
    private Time time;
    private String type;
    private int booked;
    String fk_user_email;
    String fk_SP_email;

    public Appointment(){ //empty constructor

    }
    public Appointment(String id, Date date, Time time, String type, int booked, String fk_user_email, String fk_SP_email){
        apptId = id;
        this.date = date;
        this.time = time;
        this.type = type;
        this.booked = booked;
        this.fk_user_email = fk_user_email;
        this.fk_SP_email = fk_SP_email;
    }
    public String getApptId() {
        return apptId;
    }

    public void setApptId(String apptId) {
        this.apptId = apptId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate (Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime (Time time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public int getBooked() {
        return booked;
    }

    public void setBooked (int booked) {
        this.booked = booked;
    }

    public String getUserEmail() {
        return fk_user_email;
    }

    public void setUserEmail(String fk_user_email) {
        this.fk_user_email = fk_user_email;
    }

    public String getSPEmail() {
        return fk_SP_email;
    }

    public void setSPEmail(String fk_SP_email) {
        this.fk_SP_email = fk_SP_email;
    }

}
