import java.sql.*;

public class Appointment {
    private String apptId;
    private Date date;
    private Time time;
    private String type;
    private int booked;

    public Appointment(){ //empty constructor

    }
    public Appointment(String id, Date date, Time time, String type, int booked){
        apptId = id;
        this.date = date;
        this.time = time;
        this.type = type;
        this.booked = booked;
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

}
