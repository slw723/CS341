public class Appointment {
    @Id
    private String apptId;
    @NotEmpty
    private LocalDate date;
    @NotEmpty
    private LocalTime time;

    @NotEmpty
    private LocalTime duration;

    private String type;

    @NotEmpty
    private Boolean booked;

    public Appointment(){ //empty constructor

    }
    public Appointment(String id, LocalDate date, LocalTime time, LocalTime duration, String type, Boolean booked){
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate (LocalDate date) {
        this.date = date;
    }


    public LocalTime getTime() {
        return time;
    }

    public void setTime (LocalTime time) {
        this.time = time;
    }

    public LocalTime getDuration() {
        return duration;
    }

    public void setDuration (LocalTime duration) {
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
