package hu.unideb.inf.nfcapp.Databases;

import android.os.StrictMode;
import android.util.Log;

import hu.unideb.inf.nfcapp.Enums.LogTypeEnums;
import hu.unideb.inf.nfcapp.Enums.LoginStatesEnum;
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
    private String query;
    private ResultSet rs;
    private int size;
    private Statement stmt;

    private final String _serverName = "server.logcontrol.hu";
    private final String _portNumber = "4241";
    private final String _databaseName = "Galmihaly";
    private final String _userId = "Galmihaly";
    private final String _password = "Gm2022!!!";

    private String _lastLogoutDate = null;
    private String _lastLoginDate = null;
    private int result = 0;
    private MyLog myLog;
    private String[] datetime;
    private List<MyLog> myLogs;
    private List<Integer> _gatePermissions;

    @Override
    public Enum loginUser(String username, String password) {

        getConnection();
        if(MyLog._myMessage == SQLEnums.SQL_CONNECTION_FAILED) return null;

        try {
            if (connection != null) {

                query = "SELECT [Id], [Name], [Address], [Account] FROM Users WHERE Account = '" + username + "' and " + "Password = '" + password + "'";

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
                    return LoginStatesEnum.LOGIN_FAILED;
                }

                connection.close();
            }
        } catch (Exception e2) {
            return SQLEnums.SQL_READING_FAILED;
        }

        return LoginStatesEnum.LOGIN_ACCESS;
    }

    @Override
    public List<Integer> getGatePermissionList() {

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

            MyLog._myMessage = SQLEnums.SQL_READING_FAILED;
            return null;
        }

        MyLog._myMessage = SQLEnums.SQL_READING_SUCCES;
        return _gatePermissions;
    }

    @Override
    public List<MyLog> getLogsbyDate(int year, int mounth, int dayOfMounth) {

        myLogs = new ArrayList<>();

        getConnection();
        if(MyLog._myMessage == SQLEnums.SQL_CONNECTION_FAILED) return null;

        try {
            if (connection != null) {
                String[] dates = Helper.getFormattedDate(year, mounth, dayOfMounth, '-');

                query = "SELECT [UserId], [GateId], CONVERT(varchar, EntryDate, 120), [LogTypeId] FROM Log WHERE UserId = " + User._id + "and " +
                        "EntryDate >= '" + dates[0] + "' and EntryDate <= '" + dates[1] + "'";

                stmt = connection.createStatement();
                rs = stmt.executeQuery(query);
                size = 0;

                while (rs.next()) {

                    myLog = new MyLog();

                    myLog._userId = rs.getInt(1);
                    myLog._gateId = rs.getInt(2);

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
            MyLog._myMessage = SQLEnums.SQL_READING_FAILED;
            return null;
        }

        MyLog._myMessage = SQLEnums.SQL_READING_SUCCES;
        return myLogs;
    }

    @Override
    public String getLastLoginDate() {

        getConnection();
        if(MyLog._myMessage == SQLEnums.SQL_CONNECTION_FAILED) return null;

        try {
            if (connection != null) {

                query = "SELECT MAX(CONVERT(varchar, EntryDate, 120)) FROM Log where UserId = '" + User._id + "' and " +
                        "(LogTypeId = '" + LogTypeEnums.LOGIN_CARD.getLevelCode() + "' or " +
                        "LogTypeId = '" + LogTypeEnums.LOGIN_PASSWORD.getLevelCode() + "')";

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

    @Override
    public String getLastLogoutDate() {

        getConnection();
        if(MyLog._myMessage == SQLEnums.SQL_CONNECTION_FAILED) return null;

        try {
            if (connection != null) {
                query = "SELECT MAX(CONVERT(varchar, EntryDate, 120)) FROM Log where UserId = '" + User._id + "' and " +
                        "(LogTypeId = '" + LogTypeEnums.LOGOUT_CARD.getLevelCode() +"' or " +
                        "LogTypeId = '" + LogTypeEnums.LOGOUT_PASSWORD.getLevelCode() + "')";

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

    @Override
    public int getStatistic(String command) {

        getConnection();
        if(MyLog._myMessage == SQLEnums.SQL_CONNECTION_FAILED) return 0;

        try {
            if (connection != null) {
                query = command;
                stmt = connection.createStatement();
                rs = stmt.executeQuery(query);
                size = 0;

                while (rs.next()) {
                    result = rs.getInt(1);
                    size++;
                }

                if (size == 0) {
                    MyLog._myMessage = SQLEnums.SQL_NO_EVENTS;
                    return 0;
                }
                connection.close();
            }
        } catch (Exception e2) {
            MyLog._myMessage = SQLEnums.SQL_READING_FAILED;
            return 0;
        }

        MyLog._myMessage = SQLEnums.SQL_READING_SUCCES;
        return result;
    }

    public void getConnection(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            StringBuilder sb = new StringBuilder();

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