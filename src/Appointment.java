import java.sql.*;

public class Appointment {
    private int apptId;
    private String description;
    private Date date;
    private Time time;
    private String type;
    private int booked;
    private int canceled;
    String UserEmail;
    String SPEmail;

    public Appointment(){ //empty constructor

    }
    public Appointment(String des, Date date, Time time, String type, int booked, String UserEmail, String SPEmail){
        description = des;
        this.date = date;
        this.time = time;
        this.type = type;
        this.booked = booked;
        canceled = 0;
        this.UserEmail = UserEmail;
        this.SPEmail = SPEmail;
    }
    public int getApptId() {
        return apptId;
    }

    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String des) {
        description = des;
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

    public int getCanceled() {
        return booked;
    }

    public void setCanceled (int cancel) {
        canceled = cancel;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String fk_user_email) {
        this.UserEmail = fk_user_email;
    }

    public String getSPEmail() {
        return SPEmail;
    }

    public void setSPEmail(String fk_SP_email) {
        this.SPEmail = fk_SP_email;
    }

}
