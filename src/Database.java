import java.sql.*;

public class Database {

    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/?user=root";


    public void connect() throws SQLException {
        connection = DriverManager.getConnection(url);
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    public ResultSet runQuery(String query) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        return stmt.executeQuery();
    }

    public void dbExecuteUpdate(String query) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.executeUpdate();
    }

    //put INSERTs, UPDATEs, DELETEs, and SELECTs here

    public void insertUser(User user){
        String sql = "INSERT INTO User(FirstName, LastName, Email, Password, PhoneNum) VALUES (?, ?, ?, ?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setInt(5, user.getPhoneNumber());

            if(stmt.execute())
                System.out.println("Inserted " + user.getEmail() + " into User");
        }
        catch(SQLException e){
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }
    }

    public void deleteUser(User user){
        String sql = "DELETE FROM User WHERE Email = \"" + user.getEmail() + "\"";
        try{
            dbExecuteUpdate(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void insertSP(ServiceProvider sp){
        String sql = "INSERT INTO ServiceProvider(FirstName, LastName, Email, Password, PhoneNum, StartTime, StopTime, Qualification, YearGraduated, Type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, sp.getFirstName());
            stmt.setString(2, sp.getLastName());
            stmt.setString(3, sp.getEmail());
            stmt.setString(4, sp.getPassword());
            stmt.setInt(5, sp.getPhoneNumber());
            stmt.setInt(6, sp.getOfficeHoursStart());
            stmt.setInt(7, sp.getOfficeHoursStop());
            stmt.setString(8, sp.getQualifcation());
            stmt.setInt(9, sp.getYearGraduated());
            stmt.setString(10, sp.getType());

            if(stmt.execute())
                System.out.println("Inserted " + sp.getEmail() + " into ServiceProvider");
        }
        catch(SQLException e){
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }
    }

    public void deleteSP(ServiceProvider sp){
        String sql = "DELETE FROM ServiceProvider WHERE Email = \"" + sp.getEmail() + "\"";
        try{
            dbExecuteUpdate(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void insertAppt(Appointment appt){
        String sql = "INSERT INTO Appointment(ApptId, Date, Time, Type, Booked) VALUES (?, ?, ?, ?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, appt.getApptId());
            stmt.setDate(2, appt.getDate());
            stmt.setTime(3, appt.getTime());
            stmt.setString(4, appt.getType());
            stmt.setInt(5, appt.getBooked());

            if(stmt.execute())
                System.out.println("Inserted " + appt.getApptId() + " into Appointment");
        }
        catch(SQLException e){
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }
    }

    public void updateAppt(Appointment appt){
        String sql = "UPDATE Brand SET "
                    + "Date = \"" + appt.getDate() + "\","
                    + "Time = \"" + appt.getTime() + "\","
                    + "Type = \"" + appt.getType() + "\","
                    + "Booked = \"" + appt.getBooked() + "\""
                    + "WHERE ApptId = \"" + appt.getApptId() + "\"";
        try {
            dbExecuteUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAppt(Appointment appt){
        String sql = "DELETE FROM Appointment WHERE ApptId = \"" + appt.getApptId() + "\"";
        try{
            dbExecuteUpdate(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
