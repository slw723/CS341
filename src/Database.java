import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

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

    public int insertUser(User user){
        String sql = "INSERT INTO User(FirstName, LastName, Email, Password, PhoneNum, Active) VALUES (?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setLong(5, user.getPhoneNumber());
            stmt.setInt(6, user.getActive());

            if(stmt.execute())
                System.out.println("Inserted " + user.getEmail() + " into User");
            return 0;
        }
        catch(SQLIntegrityConstraintViolationException e){
            JOptionPane.showMessageDialog(null, 
                                "Username already in use. Please choose another.");
            e.printStackTrace();
            return -1;
        }
        catch(SQLException e){
            System.out.println("Something went wrong.");
            e.printStackTrace();
            return -1;
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

    public void deleteuser (String email) {
        String sql = "DELETE FROM User WHERE Email = \"" + email + "\"";
        try {
            dbExecuteUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getUserName(String email){
        String sql = "SELECT FirstName, LastName FROM User WHERE Email = ?";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);

            return stmt.executeQuery();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public int insertSP(ServiceProvider sp){
        String sql = "INSERT INTO ServiceProvider(FirstName, LastName, Email, Password, PhoneNum, Qualification, YearGraduated, Type, Active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, sp.getFirstName());
            stmt.setString(2, sp.getLastName());
            stmt.setString(3, sp.getEmail());
            stmt.setString(4, sp.getPassword());
            stmt.setLong(5, sp.getPhoneNumber());
            stmt.setString(6, sp.getQualifcation());
            stmt.setInt(7, sp.getYearGraduated());
            stmt.setString(8, sp.getType());
            stmt.setInt(9, sp.getActive());

            if(stmt.execute())
                System.out.println("Inserted " + sp.getEmail() + " into ServiceProvider");
            return 0;
        }
        catch(SQLIntegrityConstraintViolationException e){
            JOptionPane.showMessageDialog(null, 
                                "Username already in use. Please choose another.");
            e.printStackTrace();
            return -1;
        }
        catch(SQLException e){
            System.out.println("Something went wrong.");
            e.printStackTrace();
            return -1;
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

    public void deleteServiceProvider(String email) {
        String sql = "DELETE FROM ServiceProvider WHERE Email = \"" + email + "\"";
        try {
            dbExecuteUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getSPName(String email) {
        String sql = "SELECT FirstName, LastName FROM ServiceProvider WHERE Email = \"" + email + "\"";
        try {
            return runQuery(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getSPEmail (String firstName, String lastName) {
        String sql = "SELECT Email FROM ServiceProvider WHERE FirstName = \"" + firstName + "\" AND LastName = \"" + lastName + "\"";
        try {
            return runQuery(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertAdmin(Admin admin){
        String sql = "INSERT INTO Admin(UserId, Password) VALUES (?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, admin.getID());
            stmt.setString(2, admin.getPassword());

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
                    + "Description = \"" + appt.getDescription() + "\","
                    + "Time = \"" + appt.getTime() + "\","
                    + "Type = \"" + appt.getType() + "\","
                    + "Booked = " + appt.getBooked() + "\","
                    + "UserEmail = \"" + appt.getUserEmail() + "\","
                    + "SPEmail = \"" + appt.getSPEmail() + "\""
                    + "WHERE ApptId = \"" + appt.getApptId() + "\"";
        try {
            dbExecuteUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean haveAlerts(ServiceProvider sp){
        String sql = "SELECT Notified FROM Appointment WHERE SPEmail = \"" + sp.getEmail() + "\" AND Canceled = 1";
        try{
            ResultSet rs = runQuery(sql);

            while(rs.next()){
                if(rs.getInt("Notified") == 0){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean haveAlerts(User user){
        String sql = "SELECT Notified FROM Appointment WHERE UserEmail = \"" + user.getEmail() + "\" AND Canceled = 1";
        try{
            ResultSet rs = runQuery(sql);

            while(rs.next()){
                if(rs.getInt("Notified") == 0){
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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

    public void deleteAppointment (int apptId) {
        String sql = "DELETE FROM Appointment WHERE ApptId = \"" + apptId + "\"";
        try {
            dbExecuteUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelAppointment (int apptId) {
        String sql = "UPDATE Appointment SET Canceled = 1 , Notified = 0 WHERE ApptId = \"" + apptId + "\"";
        try {
            dbExecuteUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelAppointment (String userEmail) {
        String sql = "UPDATE Appointment SET Canceled = 1 , Notified = 0 WHERE UserEmail = \"" + userEmail + "\"";
        try {
            dbExecuteUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCanceled(int apptId){
        String sql = "SELECT Canceled FROM Appointment WHERE ApptID = \'" + apptId + "\';";
        try{
            ResultSet rs = runQuery(sql);
            if(rs.next()){
                return rs.getInt(1);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int apptConflict(ServiceProvider sp, Appointment appt){
        String sql = "SELECT * FROM Appointment WHERE SPEmail = \"" + sp.getEmail() + "\" "
                    + "AND Date = \"" + appt.getDate() + "\" "
                    + "AND Time = \"" + appt.getTime() + "\";";
        try{        
            ResultSet results = runQuery(sql);
            if(results.next()){
                return -1;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public int apptConflictUser(String email, String date, String time){
        String sql = "SELECT * FROM Appointment WHERE UserEmail = \"" + email + "\" "
                    + "AND Date = \"" + date + "\" "
                    + "AND Time = \"" + time + "\";";
        try{
            ResultSet results = runQuery(sql);
            if(results.next()){
                return 1;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    public int getApptId(String date, String time, String email){
        try{
           String sql = "Select ApptId FROM Appointment" +
                        " WHERE SPEmail = \"" +  email + "\" " +
                        " AND Time = \"" + time + "\"" +
                        " AND Date = \"" + date + "\";";
           ResultSet rs = runQuery(sql);
           if(rs.next()){
               return rs.getInt("ApptId");
           }
       }
       catch(SQLException e){
           System.out.println(e.getMessage());

       }
       return -1;
   }

   public int getApptIdUser(String date, String time, String email) {
        try {
            String sql = "SELECT ApptId FROM Appointment" +
                         " WHERE UserEmail = \"" + email + "\"" +
                         " AND Time = \"" + time + "\"" +
                         " AND Date = \"" + date + "\";";
            ResultSet rs = runQuery(sql);
            if (rs.next()) {
                return rs.getInt("ApptId");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
   }

    public void bookAppt(String email, int apptId){
        String sql = "UPDATE Appointment SET "
                    + "UserEmail = ?, "
                    + "Booked = 1 " 
                    + "WHERE ApptId = ?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setInt(2, apptId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getUnbookedAppts(String type){
        ResultSet result = null;
        if(type.equals("")){
            return null;
        }
        String sql = "SELECT * "+
                    "FROM Appointment " +
                    "WHERE Type = \"" + type  + "\" " +
                    "AND Booked = 0 " +
                    "AND Canceled = 0 " +
                    "AND Date >= date(NOW());";
        try{
           result = runQuery(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
        }  

        return result;
    }

    public ResultSet getApptSP (String SPEmail) {
        ResultSet result = null;
        if (SPEmail.equals("")) {
            return null;
        }
        String sql = "SELECT * FROM Appointment WHERE SPEmail = \"" + SPEmail + "\" AND Booked = 0";
        try {
            result = runQuery(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<String> getNewAlerts(ServiceProvider sp){
        String sql = "SELECT * FROM Appointment WHERE SPEmail = \"" + sp.getEmail() + "\" AND Canceled = '1'";
        ArrayList<String> notifs = new ArrayList<>();
        try{
            ResultSet rs = runQuery(sql);

            while(rs.next()){
                if(rs.getInt("Notified") == 0){
                    String client = rs.getString("UserEmail");
                    String date = String.valueOf(rs.getDate("Date"));
                    String time = String.valueOf(rs.getTime("Time"));

                    if(client == null){
                        client = "Admin";
                        notifs.add(client + " has canceled the appointment on " + date + " at " + time);
                    }
                    else{
                        ResultSet username = getUserName(client);
                        if(username.next()){
                            String clientName = username.getString("FirstName") 
                                        + " " + username.getString("LastName");
                            notifs.add(clientName + " has canceled the appointment on " + date + " at " + time);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifs;
    }

    public ArrayList<String> getNewAlerts(User user){
        String sql = "SELECT * FROM Appointment WHERE UserEmail = \"" + user.getEmail() + "\" AND Canceled = '1'";
        ArrayList<String> notifs = new ArrayList<>();
        try{
            ResultSet rs = runQuery(sql);

            while(rs.next()){
                if(rs.getInt("Notified") == 0){
                    String sp = rs.getString("SPEmail");
                    String date = String.valueOf(rs.getDate("Date"));
                    String time = String.valueOf(rs.getTime("Time"));
                    if(sp == null){
                        sp = "Admin";
                        notifs.add(sp + " has canceled the appointment on " + date + " at " + time);
                    }
                    else{
                        ResultSet username = getSPName(sp);
                        if(username.next()){
                            String spName = username.getString("FirstName") 
                                        + " " + username.getString("LastName");
                            notifs.add(spName + " has canceled the appointment on " + date + " at " + time);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifs;
    }

    public ArrayList<String> getPastAlerts(ServiceProvider sp){
        String sql = "SELECT * FROM Appointment WHERE SPEmail = \"" + sp.getEmail() + "\" AND Canceled = '1'";
        ArrayList<String> notifs = new ArrayList<>();
        try{
            ResultSet rs = runQuery(sql);

            while(rs.next()){
                if(rs.getInt("Notified") == 1){
                    String client = rs.getString("UserEmail");
                    String date = String.valueOf(rs.getDate("Date"));
                    String time = String.valueOf(rs.getTime("Time"));

                    if(client == null){
                        client = "Admin";
                        notifs.add(client + " has canceled the appointment on " + date + " at " + time);
                    }
                    else{
                        ResultSet username = getUserName(client);
                        if(username.next()){
                            String clientName = username.getString("FirstName") 
                                            + " " + username.getString("LastName");
                            notifs.add(clientName + " has canceled the appointment on " + date + " at " + time);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifs;
    }

    public ArrayList<String> getPastAlerts(User user){
        String sql = "SELECT * FROM Appointment WHERE UserEmail = \"" + user.getEmail() + "\" AND Canceled = 1";
        ArrayList<String> notifs = new ArrayList<>();
        try{
            ResultSet rs = runQuery(sql);

            while(rs.next()){
                if(rs.getInt("Notified") == 1){
                    String sp = rs.getString("SPEmail");
                    String date = String.valueOf(rs.getDate("Date"));
                    String time = String.valueOf(rs.getTime("Time"));
                    if(sp == null){
                        sp = "Admin";
                        notifs.add(sp + " has canceled the appointment on " + date + " at " + time);
                    }
                    else{
                        ResultSet username = getSPName(sp);
                        if(username.next()){
                            String spName = username.getString("FirstName") 
                                            + " " + username.getString("LastName");
                            notifs.add(spName + " has canceled the appointment on " + date + " at " + time);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifs;
    }


    public void sawNewAlerts(ServiceProvider sp){
        String sql = "SELECT ApptId, Notified FROM Appointment WHERE SPEmail = \"" + sp.getEmail() + "\" AND Canceled = 1";
        try{
            
            ResultSet rs = runQuery(sql);

            while(rs.next()){
                if(rs.getInt("Notified") == 0){
                    int apptId = rs.getInt("ApptId");
                    sawApptAlert(apptId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sawNewAlerts(User user){
        String sql = "SELECT ApptId, Notified FROM Appointment WHERE UserEmail = \"" + user.getEmail() + "\" AND Canceled = 1";
        try{
            
            ResultSet rs = runQuery(sql);

            while(rs.next()){
                if(rs.getInt("Notified") == 0){
                    int apptId = rs.getInt("ApptId");
                    sawApptAlert(apptId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sawApptAlert(int apptId){
        String sql = "UPDATE Appointment SET Notified = 1 WHERE ApptId = \"" + apptId + "\"";
        try{
            dbExecuteUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }


    /*The below methods are for the purpose of log in validation. */
    public ResultSet findUser(String username, String password) {
        String query = "SELECT * FROM User WHERE Email = ? AND Active = 1 AND Password = ?";
        try{
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, password);
        return stmt.executeQuery();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet findServiceProvider(String username, String password) {
        String query = "SELECT * FROM serviceprovider WHERE Email = ? AND Password = ?";
        try{
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            if(stmt.execute()){
                return stmt.executeQuery();
            }
        }
        
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet findAdmin(String userID, String password) throws SQLException {
        String query = "SELECT * FROM Admin WHERE userID = ? AND Password = ?";
            try{
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, userID);
                stmt.setString(2, password);
                if(stmt.execute()){
                    return stmt.executeQuery();
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
            return null;
     }

    public void deleteUser(String userEmail) {
        String sql = "UPDATE User SET Active = 0 WHERE Email = \"" + userEmail + "\"";
        try {
            dbExecuteUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
