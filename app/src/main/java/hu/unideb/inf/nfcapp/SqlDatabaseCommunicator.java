package hu.unideb.inf.nfcapp;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlDatabaseCommunicator implements Communicator {

    private final String _ipAddress = "172.16.1.6";
    private final String _portNumber = "1433";
    private final String _databaseName = "test";
    private final String _userId = "sa";
    private final String _password = "0207";

    Connection connection;
    String request;
    Statement stmt;
    ResultSet rs;
    int size;
    String query;

    StringBuilder sb;



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
}
