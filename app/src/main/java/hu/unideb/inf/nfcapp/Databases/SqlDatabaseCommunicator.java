package hu.unideb.inf.nfcapp.Databases;

import android.os.StrictMode;
import android.util.Log;

import hu.unideb.inf.nfcapp.Enums.LogTypeEnums;
import hu.unideb.inf.nfcapp.Enums.LoginTypeEnum;
import hu.unideb.inf.nfcapp.Enums.SQLEnums;
import hu.unideb.inf.nfcapp.Models.MyLog;
import hu.unideb.inf.nfcapp.Models.User;
import hu.unideb.inf.nfcapp.helpers.Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlDatabaseCommunicator implements Communicator {

    private Connection connection;
    private MyLog myLog;
    private List<MyLog> myLogs;
    private String query;
    private ResultSet rs;
    private StringBuilder sb;
    private int size;
    private Statement stmt;

    private final String _serverName = "server.logcontrol.hu";
    private final String _portNumber = "4241";
    private final String _databaseName = "Galmihaly";
    private final String _userId = "Galmihaly";
    private final String _password = "Gm2022!!!";

    private String _lastLogoutDate = null;
    private String[] dates;
    private List<Integer> _gatePermissions;

    @Override // hu.unideb.inf.nfcapp.Databases.Communicator
    public Enum loginUser(String username, String password) {

        getConnection();

        if(MyLog._myMessage == SQLEnums.SQL_CONNECTION_FAILED) return null;

        try {
            if (connection != null) {

                query = "SELECT [Id], [Name], [Address], [Account] FROM Users WHERE Account = '" + username + "' and " +
                        "                                                           Password = '" + password + "'";
                stmt = connection.createStatement();
                rs = stmt.executeQuery(this.query);
                size = 0;

                while (rs.next()) {

                    User._id = rs.getInt(1);
                    User._name = rs.getString(2);
                    User._address = rs.getString(3);
                    User._account = rs.getString(4);

                    size++;
                }

                if (size == 0) {
                    return LoginTypeEnum.LOGIN_FAILED;
                }

                connection.close();
            }
        } catch (Exception e2) {
            return SQLEnums.SQL_READING_FAILED;
        }

        return LoginTypeEnum.LOGIN_ACCESS;
    }

    @Override
    public List<Integer> getGatePermissions() {

        _gatePermissions = new ArrayList<>();
        getConnection();

        if(MyLog._myMessage == SQLEnums.SQL_CONNECTION_FAILED) return null;

        try {
            if (connection != null) {

                query = "SELECT GateId FROM UserGates WHERE UserId = '" + User._id + "'";
                stmt = connection.createStatement();
                rs = stmt.executeQuery(query);
                size = 0;

                while (rs.next()) {

                    _gatePermissions.add(rs.getInt(1));
                    size++;
                }

                if (size == 0) {
                    MyLog._myMessage = SQLEnums.SQL_NO_EVENTS;
                    return null;
                }

                connection.close();
            }
        } catch (SQLException e) {
            //Log.e("Hiba: ", e.getMessage());
            MyLog._myMessage = SQLEnums.SQL_READING_FAILED;
            return null;
        }

        MyLog._myMessage = SQLEnums.SQL_READING_SUCCES;
        return _gatePermissions;
    }

    @Override // hu.unideb.inf.nfcapp.Databases.Communicator
    public List<MyLog> getLogsbyDate(int year, int mounth, int dayOfMounth) {

        String[] datetime;
        String[] seged;
        myLogs = new ArrayList();
        getConnection();

        if(MyLog._myMessage == SQLEnums.SQL_CONNECTION_FAILED) return null;

        try {
            if (connection != null) {
                dates = Helper.getFormattedDate(year, mounth, dayOfMounth, '-');

                Log.e("e:", String.valueOf(dates[0]));
                Log.e("s:", String.valueOf(dates[1]));

                query = "SELECT [UserId], [GateId], CONVERT(varchar, EntryDate, 120), [LogTypeId] FROM Log WHERE UserId = " + User._id + "and " +
                                                                                                                "EntryDate >= '" + dates[0] + "' and EntryDate <= '" + dates[1] + "'";
                stmt = connection.createStatement();
                rs = stmt.executeQuery(query);
                size = 0;

                while (rs.next()) {

                    myLog = new MyLog();

                    myLog._userId = rs.getInt(1);
                    myLog._gateId = rs.getInt(2);
                    Log.e("c:", String.valueOf(rs.getInt(2)));
                    datetime = rs.getString(3).split(" ");

                    myLog._date = datetime[0];
                    myLog._time = datetime[1];
                    myLog._logTypeId = rs.getInt(4);
                    myLogs.add(myLog);

                    size++;
                }

                if (size == 0) {
                    MyLog._myMessage = SQLEnums.SQL_NO_EVENTS;
                    return null;
                }

                connection.close();
            }
        } catch (SQLException e) {
            //Log.e("Hiba: ", e.getMessage());
            MyLog._myMessage = SQLEnums.SQL_READING_FAILED;
            return null;
        }

        MyLog._myMessage = SQLEnums.SQL_READING_SUCCES;
        return myLogs;
    }

    @Override // hu.unideb.inf.nfcapp.Databases.Communicator
    public String getLastLoginDate() {

        String _lastLoginDate = null;

        getConnection();

        if(MyLog._myMessage == SQLEnums.SQL_CONNECTION_FAILED) return null;

        try {
            if (connection != null) {

                query = "SELECT MAX(CONVERT(varchar, EntryDate, 120)) FROM Log where UserId = '" + User._id + "' and " +
                                                                                    "LogTypeId = '" + LogTypeEnums.LOGIN_CARD.getLevelCode() + "' or " +
                                                                                    "LogTypeId = '" + LogTypeEnums.LOGIN_PASSWORD.getLevelCode() + "'";
                stmt = connection.createStatement();
                rs = stmt.executeQuery(query);
                size = 0;

                while (rs.next()) {
                    _lastLoginDate = rs.getString(1);
                    size++;
                }

                if (size == 0) {
                    MyLog._myMessage = SQLEnums.SQL_NO_EVENTS;
                    return null;
                }

                connection.close();
            }
        } catch (Exception e2) {
            MyLog._myMessage = SQLEnums.SQL_READING_FAILED;
            return null;
        }

        MyLog._myMessage = SQLEnums.SQL_READING_SUCCES;
        return _lastLoginDate;
    }

    @Override // hu.unideb.inf.nfcapp.Databases.Communicator
    public String getLastLogoutDate() {

        getConnection();
        if(MyLog._myMessage == SQLEnums.SQL_CONNECTION_FAILED) return null;

        try {
            if (connection != null) {
                query = "SELECT MAX(CONVERT(varchar, EntryDate, 120)) FROM Log where UserId = '" + User._id + "' and " +
                                                                                    "LogTypeId = '" + LogTypeEnums.LOGOUT_CARD.getLevelCode() +"' or " +
                                                                                    "LogTypeId = '" + LogTypeEnums.LOGOUT_PASSWORD.getLevelCode() + "'";
                stmt = connection.createStatement();
                rs = stmt.executeQuery(query);
                size = 0;

                while (rs.next()) {

                    _lastLogoutDate = rs.getString(1);
                    size++;
                }

                if (size == 0) {

                    MyLog._myMessage = SQLEnums.SQL_NO_EVENTS;
                    return null;
                }

                connection.close();
            }
        } catch (Exception e2) {
            MyLog._myMessage = SQLEnums.SQL_READING_FAILED;
            return null;
        }

        MyLog._myMessage = SQLEnums.SQL_READING_SUCCES;
        return _lastLogoutDate;
    }

    public void getConnection(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            sb = new StringBuilder();

            String connectionURL = sb.append("jdbc:jtds:sqlserver://")
                                     .append(_serverName)
                                     .append(":").append(_portNumber)
                                     .append("/").append(_databaseName)
                                     .toString();

            connection = DriverManager.getConnection(connectionURL, _userId, _password);
        }
        catch (SQLException | ClassNotFoundException e) {
            MyLog._myMessage = SQLEnums.SQL_CONNECTION_FAILED;
        }
    }
}