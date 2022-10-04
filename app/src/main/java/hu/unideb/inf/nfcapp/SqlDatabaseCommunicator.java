package hu.unideb.inf.nfcapp;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlDatabaseCommunicator implements Communicator {

    private final String _ipAddress = "172.16.1.6";
    //private final String _ipAddress = "192.168.1.69";
    private final String _portNumber = "1433";
    private final String _databaseName = "test";
    private final String _userId = "sa";
    private final String _password = "0207";

    private Connection connection;
    private String request;
    private Statement stmt;
    private ResultSet rs;
    private int size;
    private String query;
    private List<MyLog> myLogs;
    private MyLog myLog;
    private String date;

    private StringBuilder sb;



    @Override
    public LoginTypeEnum loginUser(String username, String password) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String connectionURL = null;

        try {

            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            sb = new StringBuilder();

            connectionURL = sb.append("jdbc:jtds:sqlserver://").append(_ipAddress).append(":").append(_portNumber).append("/")
                            .append(_databaseName).toString();

            connection = DriverManager.getConnection(connectionURL, _userId, _password);

            try{
                if(connection != null){
                    query = "SELECT [Id], [Name], [Address], [Account] FROM Users WHERE Account = '" + username + "' and Password = '" + password + "'";
                    stmt = connection.createStatement();
                    rs = stmt.executeQuery(query);

                    size = 0;

                    while (rs.next()){
                        User._id = rs.getInt(1);
                        User._name = rs.getString(2);
                        User._address = rs.getString(3);
                        User._account = rs.getString(4);
                        size++;
                    }

                    if(size == 0){
                        return LoginTypeEnum.LOGIN_FAILED;
                    }
                }

            }catch (SQLException e){
                return LoginTypeEnum.SQL_READING_FAILED;
            }

            connection.close();
        }
        catch (Exception e){
            return LoginTypeEnum.SQL_CONNECTION_FAILED;
        }

        return LoginTypeEnum.LOGIN_ACCESS;
    }

    @Override
    public List<MyLog> getLogbyDate(int year, int mounth, int dayOfMounth) {
        myLogs = new ArrayList<>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String connectionURL = null;

        try {

            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            sb = new StringBuilder();

            connectionURL = sb.append("jdbc:jtds:sqlserver://").append(_ipAddress).append(":").append(_portNumber).append("/")
                    .append(_databaseName).toString();

            connection = DriverManager.getConnection(connectionURL, _userId, _password);

            String today;
            String tommorow;

            /*if(mounth < 10){
                today = String.valueOf(year) + "-0" + String.valueOf(mounth) + "-" + String.valueOf(dayOfMounth);
                tommorow = String.valueOf(year) + "-0" + String.valueOf(mounth) + "-" + String.valueOf(dayOfMounth + 1);
            }
            else {
                today = String.valueOf(year) + "-" + String.valueOf(mounth) + "-" + String.valueOf(dayOfMounth);
                tommorow = String.valueOf(year) + "-" + String.valueOf(mounth) + "-" + String.valueOf(dayOfMounth + 1);
            }*/

            today = String.valueOf(year) + "-" + String.valueOf(mounth) + "-" + String.valueOf(dayOfMounth);
            tommorow = String.valueOf(year) + "-" + String.valueOf(mounth) + "-" + String.valueOf(dayOfMounth + 1);

            Log.e("today: ", today);
            Log.e("tommorow: ", tommorow);

            try{
                if(connection != null){
                    query = "SELECT [GateId], [EntryDate], [LogTypeId] FROM Log WHERE EntryDate >= '" + today + "' and EntryDate <= '" + tommorow + "'";
                    Log.e("Query: ", query);
                    stmt = connection.createStatement();
                    rs = stmt.executeQuery(query);

                    size = 0;

                    while (rs.next()){
                        myLog = new MyLog();
                        myLog._gateId = rs.getInt(0);
                        myLog._date = rs.getString(1);
                        myLog._logTypeId = rs.getInt(2);

                        myLogs.add(myLog);
                        size++;
                    }

                    if(size == 0){
                        Log.e("Mérete: ", String.valueOf(size));
                        return null;
                    }

                }

            }catch (SQLException e){
                //return null;
            }

            connection.close();
        }
        catch (Exception e){
            //return null;
        }

        return myLogs;
    }
}
