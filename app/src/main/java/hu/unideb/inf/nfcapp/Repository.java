package hu.unideb.inf.nfcapp;

import android.widget.Toast;

public class Repository {
    public enum CommunicatorTypeEnum
    {
        MsSqlServer,
        Oracle,
        MySql,
        Xml,
        TextFile
    }

    public static CommunicatorTypeEnum CommunicatorType = CommunicatorTypeEnum.MsSqlServer;
    public static Communicator Communicator = null;

    public static User LoggedInUser = null;

    public static String VersionInfo()
    {
        return "1.0";
    }


    public static boolean Initialize() throws Exception {

        switch(CommunicatorType)
        {
            case MsSqlServer:
                Communicator = new SqlDatabaseCommunicator();
                return true;

            default:
                throw new Exception("A megadott interfész még nincs implementálva!");
        }
    }
}
