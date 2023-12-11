import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Database {
    private Connection connection;

    /* Open and close database connection */
    public void connect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/cs341?user=root&password=3871";
        connection = DriverManager.getConnection(url);
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    /* SQL query execution */
    public ResultSet executeSQL(String query) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        return stmt.executeQuery();
    }

    public void dbExecuteUpdate(String query) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.executeUpdate();
    }

    /* Inserts into database */

    public int insertUser(User user){
        // sql statement for user
        String sql = "INSERT INTO User(" +
                "FirstName, LastName, Email, Password, PhoneNum, Active)" +
                " VALUES (?, ?, ?, ?, ?, ?)";
        try{
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, user.getFirstName());
            stmt.setString(2, user.getLastName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPassword());
            stmt.setLong(5, user.getPhoneNumber());
            stmt.setInt(6, user.getActive());

            if(stmt.execute())
                // successfully inserted user
                System.out.println("Inserted " + user.getEmail() + " into User");
            return 0;
        }
        catch(SQLIntegrityConstraintViolationException e){
            // if foreign key (username) matches a preexisting username in the table, doesn't insert
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

    public int insertSP(ServiceProvider sp){
        // sql statement for service provider
        String sql = "INSERT INTO ServiceProvider(" +
                "FirstName, LastName, Email, Password, PhoneNum, Qualification, YearGraduated, Type, Active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
                // successfully inserted service provider
                System.out.println("Inserted " + sp.getEmail() + " into ServiceProvider");
            return 0;
        }
        catch(SQLIntegrityConstraintViolationException e){
            // if foreign key (username) matches a preexisting username in the table, doesn't insert
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

    public void insertAppt(Appointment appt){
        // sql statement
        String sql = "INSERT INTO Appointment(" +
                "Description, Date, Time, Type, Booked, UserEmail, SPEmail) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
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
                // successfully inserted appointment
                System.out.println("Inserted " + appt.getApptId() + " into Appointemnt");
            }
        }
        catch(SQLException e){
            System.out.println("Something went wrong.");
            e.printStackTrace();
        }
    }

    /* Deletes from database */

    public void deleteUser(String userEmail) {
        // updates Active to 0 to "soft delete"
        String sql = "UPDATE User SET Active = 0 WHERE Email = \"" + userEmail + "\"";
        try {
            dbExecuteUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Getters */

    public ResultSet getUserName(String email){
        // sql statement
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

    public ResultSet getSPName(String email) {
        // sql statement
        String sql = "SELECT FirstName, LastName FROM ServiceProvider WHERE Email = \"" + email + "\"";
        try {
            return executeSQL(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getSPEmail (String firstName, String lastName) {
        // sql statement
        String sql = "SELECT Email FROM ServiceProvider " +
                "WHERE FirstName = \"" + firstName + "\" AND LastName = \"" + lastName + "\"";
        try {
            return executeSQL(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getApptId(String date, String time, String email){
        try{
            // sql statement
            String sql = "Select ApptId FROM Appointment" +
                    " WHERE SPEmail = \"" +  email + "\" " +
                    " AND Time = \"" + time + "\"" +
                    " AND Date = \"" + date + "\";";
            ResultSet rs = executeSQL(sql);
            if(rs.next()){
                // successfully executed
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
            // sql statement
            String sql = "SELECT ApptId FROM Appointment" +
                    " WHERE UserEmail = \"" + email + "\"" +
                    " AND Time = \"" + time + "\"" +
                    " AND Date = \"" + date + "\";";
            ResultSet rs = executeSQL(sql);
            if (rs.next()) {
                // successfully executed
                return rs.getInt("ApptId");
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public ResultSet getUnbookedAppts(String type){
        ResultSet result = null;
        if(type.equals("")){
            return null;
        }
        // sql statement
        String sql = "SELECT * "+
                "FROM Appointment " +
                "WHERE Type = \"" + type  + "\" " +
                "AND Booked = 0 " +
                "AND Canceled = 0 " +
                "AND Date >= date(NOW());";
        try{
            result = executeSQL(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return result;
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
            result = executeSQL(sql);
        }
        catch(SQLException e){
            e.printStackTrace();
        }

        return result;
    }

    /* Checks if there are any new alerts */

    public boolean haveAlerts(ServiceProvider sp){
        // sql statement
        String sql = "SELECT Notified FROM Appointment WHERE SPEmail = \"" + sp.getEmail() + "\" AND Canceled = 1";
        try{
            ResultSet rs = executeSQL(sql);

            while(rs.next()){
                if(rs.getInt("Notified") == 0){
                    // if it's a new alert, return true
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean haveAlerts(User user){
        // sql statement
        String sql = "SELECT Notified FROM Appointment WHERE UserEmail = \"" + user.getEmail() + "\" AND Canceled = 1";
        try{
            ResultSet rs = executeSQL(sql);

            while(rs.next()){
                if(rs.getInt("Notified") == 0){
                    // if it's a new alert, return true
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /* Cancels appointment */

    public void cancelAppointment (int apptId) {
        // sql statement
        String sql = "UPDATE Appointment SET Canceled = 1 , Notified = 0 WHERE ApptId = \"" + apptId + "\"";
        try {
            dbExecuteUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelAppointment (String userEmail) {
        // sql statement
        String sql = "UPDATE Appointment SET Canceled = 1 , Notified = 0 WHERE UserEmail = \"" + userEmail + "\"";
        try {
            dbExecuteUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // checks for Service Provider appointment conflicts
    public int apptConflict(ServiceProvider sp, Appointment appt){
        // sql statement
        String sql = "SELECT * FROM Appointment WHERE SPEmail = \"" + sp.getEmail() + "\" "
                    + "AND Date = \"" + appt.getDate() + "\" "
                    + "AND Time = \"" + appt.getTime() + "\";";
        try{        
            ResultSet results = executeSQL(sql);
            if(results.next()){
                // if there is another appointment with the same date and time, return -1 indicating a conflict
                return -1;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    // checks for User appointment conflicts
    public int apptConflictUser(String email, String date, String time){
        // sql statement
        String sql = "SELECT * FROM Appointment WHERE UserEmail = \"" + email + "\" "
                    + "AND Date = \"" + date + "\" "
                    + "AND Time = \"" + time + "\";";
        try{
            ResultSet results = executeSQL(sql);
            if(results.next()){
                // if there is another appointment with the same data and time, return -1 indicating a conflict
                return 1;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    // Books appointment for User
    public void bookAppt(String email, int apptId){
        // sql statement
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

    // Gets new alerts for Service Provider and provides messages regarding cancellation
    public ArrayList<String> getNewAlerts(ServiceProvider sp){
        // sql statement
        String sql = "SELECT * FROM Appointment WHERE SPEmail = \"" + sp.getEmail() + "\" AND Canceled = '1'";
        ArrayList<String> notifs = new ArrayList<>();
        try{
            ResultSet rs = executeSQL(sql);

            while(rs.next()){
                if(rs.getInt("Notified") == 0){
                    // if the service provider has not been notified of the appointment change
                    String client = rs.getString("UserEmail");
                    String date = String.valueOf(rs.getDate("Date"));
                    String time = String.valueOf(rs.getTime("Time"));

                    // canceled by admin
                    if(client == null){
                        client = "Admin";
                        notifs.add(client + " has canceled the appointment on " + date + " at " + time);
                    }
                    // canceled by user
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

    // Gets new alerts for User and provides messages regarding cancellation
    public ArrayList<String> getNewAlerts(User user){
        // sql statement
        String sql = "SELECT * FROM Appointment WHERE UserEmail = \"" + user.getEmail() + "\" AND Canceled = '1'";
        ArrayList<String> notifs = new ArrayList<>();
        try{
            ResultSet rs = executeSQL(sql);

            while(rs.next()){
                if(rs.getInt("Notified") == 0){
                    // if the user has not been notified of this appointment change
                    String sp = rs.getString("SPEmail");
                    String date = String.valueOf(rs.getDate("Date"));
                    String time = String.valueOf(rs.getTime("Time"));
                    // canceled by admin
                    if(sp == null){
                        sp = "Admin";
                        notifs.add(sp + " has canceled the appointment on " + date + " at " + time);
                    }
                    // canceled by service provider
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

    // Gets past alerts for Service Provider and provides messages regarding cancellation
    public ArrayList<String> getPastAlerts(ServiceProvider sp){
        // sql statement
        String sql = "SELECT * FROM Appointment WHERE SPEmail = \"" + sp.getEmail() + "\" AND Canceled = '1'";
        ArrayList<String> notifs = new ArrayList<>();
        try{
            ResultSet rs = executeSQL(sql);

            while(rs.next()){
                if(rs.getInt("Notified") == 1){
                    // if the service provider has already been notified
                    String client = rs.getString("UserEmail");
                    String date = String.valueOf(rs.getDate("Date"));
                    String time = String.valueOf(rs.getTime("Time"));

                    // canceled by admin
                    if(client == null){
                        client = "Admin";
                        notifs.add(client + " has canceled the appointment on " + date + " at " + time);
                    }
                    // canceled by user
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

    // Gets past alerts for User and provides messages regarding cancellation
    public ArrayList<String> getPastAlerts(User user){
        // sql statement
        String sql = "SELECT * FROM Appointment WHERE UserEmail = \"" + user.getEmail() + "\" AND Canceled = 1";
        ArrayList<String> notifs = new ArrayList<>();
        try{
            ResultSet rs = executeSQL(sql);

            while(rs.next()){
                if(rs.getInt("Notified") == 1){
                    // if the user has already been notified
                    String sp = rs.getString("SPEmail");
                    String date = String.valueOf(rs.getDate("Date"));
                    String time = String.valueOf(rs.getTime("Time"));
                    // canceled by admin
                    if(sp == null){
                        sp = "Admin";
                        notifs.add(sp + " has canceled the appointment on " + date + " at " + time);
                    }
                    // canceled by service provider
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

    /* Updates new alerts to past alerts when once notified */

    public void sawNewAlerts(ServiceProvider sp){
        // sql statement
        String sql = "SELECT ApptId, Notified FROM Appointment WHERE SPEmail = \"" + sp.getEmail() + "\" AND Canceled = 1";
        try{
            
            ResultSet rs = executeSQL(sql);

            while(rs.next()){
                if(rs.getInt("Notified") == 0){
                    // if the service provider has not been notified
                    int apptId = rs.getInt("ApptId");
                    sawApptAlert(apptId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sawNewAlerts(User user){
        // sql statement
        String sql = "SELECT ApptId, Notified FROM Appointment WHERE UserEmail = \"" + user.getEmail() + "\" AND Canceled = 1";
        try{
            
            ResultSet rs = executeSQL(sql);

            while(rs.next()){
                if(rs.getInt("Notified") == 0){
                    // if the user has not been notified
                    int apptId = rs.getInt("ApptId");
                    sawApptAlert(apptId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // updates appointment notification by ApptId
    public void sawApptAlert(int apptId){
        // sql statement updates notified to 1
        String sql = "UPDATE Appointment SET Notified = 1 WHERE ApptId = \"" + apptId + "\"";
        try{
            dbExecuteUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*The below methods are for the purpose of log in validation. */

    public ResultSet findUser(String username, String password) {
        // sql statement
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
        // sql statement
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
        // sql statement
        String query = "SELECT * FROM Admin WHERE userID = ?";
            try{
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, userID);
                //stmt.setString(2, password);
                if(stmt.execute()){
                    return stmt.executeQuery();
                }
            }
            catch(SQLException e){
                e.printStackTrace();
            }
            return null;
     }
}
