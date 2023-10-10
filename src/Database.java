// package src;

import java.sql.*;

import javax.swing.JPopupMenu.Separator;

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

    public ResultSet executeSQL(String query) throws SQLException {
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
        String sql = "INSERT INTO Appointment(Description, Date, Time, Type, Booked, UserEmail, SPEmail) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, appt.getDescription());
            stmt.setDate(2, appt.getDate());
            stmt.setTime(3, appt.getTime());
            stmt.setString(4, appt.getType());
            stmt.setInt(5, appt.getBooked());
            stmt.setString(6, appt.getUserEmail());
            stmt.setString(7, appt.getSPEmail());

            if(stmt.execute()){
                ResultSet rs = stmt.getGeneratedKeys();
                appt.setApptId(rs.getInt(1));
                System.out.println("Inserted " + appt.getApptId() + " into Appointemnt");
            }
        }
        catch(SQLException e){
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }
    }

    public void updateAppt(Appointment appt){
        String sql = "UPDATE Appointment SET "
                    + "Date = \"" + appt.getDate() + "\","
                    + "Description \"" + appt.getDescription() + "\","
                    + "Time = \"" + appt.getTime() + "\","
                    + "Type = \"" + appt.getType() + "\","
                    + "Booked = " + appt.getBooked() + ""
                    + "UserEmail = \"" + appt.getUserEmail() + "\""
                    + "SPEmail = \"" + appt.getSPEmail() + "\""
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

    public int apptConflict(ServiceProvider sp, Appointment appt){
        String sql = "SELECT * FROM Appointment WHERE SPEmail = \"" + sp.getEmail() + "\" "
                    + "AND Date = \"" + appt.getDate() + "\" "
                    + "AND Time = \"" + appt.getTime() + "\";";
        try{
            ResultSet results = runQuery(sql);
            if(results.getFetchSize() > 0){
                return -1;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public void bookAppt(String email, int apptId){
        String sql = "UPDATE Appointment SET  "
                    + "SPEmail = \"" + email + "\", "
                    + "Booked = 1 " 
                    + "WHERE ApptId = \"" + apptId + "\";";

        System.out.println(email);
        System.out.println(apptId);
        try {
            dbExecuteUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getApptType(String type){
        ResultSet result = null;
        if(type.equals("")){
            return null;
        }
        String sql = "SELECT * "+
                    "FROM Appointment " +
                    "WHERE Type = \"" + type  + "\" " +
                    "AND Booked = 0;";
        try{
           result = runQuery(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
        }  

        return result;
    }

    /*The below methods are for the purpose of log in validation. DEMO #1 asks for just names, not emails, keep the column name the same or
     * change to username for now?*/
    public ResultSet findUser(String username, String password) throws SQLException {
        String query = "SELECT * FROM User WHERE Email=? and Password=?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet results = stmt.executeQuery();
        return results;
    }

    public ResultSet findServiceProvider(String username, String password) throws SQLException {
        String query = "SELECT * FROM ServiceProvider WHERE Email=? and Password=?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet results = stmt.executeQuery();
        return results;
    }

    public ResultSet findAdmin(String userID, String password) throws SQLException {
        String query = "SELECT * FROM ServiceProvider WHERE UserId=? and Password=?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, userID);
        stmt.setString(2, password);
        ResultSet results = stmt.executeQuery();
        return results;
    }


}
