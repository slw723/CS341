import java.sql.*;

public class Appointment {
    private String apptId;
    private Date date;
    private Time time;
    private Time duration;
    private String type;
    private Boolean booked;

    public Appointment(){ //empty constructor

    }
    public Appointment(String id, Date date, Time time, Time duration, String type, Boolean booked){
        apptId = id;
        this.date = date;
        this.time = time;
        this.duration = duration;
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

    public Time getDuration() {
        return duration;
    }

    public void setDuration (Time duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public Boolean getBooked() {
        return booked;
    }

    public void setBooked (Boolean booked) {
        this.booked = booked;
    }

}
