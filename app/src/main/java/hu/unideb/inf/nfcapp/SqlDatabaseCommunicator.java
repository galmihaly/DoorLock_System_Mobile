package hu.unideb.inf.nfcapp;

import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SqlDatabaseCommunicator implements Communicator {

    private final String _ipAddress = "192.168.1.69";
    private final String _portNumber = "1433";
    private final String _databaseName = "test";
    private final String _userId = "sa";
    private final String _password = "0207";

    Connection connection;
    User user;
    String query;

    StringBuilder sb = new StringBuilder();

    @Override
    public User loginUser() {
        connection = getConnection();

        try{
            if(connection != null){
                Statement statement = connection.createStatement();


            }


        }catch (Exception e){
            Log.e("SQL Connection Failed !!!\nHiba: ", e.getMessage());
        }

        return null;
    }

    @Override
    public Connection getConnection() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String connectionURL = null;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");

            connectionURL = sb.append("jdbc:jtds:sqlserver://").append(_ipAddress).append(":").append(_portNumber).append("/")
                    .append(_databaseName).toString();

            connection = DriverManager.getConnection(connectionURL, _userId, _password);
        }
        catch (Exception e){
            Log.e("SQL Connection Failed !!!\nHiba: ", e.getMessage());
        }

        return connection;

    }
}
