package src;

import java.sql.*;

public class Database {

    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/cs341?user=root&password=3871";


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
            stmt.setBytes(4, user.getPassword());
            stmt.setLong(5, user.getPhoneNumber());

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
        String sql = "INSERT INTO ServiceProvider(FirstName, LastName, Email, Password, PhoneNum, Qualification, YearGraduated, Type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, sp.getFirstName());
            stmt.setString(2, sp.getLastName());
            stmt.setString(3, sp.getEmail());
            stmt.setBytes(4, sp.getPassword());
            stmt.setLong(5, sp.getPhoneNumber());
            stmt.setString(6, sp.getQualifcation());
            stmt.setInt(7, sp.getYearGraduated());
            stmt.setString(8, sp.getType());

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


        public void insertAdmin(Admin admin){
        String sql = "INSERT INTO Admin(UserId, Password) VALUES (?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, admin.getID());
            stmt.setBytes(2, admin.getPassword());

            if(stmt.execute())
                System.out.println("Inserted " + admin.getID() + " into ServiceProvider");
        }
        catch(SQLException e){
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }
    }

    public void deleteAdmin(ServiceProvider sp){
        String sql = "DELETE FROM ServiceProvider WHERE Email = \"" + sp.getEmail() + "\"";
        try{
            dbExecuteUpdate(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void insertAppt(Appointment appt){
        String sql = "INSERT INTO Appointment(ApptId, Date, Time, Type, Booked, FK_User_email, FK_SP_email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, appt.getApptId());
            stmt.setDate(2, appt.getDate());
            stmt.setTime(3, appt.getTime());
            stmt.setString(4, appt.getType());
            stmt.setInt(5, appt.getBooked());
            stmt.setString(6, appt.getFk_user_email());
            stmt.setString(7, appt.getFk_SP_email());

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
                    + "FK_User_email = \"" + appt.getFk_user_email() + "\""
                    + "FK_SP_email = \"" + appt.getFk_SP_email() + "\""
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
