package hu.unideb.inf.nfcapp.Databases;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import hu.unideb.inf.nfcapp.Enums.LogEnums;
import hu.unideb.inf.nfcapp.Enums.LoginTypeEnum;
import hu.unideb.inf.nfcapp.Models.MyLog;
import hu.unideb.inf.nfcapp.Models.User;

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

            if(dayOfMounth == 31){
                Log.e("dayofmounth= ", String.valueOf(dayOfMounth));
                today = String.valueOf(year) + "-" + String.valueOf(mounth) + "-" + String.valueOf(dayOfMounth);
                tommorow = String.valueOf(year) + "-" + String.valueOf(mounth + 1) + "-" + String.valueOf(1);
            }
            else{
                today = String.valueOf(year) + "-" + String.valueOf(mounth) + "-" + String.valueOf(dayOfMounth);
                tommorow = String.valueOf(year) + "-" + String.valueOf(mounth) + "-" + String.valueOf(dayOfMounth + 1);
            }

            try{
                if(connection != null){
                    query = "SELECT [GateId], CONVERT(varchar, EntryDate, 120), [LogTypeId] FROM Log WHERE EntryDate >= '" + today + "' and EntryDate <= '" + tommorow + "'";
                    stmt = connection.createStatement();
                    rs = stmt.executeQuery(query);

                    size = 0;

                    while (rs.next()){
                        myLog = new MyLog();
                        myLog._gateId = rs.getInt(1);
                        myLog._date = rs.getString(2);
                        myLog._logTypeId = rs.getInt(3);

                        myLogs.add(myLog);

                        size++;
                    }
                    Log.e("Mérete logs: ", String.valueOf(myLogs.size()));

                    if(size == 0){
                        Log.e("Mérete: ", String.valueOf(size));
                        MyLog._myMessage = LogEnums.LOG_NO_EVENTS;
                        return null;
                    }

                }

            }catch (SQLException e){
                Log.e("Hiba: ", e.getMessage());
                MyLog._myMessage = LogEnums.SQL_READING_FAILED;
                return null;
            }

            connection.close();
        }
        catch (Exception e){
            Log.e("Hiba: ", e.getMessage());
            MyLog._myMessage = LogEnums.SQL_CONNECTION_FAILED;
            return null;
        }

        MyLog._myMessage = LogEnums.LOG_HAS_EVENTS;
        return myLogs;
    }
}
